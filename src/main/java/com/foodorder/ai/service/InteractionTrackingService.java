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

import com.foodorder.ai.model.UserItemInteraction;
import com.foodorder.ai.repository.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InteractionTrackingService {
    
    @Autowired
    private InteractionRepository interactionRepository;
    
    public void trackOrder(String userId, Map<Long, Integer> items) {
        items.forEach((itemId, quantity) -> {
            UserItemInteraction interaction = new UserItemInteraction();
            interaction.setUserId(userId);
            interaction.setFoodItemId(itemId);
            interaction.setInteractionType(UserItemInteraction.InteractionType.ORDER);
            interaction.setQuantity(quantity);
            interaction.setRating(quantity * 1.0);
            
            interactionRepository.save(interaction);
        });
    }
    
    public void trackView(String userId, Long foodItemId) {
        UserItemInteraction interaction = new UserItemInteraction();
        interaction.setUserId(userId);
        interaction.setFoodItemId(foodItemId);
        interaction.setInteractionType(UserItemInteraction.InteractionType.VIEW);
        interaction.setRating(0.5);
        interaction.setQuantity(1);
        
        interactionRepository.save(interaction);
    }
    
    public void trackRating(String userId, Long foodItemId, Double rating) {
        UserItemInteraction interaction = new UserItemInteraction();
        interaction.setUserId(userId);
        interaction.setFoodItemId(foodItemId);
        interaction.setInteractionType(UserItemInteraction.InteractionType.RATING);
        interaction.setRating(rating);
        interaction.setQuantity(1);
        
        interactionRepository.save(interaction);
    }
    
    public Long getOrderCount(String userId) {
        return interactionRepository.countOrdersByUserId(userId);
    }
}
