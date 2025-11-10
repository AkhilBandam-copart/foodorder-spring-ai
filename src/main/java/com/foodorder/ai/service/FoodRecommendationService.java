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

import com.foodorder.ai.dto.RecommendationRequest;
import com.foodorder.ai.dto.RecommendationResponse;
import com.foodorder.ai.dto.UserPreferences;
import com.foodorder.ai.model.FoodItem;
import com.foodorder.ai.repository.FoodItemRepository;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodRecommendationService {

    @Value("${openai.api.key:#{null}}")
    private String apiKey;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private MLRecommendationService mlService;

    public RecommendationResponse getRecommendations(RecommendationRequest request) {
        UserPreferences prefs = request.getUserPreferences();
        List<FoodItem> availableItems = foodItemRepository.findAll();
        
        String userId = prefs.getUserId();
        
        if (userId != null && mlService.hasEnoughData(userId)) {
            System.out.println("Using ML recommendations for user: " + userId);
            List<FoodItem> mlRecs = mlService.recommend(userId, 5);
            System.out.println("ML returned " + (mlRecs != null ? mlRecs.size() : 0) + " recommendations");
            
            if (mlRecs != null && !mlRecs.isEmpty()) {
                mlRecs = applyUserFilters(mlRecs, prefs);
                System.out.println("After filtering: " + mlRecs.size() + " recommendations");
                
                double estimatedTotal = mlRecs.stream()
                        .mapToDouble(FoodItem::getPrice)
                        .sum();
                
                String reasoning = "ML-based personalized recommendations based on your order history and similar users' preferences.";
                return new RecommendationResponse(mlRecs, reasoning, estimatedTotal);
            }
        } else {
            System.out.println("Falling back to AI/Mock recommendations for user: " + userId);
        }

        String prompt = buildPrompt(prefs, availableItems);
        String aiResponse;

        if (geminiService.isAvailable()) {
            aiResponse = callGemini(prompt);
        } else if (apiKey != null && !apiKey.isEmpty()) {
            aiResponse = callOpenAI(prompt);
        } else {
            aiResponse = mockAIResponse(prefs, availableItems);
        }

        List<FoodItem> recommendedItems = parseRecommendations(aiResponse, availableItems);
        
        double estimatedTotal = recommendedItems.stream()
                .mapToDouble(FoodItem::getPrice)
                .sum();

        return new RecommendationResponse(recommendedItems, aiResponse, estimatedTotal);
    }
    
    private List<FoodItem> applyUserFilters(List<FoodItem> items, UserPreferences prefs) {
        return items.stream()
            .filter(item -> {
                if (prefs.getBudgetMax() != null && item.getPrice() > prefs.getBudgetMax()) {
                    return false;
                }
                if (prefs.getDietaryPreference() != null) {
                    String dietPref = prefs.getDietaryPreference().toLowerCase();
                    if (dietPref.contains("vegan") && !item.isVegan()) {
                        return false;
                    }
                    if (dietPref.contains("vegetarian") && !item.isVegetarian()) {
                        return false;
                    }
                }
                if (prefs.getAllergens() != null && item.getAllergens() != null) {
                    for (String allergen : item.getAllergens()) {
                        if (prefs.getAllergens().stream()
                            .anyMatch(a -> a.equalsIgnoreCase(allergen))) {
                            return false;
                        }
                    }
                }
                return true;
            })
            .limit(3)
            .collect(Collectors.toList());
    }

    private String buildPrompt(UserPreferences prefs, List<FoodItem> availableItems) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are a food recommendation assistant. Based on the user's preferences, recommend food items from the available menu.\n\n");
        
        prompt.append("User Preferences:\n");
        if (prefs.getDietaryPreference() != null) {
            prompt.append("- Dietary Preference: ").append(prefs.getDietaryPreference()).append("\n");
        }
        if (prefs.getAllergens() != null && !prefs.getAllergens().isEmpty()) {
            prompt.append("- Allergens to Avoid: ").append(String.join(", ", prefs.getAllergens())).append("\n");
        }
        if (prefs.getBudgetMin() != null && prefs.getBudgetMax() != null) {
            prompt.append("- Budget Range: $").append(prefs.getBudgetMin()).append(" - $").append(prefs.getBudgetMax()).append("\n");
        }
        if (prefs.getMealType() != null) {
            prompt.append("- Meal Type: ").append(prefs.getMealType()).append("\n");
        }
        if (prefs.getDislikedFoods() != null && !prefs.getDislikedFoods().isEmpty()) {
            prompt.append("- Disliked Foods: ").append(String.join(", ", prefs.getDislikedFoods())).append("\n");
        }
        if (prefs.getPastOrders() != null && !prefs.getPastOrders().isEmpty()) {
            prompt.append("- Past Orders: ").append(String.join(", ", prefs.getPastOrders())).append("\n");
        }
        if (prefs.getPreferredCuisine() != null) {
            prompt.append("- Preferred Cuisine: ").append(prefs.getPreferredCuisine()).append("\n");
        }

        prompt.append("\nAvailable Menu Items:\n");
        for (FoodItem item : availableItems) {
            prompt.append("- ID: ").append(item.getId())
                  .append(", Name: ").append(item.getName())
                  .append(", Price: $").append(item.getPrice())
                  .append(", Category: ").append(item.getCategory())
                  .append(", Vegetarian: ").append(item.isVegetarian())
                  .append(", Vegan: ").append(item.isVegan())
                  .append(", Allergens: ").append(String.join(", ", item.getAllergens()))
                  .append(", In Stock: ").append(item.getQuantity())
                  .append("\n");
        }

        prompt.append("\nBased on the user's preferences, recommend 2-3 suitable food items. ");
        prompt.append("Provide the recommendation in the following format:\n");
        prompt.append("RECOMMENDED_IDS: [comma-separated list of food item IDs]\n");
        prompt.append("REASONING: [Your explanation for why these items are recommended]\n");

        return prompt.toString();
    }

    private String callOpenAI(String prompt) {
        try {
            OpenAiService service = new OpenAiService(apiKey, Duration.ofSeconds(30));
            
            ChatCompletionRequest chatRequest = ChatCompletionRequest.builder()
                    .model("gpt-3.5-turbo")
                    .messages(Arrays.asList(
                            new ChatMessage(ChatMessageRole.SYSTEM.value(), "You are a helpful food recommendation assistant."),
                            new ChatMessage(ChatMessageRole.USER.value(), prompt)
                    ))
                    .temperature(0.7)
                    .build();
            
            return service.createChatCompletion(chatRequest)
                    .getChoices()
                    .get(0)
                    .getMessage()
                    .getContent();
        } catch (Exception e) {
            return mockAIResponse(null, foodItemRepository.findAll());
        }
    }

    private String callGemini(String prompt) {
        try {
            String response = geminiService.generateContent(prompt);
            if (response != null && !response.isEmpty()) {
                return response;
            }
            return mockAIResponse(null, foodItemRepository.findAll());
        } catch (Exception e) {
            return mockAIResponse(null, foodItemRepository.findAll());
        }
    }

    private String mockAIResponse(UserPreferences prefs, List<FoodItem> availableItems) {
        StringBuilder response = new StringBuilder();
        List<FoodItem> filteredItems = new ArrayList<>(availableItems);
        
        if (prefs != null) {
            if (prefs.getDietaryPreference() != null) {
                String dietPref = prefs.getDietaryPreference().toLowerCase();
                filteredItems = filteredItems.stream()
                    .filter(item -> {
                        if (dietPref.contains("vegan")) {
                            return item.isVegan();
                        } else if (dietPref.contains("vegetarian")) {
                            return item.isVegetarian();
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
            }
            
            if (prefs.getBudgetMax() != null) {
                filteredItems = filteredItems.stream()
                    .filter(item -> item.getPrice() <= prefs.getBudgetMax())
                    .collect(Collectors.toList());
            }
            
            if (prefs.getBudgetMin() != null) {
                filteredItems = filteredItems.stream()
                    .filter(item -> item.getPrice() >= prefs.getBudgetMin())
                    .collect(Collectors.toList());
            }
            
            if (prefs.getAllergens() != null && !prefs.getAllergens().isEmpty()) {
                filteredItems = filteredItems.stream()
                    .filter(item -> {
                        if (item.getAllergens() == null) return true;
                        for (String allergen : item.getAllergens()) {
                            if (prefs.getAllergens().stream()
                                .anyMatch(a -> a.equalsIgnoreCase(allergen))) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
            }
            
            if (prefs.getPreferredCuisine() != null) {
                List<FoodItem> cuisineMatches = filteredItems.stream()
                    .filter(item -> item.getCategory() != null && 
                           item.getCategory().toLowerCase().contains(prefs.getPreferredCuisine().toLowerCase()))
                    .collect(Collectors.toList());
                if (!cuisineMatches.isEmpty()) {
                    filteredItems = cuisineMatches;
                }
            }
        }
        
        List<Long> recommendedIds = filteredItems.stream()
                .limit(3)
                .map(FoodItem::getId)
                .collect(Collectors.toList());
        
        if (recommendedIds.isEmpty() && prefs != null) {
            List<FoodItem> partialMatch = new ArrayList<>(availableItems);
            
            if (prefs.getDietaryPreference() != null) {
                String dietPref = prefs.getDietaryPreference().toLowerCase();
                List<FoodItem> dietMatches = partialMatch.stream()
                    .filter(item -> {
                        if (dietPref.contains("vegan")) return item.isVegan();
                        if (dietPref.contains("vegetarian")) return item.isVegetarian();
                        return true;
                    })
                    .collect(Collectors.toList());
                if (!dietMatches.isEmpty()) {
                    partialMatch = dietMatches;
                }
            }
            
            if (prefs.getBudgetMax() != null && !partialMatch.isEmpty()) {
                List<FoodItem> budgetMatches = partialMatch.stream()
                    .filter(item -> item.getPrice() <= prefs.getBudgetMax())
                    .collect(Collectors.toList());
                if (!budgetMatches.isEmpty()) {
                    partialMatch = budgetMatches;
                }
            }
            
            if (prefs.getAllergens() != null && !prefs.getAllergens().isEmpty() && !partialMatch.isEmpty()) {
                List<FoodItem> allergenSafe = partialMatch.stream()
                    .filter(item -> {
                        if (item.getAllergens() == null) return true;
                        for (String allergen : item.getAllergens()) {
                            if (prefs.getAllergens().stream()
                                .anyMatch(a -> a.equalsIgnoreCase(allergen))) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .collect(Collectors.toList());
                if (!allergenSafe.isEmpty()) {
                    partialMatch = allergenSafe;
                }
            }
            
            recommendedIds = partialMatch.stream()
                    .limit(3)
                    .map(FoodItem::getId)
                    .collect(Collectors.toList());
        }
        
        if (recommendedIds.isEmpty()) {
            recommendedIds = availableItems.stream()
                    .limit(2)
                    .map(FoodItem::getId)
                    .collect(Collectors.toList());
        }
        
        response.append("RECOMMENDED_IDS: ").append(recommendedIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "))).append("\n");
        response.append("REASONING: Based on your preferences, these items best match your dietary requirements, budget, and taste preferences.");
        
        return response.toString();
    }

    private List<FoodItem> parseRecommendations(String aiResponse, List<FoodItem> availableItems) {
        List<FoodItem> recommended = new ArrayList<>();
        
        String[] lines = aiResponse.split("\n");
        for (String line : lines) {
            if (line.startsWith("RECOMMENDED_IDS:")) {
                String idsStr = line.substring("RECOMMENDED_IDS:".length()).trim();
                idsStr = idsStr.replaceAll("[\\[\\]]", "");
                String[] ids = idsStr.split(",");
                
                for (String idStr : ids) {
                    try {
                        Long id = Long.parseLong(idStr.trim());
                        availableItems.stream()
                                .filter(item -> item.getId().equals(id))
                                .findFirst()
                                .ifPresent(recommended::add);
                    } catch (NumberFormatException e) {
                    }
                }
                break;
            }
        }

        if (recommended.isEmpty()) {
            recommended = availableItems.stream()
                    .limit(2)
                    .collect(Collectors.toList());
        }

        return recommended;
    }
}
