package lab2.service;

import lab2.model.Animal;

import javax.jws.WebService;
import java.util.List;

@WebService
public interface AnimalService {

    List<Animal> searchAnimals(String name, String species, String habitat, Integer age);

    int createAnimal(String name, String species, int age, String habitat);

    boolean updateAnimal(int id, String name, String species, int age, String habitat);

    boolean deleteAnimal(int id);
}