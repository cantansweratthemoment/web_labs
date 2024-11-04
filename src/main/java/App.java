import lab1.model.Animal;
import lab1.service.AnimalService;
import lab1.service.AnimalServlet;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import javax.xml.ws.Service;

public class App {
    public static void main(String[] args) throws Exception {
        URL wsdlURL = new URL("http://localhost:8080/lab1_war_exploded/ws/AnimalService?wsdl");
        QName qname = new QName("http://lab1.service", "AnimalService");
        Service service = Service.create(wsdlURL, qname);

        AnimalService animalServlet = service.getPort(AnimalService.class);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("name: ");
            String name = scanner.nextLine();

            if (name.isEmpty()) {
                name = null;
            }

            System.out.print("species: ");
            String species = scanner.nextLine();

            System.out.print("habitat: ");
            String habitat = scanner.nextLine();

            System.out.print("age: ");
            String ageInput = scanner.nextLine();

            Integer age = ageInput.isEmpty() ? null : Integer.parseInt(ageInput);

            List<Animal> cars = animalServlet.searchAnimals(name, species, habitat, age);

            if (cars.isEmpty()) {
                System.out.println("nothing found");
            } else {
                cars.forEach(System.out::println);
            }
        }
    }
}