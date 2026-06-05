# SpringCart 🛒

A production-ready **ecommerce REST API** built with Spring Boot, featuring JWT authentication, OAuth2 social login, role-based access control, and full Docker support.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| Security | Spring Security, JWT, OAuth2 (Google, GitHub) |
| Database | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |
| Containerization | Docker, Docker Compose |
| Documentation | Swagger / OpenAPI 3 |
| Build Tool | Maven |

---

## Features

- **Authentication** — Signup/login with JWT, Google OAuth2, GitHub OAuth2
- **Role-based Access Control** — `ADMIN` and `CUSTOMER` roles with method-level `@PreAuthorize` security
- **Ownership Checks** — Users can only access their own carts, orders, addresses, and reviews
- **Product Catalog** — Categories with hierarchy, products with stock management, price range and keyword search
- **Cart System** — Add/remove/update items, bulk add, subtotal calculation
- **Order Management** — Place orders from cart, coupon application, order status lifecycle, cancel with stock restore
- **Payment** — COD and online payment support, refund on cancel/refund status
- **Address Management** — Multiple addresses, default address, soft delete with order history preservation
- **Coupon System** — Percentage/flat discounts, min order amount, usage limits, expiry validation
- **Reviews** — One review per user per product, rating and comment, average rating
- **Pagination & Sorting** — All list endpoints support page, size, and sort

---

## Project Structure

```
src/main/java/sumitproject/SpringCart/
├── Controller/        # REST controllers
├── Service/           # Service interfaces
├── ServiceImpl/       # Business logic
├── Entity/            # JPA entities
├── Repository/        # Spring Data repositories
├── DTO/               # Response DTOs
├── RequestDTO/        # Request DTOs
├── Mapper/            # Entity ↔ DTO mappers
├── Security/          # JWT, OAuth2, UserPrincipal, SecurityUtil
├── Helper/            # Enums (Role, OrderStatus, PaymentMethod...)
└── MyException/       # Custom exceptions
```

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/SpringCart.git
cd SpringCart
```

### 2. Configure environment

Create a `.env` file in the project root:

```env
DB_URL=jdbc:mysql://localhost:3306/springcart
DB_USERNAME=
DB_PASSWORD=

SPRING_GITHUB_CLIENT_ID=
SPRING_GITHUB_CLIENT_SECRET=

SPRING_GOOGLE_CLIENT_ID=
SPRING_GOOGLE_CLIENT_SECRET=

JWT_SECRET_KEY=
```

### 3. Run with Docker

```bash
docker-compose up --build
```

This starts both the Spring Boot app and MySQL database. The app will be available at `http://localhost:8080`.

### 4. Run locally without Docker

```bash
mvn clean package -DskipTests
java -jar target/*.jar
```

---

## API Documentation

### Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

### Authorize with JWT

1. Call `POST /api/auth/login` to get a token
2. Click **Authorize** button in Swagger UI
3. Enter: `Bearer <your-token>`
4. All secured endpoints are now accessible

### Explore API Online

[Open in Swagger Editor](https://editor.swagger.io/?url=https://raw.githubusercontent.com/yourusername/SpringCart/main/docs/swagger.json)

---

## API Overview

| Module | Base URL | Access |
|---|---|---|
| Auth | `/api/auth` | Public |
| Users | `/api/users` | Admin / Own profile |
| Products | `/api/products` | Public (read), Admin (write) |
| Categories | `/api/categories` | Public (read), Admin (write) |
| Cart | `/api/cart` | Customer / Admin |
| Cart Items | `/api/cart-items` | Customer (own cart) / Admin |
| Orders | `/api/orders` | Customer (own) / Admin |
| Order Items | `/api/order-items` | Customer (own order) / Admin |
| Payments | `/api/payments` | Customer (own order) / Admin |
| Addresses | `/api/addresses` | Customer (own) / Admin |
| Coupons | `/api/coupons` | Customer (apply), Admin (manage) |
| Reviews | `/api/reviews` | Public (read), Customer (write own) |

---

## Security Model

```
PUBLIC          → Product catalog, categories, reviews (read)
CUSTOMER        → Cart, orders, addresses, reviews (own data only)
ADMIN           → Full access to all endpoints
```

Ownership is enforced via `SecurityUtil` bean used in `@PreAuthorize`:

```java
@PreAuthorize("hasRole('ADMIN') or @securityUtil.cartVerify(#cartId, authentication)")
```

---

## Order Lifecycle

```
PENDING → CONFIRMED → SHIPPED → OUT_FOR_DELIVERY → DELIVERED
                                                         
Any status (except DELIVERED) → CANCELLED → REFUNDED
```

- Stock is restored automatically on cancel or refund
- Payment is refunded automatically if already paid
- Soft-deleted addresses are preserved in order history

---

## Docker

```bash
# Start everything
docker-compose up --build -d

# View logs
docker-compose logs -f

# Stop
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

Services:
- `springcart-app` — Spring Boot app on port `8080`
- `springcart-db` — MySQL 8 on port `3306` with named volume for persistence

---

## Environment Variables

| Variable | Description |
|---|---|
| `DB_URL` | JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET_KEY` | Secret key for JWT signing |
| `SPRING_GITHUB_CLIENT_ID` | GitHub OAuth2 client ID |
| `SPRING_GITHUB_CLIENT_SECRET` | GitHub OAuth2 client secret |
| `SPRING_GOOGLE_CLIENT_ID` | Google OAuth2 client ID |
| `SPRING_GOOGLE_CLIENT_SECRET` | Google OAuth2 client secret |

---

## Author

**Sumit**
Java Backend Developer
[GitHub](https://github.com/coder0030) · [Gmail](mishrasumit0530@gmail.com)