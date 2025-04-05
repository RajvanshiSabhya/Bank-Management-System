# Bank-Management-System

## Overview
The **Bank Management System** is a software application designed to facilitate banking operations such as account creation, login authentication, balance inquiry, money withdrawal, deposit, and fund transfers. The system is built using Java for backend logic and MySQL for secure data storage. It ensures a smooth and secure banking experience with a well-structured database and efficient functionalities.

## Features
- **Account Creation**: Allows users to register by providing necessary personal and banking details.
- **Login System**: Secure authentication mechanism using hashed credentials.
- **Balance Inquiry**: Users can check their account balance in real-time.
- **Money Withdrawal**: Enables users to withdraw money while ensuring sufficient balance.
- **Money Deposit**: Users can deposit money into their accounts.
- **Fund Transfer**: Securely transfer money between accounts.
- **User Verification**: Checks if a user exists before performing transactions.
- **Database Management**: Uses MySQL to store and manage account data efficiently.

## Technologies Used
- **Programming Language**: Java
- **Database**: MySQL
- **Database Connection**: JDBC
- **IDE**: IntelliJ IDEA / Eclipse / NetBeans

## Installation
1. **Clone the Repository**
   ```sh
   git clone https://github.com/yourusername/Bank-Management-System.git
   cd Bank-Management-System
   ```

2. **Set Up Database**
   - Import the provided SQL script into MySQL.
   - Update database credentials in the Java files.

3. **Run the Application**
   - Open the project in an IDE.
   - Compile and execute the main class.

## Database Schema
The MySQL database consists of tables for storing user details, account information, and transaction history. The key tables include:
- `users`: Stores login credentials.
- `accounts`: Stores account details.
- `transactions`: Records all financial transactions.

## SQL Queries Used
- **Creating Users Table**:
   ```sql
   CREATE TABLE users (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(50) UNIQUE, password VARCHAR(255));
   ```
- **Creating Accounts Table**:
   ```sql
   CREATE TABLE accounts (id INT PRIMARY KEY AUTO_INCREMENT, user_id INT, balance DECIMAL(10,2), FOREIGN KEY (user_id) REFERENCES users(id));
   ```
- **Insert New Account**:
   ```sql
   INSERT INTO accounts (user_id, balance) VALUES (?, ?);
   ```

## Testing
- **Test Cases**
  - Verify account creation process.
  - Check authentication for correct/incorrect credentials.
  - Validate deposit and withdrawal operations.
  - Ensure balance updates after transactions.
  - Test money transfer between accounts.

- **Testing Results**
  - Login authentication successfully prevents unauthorized access.
  - Transactions accurately update account balances.
  - Invalid withdrawals are prevented when funds are insufficient.

## Challenges Faced
- **Security Concerns**: SQL injection risks were mitigated by using prepared statements.
- **Concurrency Handling**: Implemented transaction locks to prevent race conditions.
- **Database Optimization**: Indexed frequently queried columns to improve performance.

## Future Enhancements
- Implement a graphical user interface (GUI) for better user experience.
- Integrate mobile banking features.
- Enhance security with multi-factor authentication.
- Use blockchain for transaction transparency.

## Contributors
- **Sabhya Rajvanshi** (Project Lead & Developer)
- **Team Código Maestro**

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact
For queries or contributions, reach out via GitHub or email at `your.email@example.com`.

