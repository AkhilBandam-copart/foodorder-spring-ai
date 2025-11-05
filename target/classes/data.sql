-- Sample Users with Different Demographics

-- Admin User
INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('admin001', 'Admin User', 'admin@foodorder.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN', 30, 'MALE', 'non-veg', 'American', 100.0, 300.0);

-- Normal Users
-- Males 18-25 (Young adults)
INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user001', 'John Smith', 'john.smith@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 22, 'MALE', 'non-veg', 'American', 50.0, 150.0);

INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user002', 'Mike Johnson', 'mike.johnson@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 24, 'MALE', 'non-veg', 'Fast Food', 40.0, 100.0);

-- Females 18-25
INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user003', 'Emily Davis', 'emily.davis@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 21, 'FEMALE', 'vegetarian', 'Italian', 60.0, 120.0);

INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user004', 'Sarah Wilson', 'sarah.wilson@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 23, 'FEMALE', 'vegan', 'Healthy', 50.0, 100.0);

-- Males 26-35 (Young professionals)
INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user005', 'David Brown', 'david.brown@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 28, 'MALE', 'non-veg', 'American', 80.0, 200.0);

INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user006', 'James Miller', 'james.miller@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 32, 'MALE', 'non-veg', 'Fast Food', 70.0, 150.0);

-- Females 26-35
INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user007', 'Lisa Anderson', 'lisa.anderson@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 29, 'FEMALE', 'vegetarian', 'Mediterranean', 70.0, 140.0);

INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user008', 'Jennifer Taylor', 'jennifer.taylor@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 31, 'FEMALE', 'non-veg', 'American', 80.0, 180.0);

-- Males 36-50 (Middle-aged)
INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user009', 'Robert Thomas', 'robert.thomas@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 42, 'MALE', 'non-veg', 'American', 100.0, 250.0);

INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user010', 'William Martinez', 'william.martinez@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 38, 'MALE', 'vegetarian', 'Asian', 90.0, 200.0);

-- Females 36-50
INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user011', 'Mary Garcia', 'mary.garcia@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 45, 'FEMALE', 'vegetarian', 'Healthy', 80.0, 150.0);

INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user012', 'Patricia Rodriguez', 'patricia.rodriguez@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 40, 'FEMALE', 'non-veg', 'American', 90.0, 180.0);

-- Males 50+ (Seniors)
INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user013', 'Richard Lee', 'richard.lee@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 55, 'MALE', 'non-veg', 'American', 70.0, 150.0);

INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user014', 'Charles Walker', 'charles.walker@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 62, 'MALE', 'vegetarian', 'Healthy', 60.0, 120.0);

-- Females 50+
INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user015', 'Linda Hall', 'linda.hall@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 58, 'FEMALE', 'vegetarian', 'Healthy', 70.0, 130.0);

INSERT INTO users (user_id, name, email, password, role, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user016', 'Barbara Allen', 'barbara.allen@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER', 65, 'FEMALE', 'non-veg', 'American', 60.0, 120.0);

-- Add allergens for some users
INSERT INTO user_allergens (user_id, allergen) VALUES ('user003', 'gluten');
INSERT INTO user_allergens (user_id, allergen) VALUES ('user004', 'dairy');
INSERT INTO user_allergens (user_id, allergen) VALUES ('user007', 'nuts');
INSERT INTO user_allergens (user_id, allergen) VALUES ('user011', 'gluten');
INSERT INTO user_allergens (user_id, allergen) VALUES ('user015', 'dairy');

-- Add past orders (showing food preferences by demographics)
-- Young males prefer burgers and pizza
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user001', 'Ham Burger');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user001', 'Chicken Pizza');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user002', 'Cheese Burger');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user002', 'KFC Wings');

-- Young females prefer vegetarian and lighter options
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user003', 'Sandwich');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user003', 'French Fries');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user004', 'Sandwich');

-- Professional males prefer variety
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user005', 'Ham Burger');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user005', 'Chicken Pizza');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user006', 'KFC Wings');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user006', 'French Fries');

-- Professional females balanced diet
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user007', 'Sandwich');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user008', 'Chicken Pizza');

-- Middle-aged prefer familiar items
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user009', 'Ham Burger');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user010', 'Sandwich');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user011', 'French Fries');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user012', 'Cheese Burger');

-- Seniors prefer healthier options
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user013', 'Sandwich');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user014', 'Sandwich');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user015', 'French Fries');
INSERT INTO user_past_orders (user_id, food_item_name) VALUES ('user016', 'Sandwich');

-- Add disliked foods for some users
INSERT INTO user_disliked_foods (user_id, food_name) VALUES ('user004', 'meat');
INSERT INTO user_disliked_foods (user_id, food_name) VALUES ('user007', 'spicy');
INSERT INTO user_disliked_foods (user_id, food_name) VALUES ('user014', 'fried');
