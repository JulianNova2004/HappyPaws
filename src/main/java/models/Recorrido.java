package models;

public class Recorrido {

    private Integer id;

    private Pet pet_id;

    private int lat;

    private int lon;

    public Recorrido(Pet pet_id, int lat, int lon) {
        this.pet_id = pet_id;
        this.lat = lat;
        this.lon = lon;
    }

    public Recorrido(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pet getPet_id() {
        return pet_id;
    }

    public void setPet_id(Pet pet_id) {
        this.pet_id = pet_id;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }
}
