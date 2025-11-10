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

import java.io.*;
import java.util.Map;

public class ModelPersistence {
    
    private static final String MODEL_DIR = "ml-models/";
    
    public static void saveModel(Object model, String filename) throws IOException {
        File dir = new File(MODEL_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(MODEL_DIR + filename))) {
            oos.writeObject(model);
        }
    }
    
    public static Object loadModel(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(MODEL_DIR + filename))) {
            return ois.readObject();
        }
    }
    
    public static void saveMappings(Map<String, Integer> userMapping, 
                                   Map<Long, Integer> itemMapping,
                                   String filename) throws IOException {
        File dir = new File(MODEL_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(MODEL_DIR + filename))) {
            oos.writeObject(userMapping);
            oos.writeObject(itemMapping);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadMappings(String filename) 
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(MODEL_DIR + filename))) {
            Map<String, Integer> userMapping = (Map<String, Integer>) ois.readObject();
            Map<Long, Integer> itemMapping = (Map<Long, Integer>) ois.readObject();
            
            return Map.of(
                "userMapping", userMapping,
                "itemMapping", itemMapping
            );
        }
    }
    
    public static boolean modelExists(String filename) {
        File file = new File(MODEL_DIR + filename);
        return file.exists();
    }
}
