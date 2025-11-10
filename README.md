# Food Order AI System 🍕🤖

A production-ready Spring Boot application with **AI-powered food recommendations** using **collaborative filtering machine learning**, JWT authentication, and intelligent fallback systems.

## 🎯 Key Features

### Core Functionality
- **JWT Authentication & Authorization**: Secure user registration, login with role-based access (USER/ADMIN)
- **AI-Powered Recommendations**: Three-tier intelligence system (ML → Gemini → OpenAI → Rule-based)
- **Machine Learning**: Collaborative filtering using ALS (Alternating Least Squares) algorithm
- **Smart Cold-Start Handling**: Rule-based recommendations for new users, ML activates after 2+ orders
- **Real-time Order Processing**: Automatic inventory management and interaction tracking
- **Admin Analytics Dashboard**: Today's sales, orders, and top-selling items

### Intelligent Recommendation System
- **🧠 Machine Learning**: Learns from user behavior and similar users' preferences
- **📊 Collaborative Filtering**: "Users who ordered X also liked Y" pattern recognition
- **🎯 Personalized**: Considers dietary preferences, allergens, budget, and cuisine
- **🔄 Self-Improving**: Automatic daily retraining at 2 AM
- **✨ Graceful Fallback**: Never fails - falls back through AI layers to rule-based

## 🏗️ Architecture Overview

### User Flow
```
User Registration → Login (JWT) → Browse Menu → AI Recommendations → Place Order → ML Training
                                       ↓
                              Interaction Tracking
                                       ↓
                              ML Model Learns & Improves
```

### Recommendation Intelligence Layer
```
User Request
    ↓
Has ≥2 orders? ─NO──→ Rule-based Recommendations
    ↓                  (filter by preferences)
   YES
    ↓
ML Model Trained? ─NO──→ Train ALS Model (30s)
    ↓                         ↓
   YES                    Save Model
    ↓                         ↓
Generate Predictions ←────────┘
    ↓
Collaborative Filtering Scores
    ↓
Apply User Filters (diet, allergens, budget)
    ↓
Top 3 Personalized Items
```

### Backend Architecture
```
┌──────────────────────────────────────────────────────────────┐
│                     Spring Boot Application                   │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│  ┌─────────────┐  ┌──────────────┐  ┌──────────────────┐    │
│  │   Security  │  │  Controllers │  │    Services      │    │
│  │   (JWT)     │→ │  REST APIs   │→ │  Business Logic  │    │
│  └─────────────┘  └──────────────┘  └──────────────────┘    │
│                                              ↓                │
│  ┌──────────────────────────────────────────────────────┐    │
│  │         ML Recommendation Engine                      │    │
│  ├──────────────────────────────────────────────────────┤    │
│  │  • ALS Collaborative Filtering                       │    │
│  │  • Matrix Factorization (10 factors, 20 iterations)  │    │
│  │  • User-Item Interaction Tracking                    │    │
│  │  • Auto Stale Model Detection                        │    │
│  │  • Scheduled Retraining (Daily 2 AM)                 │    │
│  └──────────────────────────────────────────────────────┘    │
│                          ↓           ↓                        │
│  ┌──────────────────┐   ↓   ┌──────────────────┐            │
│  │  H2 Database     │←──────│  Model Storage   │            │
│  │  • Users         │        │  • user-factors  │            │
│  │  • Orders        │        │  • item-factors  │            │
│  │  • Interactions  │        │  • mappings      │            │
│  └──────────────────┘        └──────────────────┘            │
└──────────────────────────────────────────────────────────────┘
```

## 🚀 Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- (Optional) OpenAI API Key or Gemini API Key

### Installation

1. **Clone and navigate**:
   ```bash
   cd /path/to/SpringAI
   ```

2. **Set API keys** (optional):
   ```bash
   echo "OPENAI_API_KEY=your-key" > .env
   echo "GEMINI_API_KEY=your-key" >> .env
   ```

3. **Run**:
   ```bash
   chmod +x start.sh
   ./start.sh
   ```

The application starts on `http://localhost:8080`

### Manual Build
```bash
mvn clean install
mvn spring-boot:run
```

## 📡 API Endpoints

### Authentication
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login and get JWT token | No |

**Register Example:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "securepass",
    "age": 28,
    "gender": "MALE",
    "dietaryPreference": "VEGETARIAN",
    "preferredCuisine": "Italian",
    "budgetMin": 10.0,
    "budgetMax": 30.0,
    "allergens": ["nuts", "shellfish"]
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": "619b3360-3815-...",
  "email": "john@example.com",
  "name": "John Doe",
  "role": "USER"
}
```

### Menu Management
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/menu/items` | Get all food items | No |
| GET | `/api/menu/items/{id}` | Get item by ID | No |
| POST | `/api/menu/items` | Add new item | JWT (Admin) |
| DELETE | `/api/menu/items/{id}` | Delete item | JWT (Admin) |

### AI Recommendations 🤖
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/recommendations` | Get personalized recommendations | JWT |

**Request:**
```bash
curl -X POST http://localhost:8080/api/recommendations \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userPreferences": {
      "userId": "619b3360-3815-...",
      "age": 28,
      "gender": "MALE",
      "dietaryPreference": "VEGETARIAN",
      "preferredCuisine": "Italian",
      "budgetMin": 10.0,
      "budgetMax": 30.0,
      "allergens": ["nuts"]
    }
  }'
```

**Response (ML-based):**
```json
{
  "recommendedItems": [
    {
      "id": 5,
      "name": "Margherita Pizza",
      "description": "Classic pizza with tomato and mozzarella",
      "price": 14.99,
      "category": "Italian",
      "vegetarian": true,
      "allergens": ["gluten", "dairy"]
    },
    {
      "id": 6,
      "name": "Pasta Primavera",
      "price": 13.99,
      "category": "Italian",
      "vegetarian": true
    }
  ],
  "reasoning": "ML-based personalized recommendations based on your order history and similar users' preferences.",
  "estimatedTotal": 39.97
}
```

### Order Management
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/orders` | Place new order | JWT |
| GET | `/api/orders/user/{userId}` | Get user's orders | JWT |
| GET | `/api/orders/{orderId}` | Get order details | JWT |
| GET | `/api/orders` | Get all orders | JWT (Admin) |

**Place Order:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "619b3360-3815-...",
    "items": {
      "5": 1,
      "6": 2
    },
    "paymentMethod": "CREDIT_CARD"
  }'
```

### Admin Analytics
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/admin/analytics/today` | Today's analytics | JWT (Admin) |

**Response:**
```json
{
  "totalSales": 1247.89,
  "totalOrders": 42,
  "topSellingItems": [
    {"foodItemName": "Margherita Pizza", "totalQuantity": 28},
    {"foodItemName": "Pasta Primavera", "totalQuantity": 21}
  ]
}
```

## 🧠 Machine Learning Deep Dive

### How It Works

#### 1. **Data Collection** (Automatic)
Every order creates interaction records:
```java
Order Placed → InteractionTrackingService
    ↓
Saves to user_item_interactions table:
  - user_id: "619b3360..."
  - food_item_id: 5 (Pizza)
  - interaction_type: ORDER
  - rating: quantity × 1.0
  - timestamp: now()
```

#### 2. **ML Training** (Triggered Automatically)
**When:**
- First recommendation request from user with ≥2 orders
- Scheduled daily at 2 AM (cron: `0 0 2 * * ?`)

**Process:**
```
Step 1: Build Rating Matrix
  Load interactions → Create user-item matrix
  
  Example:
         Pizza  Pasta  Salad
  UserA [  2.0    1.0    0.0 ]
  UserB [  1.0    0.0    2.0 ]
  UserC [  0.0    2.0    1.0 ]

Step 2: ALS Algorithm (Collaborative Filtering)
  Initialize: UserFactors (random), ItemFactors (random)
  
  For 20 iterations:
    1. Fix ItemFactors, optimize UserFactors
    2. Fix UserFactors, optimize ItemFactors
    3. Minimize: ||R - U×I^T||² + λ||U||²
  
  Parameters:
    - Latent Factors: 10
    - Regularization: 0.01
    - Iterations: 20

Step 3: Save Model
  Save to ml-models/:
    - user-factors.bin (trained user vectors)
    - item-factors.bin (trained item vectors)
    - mappings.bin (user/item ID mappings)
```

#### 3. **Prediction** (Real-time)
```java
User requests recommendations
    ↓
Load user vector: [0.2, 0.5, 0.1, ..., 0.3]
    ↓
For each food item:
  score = dot_product(userVector, itemVector)
  
  Example:
    userVector = [0.2, 0.5, ..., 0.3]
    pizzaVector = [0.3, 0.4, ..., 0.2]
    score = 0.2×0.3 + 0.5×0.4 + ... = 3.21
    ↓
Sort items by score (descending)
    ↓
Apply filters: dietary, allergens, budget
    ↓
Return top 3 items
```

### Cold-Start Problem Handling
```
New User (0 orders)
    ↓
Rule-based recommendations
  - Filter by dietary preference
  - Check allergens
  - Apply budget constraints
  - Prefer selected cuisine
    ↓
Returns 3 matching items

After 2+ orders
    ↓
ML activates automatically!
    ↓
Learns from similar users
```

### Stale Model Detection
```java
On Application Startup:
    ↓
Load saved model from disk
    ↓
Check if model is stale:
  - Compare model's users with DB users
  - If mismatch detected → Invalidate model
  - Will retrain on next request
    ↓
Ensures model always matches current data
```

## 🔐 Security

### JWT Authentication
```
Login → Generate JWT Token
    ↓
Token contains:
  - User email (subject)
  - Role (USER/ADMIN)
  - Expiration (24 hours)
    ↓
Signed with HMAC-SHA256 + Secret Key
    ↓
Every request:
  JwtAuthenticationFilter validates token
    ↓
Extracts user, sets SecurityContext
```

### Password Security
- Passwords hashed with **BCrypt** (10 rounds)
- Never stored in plain text
- Verified during login with BCrypt.matches()

## 📊 Database Schema

### Core Tables
```sql
users
├─ user_id (PK, UUID)
├─ email (unique, indexed)
├─ password (BCrypt hash)
├─ name, age, gender, role
├─ dietary_preference, preferred_cuisine
└─ budget_min, budget_max

user_item_interactions ← 🔥 ML Training Data
├─ id (PK, auto-increment)
├─ user_id (FK → users)
├─ food_item_id (FK → food_items)
├─ interaction_type (ORDER/VIEW/RATING)
├─ quantity, rating
└─ timestamp (indexed for queries)

orders
├─ id (PK)
├─ user_id (FK → users)
├─ total_amount, status
├─ payment_method
└─ created_at (indexed)

order_items
├─ id (PK)
├─ order_id (FK → orders)
├─ food_item_id, food_item_name
├─ quantity, price
└─ (denormalized for performance)

food_items
├─ id (PK)
├─ name, description, price
├─ category, quantity (stock)
├─ vegetarian, vegan (boolean)
└─ allergens (JSON array)
```

## 🛠️ Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 2.7.18 |
| **Language** | Java | 11+ |
| **Build Tool** | Maven | 3.6+ |
| **Database** | H2 (in-memory) | 2.1.214 |
| **Security** | Spring Security + JWT | 5.7.11 |
| **ML Library** | Smile (Statistical ML) | 3.0.2 |
| **AI APIs** | OpenAI, Google Gemini | 0.18.2 |
| **ORM** | Spring Data JPA + Hibernate | 5.6.15 |

## 📁 Project Structure

```
SpringAI/
├── src/main/java/com/foodorder/ai/
│   ├── FoodOrderAiApplication.java          # Main entry point
│   │
│   ├── config/
│   │   ├── JwtAuthenticationFilter.java     # JWT validation filter
│   │   ├── JwtUtil.java                     # JWT utilities
│   │   └── SecurityConfig.java              # Security configuration
│   │
│   ├── controller/
│   │   ├── AuthController.java              # Login/Register
│   │   ├── FoodItemController.java          # Menu endpoints
│   │   ├── OrderController.java             # Order endpoints
│   │   ├── RecommendationController.java    # AI recommendations
│   │   ├── UserController.java              # User profile
│   │   └── AdminController.java             # Analytics
│   │
│   ├── service/
│   │   ├── AuthService.java                 # Authentication logic
│   │   ├── FoodRecommendationService.java   # Recommendation engine
│   │   ├── MLRecommendationService.java     # ALS ML model
│   │   ├── InteractionTrackingService.java  # Track user behavior
│   │   ├── OrderService.java                # Order processing
│   │   ├── UserService.java                 # User management
│   │   ├── GeminiService.java               # Google Gemini API
│   │   └── CustomUserDetailsService.java    # Spring Security
│   │
│   ├── ml/
│   │   ├── MatrixBuilder.java               # Build rating matrices
│   │   └── ModelPersistence.java            # Save/load models
│   │
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── FoodItemRepository.java
│   │   ├── OrderRepository.java
│   │   └── InteractionRepository.java       # ML data queries
│   │
│   ├── model/
│   │   ├── User.java                        # User entity
│   │   ├── FoodItem.java                    # Menu item entity
│   │   ├── Order.java, OrderItem.java       # Order entities
│   │   └── UserItemInteraction.java         # ML training data
│   │
│   └── dto/
│       ├── AuthResponse.java, LoginRequest.java
│       ├── RegisterRequest.java
│       ├── UserPreferences.java
│       ├── RecommendationRequest.java
│       ├── RecommendationResponse.java
│       └── OrderRequest.java
│
├── src/main/resources/
│   ├── application.properties               # Configuration
│   ├── data.sql                             # Sample food items
│   └── interactions-data.sql                # ML training data
│
├── ml-models/                                # Trained models (auto-generated)
│   ├── user-factors.bin
│   ├── item-factors.bin
│   └── mappings.bin
│
├── pom.xml                                   # Maven dependencies
├── start.sh                                  # Quick start script
├── clean-ml-models.sh                        # Clean ML cache
└── README.md
```

## 🎮 Complete Usage Example

### Scenario: New User "Alice" Gets Personalized Recommendations

```bash
# Day 1: Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice",
    "email": "alice@example.com",
    "password": "secure123",
    "age": 28,
    "gender": "FEMALE",
    "dietaryPreference": "VEGETARIAN",
    "preferredCuisine": "Italian",
    "budgetMin": 10.0,
    "budgetMax": 30.0,
    "allergens": ["nuts"]
  }'

# Save token
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# Get recommendations (rule-based, since 0 orders)
curl -X POST http://localhost:8080/api/recommendations \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"userPreferences": {"userId": "..."}}'

# Response: Vegetarian Italian items within budget

# Place first order
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "userId": "...",
    "items": {"5": 1, "6": 1},
    "paymentMethod": "CREDIT_CARD"
  }'

# Day 2: Place second order
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "userId": "...",
    "items": {"10": 2},
    "paymentMethod": "CREDIT_CARD"
  }'

# Day 3: Get recommendations (ML ACTIVATED! ≥2 orders)
curl -X POST http://localhost:8080/api/recommendations \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"userPreferences": {"userId": "..."}}'

# Response: ML-based recommendations!
# - Learns from Alice's orders
# - Considers similar users (vegetarian, 25-35, Italian)
# - Returns personalized top 3 items
```

### Backend Processing:
```
1st Request (0 orders) → Rule-based
2nd Request (2 orders) → ML trains (30s) → Personalized results
3rd Request (2 orders) → ML cached → Instant results (<100ms)
```

## 🔧 Configuration

### Application Properties
```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:h2:mem:foodorderdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.defer-datasource-initialization=true

# JWT
jwt.secret=your-secret-key-here
jwt.expiration=86400000  # 24 hours

# AI APIs (optional)
openai.api.key=${OPENAI_API_KEY:}
gemini.api.key=${GEMINI_API_KEY:}

# ML Scheduling
spring.task.scheduling.enabled=true  # Daily retraining at 2 AM
```

## 🐛 Troubleshooting

### Port Already in Use
```bash
lsof -ti:8080 | xargs kill -9
mvn spring-boot:run
```

### Clean ML Models (if stale)
```bash
./clean-ml-models.sh
mvn spring-boot:run
```

### JWT Token Expired
```bash
# Login again to get new token
curl -X POST http://localhost:8080/api/auth/login \
  -d '{"email": "user@example.com", "password": "pass"}'
```

### Database Not Loading Data
- Check `spring.jpa.defer-datasource-initialization=true`
- Verify `data.sql` exists in `resources/`
- Check logs for SQL errors

## 🎯 Key Features Summary

✅ **Production-Ready ML System**
- Collaborative filtering with ALS algorithm
- Automatic training and retraining
- Stale model detection
- Cold-start handling

✅ **Secure & Scalable**
- JWT authentication
- BCrypt password hashing
- Role-based access control
- RESTful API design

✅ **Intelligent Recommendations**
- Three-tier fallback system
- Personalized filtering
- Real-time predictions
- Continuous learning

✅ **Complete E-Commerce Flow**
- User registration/login
- Menu browsing
- AI recommendations
- Order processing
- Admin analytics

## 📈 Future Enhancements

- [ ] Persistent database (PostgreSQL)
- [ ] Redis caching for ML predictions
- [ ] Real-time WebSocket notifications
- [ ] Payment gateway integration
- [ ] Mobile app (React Native)
- [ ] A/B testing framework
- [ ] Advanced ML features (deep learning)
- [ ] Multi-language support

## 📄 License

MIT License - Educational project for BITS WILP Programme

## 👨‍💻 Author

**Akhil Chandra Bandam**  
📧 bandamakhilchandra@gmail.com  
🎓 BITS (WILP Programme)

---

**Built with ❤️ using Spring Boot, Machine Learning, and AI APIs**
