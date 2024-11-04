package lab1.service;

import lab1.model.Animal;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface AnimalService {
    List<Animal> searchAnimals(String name, String species, String habitat, Integer age);
}