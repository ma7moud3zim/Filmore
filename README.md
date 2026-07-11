<div align="center">
  
# 🎬 Filmore

</div>


**A full-stack movie & TV show streaming platform, inspired by Netflix.**

Built with **Spring Boot** (REST API) and **Angular** (SPA), Filmore lets users browse a video library, search and filter content, manage a personal watchlist, and — for admins — manage videos and users through a dedicated admin panel.

[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-21-DD0031?logo=angular)](https://angular.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-3178C6?logo=typescript)](https://www.typescriptlang.org/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](#license)

</div>

---

## 📖 Table of Contents

- [About](#-about)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Backend Setup](#backend-setup-filmore)
  - [Frontend Setup](#frontend-setup-filmorefe)
- [Environment Variables](#-environment-variables)
- [API Overview](#-api-overview)
- [Screenshots](#-screenshots)
- [Contributing](#-contributing)
---

## 📌 About

**Filmore** is a full-stack web application for browsing, discovering, and organizing movies and TV shows. It's composed of two independent projects living in the same repository:

| Folder      | Description                                              |
|-------------|-----------------------------------------------------------|
| `filmore`   | Spring Boot REST API — authentication, video catalog, watchlist, user management |
| `filmorefe` | Angular single-page application — public site + admin dashboard |

The project was built to practice designing a production-style architecture: secured REST endpoints, role-based access control, paginated/searchable resources, and a responsive Angular client consuming it all.

---

## ✨ Features

### 🙋 For Viewers
- Browse a searchable, paginated **video library** (movies & TV shows)
- View video details (title, description, poster, etc.)
- **Add / remove videos from a personal watchlist ("Favorites")**
- Search the catalog and the watchlist by title or description
- Secure sign-up / sign-in with email verification

### 🛠️ For Admins
- Dedicated **Filmore Admin** dashboard
- Full **video library management** (upload, edit, organize content)
- **User management** — view and manage registered users
- Role-based access separating `USER` and `ADMIN` capabilities

### 🔐 Platform-wide
- JWT-based authentication & authorization
- Role-based route/endpoint protection (Spring Security)
- RESTful, paginated, and searchable API design
- Clean separation between public site and admin panel in the Angular app

---

## 🧰 Tech Stack

**Backend (`filmore`)**
- Java 17+
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA / Hibernate
- MySQL / PostgreSQL (relational database)
- Maven

**Frontend (`filmorefe`)**
- Angular 21 (standalone components)
- TypeScript
- Angular Material (dialogs, snackbars, etc.)
- RxJS
- SCSS / CSS

---

## 📁 Project Structure

```
Filmore/
├── filmore/                  # Spring Boot backend
│   ├── src/main/java/com/azim/filmore/
│   │   ├── controller/       # REST controllers (Video, Watchlist, User, Auth...)
│   │   ├── dao/              # Spring Data JPA repositories
│   │   ├── dto/
│   │   │   ├── request/
│   │   │   └── response/     # MessageResponse, PageResponse, VideoResponse...
│   │   ├── entity/            # User, Video, Role, ...
│   │   ├── enums/             # Role, etc.
│   │   ├── security/           # JWT filters, auth config
│   │   ├── service/            # Service interfaces
│   │   ├── serviceImpl/        # Service implementations
│   │   └── util/               # PaginationUtils, ServiceUtils, ...
│   └── pom.xml
│
├── filmorefe/                # Angular frontend
│   ├── src/app/
│   │   ├── admin/             # Filmore Admin dashboard (video & user management)
│   │   ├── core/              # Services (video, watchlist, auth, error-handler...)
│   │   ├── features/          # Public-facing pages (home, watchlist, auth...)
│   │   ├── shared/             # Shared components, models, pipes
│   │   └── environments/
│   ├── angular.json
│   └── package.json
│
└── README.md
```

> Folder names above reflect the current package layout observed in the repo; adjust as the project evolves.

---

## 🚀 Getting Started

### Prerequisites

Make sure you have the following installed:

- **Java 17+** and **Maven**
- **Node.js 18+** and **npm**
- **MySQL** (or your configured relational database)
- **Angular CLI** (`npm install -g @angular/cli`)

---

### Backend Setup (`filmore`)

1. **Clone the repository**
   ```bash
   git clone https://github.com/ma7moud3zim/Filmore.git
   cd Filmore/filmore
   ```

2. **Configure the database**

   Create a database (e.g. `filmore_db`) and update `src/main/resources/application.properties` (or `application.yml`):

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/filmore_db
   spring.datasource.username=your_db_username
   spring.datasource.password=your_db_password

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

   # JWT
   app.jwt.secret=your_jwt_secret
   app.jwt.expiration-ms=86400000
   ```

3. **Run the backend**
   ```bash
   ./mvnw spring-boot:run
   ```

   The API will start at **`http://localhost:8080`**.

---

### Frontend Setup (`filmorefe`)

1. **Navigate to the frontend folder**
   ```bash
   cd ../filmorefe
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Configure the API URL**

   Update `src/environments/environment.development.ts`:
   ```typescript
   export const environment = {
     production: false,
     apiUrl: 'http://localhost:8080/api'
   };
   ```

4. **Run the frontend**
   ```bash
   ng serve
   ```

   The app will be available at **`http://localhost:4200`**.

---

## 🔑 Environment Variables

| Variable                  | Location            | Description                              |
|---------------------------|---------------------|-------------------------------------------|
| `spring.datasource.url`   | `application.properties` | Database connection string           |
| `spring.datasource.username` | `application.properties` | Database username                |
| `spring.datasource.password` | `application.properties` | Database password                |
| `app.jwt.secret`          | `application.properties` | Secret key used to sign JWTs        |
| `apiUrl`                  | `environment.development.ts` | Base URL the Angular app calls |

---

## 📡 API Overview

All endpoints are prefixed with `/api`. Most endpoints require a valid JWT in the `Authorization: Bearer <token>` header.

| Method   | Endpoint                    | Description                          | Auth required |
|----------|------------------------------|----------------------------------------|:---:|
| `POST`   | `/api/auth/register`         | Register a new user                   | ❌ |
| `POST`   | `/api/auth/login`             | Authenticate and receive a JWT         | ❌ |
| `GET`    | `/api/videos`                 | Get paginated / searchable video list  | ✅ |
| `GET`    | `/api/videos/{id}`             | Get a single video's details            | ✅ |
| `POST`   | `/api/videos`                  | Upload/create a video *(admin)*         | ✅ (Admin) |
| `GET`    | `/api/watchlist`               | Get current user's watchlist            | ✅ |
| `POST`   | `/api/watchlist/{videoId}`      | Add a video to the watchlist            | ✅ |
| `DELETE` | `/api/watchlist/{videoId}`      | Remove a video from the watchlist       | ✅ |
| `GET`    | `/api/users`                    | List users *(admin)*                    | ✅ (Admin) |

---

## 🖼️ Screenshots

**Landing Page:** The page that comes to unlogged user in the first visit for the site.
<img width="1911" height="1200" alt="image" src="https://github.com/user-attachments/assets/116932b9-2237-4b36-a26b-712ab0cbd972" />


**Sign In Page:** The user must use it to sign in.
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/92edd42d-f53a-4693-bd2a-4760d56798a8" />


**Sign Up Page:** If the user have no account, He/She can use this page to create a new account.
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/c196f361-3311-4e3d-9dde-00455d3e54ac" />


**Manage Videos by Admin Page:** The Admin is using this page to upload, remove, edit videos.
<img width="1920" height="1193" alt="image" src="https://github.com/user-attachments/assets/a86ff902-0163-467f-acef-83644c18cc01" />


**Upload new Video Dialog:** After clicking on upload new videos this dialog will pop up allowing you to upload new video.
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/9932436e-c7c3-4971-9a92-4a796d4cc502" />


**The dialog:** The left data of the dialog.
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/b06bbd68-396d-424f-98f2-d5f0e96ec718" />


**Manage Users by Admin:** In this page the Admin can browse all the users signed up in the application.
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/f06cb863-aaf4-4670-9540-512093353d05" />


**Adding user Dialog:** The Admin can add a new user manually using this page.
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/4343c8aa-6597-4481-8233-fd3400ce2caf" />


**Editing Existing User:** The Admin can Edit existed user main data and assign its role to Admin but of course cannot manage the password and credential data.
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/468ef0d1-a2ab-4b00-94cf-fd952cf31aa1" />


**User Menu:** A menu that used to logout and convert to user, change password, or go to my favorites.
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/1a3fd91d-aedb-42e9-a7ca-a2adb75e6ff4" />


**Logout pop up:** When you click logout it gives you a logout confimation.
<img width="1920" height="1199" alt="image" src="https://github.com/user-attachments/assets/3047f48d-a3c2-4211-91fe-b6d8b134b09e" />


**The home page of user:** The page of videos that user got after signing in.
<img width="1920" height="1195" alt="image" src="https://github.com/user-attachments/assets/e46026e9-af16-46b5-ab2f-0a89289c752b" />


**My favorites Page:** The page of favorite movies of the user.
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/0154785a-7d3c-4e5e-a904-593dc4c3bbe6" />


**The video player of the movie:** After playing a movie from anywhere it gets you to this video player with some options you can manage video from.
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/6c67a31f-ce20-4a31-b827-322c5a05e2dc" />


**Change password:** The page of changing the user password and data.
<img width="1920" height="1076" alt="image" src="https://github.com/user-attachments/assets/cd185642-eb9e-45f8-87a4-c464c3989097" />



---


## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

1. Fork the project
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

<div align="center">

If you found this project interesting, consider giving it a ⭐!

</div>
