package ProjectAdamasDeepShekharHalder063;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class MainApp {
    private static final String URL = "jdbc:mysql://localhost:3306/ProjectAdamasDeepShekharHalder063";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n==========================================");
            System.out.println("   VEHICLE RENTAL MANAGEMENT SYSTEM");
            System.out.println("==========================================");
            System.out.println("1. View All Vehicles");
            System.out.println("2. Add New Vehicle");
            System.out.println("3. Search Vehicle by Type");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    viewAllVehicles();
                    break;
                case 2:
                    addVehicle(scanner);
                    break;
                case 3:
                    searchVehicleByType(scanner);
                    break;
                case 4:
                    System.out.println("Exiting application. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please select 1, 2, 3, or 4.");
            }
        }
    }

    private static void viewAllVehicles() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM vehicles")) {

            System.out.println("\n--- ALL VEHICLE RECORDS ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                                   " | Type: " + rs.getString("type") +
                                   " | Model: " + rs.getString("model") +
                                   " | Rate: $" + rs.getDouble("base_rate") +
                                   " | Feature: " + rs.getString("extra_feature"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addVehicle(Scanner scanner) {
        System.out.print("Enter Type (Car, SUV, Bike, EV, etc.): ");
        String type = scanner.nextLine();
        System.out.print("Enter Model: ");
        String model = scanner.nextLine();
        System.out.print("Enter Base Rate ($): ");
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid rate. Enter a numeric value ($): ");
            scanner.next();
        }
        double baseRate = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Extra Feature: ");
        String extraFeature = scanner.nextLine();

        String sql = "INSERT INTO vehicles (type, model, base_rate, extra_feature) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);
            pstmt.setString(2, model);
            pstmt.setDouble(3, baseRate);
            pstmt.setString(4, extraFeature);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Vehicle added successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void searchVehicleByType(Scanner scanner) {
        System.out.print("Enter vehicle type to search (e.g., Car, SUV, Bike): ");
        String typeFilter = scanner.nextLine();

        String sql = "SELECT * FROM vehicles WHERE type = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, typeFilter);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n--- SEARCH RESULTS FOR TYPE: " + typeFilter + " ---");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("ID: " + rs.getInt("id") +
                                   " | Model: " + rs.getString("model") +
                                   " | Rate: $" + rs.getDouble("base_rate") +
                                   " | Feature: " + rs.getString("extra_feature"));
            }
            if (!found) {
                System.out.println("No vehicles found for type: " + typeFilter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
