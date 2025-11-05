/*
 * MIT License
 *
 * Copyright (c) 2025 Food Order AI System
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

package com.foodorder.ai.controller;

import com.foodorder.ai.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/analytics/today")
    public ResponseEntity<Map<String, Object>> getTodayAnalytics() {
        Map<String, Object> analytics = orderService.getTodayAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/sales/today")
    public ResponseEntity<Map<String, Double>> getTodaySales() {
        Double sales = orderService.getTodaySales();
        return ResponseEntity.ok(Map.of("totalSales", sales));
    }
    
    @GetMapping("/top-selling/today")
    public ResponseEntity<List<Map<String, Object>>> getTopSellingToday() {
        List<Map<String, Object>> topItems = orderService.getTopSellingItemsToday();
        return ResponseEntity.ok(topItems);
    }
    
    @GetMapping("/orders/today")
    public ResponseEntity<Map<String, Integer>> getTotalOrdersToday() {
        Integer totalOrders = orderService.getTotalOrdersToday();
        return ResponseEntity.ok(Map.of("totalOrders", totalOrders));
    }
}
