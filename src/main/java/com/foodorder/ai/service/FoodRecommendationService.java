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

    public RecommendationResponse getRecommendations(RecommendationRequest request) {
        UserPreferences prefs = request.getUserPreferences();
        List<FoodItem> availableItems = foodItemRepository.findAll();

        String prompt = buildPrompt(prefs, availableItems);
        String aiResponse;

        if (apiKey != null && !apiKey.isEmpty()) {
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

    private String mockAIResponse(UserPreferences prefs, List<FoodItem> availableItems) {
        StringBuilder response = new StringBuilder();
        List<Long> recommendedIds = new ArrayList<>();
        
        if (prefs != null && "vegetarian".equalsIgnoreCase(prefs.getDietaryPreference())) {
            for (FoodItem item : availableItems) {
                if (item.isVegetarian() && recommendedIds.size() < 3) {
                    recommendedIds.add(item.getId());
                }
            }
        } else if (prefs != null && prefs.getBudgetMax() != null) {
            for (FoodItem item : availableItems) {
                if (item.getPrice() <= prefs.getBudgetMax() && recommendedIds.size() < 3) {
                    recommendedIds.add(item.getId());
                }
            }
        } else {
            recommendedIds = availableItems.stream()
                    .limit(3)
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
