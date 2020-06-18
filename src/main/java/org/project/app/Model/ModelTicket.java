package org.project.app.Model;

public class ModelTicket {
    int id;
    String date;
    String id_user;
    String id_fl;

    public ModelTicket(int id, String id_user, String id_fl, String date) {
        this.id = id;
        this.id_user = id_user;
        this.id_fl = id_fl;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_fl() {
        return id_fl;
    }

    public void setId_fl(String id_fl) {
        this.id_fl = id_fl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
