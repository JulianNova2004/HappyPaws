package models;

public class Admin {

    private Integer id;

    private String name;

    private String email;

    private String phoneNum;

    private String passw;

    public Admin(String name, String email, String phoneNum, String passw) {
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;
        this.passw = passw;
    }

    public Admin(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }
}
