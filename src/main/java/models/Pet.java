package models;

public class Pet {

    private Integer petId;
    
    private String name;
    
    private String race;
    
    private int amount_of_walks;
    
    private int amount_of_food;
    
    private String food;
    
    private int weight;
    
    private String description;
    
    private String age;

    private User user;


    public Pet(String name, String race, int amount_of_walks, int amount_of_food, String food, int weight, String description,String age) {
        this.name = name;
        this.race = race;
        this.amount_of_walks = amount_of_walks;
        this.amount_of_food = amount_of_food;
        this.food = food;
        this.weight = weight;
        this.description = description;
        this.age = age;
    }

    public Pet(){}

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public int getAmount_of_walks() {
        return amount_of_walks;
    }

    public void setAmount_of_walks(int amount_of_walks) {
        this.amount_of_walks = amount_of_walks;
    }

    public int getAmount_of_food() {
        return amount_of_food;
    }

    public void setAmount_of_food(int amount_of_food) {
        this.amount_of_food = amount_of_food;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
