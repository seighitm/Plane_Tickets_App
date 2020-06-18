package org.project.app.Model;

public class ModelAccount {
    int idtab1;
    String name;
    String email;
    int phone;
    String pers;

    public ModelAccount(int idtab1, String name, String email, int phone, String pers) {
        this.idtab1 = idtab1;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pers = pers;
    }

    public int getIdtab1() {
        return idtab1;
    }

    public void setIdtab1(int idtab1) {
        this.idtab1 = idtab1;
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

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPers() {
        return pers;
    }

    public void setPers(String pers) {
        this.pers = pers;
    }
}
