# Task Management System

A backend web application built to manage tasks, user assignments, and comments.

_**Note**_: This project was developed primarily for learning and educational purposes to understand core modern backend concepts.
## Description

The Task Management System is a RESTful API designed to track tasks and team collaboration. Through building this project, several core concepts of modern backend development were implemented, including secure user authentication, database persistence, and task management.

The application includes the following core functionalities:
- **User Registration**: Registering new users securely into the system.
- **Authentication**: Securing endpoints using JSON Web Tokens (JWT) and Spring Security.
- **Task Management**: Creating new tasks and updating their details.
- **Task Assignment**: Assigning tasks to specific users.
- **Commenting System**: Allowing users to leave comments on assigned tasks.

## Technologies Used

- **Java** & Spring Boot: Core framework for the application
- **Spring Web** (MVC): building RESTful APIs
- **Spring Security**: securing endpoints and handling authentication/authorization
- **Spring Data JPA & Hibernate**: database interactions and ORM
- **H2 Database**: Lightweight relational database used for storing data (runtime).
- **Lombok**: To reduce boilerplate code (getters, setters, constructors).
- **Gradle**: For dependency management and building the project.

## Project Structure Overview

This project is structured iteratively into learning stages:
- `Registering users`
- `Authenticating with JWT`
- `Creating tasks`
- `Assigning tasks`
- `Leaving comments`

## Setup

To run the application locally:

1. Ensure you have Java 17+ installed.
2. Clone the repository and navigate to the root directory.
3. Run the application with gradle (if you have gradle installed):
   ```bash
   gradle bootRun