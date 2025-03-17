package models;

import java.sql.Date;

public class History {

    private Integer id;
    private Integer petId;
    private Date date;
    private String vaccine;
    private int dosis;
    private double cuantity;

    public History(){

    }
    public History(Integer id, Integer petId, Date date, String vaccine, int dosis, double cuantity) {
        this.id = id;
        this.petId = petId;
        this.date = date;
        this.vaccine = vaccine;
        this.dosis = dosis;
        this.cuantity = cuantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public int getDosis() {
        return dosis;
    }

    public void setDosis(int dosis) {
        this.dosis = dosis;
    }

    public double getCuantity() {
        return cuantity;
    }

    public void setCuantity(double cuantity) {
        this.cuantity = cuantity;
    }

}
