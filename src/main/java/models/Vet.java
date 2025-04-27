package models;

public class Vet {

    private Integer vet_id;

    private String name;

    private String identification;

    private String email;

    private String phoneNumber;

    private String passw;

    private String speciality;

    public Vet(String name, String identification, String email, String phoneNumber, String passw, String speciality) {
        this.name = name;
        this.identification = identification;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passw = passw;
        this.speciality = speciality;
    }

    public Integer getVetId() {
        return vet_id;
    }

    public void setVetId(Integer vet_id) {
        this.vet_id = vet_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
