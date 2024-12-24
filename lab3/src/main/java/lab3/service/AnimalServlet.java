package lab3.service;

import lab3.dao.AnimalDAO;
import lab3.exceptions.InvalidFieldException;
import lab3.exceptions.RecordNotFoundException;
import lab3.model.Animal;

import javax.jws.WebService;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

@WebService(
        serviceName = "AnimalService",
        endpointInterface = "lab3.service.AnimalService",
        targetNamespace = "http://lab3.service"
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
    public int createAnimal(String name, String species, int age, String habitat) throws InvalidFieldException {
        try {
            return AnimalDAO.create(name, species, age, habitat);
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean updateAnimal(int id, String name, String species, int age, String habitat) throws RecordNotFoundException, InvalidFieldException {
        try {
            return AnimalDAO.update(id, name, species, age, habitat);
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteAnimal(int id) throws RecordNotFoundException {
        try {
            return AnimalDAO.delete(id);
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}