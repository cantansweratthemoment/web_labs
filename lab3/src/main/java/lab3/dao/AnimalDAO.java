package lab3.dao;

import lab3.exceptions.InvalidFieldException;
import lab3.exceptions.RecordNotFoundException;
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
            if (name != null) {
                sql += " AND name = ?";
            }
            if (species != null) {
                sql += " AND species = ?";
            }
            if (habitat != null) {
                sql += " AND habitat = ?";
            }
            if (age != null) {
                sql += " AND age = ?";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            if (name != null) {
                stmt.setString(paramIndex++, name);
            }
            if (species != null) {
                stmt.setString(paramIndex++, species);
            }
            if (habitat != null) {
                stmt.setString(paramIndex++, habitat);
            }
            if (age != null) {
                stmt.setInt(paramIndex, age);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Animal animal = new Animal(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getInt("age"),
                        rs.getString("habitat")
                );
                animals.add(animal);
            }
        }
        return animals;
    }

    public static int create(String name, String species, int age, String habitat)
            throws NamingException, SQLException, InvalidFieldException {
        if (name == null || name.isEmpty() || species == null || species.isEmpty() || age < 0 || habitat == null || habitat.isEmpty()) {
            throw new InvalidFieldException("Invalid input: All fields must be non-empty, and age must be non-negative.");
        }

        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/AnimalDB");
        try (Connection conn = ds.getConnection()) {
            String sql = "INSERT INTO Animals (name, species, age, habitat) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, species);
            stmt.setInt(3, age);
            stmt.setString(4, habitat);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Error creating animal: No ID returned.");
        }
    }

    public static boolean update(int id, String name, String species, int age, String habitat)
            throws NamingException, SQLException, InvalidFieldException, RecordNotFoundException {
        if (name == null || name.isEmpty() || species == null || species.isEmpty() || age < 0 || habitat == null || habitat.isEmpty()) {
            throw new InvalidFieldException("Invalid input: All fields must be non-empty, and age must be non-negative.");
        }

        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/AnimalDB");
        try (Connection conn = ds.getConnection()) {
            String sql = "UPDATE Animals SET name = ?, species = ?, age = ?, habitat = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, species);
            stmt.setInt(3, age);
            stmt.setString(4, habitat);
            stmt.setInt(5, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new RecordNotFoundException("Record with ID " + id + " does not exist.");
            }
            return true;
        }
    }

    public static boolean delete(int id) throws NamingException, SQLException, RecordNotFoundException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/AnimalDB");
        try (Connection conn = ds.getConnection()) {
            String sql = "DELETE FROM Animals WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new RecordNotFoundException("Record with ID " + id + " does not exist.");
            }
            return true;
        }
    }
}