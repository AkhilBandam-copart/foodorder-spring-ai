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

import com.foodorder.ai.model.User;
import com.foodorder.ai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(String userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setName(updatedUser.getName());
            user.setAge(updatedUser.getAge());
            user.setGender(updatedUser.getGender());
            user.setDietaryPreference(updatedUser.getDietaryPreference());
            user.setAllergens(updatedUser.getAllergens());
            user.setPreferredCuisine(updatedUser.getPreferredCuisine());
            user.setDislikedFoods(updatedUser.getDislikedFoods());
            user.setBudgetMin(updatedUser.getBudgetMin());
            user.setBudgetMax(updatedUser.getBudgetMax());
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found: " + userId);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    public void addPastOrder(String userId, String foodItemName) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.getPastOrders().add(foodItemName);
            userRepository.save(user);
        }
    }

    public List<User> getUsersByDemographics(User.Gender gender, Integer age) {
        int ageMin = (age / 10) * 10;
        int ageMax = ageMin + 9;
        return userRepository.findByDemographics(gender, ageMin, ageMax);
    }

    public List<User> getUsersByDietaryPreference(String dietaryPreference) {
        return userRepository.findByDietaryPreference(dietaryPreference);
    }
}
