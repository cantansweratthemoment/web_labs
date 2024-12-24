package lab3.service;


import lab3.exceptions.InvalidFieldException;
import lab3.exceptions.RecordNotFoundException;
import lab3.model.Animal;

import javax.jws.WebService;
import java.util.List;

@WebService
public interface AnimalService {

    List<Animal> searchAnimals(String name, String species, String habitat, Integer age);

    int createAnimal(String name, String species, int age, String habitat)
            throws InvalidFieldException;

    boolean updateAnimal(int id, String name, String species, int age, String habitat)
            throws InvalidFieldException, RecordNotFoundException;

    boolean deleteAnimal(int id)
            throws RecordNotFoundException;
}