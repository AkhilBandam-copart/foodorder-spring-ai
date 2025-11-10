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

package com.foodorder.ai.repository;

import com.foodorder.ai.model.FoodItem;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FoodItemRepository {
    private final Map<Long, FoodItem> foodItems = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public FoodItemRepository() {
        initializeDefaultItems();
    }

    private void initializeDefaultItems() {
        save(new FoodItem(null, "Ham Burger", "Classic ham burger with fresh vegetables", 12.99, 23, "American", false, false, new String[]{"gluten", "dairy"}));
        save(new FoodItem(null, "Cheese Burger", "Delicious cheese burger with melted cheese", 10.99, 13, "American", false, false, new String[]{"gluten", "dairy"}));
        save(new FoodItem(null, "Veggie Sandwich", "Fresh vegetable sandwich", 7.99, 8, "American", true, true, new String[]{"gluten"}));
        save(new FoodItem(null, "KFC Wings", "Crispy fried chicken wings", 8.99, 46, "American", false, false, new String[]{"gluten"}));
        save(new FoodItem(null, "Margherita Pizza", "Classic pizza with tomato and mozzarella", 14.99, 30, "Italian", true, false, new String[]{"gluten", "dairy"}));
        save(new FoodItem(null, "Pasta Primavera", "Vegetarian pasta with seasonal vegetables", 13.99, 25, "Italian", true, false, new String[]{"gluten", "dairy"}));
        save(new FoodItem(null, "French Fries", "Crispy golden french fries", 4.99, 34, "American", true, true, new String[]{}));
        save(new FoodItem(null, "Caesar Salad", "Fresh romaine with parmesan and croutons", 9.99, 20, "American", true, false, new String[]{"gluten", "dairy"}));
        save(new FoodItem(null, "Veggie Burger", "Plant-based burger with fresh toppings", 11.99, 15, "American", true, false, new String[]{"gluten"}));
        save(new FoodItem(null, "Caprese Salad", "Tomato, mozzarella, and basil", 10.99, 18, "Italian", true, false, new String[]{"dairy"}));
    }

    public FoodItem save(FoodItem foodItem) {
        if (foodItem.getId() == null) {
            foodItem.setId(idGenerator.getAndIncrement());
        }
        foodItems.put(foodItem.getId(), foodItem);
        return foodItem;
    }

    public Optional<FoodItem> findById(Long id) {
        return Optional.ofNullable(foodItems.get(id));
    }

    public List<FoodItem> findAll() {
        return new ArrayList<>(foodItems.values());
    }

    public void deleteById(Long id) {
        foodItems.remove(id);
    }

    public boolean updateQuantity(Long id, Integer newQuantity) {
        FoodItem item = foodItems.get(id);
        if (item != null) {
            item.setQuantity(newQuantity);
            return true;
        }
        return false;
    }
}
