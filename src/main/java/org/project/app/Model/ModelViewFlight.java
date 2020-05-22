package org.project.app.Model;

public class ModelViewFlight {
    String Location;
    String Destination;
    String Date;
    int ID;
    int Hour;
    int Price;
    int Seats;

    public ModelViewFlight(int id, String locatia, String destinatia, String data, int pret, int ore, int nr) {
        this.Location = locatia;
        this.Destination = destinatia;
        this.Date = data;
        this.ID =id;
        this.Price =pret;
        this.Hour =ore;
        this.Seats =nr;
    }

    public int getSeats() {
        return Seats;
    }

    public void setSeats(int seats) {
        this.Seats = seats;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public String getDestination() {
        return Destination;
    }

    public void setDestination(String destination) {
        this.Destination = destination;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getHour() {
        return Hour;
    }

    public void setHour(int hour) {
        this.Hour = hour;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        this.Price = price;
    }
}
