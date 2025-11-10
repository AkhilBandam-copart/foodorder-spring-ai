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

package com.foodorder.ai.ml;

import com.foodorder.ai.model.UserItemInteraction;
import smile.math.matrix.Matrix;

import java.util.*;

public class MatrixBuilder {
    
    private Map<String, Integer> userIdToIndex;
    private Map<Long, Integer> itemIdToIndex;
    private Map<Integer, String> indexToUserId;
    private Map<Integer, Long> indexToItemId;
    
    public MatrixBuilder() {
        this.userIdToIndex = new HashMap<>();
        this.itemIdToIndex = new HashMap<>();
        this.indexToUserId = new HashMap<>();
        this.indexToItemId = new HashMap<>();
    }
    
    public Matrix buildRatingMatrix(List<UserItemInteraction> interactions) {
        buildMappings(interactions);
        
        int numUsers = userIdToIndex.size();
        int numItems = itemIdToIndex.size();
        
        double[][] data = new double[numUsers][numItems];
        
        for (UserItemInteraction interaction : interactions) {
            Integer userIndex = userIdToIndex.get(interaction.getUserId());
            Integer itemIndex = itemIdToIndex.get(interaction.getFoodItemId());
            
            if (userIndex != null && itemIndex != null) {
                data[userIndex][itemIndex] = interaction.getRating();
            }
        }
        
        return Matrix.of(data);
    }
    
    private void buildMappings(List<UserItemInteraction> interactions) {
        Set<String> userIds = new HashSet<>();
        Set<Long> itemIds = new HashSet<>();
        
        for (UserItemInteraction interaction : interactions) {
            userIds.add(interaction.getUserId());
            itemIds.add(interaction.getFoodItemId());
        }
        
        int userIndex = 0;
        for (String userId : userIds) {
            userIdToIndex.put(userId, userIndex);
            indexToUserId.put(userIndex, userId);
            userIndex++;
        }
        
        int itemIndex = 0;
        for (Long itemId : itemIds) {
            itemIdToIndex.put(itemId, itemIndex);
            indexToItemId.put(itemIndex, itemId);
            itemIndex++;
        }
    }
    
    public Integer getUserIndex(String userId) {
        return userIdToIndex.get(userId);
    }
    
    public Integer getItemIndex(Long itemId) {
        return itemIdToIndex.get(itemId);
    }
    
    public String getUserId(int index) {
        return indexToUserId.get(index);
    }
    
    public Long getItemId(int index) {
        return indexToItemId.get(index);
    }
    
    public Map<String, Integer> getUserIdToIndex() {
        return userIdToIndex;
    }
    
    public Map<Long, Integer> getItemIdToIndex() {
        return itemIdToIndex;
    }
    
    public int getNumUsers() {
        return userIdToIndex.size();
    }
    
    public int getNumItems() {
        return itemIdToIndex.size();
    }
}
