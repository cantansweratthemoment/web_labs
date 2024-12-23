package lab2.model;

public class Animal {
    private int id;
    private String name;
    private String species;
    private int age;
    private String habitat;

    public Animal() {
    }

    public Animal(int id, String name, String species, int age, String habitat) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.age = age;
        this.habitat = habitat;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getHabitat() { return habitat; }
    public void setHabitat(String habitat) { this.habitat = habitat; }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", age=" + age +
                ", habitat='" + habitat + '\'' +
                '}';
    }
}