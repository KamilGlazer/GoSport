<h1>GoSport - Social Platform for Athletes & Trainers 🏋️‍♂️🏃‍♀️</h1>

<p>GoSport is a full-featured backend application for a social networking platform connecting athletes and trainers. Built with Spring Boot, it offers user registration, profiles, posts, comments, messaging, notifications, and trainer discovery based on location.</p>

### ✨ Features

<hr>

- JWT-based Authentication

- User Profile Management

- Post Creation, Likes & Comments

- Private Messaging

- Notifications System

- Friend Connection Requests

- Trainer Registration and Search

- Avatar Upload & Image Serving

- Swagger (OpenAPI 3) documentation

### 🛠 Technologies 
<hr>

- Java 17

- Spring Boot 3.4.3

- Spring Security + JWT

- Spring Data JPA + Hibernate

- MySQL

- Lombok

- Swagger/OpenAPI 

- React + Redux 

### 📁 Project Structure
<hr>

<pre><code>
GoSport
│── config             # Security, JWT, Swagger config
│── controller         # REST API Controllers
│── domain             # Enums and constants
│── dto                # Request & response DTOs
│   ├── request
│   ├── response
│── exception          # Global exception handling
│── model              # JPA Entities
│── repository         # Spring Data JPA interfaces
│── service            # Business logic interfaces
│   ├── impl           # Service implementations
│── Application.java   # Spring Boot entry point
</code></pre>

### 🚀 Installation & Setup
<hr>

1. Clone the repository:
<pre><code>git clone https://github.com/KamilGlazer/GoSport.git
cd GoSport
</code></pre>



### 📡 API Endpoints Overview
<hr>

🔐 Authentication
- **POST** `/api/auth/register` – Register new user

- **POST** `/api/auth/login` – Log in and receive JWT token

- **GET** `/api/auth/validate` – Validate current token

👤 User Profile
- **GET** `/api/profile` – Get current user's profile

- **PUT** `/api/profile` – Update current user's profile

- **GET** `/api/profile/{id}` – Get public profile by user ID

- **POST** `/api/profile/upload-avatar` – Upload avatar image

- **GET** `/api/profile/avatar` – Get current user's avatar

💬 Comments
- **POST** `/api/comments` – Add a comment to a post

- **GET** `/api/comments/{postId}` – Get comments for a specific post

- **DELETE** `/api/comments/delete/{id}` – Delete a comment by ID

📝 Posts
- **POST** `/api/posts` – Create a new post

- **GET** `/api/posts/my` – Get posts created by current user

- **GET** `/api/posts/feed` – Get post feed from connected users

- **DELETE** `/api/posts/delete/{id}` – Delete a post by ID

- **POST** `/api/posts/{postId}/like` – Toggle like for a post

📨 Messaging
- **GET** `/api/messages/getConnected` – Get all users connected to current user

- **POST** `/api/messages/send` – Send a private message to another user

- **GET** `/api/messages/with/{userId}` – Get all messages exchanged with a specific user

🔔 Notifications
- **GET** `/api/notifications/unread/count` – Get count of unread notifications

- **GET** `/api/notifications` – Get all notifications for current user

- **PATCH** `/api/notifications` – Mark all notifications as read

🧑‍🤝‍🧑 Friend Connections
- **POST** `/api/connection/{id}` – Send a connection request to a user

- **PATCH** `/api/connection/{id}?action=accept|reject` – Accept or reject a connection request

🔎 User Search
- **GET** `/api/search?query=...` – Search users by first name or last name

🧑‍🏫 Trainer Features
- **POST** `/api/trainers/toggle` – Toggle current user's trainer status (on/off)

- **GET** `/api/trainers/statu` – Check if current user is marked as trainer

- **GET**` /api/trainers/search?city=...&postalCode=...` – Search for trainers by city and/or postal code


### 🔮 Future Improvements
- Email verification and password recovery

- Admin panel for user moderation

- Trainer certifications & reviews



