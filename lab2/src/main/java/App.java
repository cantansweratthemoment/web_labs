import lab2.model.Animal;
import lab2.service.AnimalService;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import javax.xml.ws.Service;

public class App {

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";

    public static void main(String[] args) throws Exception {
        URL wsdlURL = new URL("http://localhost:8080/labs/ws/AnimalService?wsdl");
        QName qname = new QName("http://lab2.service", "AnimalService");
        Service service = Service.create(wsdlURL, qname);

        AnimalService animalServlet = service.getPort(AnimalService.class);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Animal Management ---");
            System.out.println(CYAN + "1. Search Animals" + RESET);
            System.out.println(GREEN + "2. Create Animal" + RESET);
            System.out.println(YELLOW + "3. Update Animal" + RESET);
            System.out.println(RED + "4. Delete Animal" + RESET);
            System.out.println(BLUE + "5. Exit" + RESET);
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("name: ");
                    String name = scanner.nextLine();
                    name = name.isEmpty() ? null : name;

                    System.out.print("species: ");
                    String species = scanner.nextLine();
                    species = species.isEmpty() ? null : species;

                    System.out.print("habitat: ");
                    String habitat = scanner.nextLine();
                    habitat = habitat.isEmpty() ? null : habitat;

                    System.out.print("age: ");
                    String ageInput = scanner.nextLine();
                    Integer age = ageInput.isEmpty() ? null : Integer.parseInt(ageInput);

                    List<Animal> animals = animalServlet.searchAnimals(name, species, habitat, age);
                    if (animals.isEmpty()) {
                        System.out.println(RED + "No animals found." + RESET);
                    } else {
                        animals.forEach(animal -> System.out.println(GREEN + animal + RESET));
                    }
                    break;

                case 2:
                    System.out.print("Enter name: ");
                    String newName = scanner.nextLine();

                    System.out.print("Enter species: ");
                    String newSpecies = scanner.nextLine();

                    System.out.print("Enter age: ");
                    int newAge = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter habitat: ");
                    String newHabitat = scanner.nextLine();

                    int newId = animalServlet.createAnimal(newName, newSpecies, newAge, newHabitat);
                    if (newId != -1) {
                        System.out.println(GREEN + "Animal created with ID: " + newId + RESET);
                    } else {
                        System.out.println(RED + "Failed to create animal." + RESET);
                    }
                    break;

                case 3:
                    System.out.print("Enter ID of the animal to update: ");
                    int updateId = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter new name: ");
                    String updatedName = scanner.nextLine();

                    System.out.print("Enter new species: ");
                    String updatedSpecies = scanner.nextLine();

                    System.out.print("Enter new age: ");
                    int updatedAge = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter new habitat: ");
                    String updatedHabitat = scanner.nextLine();

                    boolean updated = animalServlet.updateAnimal(updateId, updatedName, updatedSpecies, updatedAge, updatedHabitat);
                    if (updated) {
                        System.out.println(GREEN + "Animal updated successfully." + RESET);
                    } else {
                        System.out.println(RED + "Failed to update animal. Make sure the ID exists." + RESET);
                    }
                    break;

                case 4:
                    System.out.print("Enter ID of the animal to delete: ");
                    int deleteId = Integer.parseInt(scanner.nextLine());

                    boolean deleted = animalServlet.deleteAnimal(deleteId);
                    if (deleted) {
                        System.out.println(GREEN + "Animal deleted successfully." + RESET);
                    } else {
                        System.out.println(RED + "Failed to delete animal. Make sure the ID exists." + RESET);
                    }
                    break;

                case 5:
                    System.out.println(BLUE + "Exiting..." + RESET);
                    scanner.close();
                    return;

                default:
                    System.out.println(RED + "Invalid choice. Please select again." + RESET);
                    break;
            }
        }
    }
}