package lab1.service;

import lab1.dao.AnimalDAO;
import lab1.model.Animal;

import javax.jws.WebService;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

@WebService(serviceName = "AnimalService",
        endpointInterface = "lab1.service.AnimalService",
        targetNamespace = "http://lab1.service")
public class AnimalServlet implements AnimalService {

    @Override
    public List<Animal> searchAnimals(String name, String species, String habitat, Integer age) {
        List<Animal> result = null;

        try {
            result = AnimalDAO.search(name, species, habitat, age);
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}