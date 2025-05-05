package models;

public class Recorrido {

    private Integer id;

    private Pet pet_id;

    private Double lat;

    private Double lon;

    public Recorrido(Pet pet_id, Double lat, Double lon) {
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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
