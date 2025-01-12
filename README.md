# Christmas Market: Full-Stack System

This project is a full-stack system for managing customers, vendors, items, shopping carts, notifications, and events. It provides a React-based frontend with TypeScript and Tailwind CSS, and a backend powered by Java, Spring Boot, and Hibernate. The system is hosted on **Vercel** (frontend) and **Render** (backend).

---

## Features

- **User Authentication**: Register, login, and logout for customers and vendors.
- **Customer Management**: Manage customer profiles and balance.
- **Vendor Management**: Manage vendor profiles and inventory.
- **Item Management**: CRUD operations for items and vendor items.
- **Shopping Cart**: Add, remove, and checkout items in a cart using the **Command** pattern.
- **Notification System**: Subscribe/unsubscribe and receive notifications about item availability and discounts using the **Observer** pattern.
- **Event System**: Trigger special events like discounts, gifts, and bonuses.

---

## Tech Stack

### Frontend
- **React** with **TypeScript** for building a responsive and interactive user interface.
- **Tailwind CSS** for styling.
- Hosted on **Vercel**.

### Backend
- **Spring Boot** with **Hibernate** for RESTful API development.
- Hosted on **Render**.

### Tools & Technologies
- REST API
- Maven for dependency management.
- Session-based authentication using Jakarta Servlet.

---

## How to Run

- Visit: https://christmas-market-frontend-5gw9.vercel.app/register

## How to Run Locally:

### Run with Docker
1. Create a `.env` file with database credentials:
   ```env
   DB_HOST=localhost
   DB_PORT=5432
   DB_NAME=your_database_name
   DB_USER=your_database_user
   DB_PASSWORD=your_database_password
2. Build and run the Docker container:
   ```
   docker build -t christmas-market-backend .
   docker run --env-file .env -p 8080:8080 christmas-market-backend
### Run Locally Without Docker
1. Ensure PostgreSQL is running and configured as per application.properties.
2. Build and run:
   ```
   ./gradlew clean build
    java -jar build/libs/app.jar

### Run Frontend Locally
1. Navigate to the frontend directory:
   ```
   cd frontend/christmas-market-website
2. Install dependencies:
   ```
   npm install
3. Start the development server:
   ```
   npm run dev

## Notes
- Backend APIs are accessible at http://localhost:8080.
- Frontend runs at http://localhost:5173.