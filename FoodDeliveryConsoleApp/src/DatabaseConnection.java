import java.awt.desktop.PrintFilesEvent;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/fastfoodappdb";
    private static final String USER = "root"; // Change if you have a different username
    private static final String PASSWORD = "<>?f5abc30v5";

    private static ArrayList<Profile> registeredProfiles=new ArrayList<Profile>();
    private static ArrayList<Restaurant> registeredEstablishments=new ArrayList<Restaurant>();
    private static ArrayList<Dish> menu=new ArrayList<Dish>();
    private static ArrayList<Order> orders = new ArrayList<Order>();
    public ArrayList<Profile> getProfiles(){
        return  registeredProfiles;
    }
    public ArrayList<Restaurant> getRegisteredEstablishments(){
        return registeredEstablishments;
    }
    public ArrayList<Dish> getMenu(){
        return menu;
    }
    public ArrayList<Order> getOrders(){
        return orders;
    }

    public void getProfiles(Connection connection) throws SQLException {
        String query = "SELECT * FROM profiles";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
    }

    public void addProfile(Connection connection, String name, String password, String role) {
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
            System.err.println("Error adding profile: " + e.getMessage());
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
                System.out.println("Restaurant added successfully.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding dish: " + e.getMessage());
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

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String role = rs.getString("role");

                registeredProfiles.add(new Profile(name,password,role));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching profiles: " + e.getMessage());
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

                registeredEstablishments.add(new Restaurant(name,address,rating));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching restaurants: " + e.getMessage());
        }

        sql = "SELECT * FROM dishes";

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                String restaurant_name = rs.getString("restaurant_name");
                String restaurant_address = rs.getString("restaurant_address");
                String filter = rs.getString("filter");
                double price = rs.getDouble("price");

                Restaurant r=null;

                for(int i=0; i<registeredEstablishments.size(); i++){
                    if(restaurant_name.equals(registeredEstablishments.get(i).getName()) && restaurant_address.equals(registeredEstablishments.get(i).getAddress())){
                        r=new Restaurant(restaurant_name,restaurant_address,registeredEstablishments.get(i).getRating());
                    }
                }
                if(r!=null){
                    menu.add(new Dish(name,description,r,filter,price));
                }

            }

        } catch (SQLException e) {
            System.err.println("Error fetching dishes: " + e.getMessage());
        }

        sql = "SELECT * FROM orders";

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String client_username = rs.getString("client_username");
                String client_address = rs.getString("client_address");
                double delivery_payment = rs.getInt("delivery_payment");
                String status = rs.getString("status");

                orders.add(new Order(client_username,client_address,delivery_payment,status));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
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
