-- Populate historical interactions from user past orders
-- This creates synthetic interaction data for ML training

-- User001 (John Smith) - Non-veg, American
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user001', 1, 'ORDER', 2.0, 2, DATEADD('DAY', -10, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user001', 5, 'ORDER', 1.0, 1, DATEADD('DAY', -5, CURRENT_TIMESTAMP));

-- User002 (Mike Johnson) - Non-veg, Fast Food
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user002', 2, 'ORDER', 3.0, 3, DATEADD('DAY', -8, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user002', 4, 'ORDER', 2.0, 2, DATEADD('DAY', -3, CURRENT_TIMESTAMP));

-- User003 (Emily Davis) - Vegetarian, Italian
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user003', 3, 'ORDER', 2.0, 2, DATEADD('DAY', -12, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user003', 7, 'ORDER', 1.0, 1, DATEADD('DAY', -6, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user003', 5, 'ORDER', 1.0, 1, DATEADD('DAY', -2, CURRENT_TIMESTAMP));

-- User004 (Sarah Wilson) - Vegan, Healthy
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user004', 3, 'ORDER', 2.0, 2, DATEADD('DAY', -9, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user004', 7, 'ORDER', 3.0, 3, DATEADD('DAY', -4, CURRENT_TIMESTAMP));

-- User005 (David Brown) - Non-veg, American
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user005', 1, 'ORDER', 1.0, 1, DATEADD('DAY', -11, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user005', 5, 'ORDER', 2.0, 2, DATEADD('DAY', -7, CURRENT_TIMESTAMP));

-- User006 (James Miller) - Non-veg, Fast Food  
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user006', 4, 'ORDER', 2.0, 2, DATEADD('DAY', -10, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user006', 7, 'ORDER', 1.0, 1, DATEADD('DAY', -5, CURRENT_TIMESTAMP));

-- User007 (Lisa Anderson) - Vegetarian, Mediterranean
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user007', 3, 'ORDER', 1.0, 1, DATEADD('DAY', -13, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user007', 8, 'ORDER', 2.0, 2, DATEADD('DAY', -6, CURRENT_TIMESTAMP));

-- User008 (Jennifer Taylor) - Non-veg, American
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user008', 5, 'ORDER', 1.0, 1, DATEADD('DAY', -8, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user008', 1, 'ORDER', 2.0, 2, DATEADD('DAY', -3, CURRENT_TIMESTAMP));

-- User009 (Robert Thomas) - Non-veg, American
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user009', 1, 'ORDER', 3.0, 3, DATEADD('DAY', -12, CURRENT_TIMESTAMP));

-- User010 (William Martinez) - Vegetarian, Asian
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user010', 3, 'ORDER', 1.0, 1, DATEADD('DAY', -9, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user010', 9, 'ORDER', 2.0, 2, DATEADD('DAY', -4, CURRENT_TIMESTAMP));

-- User011 (Mary Garcia) - Vegetarian, Healthy
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user011', 7, 'ORDER', 2.0, 2, DATEADD('DAY', -11, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user011', 8, 'ORDER', 1.0, 1, DATEADD('DAY', -6, CURRENT_TIMESTAMP));

-- User012 (Patricia Rodriguez) - Non-veg, American
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user012', 2, 'ORDER', 2.0, 2, DATEADD('DAY', -10, CURRENT_TIMESTAMP));

-- User013 (Richard Lee) - Non-veg, American
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user013', 3, 'ORDER', 1.0, 1, DATEADD('DAY', -7, CURRENT_TIMESTAMP));

-- User014 (Charles Walker) - Vegetarian, Healthy
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user014', 3, 'ORDER', 2.0, 2, DATEADD('DAY', -13, CURRENT_TIMESTAMP));
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user014', 7, 'ORDER', 1.0, 1, DATEADD('DAY', -8, CURRENT_TIMESTAMP));

-- User015 (Linda Hall) - Vegetarian, Healthy
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user015', 7, 'ORDER', 3.0, 3, DATEADD('DAY', -9, CURRENT_TIMESTAMP));

-- User016 (Barbara Allen) - Non-veg, American
INSERT INTO user_item_interactions (user_id, food_item_id, interaction_type, rating, quantity, timestamp) 
VALUES ('user016', 3, 'ORDER', 1.0, 1, DATEADD('DAY', -12, CURRENT_TIMESTAMP));
