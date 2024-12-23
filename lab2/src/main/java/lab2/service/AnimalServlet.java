package lab2.service;

import lab2.dao.AnimalDAO;
import lab2.model.Animal;

import javax.jws.WebService;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

@WebService(
        serviceName = "AnimalService",
        endpointInterface = "lab2.service.AnimalService",
        targetNamespace = "http://lab2.service"
)
public class AnimalServlet implements AnimalService {

    @Override
    public List<Animal> searchAnimals(String name, String species, String habitat, Integer age) {
        try {
            return AnimalDAO.search(name, species, habitat, age);
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int createAnimal(String name, String species, int age, String habitat) {
        try {
            return AnimalDAO.create(name, species, age, habitat);
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateAnimal(int id, String name, String species, int age, String habitat) {
        try {
            return AnimalDAO.update(id, name, species, age, habitat);
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAnimal(int id) {
        try {
            return AnimalDAO.delete(id);
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}