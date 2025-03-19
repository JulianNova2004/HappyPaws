package models;

import java.sql.Date;

public class History {

    private Integer id;
    private Integer petId;
    private String date;
    private String vaccine;
    private int dosis;
    private double cuantity;
    private String reason;
    private String comments;

    public History(){

    }
    public History(String date, String vaccine, int dosis, double cuantity, String reason, String comments) {
        //this.id = id;
        //this.petId = petId;
        this.date = date;
        this.vaccine = vaccine;
        this.dosis = dosis;
        this.cuantity = cuantity;
        this.reason = reason;
        this.comments = comments;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
