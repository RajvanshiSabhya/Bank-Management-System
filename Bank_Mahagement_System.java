import java.sql.*;
import java.util.*;

public class Bank_Management_System {
    private static final String url = "jdbc:mysql://localhost:3306/mydata";
    private static final String username = "root";
    private static final String password = "Sabhya@23";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            CreateAcc create = new CreateAcc(conn, scanner);
            Login login = new Login(conn, scanner);

            System.out.println("**** Welcome to the Unknown Bank ****");
            while (true) {
                System.out.println("Tell us what you want to do:");
                System.out.println("Press 1 : Create new account");
                System.out.println("Press 2 : Log in to existing account");
                System.out.println("Press 3 : Check if user exists");
                System.out.println("Press 4 : Exit");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        create.createAcc();
                        break;
                    case 2:
                        login.login();
                        break;
                    case 3:
                        login.exists();
                        break;
                    case 4:
                        System.out.println("Thank you for using Unknown Bank");
                        return;
                    default:
                        System.out.println("Please enter a valid choice.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class CreateAcc {
    private Connection conn;
    private Scanner scanner;

    public CreateAcc(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void createAcc() {
        try {
            scanner.nextLine(); // Clear buffer
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your age: ");
            int age = scanner.nextInt();

            System.out.print("Enter your first amount to be deposited: ");
            int amount = scanner.nextInt();

            scanner.nextLine(); // Clear buffer
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            if (!email.matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,}$")) {
                System.out.println("Invalid email address.");
                return;
            }

            if (name.isEmpty() || email.isEmpty()) {
                System.out.println("Fill all the fields first!!!");
                return;
            }

            String query = "INSERT INTO accounts(account_holder, holder_age, account_email, account_balance) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, name);
                ps.setInt(2, age);
                ps.setString(3, email);
                ps.setInt(4, amount);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int accNo = rs.getInt(1);
                            System.out.println("Your account number is " + accNo);
                        }
                    }
                    System.out.println("***** Account created ******");
                } else {
                    System.out.println("***** Account not created *******");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

class Login {
    private Connection conn;
    private Scanner scanner;

    public Login(Connection conn, Scanner scanner) {
        this.conn = conn;
        this.scanner = scanner;
    }

    public void login() {
        try {
            scanner.nextLine(); // Clear buffer
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            System.out.print("Enter your account number: ");
            int accNo = scanner.nextInt();

            if (name.isEmpty() || email.isEmpty()) {
                System.out.println("Fill all the details first !");
                return;
            }

            String query = "SELECT account_email, account_no FROM accounts WHERE account_holder = ? AND account_email = ? AND account_no = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setInt(3, accNo);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("You have logged in successfully");
                        boolean loggedIn = true;
                        while (loggedIn) {
                            System.out.println("Tell us what you want:");
                            System.out.println("1: View Balance");
                            System.out.println("2: Withdraw Money");
                            System.out.println("3: Deposit Money");
                            System.out.println("4: Transfer Money");
                            System.out.println("5: Log Out");
                            int choice = scanner.nextInt();

                            switch (choice) {
                                case 1:
                                    viewBalance(accNo);
                                    break;
                                case 2:
                                    withdrawMoney(accNo);
                                    break;
                                case 3:
                                    depositMoney(accNo);
                                    break;
                                case 4:
                                    transferMoney(accNo);
                                    break;
                                case 5:
                                    System.out.println("Logged Out Successfully");
                                    loggedIn = false;
                                    break;
                                default:
                                    System.out.println("Please enter a valid choice.");
                            }
                        }
                    } else {
                        System.out.println("Incorrect Details !!!");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void viewBalance(int accNo) {
        String query = "SELECT account_balance FROM accounts WHERE account_no = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int balance = rs.getInt("account_balance");
                    System.out.println("Your current balance is ₹" + balance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void withdrawMoney(int accNo) {
        System.out.print("Enter the amount to withdraw: ");
        int withdrawAmount = scanner.nextInt();
        String query = "SELECT account_balance FROM accounts WHERE account_no = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int balance = rs.getInt("account_balance");
                    if (withdrawAmount <= balance) {
                        String updateQuery = "UPDATE accounts SET account_balance = account_balance - ? WHERE account_no = ?";
                        try (PreparedStatement psUpdate = conn.prepareStatement(updateQuery)) {
                            psUpdate.setInt(1, withdrawAmount);
                            psUpdate.setInt(2, accNo);
                            int rows = psUpdate.executeUpdate();
                            if (rows > 0) {
                                System.out.println("Transaction of ₹" + withdrawAmount + " is successful.");
                            } else {
                                System.out.println("Transaction could not happen.");
                            }
                        }
                    } else {
                        System.out.println("Insufficient balance for this transaction.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void depositMoney(int accNo) {
        System.out.print("Enter the amount to deposit: ");
        int depositAmount = scanner.nextInt();
        String query = "UPDATE accounts SET account_balance = account_balance + ? WHERE account_no = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, depositAmount);
            ps.setInt(2, accNo);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Deposited amount ₹" + depositAmount + " successfully.");
            } else {
                System.out.println("Amount didn't get deposited.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void transferMoney(int accNo) {
        System.out.print("Enter the amount to transfer: ");
        int transferAmount = scanner.nextInt();

        System.out.print("Enter the recipient's account number: ");
        int recipientAccNo = scanner.nextInt();

        String query = "SELECT account_balance FROM accounts WHERE account_no = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int balance = rs.getInt("account_balance");
                    if (transferAmount <= balance) {
                        conn.setAutoCommit(false);
                        String withdrawQuery = "UPDATE accounts SET account_balance = account_balance - ? WHERE account_no = ?";
                        String depositQuery = "UPDATE accounts SET account_balance = account_balance + ? WHERE account_no = ?";
                        try (PreparedStatement withdrawStmt = conn.prepareStatement(withdrawQuery);
                             PreparedStatement depositStmt = conn.prepareStatement(depositQuery)) {
                            withdrawStmt.setInt(1, transferAmount);
                            withdrawStmt.setInt(2, accNo);
                            depositStmt.setInt(1, transferAmount);
                            depositStmt.setInt(2, recipientAccNo);

                            int rowsWithdraw = withdrawStmt.executeUpdate();
                            int rowsDeposit = depositStmt.executeUpdate();

                            if (rowsWithdraw > 0 && rowsDeposit > 0) {
                                conn.commit();
                                System.out.println("Money transfer of ₹" + transferAmount + " successful.");
                            } else {
                                conn.rollback();
                                System.out.println("Money transfer failed.");
                            }
                        }
                        conn.setAutoCommit(true);
                    } else {
                        System.out.println("Insufficient balance for this transfer.");
                    }
                }
            }
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    public void exists() {
        scanner.nextLine(); // Clear buffer
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();

        System.out.print("Enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Enter your account number: ");
        int accNo = scanner.nextInt();

        String query = "SELECT account_holder FROM accounts WHERE account_holder = ? AND account_email = ? AND account_no = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setInt(3, accNo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Yes, the user " + name + " exists.");
                } else {
                    System.out.println("No, this user does not exist.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}