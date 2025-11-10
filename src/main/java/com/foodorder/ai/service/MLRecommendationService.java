/*
 * MIT License
 *
 * Copyright (c) 2025 Akhil Chandra Bandam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.foodorder.ai.service;

import com.foodorder.ai.ml.MatrixBuilder;
import com.foodorder.ai.ml.ModelPersistence;
import com.foodorder.ai.model.FoodItem;
import com.foodorder.ai.model.UserItemInteraction;
import com.foodorder.ai.repository.FoodItemRepository;
import com.foodorder.ai.repository.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import smile.math.matrix.Matrix;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MLRecommendationService {
    
    @Autowired
    private InteractionRepository interactionRepository;
    
    @Autowired
    private FoodItemRepository foodItemRepository;
    
    private Matrix userFactors;
    private Matrix itemFactors;
    private MatrixBuilder matrixBuilder;
    private boolean modelTrained = false;
    
    private static final int LATENT_FACTORS = 10;
    private static final double REGULARIZATION = 0.01;
    private static final int ITERATIONS = 20;
    private static final int MIN_ORDERS_FOR_ML = 2;
    
    @PostConstruct
    public void init() {
        try {
            loadModel();
        } catch (Exception e) {
            System.out.println("No existing model found. Will train on first request.");
        }
    }
    
    @Scheduled(cron = "0 0 2 * * ?")
    public void scheduledTraining() {
        try {
            trainModel();
            System.out.println("Scheduled ML model training completed successfully");
        } catch (Exception e) {
            System.err.println("Scheduled training failed: " + e.getMessage());
        }
    }
    
    public void trainModel() {
        try {
            List<UserItemInteraction> interactions = interactionRepository.findAll();
            
            if (interactions.isEmpty()) {
                System.out.println("No interactions found. Skipping training.");
                return;
            }
            
            matrixBuilder = new MatrixBuilder();
            Matrix ratings = matrixBuilder.buildRatingMatrix(interactions);
            
            System.out.println("Training ALS model with " + matrixBuilder.getNumUsers() + 
                             " users and " + matrixBuilder.getNumItems() + " items");
            
            int numUsers = matrixBuilder.getNumUsers();
            int numItems = matrixBuilder.getNumItems();
            
            userFactors = Matrix.rand(numUsers, LATENT_FACTORS);
            itemFactors = Matrix.rand(numItems, LATENT_FACTORS);
            
            for (int iter = 0; iter < ITERATIONS; iter++) {
                updateUserFactors(ratings);
                updateItemFactors(ratings);
            }
            
            modelTrained = true;
            
            saveModel();
            
            System.out.println("ML model training completed successfully");
            
        } catch (Exception e) {
            System.err.println("Error training model: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void updateUserFactors(Matrix ratings) {
        int numUsers = ratings.nrow();
        
        for (int u = 0; u < numUsers; u++) {
            double[][] A = new double[LATENT_FACTORS][LATENT_FACTORS];
            double[] b = new double[LATENT_FACTORS];
            
            for (int i = 0; i < ratings.ncol(); i++) {
                double rating = ratings.get(u, i);
                if (rating > 0) {
                    double[] itemFactor = itemFactors.row(i);
                    
                    for (int k = 0; k < LATENT_FACTORS; k++) {
                        b[k] += rating * itemFactor[k];
                        for (int l = 0; l < LATENT_FACTORS; l++) {
                            A[k][l] += itemFactor[k] * itemFactor[l];
                        }
                    }
                }
            }
            
            for (int k = 0; k < LATENT_FACTORS; k++) {
                A[k][k] += REGULARIZATION;
            }
            
            try {
                double[] solution = solveLinearSystem(A, b);
                for (int k = 0; k < LATENT_FACTORS; k++) {
                    userFactors.set(u, k, solution[k]);
                }
            } catch (Exception e) {
                System.err.println("Error updating user " + u + ": " + e.getMessage());
            }
        }
    }
    
    private void updateItemFactors(Matrix ratings) {
        int numItems = ratings.ncol();
        
        for (int i = 0; i < numItems; i++) {
            double[][] A = new double[LATENT_FACTORS][LATENT_FACTORS];
            double[] b = new double[LATENT_FACTORS];
            
            for (int u = 0; u < ratings.nrow(); u++) {
                double rating = ratings.get(u, i);
                if (rating > 0) {
                    double[] userFactor = userFactors.row(u);
                    
                    for (int k = 0; k < LATENT_FACTORS; k++) {
                        b[k] += rating * userFactor[k];
                        for (int l = 0; l < LATENT_FACTORS; l++) {
                            A[k][l] += userFactor[k] * userFactor[l];
                        }
                    }
                }
            }
            
            for (int k = 0; k < LATENT_FACTORS; k++) {
                A[k][k] += REGULARIZATION;
            }
            
            try {
                double[] solution = solveLinearSystem(A, b);
                for (int k = 0; k < LATENT_FACTORS; k++) {
                    itemFactors.set(i, k, solution[k]);
                }
            } catch (Exception e) {
                System.err.println("Error updating item " + i + ": " + e.getMessage());
            }
        }
    }
    
    private double[] solveLinearSystem(double[][] A, double[] b) {
        int n = b.length;
        double[] x = new double[n];
        
        for (int k = 0; k < n; k++) {
            for (int i = k + 1; i < n; i++) {
                double factor = A[i][k] / A[k][k];
                for (int j = k; j < n; j++) {
                    A[i][j] -= factor * A[k][j];
                }
                b[i] -= factor * b[k];
            }
        }
        
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }
        
        return x;
    }
    
    public List<FoodItem> recommend(String userId, int topN) {
        if (!modelTrained || matrixBuilder == null) {
            trainModel();
        }
        
        if (!hasEnoughData(userId)) {
            return null;
        }
        
        Integer userIndex = matrixBuilder.getUserIndex(userId);
        if (userIndex == null) {
            return null;
        }
        
        double[] userVector = userFactors.row(userIndex);
        Map<Long, Double> scores = new HashMap<>();
        
        for (int i = 0; i < itemFactors.nrow(); i++) {
            double[] itemVector = itemFactors.row(i);
            double score = dotProduct(userVector, itemVector);
            Long itemId = matrixBuilder.getItemId(i);
            scores.put(itemId, score);
        }
        
        List<Long> topItemIds = scores.entrySet().stream()
            .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
            .limit(topN)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        return topItemIds.stream()
            .map(id -> foodItemRepository.findById(id))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }
    
    private double dotProduct(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }
    
    public boolean hasEnoughData(String userId) {
        Long orderCount = interactionRepository.countOrdersByUserId(userId);
        System.out.println("ML hasEnoughData for user " + userId + ": count=" + orderCount + " (min=" + MIN_ORDERS_FOR_ML + ")");
        return orderCount != null && orderCount >= MIN_ORDERS_FOR_ML;
    }
    
    public boolean isModelTrained() {
        return modelTrained;
    }
    
    private void saveModel() {
        try {
            ModelPersistence.saveModel(userFactors, "user-factors.bin");
            ModelPersistence.saveModel(itemFactors, "item-factors.bin");
            ModelPersistence.saveMappings(
                matrixBuilder.getUserIdToIndex(),
                matrixBuilder.getItemIdToIndex(),
                "mappings.bin"
            );
            System.out.println("Model saved successfully");
        } catch (Exception e) {
            System.err.println("Error saving model: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadModel() {
        try {
            if (ModelPersistence.modelExists("user-factors.bin")) {
                userFactors = (Matrix) ModelPersistence.loadModel("user-factors.bin");
                itemFactors = (Matrix) ModelPersistence.loadModel("item-factors.bin");
                
                Map<String, Object> mappings = ModelPersistence.loadMappings("mappings.bin");
                matrixBuilder = new MatrixBuilder();
                
                if (isModelStale()) {
                    System.out.println("Model is stale (DB was reset). Will retrain on next request.");
                    modelTrained = false;
                    userFactors = null;
                    itemFactors = null;
                    matrixBuilder = null;
                } else {
                    modelTrained = true;
                    System.out.println("Model loaded successfully");
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading model: " + e.getMessage());
        }
    }
    
    private boolean isModelStale() {
        if (matrixBuilder == null) return true;
        
        List<String> dbUsers = interactionRepository.findAllDistinctUserIds();
        if (dbUsers.isEmpty()) {
            return matrixBuilder.getNumUsers() > 0;
        }
        
        boolean anyUserMissing = dbUsers.stream()
            .anyMatch(userId -> matrixBuilder.getUserIndex(userId) == null);
        
        if (anyUserMissing) {
            System.out.println("Model has " + matrixBuilder.getNumUsers() + " users, but DB has " + dbUsers.size() + " users (mismatch detected)");
            return true;
        }
        
        return false;
    }
}
