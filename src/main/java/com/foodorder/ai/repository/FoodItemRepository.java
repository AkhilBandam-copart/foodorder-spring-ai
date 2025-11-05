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
        save(new FoodItem(null, "Ham Burger", "Classic ham burger with fresh vegetables", 120.23, 23, "Burger", false, false, new String[]{"gluten", "dairy"}));
        save(new FoodItem(null, "Cheese Burger", "Delicious cheese burger with melted cheese", 100.67, 13, "Burger", false, false, new String[]{"gluten", "dairy"}));
        save(new FoodItem(null, "Sandwich", "Fresh vegetable sandwich", 720.83, 8, "Sandwich", true, true, new String[]{"gluten"}));
        save(new FoodItem(null, "KFC Wings", "Crispy fried chicken wings", 70.23, 46, "Chicken", false, false, new String[]{"gluten"}));
        save(new FoodItem(null, "Chicken Pizza", "Large chicken pizza with toppings", 70.23, 46, "Pizza", false, false, new String[]{"gluten", "dairy"}));
        save(new FoodItem(null, "French Fries", "Crispy golden french fries", 60.23, 34, "Sides", true, true, new String[]{}));
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
