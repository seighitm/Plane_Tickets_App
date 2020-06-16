package org.project.app.Model;

public class ModelTicket {
    int id;
    int ore;
    String id_user;
    String id_fl;

    public ModelTicket(int id, String id_user, String id_fl, int ore) {
        this.id = id;
        this.id_user = id_user;
        this.id_fl = id_fl;
        this.ore = ore;
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

    public int getOre() {
        return ore;
    }

    public void setOre(int ore) {
        this.ore = ore;
    }
}
