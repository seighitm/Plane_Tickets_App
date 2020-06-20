package org.project.app;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public abstract class abstractClass{
    public void alertWindow(int index) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alert.png"));
        if(index==1) {
            alert.setContentText("No flights were found!");
        }else if(index==2) {
            alert.setContentText("Account is not validated.\nPlease try again later.");
        } else if(index==3) {
            alert.setContentText("Username and/or password is not correct!");
        } else if(index==4) {
            alert.setContentText("You have not entered all the data!");
        }else if(index==5){
            alert.setContentText("You have not filled in all the fields!");
        }else if(index==6){
            alert.setContentText("Phone number is incorrect!");
        }else if(index==7){
            alert.setContentText("Email is incorrect!");
        }else if(index==8){
            alert.setContentText("Email or phone number has already been registered!");
        }else if(index==9){
            alert.setContentText("You have not chosen the account type!");
        }else if(index==10){
            alert.setContentText("Wrong minute format!");
        }else if(index==11){
            alert.setContentText("Wrong date format!");
        }else if(index==12){
            alert.setContentText("Wrong price format!");
        }else if(index==13){
            alert.setContentText("Wrong hour format!");
        }else if(index==14){
            alert.setContentText("Wrong format of the number of seats!");
        }else if(index==15) {
            alert.setContentText("You have not filled in all the fields!");
        } else if(index==16) {
            alert.setContentText("The news already exists!");
        } else if(index==17) {
            alert.setContentText("This flight already exists!");
        }
        alert.show();
    }

}
