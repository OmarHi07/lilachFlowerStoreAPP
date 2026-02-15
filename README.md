<div align="center">

# 🌸 Lilach Flower Store – Information System

### Developed by:  
**Omar Hijab**  
+ 5 Additional Students  

---

### A Full-Stack Client–Server Information System for Managing an Online Flower Store

Built as part of a university Software Engineering project.

---

</div>

---

## 📌 Project Overview

Lilach Flower Store is a multi-tier information system designed to support the complete operation of an online flower store, including:

- User management (Customers, Workers, Managers, System Managers)  
- Product catalog and flower inventory management  
- Order processing and payment handling  
- Subscriptions, discounts, and customer benefits  
- Administrative control panels and reports  

The system emphasizes modular design, scalability, and separation of concerns.

---

## 🏗 System Architecture

The project is implemented using a **Client–Server architecture** with clear separation between:

- Client application (JavaFX GUI)  
- Server application (Business logic & communication layer)  
- Database (MySQL with ORM)

Communication between client and server is performed using the **OCSF framework**.  
The client side uses **EventBus** to implement the **Mediator design pattern** for decoupled communication between controllers and networking components.

---

## 📂 Project Structure

Pay attention to the three main modules:

1. **client**  
   A JavaFX client built using OCSF.  
   Uses EventBus (Mediator Pattern) to pass events between classes (e.g., between the network client and GUI controllers).

2. **server**  
   A server built using OCSF that handles business logic, validation, and database access.

3. **entities**  
   A shared module containing all project entities and data transfer objects.

---

## 🛠 Technologies Used

- Java  
- JavaFX & Scene Builder  
- MySQL  
- Hibernate (ORM)  
- OCSF (Object Client–Server Framework)  
- EventBus (Mediator Pattern)  
- MVC Architecture  

---

## ✨ Main Features

- Secure login and role-based access control  
- Graphical user interface for all user roles  
- Order creation, tracking, and history  
- Inventory and product management  
- Subscription management  
- Administrative dashboards  

---

## ▶ Running the Project

1. Run **Maven install** in the parent project.  
2. Run the server using the `exec:java` goal in the server module.  
3. Run the client using the `javafx:run` goal in the client module.  
4. Launch the client and start interacting with the system.

---

## 👥 Team

This project was developed collaboratively by:

- **Omar Hijab**  
- 5 Additional Students  

---

<div align="center">

⭐ Academic Project – University of Haifa  

</div>
