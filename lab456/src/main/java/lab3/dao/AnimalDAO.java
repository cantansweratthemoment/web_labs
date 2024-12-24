package lab3.dao;

import lab3.model.Animal;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {

    public static List<Animal> search(String name, String species, String habitat, Integer age) throws NamingException, SQLException {
        List<Animal> animals = new ArrayList<>();
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/AnimalDB");
        try (Connection conn = ds.getConnection()) {
            String sql = "SELECT * FROM Animals WHERE 1=1";
            if (name != null) sql += " AND name = ?";
            if (species != null) sql += " AND species = ?";
            if (habitat != null) sql += " AND habitat = ?";
            if (age != null) sql += " AND age = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (name != null) stmt.setString(paramIndex++, name);
            if (species != null) stmt.setString(paramIndex++, species);
            if (habitat != null) stmt.setString(paramIndex++, habitat);
            if (age != null) stmt.setInt(paramIndex++, age);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                animals.add(new Animal(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getInt("age"),
                        rs.getString("habitat")
                ));
            }
        }
        return animals;
    }

    public static int create(Animal animal) throws NamingException, SQLException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/AnimalDB");
        try (Connection conn = ds.getConnection()) {
            String sql = "INSERT INTO Animals (name, species, age, habitat) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, animal.getName());
            stmt.setString(2, animal.getSpecies());
            stmt.setInt(3, animal.getAge());
            stmt.setString(4, animal.getHabitat());
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // Возвращаем сгенерированный ID
            } else {
                throw new SQLException("Failed to create animal, no ID obtained.");
            }
        }
    }

    public static boolean update(int id, Animal animal) throws NamingException, SQLException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/AnimalDB");
        try (Connection conn = ds.getConnection()) {
            String sql = "UPDATE Animals SET name = ?, species = ?, age = ?, habitat = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, animal.getName());
            stmt.setString(2, animal.getSpecies());
            stmt.setInt(3, animal.getAge());
            stmt.setString(4, animal.getHabitat());
            stmt.setInt(5, id);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    public static boolean delete(int id) throws NamingException, SQLException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/AnimalDB");
        try (Connection conn = ds.getConnection()) {
            String sql = "DELETE FROM Animals WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }
}