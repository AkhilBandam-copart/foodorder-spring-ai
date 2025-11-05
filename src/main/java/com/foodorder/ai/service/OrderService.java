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

import com.foodorder.ai.dto.OrderRequest;
import com.foodorder.ai.model.FoodItem;
import com.foodorder.ai.model.Order;
import com.foodorder.ai.model.OrderItem;
import com.foodorder.ai.repository.FoodItemRepository;
import com.foodorder.ai.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    public Order createOrder(OrderRequest request) {
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (var entry : request.getItems().entrySet()) {
            Long foodItemId = entry.getKey();
            Integer quantity = entry.getValue();

            Optional<FoodItem> foodItemOpt = foodItemRepository.findById(foodItemId);
            if (foodItemOpt.isPresent()) {
                FoodItem foodItem = foodItemOpt.get();

                if (foodItem.getQuantity() < quantity) {
                    throw new RuntimeException("Insufficient stock for " + foodItem.getName());
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setFoodItemId(foodItem.getId());
                orderItem.setFoodItemName(foodItem.getName());
                orderItem.setQuantity(quantity);
                orderItem.setPrice(foodItem.getPrice());
                
                orderItems.add(orderItem);
                totalAmount += foodItem.getPrice() * quantity;

                foodItemRepository.updateQuantity(foodItemId, foodItem.getQuantity() - quantity);
            } else {
                throw new RuntimeException("Food item not found: " + foodItemId);
            }
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setStatus("CONFIRMED");
        order.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Double getTodaySales() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        Double sales = orderRepository.getTotalSalesBetween(startOfDay, endOfDay);
        return sales != null ? sales : 0.0;
    }
    
    public List<Map<String, Object>> getTopSellingItemsToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        List<Object[]> results = orderRepository.getTopSellingItemsBetween(startOfDay, endOfDay);
        
        return results.stream()
            .map(result -> {
                Map<String, Object> item = new HashMap<>();
                item.put("foodItemName", result[0]);
                item.put("totalQuantity", result[1]);
                return item;
            })
            .collect(Collectors.toList());
    }
    
    public Integer getTotalOrdersToday() {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return orderRepository.findByCreatedAtBetween(startOfDay, endOfDay).size();
    }
    
    public Map<String, Object> getTodayAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalSales", getTodaySales());
        analytics.put("totalOrders", getTotalOrdersToday());
        analytics.put("topSellingItems", getTopSellingItemsToday());
        return analytics;
    }
}
