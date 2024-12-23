package lab1.dao;

import lab1.model.Animal;

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
        Connection conn =  ds.getConnection();
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
        return animals;
    }

    
}