# Food Order AI System

A Spring Boot application with AI-powered food recommendations using demographic analysis and user preferences.

## Features

- **User Profile Management**: Store and manage user profiles with demographics
- **Demographic-Based AI Recommendations**: Personalized recommendations based on age, gender, and preferences
- **H2 In-Memory Database**: Fast and lightweight data storage
- **Menu Management**: View and manage food items
- **Order Processing**: Place and track orders with real-time inventory updates
- **Order History Tracking**: Automatically track user orders for better recommendations
- **RESTful API**: Clean REST endpoints for all operations

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- OpenAI API Key (optional - falls back to smart mock AI)

## Setup

### Quick Start

1. **Clone and navigate to project**:
   ```bash
   cd /path/to/SpringAI
   ```

2. **Set your OpenAI API key** (optional):
   ```bash
   echo "OPENAI_API_KEY=your-api-key-here" > .env
   ```

3. **Run the application**:
   ```bash
   ./start.sh
   ```

The application will start on `http://localhost:8080`

### Manual Setup

1. Build the project:
   ```bash
   mvn clean install
   ```

2. Run the application:
   ```bash
   export OPENAI_API_KEY=your-key-here  # Optional
   mvn spring-boot:run
   ```

## Architecture

```
┌─────────────────┐
│   User Request  │
└────────┬────────┘
         │ userId
         ▼
┌─────────────────────┐
│  H2 Database        │
│  - User Profile     │
│  - Demographics     │
│  - Order History    │
└────────┬────────────┘
         │
         ▼
┌─────────────────────────┐
│ Demographic Analysis    │
│ "Males 25-35 prefer..." │
└────────┬────────────────┘
         │
         ▼
┌─────────────────────────┐
│  AI Recommendations     │
│  (OpenAI or Mock AI)    │
└─────────────────────────┘
```

## API Endpoints

### User Management

- **POST** `/api/users` - Create new user profile
- **GET** `/api/users/{userId}` - Get user profile
- **GET** `/api/users` - Get all users
- **PUT** `/api/users/{userId}` - Update user profile
- **DELETE** `/api/users/{userId}` - Delete user profile

**Create User Example:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "name": "John Doe",
    "age": 28,
    "gender": "MALE",
    "dietaryPreference": "non-veg",
    "allergens": ["peanuts"],
    "preferredCuisine": "American",
    "budgetMin": 50.0,
    "budgetMax": 150.0
  }'
```

### Menu Management

- **GET** `/api/menu` - Get all food items
- **GET** `/api/menu/{id}` - Get food item by ID
- **POST** `/api/menu` - Create new food item (Admin)
- **DELETE** `/api/menu/{id}` - Delete food item (Admin)

### AI Recommendations

- **POST** `/api/recommendations` - Get AI-powered food recommendations

**Request Body:**
```json
{
  "userPreferences": {
    "userId": "user001"
  }
}
```

The system will:
1. Fetch user profile from database (age, gender, preferences)
2. Analyze demographic patterns (what similar users order)
3. Apply personal preferences (dietary restrictions, allergens, budget)
4. Generate AI-powered recommendations

### Order Management

- **POST** `/api/orders` - Create new order
- **GET** `/api/orders/{orderId}` - Get order by ID
- **GET** `/api/orders/user/{userId}` - Get all orders for a user
- **GET** `/api/orders` - Get all orders (Admin)

**Create Order:**
```json
{
  "userId": "user001",
  "items": {
    "1": 2,
    "6": 1
  },
  "paymentMethod": "CASH"
}
```

## How Demographic-Based Recommendations Work

### User Demographics
The system analyzes users by:
- **Age Groups**: 18-25, 26-35, 36-50, 50+
- **Gender**: Male, Female, Other
- **Dietary Preferences**: Vegetarian, Vegan, Non-Veg
- **Past Order History**: What they've ordered before

### Recommendation Logic

1. **Fetch User Profile**: Get age, gender, preferences from database
2. **Demographic Analysis**: Find similar users (same age group + gender)
3. **Pattern Recognition**: What do similar users typically order?
4. **Personal Filters**: Apply individual preferences, allergens, budget
5. **AI Enhancement**: OpenAI refines recommendations with reasoning

### Sample Patterns in Dataset

- **Young Males (18-25)**: Prefer burgers, pizza, wings
- **Young Females (18-25)**: Prefer vegetarian options, sandwiches
- **Professionals (26-35)**: Balanced variety
- **Middle-aged (36-50)**: Familiar comfort foods
- **Seniors (50+)**: Healthier options, lighter meals

## Sample Users Included

The system comes with 16 pre-loaded users across demographics:

| User ID | Name | Age | Gender | Dietary | Past Orders |
|---------|------|-----|--------|---------|-------------|
| user001 | John Smith | 22 | MALE | non-veg | Ham Burger, Chicken Pizza |
| user003 | Emily Davis | 21 | FEMALE | vegetarian | Sandwich, French Fries |
| user005 | David Brown | 28 | MALE | non-veg | Ham Burger, Chicken Pizza |
| user007 | Lisa Anderson | 29 | FEMALE | vegetarian | Sandwich |
| user009 | Robert Thomas | 42 | MALE | non-veg | Ham Burger |
| user013 | Richard Lee | 55 | MALE | non-veg | Sandwich |

## Example Usage

### 1. Get All Users
```bash
curl http://localhost:8080/api/users
```

### 2. Get Specific User
```bash
curl http://localhost:8080/api/users/user001
```

### 3. Get Recommendations for User
```bash
curl -X POST http://localhost:8080/api/recommendations \
  -H "Content-Type: application/json" \
  -d '{
    "userPreferences": {
      "userId": "user001"
    }
  }'
```

### 4. Place Order
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user001",
    "items": {"1": 2, "6": 1},
    "paymentMethod": "CASH"
  }'
```

### 5. View Menu
```bash
curl http://localhost:8080/api/menu
```

## H2 Database Console

Access the H2 console for debugging:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:foodorderdb`
- Username: `sa`
- Password: _(leave blank)_

## Default Menu Items

1. Ham Burger - $120.23 (Non-Veg, Allergens: gluten, dairy)
2. Cheese Burger - $100.67 (Non-Veg, Allergens: gluten, dairy)
3. Sandwich - $720.83 (Vegetarian/Vegan, Allergens: gluten)
4. KFC Wings - $70.23 (Non-Veg, Allergens: gluten)
5. Chicken Pizza - $70.23 (Non-Veg, Allergens: gluten, dairy)
6. French Fries - $60.23 (Vegetarian/Vegan)

## Technology Stack

- **Spring Boot** 2.7.18
- **Spring Data JPA** - Database access
- **H2 Database** - In-memory database
- **OpenAI Java Client** 0.18.2
- **Maven** - Build tool
- **Lombok** - Reduce boilerplate code

## Project Structure

```
SpringAI/
├── src/main/java/com/foodorder/ai/
│   ├── FoodOrderAiApplication.java
│   ├── controller/
│   │   ├── FoodItemController.java
│   │   ├── OrderController.java
│   │   ├── RecommendationController.java
│   │   └── UserController.java
│   ├── service/
│   │   ├── FoodRecommendationService.java
│   │   ├── OrderService.java
│   │   └── UserService.java
│   ├── repository/
│   │   ├── FoodItemRepository.java
│   │   ├── OrderRepository.java
│   │   └── UserRepository.java
│   ├── model/
│   │   ├── User.java
│   │   ├── FoodItem.java
│   │   ├── Order.java
│   │   └── OrderItem.java
│   └── dto/
│       ├── UserPreferences.java
│       ├── RecommendationRequest.java
│       └── RecommendationResponse.java
├── src/main/resources/
│   ├── application.properties
│   └── data.sql (16 sample users)
├── pom.xml
├── start.sh
└── README.md
```

## Development

### Adding New Users

Users are auto-loaded from `data.sql` on startup. To add more:

```sql
INSERT INTO users (user_id, name, age, gender, dietary_preference, preferred_cuisine, budget_min, budget_max) 
VALUES ('user017', 'Jane Doe', 30, 'FEMALE', 'vegetarian', 'Italian', 60.0, 140.0);
```

### Modifying Recommendation Logic

See `FoodRecommendationService.java`:
- `mockAIResponse()` - Current demographic-based logic
- `callOpenAI()` - OpenAI integration
- `getUsersByDemographics()` - Demographic analysis

## Troubleshooting

**Issue**: `Port 8080 already in use`
```bash
lsof -ti:8080 | xargs kill -9
./start.sh
```

**Issue**: `OpenAI quota exceeded`
- System automatically falls back to mock AI
- Add billing to OpenAI account or use mock AI

**Issue**: `Database not initialized`
- Check `spring.jpa.defer-datasource-initialization=true` in properties
- Verify `data.sql` exists in `src/main/resources/`

## Future Enhancements

- [ ] Implement actual demographic-based recommendation algorithm
- [ ] Add machine learning model for better predictions
- [ ] Persistent database (PostgreSQL/MySQL)
- [ ] User authentication and authorization
- [ ] Real-time order tracking
- [ ] Payment gateway integration
- [ ] Admin dashboard

## License

Educational project - To be submitted to BITS (WILP Programme)

## Contact

For questions or issues, reach out to bandamakhilchandra@gmail.com.
