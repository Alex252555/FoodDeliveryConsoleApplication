import java.awt.desktop.PrintFilesEvent;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/fastfoodappdb";
    private static final String USER = "root"; // Change if you have a different username
    private static final String PASSWORD = "<>?f5abc30v5";

    public void getProfiles(Connection connection) throws SQLException {
        String query = "SELECT * FROM profiles";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
    }

    public void removeEstablishment(Connection connection, String name, String address) throws SQLException {
        String sql = "DELETE FROM restaurants WHERE name = (?) AND address = (?)";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, address);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Establishment removed successfully!");
            }

        } catch (SQLException e) {
            System.err.println("Error removing establishment: " + e.getMessage());
        }
    }

    public void updateEstablishmentBudget(Connection connection, double budget, String name){
        String sql = "UPDATE restaurants SET budget = (?) WHERE name=(?)";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDouble(1, budget);
            stmt.setString(2, name);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Budget updated successfully!");
            }

        } catch (SQLException e) {
            System.err.println("Error updating budget: " + e.getMessage());
        }
    }

    public void addProfile(Connection connection, String name, String password, String role) {
        System.out.println(role);
        String sql = "INSERT INTO profiles (name, password, role) VALUES (?, ?, ?)";

        try (
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, password);
            stmt.setString(3, role);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Profile added successfully.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding profile: " + e.getMessage());
        }
    }

    public void addOrder(Connection connection, String name, double reward, String status) {
        String sql = "INSERT INTO orders (client_username, delivery_payment, status) VALUES (?, ?, ?)";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setDouble(2, reward);
            stmt.setString(3, status);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Profile added successfully.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding order: " + e.getMessage());
        }
    }

    public void changeOrderStatus(Connection connection, int orderNum) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            System.out.println("ORDER NUM: "+orderNum);
            stmt.setString(1, "Delivered"); // ENUM value
            stmt.setInt(2, orderNum);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Order status successfully updated.");
            } else {
                System.out.println("No order found with the given ID.");
            }

        } catch (SQLException e) {
            System.err.println("Error updating order status: " + e.getMessage());
        }
    }

    public void changeUserPassword(Connection connection, String username, String newPassword) {
        String sql = "UPDATE profiles SET password = ? WHERE name = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, newPassword); // ENUM value
            stmt.setString(2, username);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Password successfully updated.");
            } else {
                System.out.println("Password not updated.");
            }

        } catch (SQLException e) {
            System.err.println("Error updating password: " + e.getMessage());
        }
    }

    public void addEstablishment(Connection connection, String name, String address, int rating) {
        String sql = "INSERT INTO restaurants (name, address, rating) VALUES (?, ?, ?)";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setInt(3, rating);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Restaurant added successfully.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding Restaurant: " + e.getMessage());
        }
    }

    public void addDish(Connection connection, String name, String desc, String res_name, String res_address, String filter, double price) {
        String sql = "INSERT INTO dishes (dish_name, description, restaurant_name, restaurant_address, filter, price) VALUES (?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, desc);
            stmt.setString(3, res_name);
            stmt.setString(4, res_address);
            stmt.setString(5, filter);
            stmt.setDouble(6, price);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Dish added successfully.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding dish: " + e.getMessage());
        }
    }

    public void removeDish(Connection connection, String name, String resName, String resAddress) {
        String sql = "DELETE FROM dishes WHERE dish_name = ? AND restaurant_name = ? AND restaurant_address = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, resName);
            stmt.setString(3, resAddress);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Dish removed successfully.");
            }

        } catch (SQLException e) {
            System.err.println("Error removing dish: " + e.getMessage());
        }
    }

    private void deleteProfile(Connection connection, int id) {
        String sql = "DELETE FROM profiles WHERE ID = ?";

        try (
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
            System.out.println("Profile deleted successfully!");

        } catch (SQLException e) {
            System.err.println("Error deleting profile: " + e.getMessage());
        }
    }

    public void printAllProfiles(Connection connection) {
        String sql = "SELECT * FROM profiles";

        try (
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Profiles:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");

                System.out.printf("ID: %d | Name: %s | Password: %s | Role: %s%n",
                        id, name, password, role);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching profiles: " + e.getMessage());
        }
    }

    public void loadTables(Connection connection) {
        String sql = "SELECT * FROM profiles";

        Server.registeredEstablishments.clear();
        Server.menu.clear();
        Server.registeredProfiles.clear();
        Server.orders.clear();

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");

                Server.registeredProfiles.add(new Profile(name,password,role));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching profiles: " + e.getMessage());
        }

        for(int i=0; i<Server.registeredProfiles.size(); i++){
            System.out.println("Profile: "+Server.registeredProfiles.get(i).getUsername());
        }

        sql = "SELECT * FROM restaurants";

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String address = rs.getString("address");
                int rating = rs.getInt("rating");
                double budget=rs.getDouble("budget");

                Server.registeredEstablishments.add(new Restaurant(name,address,rating,budget));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching restaurants: " + e.getMessage());
        }

        for(int i=0; i<Server.registeredEstablishments.size(); i++){
            System.out.println("Restaurant: "+Server.registeredEstablishments.get(i).getName());
        }

        sql = "SELECT * FROM dishes";

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("dish_name");
                String description = rs.getString("description");
                String restaurant_name = rs.getString("restaurant_name");
                String restaurant_address = rs.getString("restaurant_address");
                String filter = rs.getString("filter");
                double price = rs.getDouble("price");

                Restaurant r=null;

                for(int i=0; i<Server.registeredEstablishments.size(); i++){
                    if(restaurant_name.equals(Server.registeredEstablishments.get(i).getName()) && restaurant_address.equals(Server.registeredEstablishments.get(i).getAddress())){
                        r=new Restaurant(restaurant_name,restaurant_address,Server.registeredEstablishments.get(i).getRating());
                    }
                }
                if(r!=null){
                    Server.menu.add(new Dish(name,description,r,filter,price));
                }

            }

        } catch (SQLException e) {
            System.err.println("Error fetching dishes: " + e.getMessage());
        }

        for(int i=0; i<Server.menu.size(); i++){
            System.out.println("Menu: "+Server.menu.get(i).getName());
        }

        sql = "SELECT * FROM orders";

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String client_username = rs.getString("client_username");
                double delivery_payment = rs.getInt("delivery_payment");
                String status = rs.getString("status");

                Server.orders.add(new Order(client_username,delivery_payment,status));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
        }

        for(int i=0; i<Server.menu.size(); i++){
            System.out.println("Order: "+Server.menu.get(i).getName());
        }
    }

    public Connection setUpConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish connection
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Database connected successfully!");
        return connection;
    }

    public void closeConnection(Connection c) throws SQLException {
        c.close();
    }

}
