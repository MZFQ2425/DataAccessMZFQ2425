package org.mzfq2425.finalactivity.finalactivity;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.mzfq2425.finalactivity.finalactivity.dao.SellersDAO;
import org.mzfq2425.finalactivity.finalactivity.model.Sellers;
import org.mzfq2425.finalactivity.finalactivity.utils.ObjUtil;

import java.io.*;
import java.util.ArrayList;

public class LoginViewController {
    @FXML private TextField field_user;
    @FXML private PasswordField field_pwd;
    @FXML private Label label_msg;
    @FXML private CheckBox check_remember;

    SellersDAO sellersDAO = new SellersDAO();
    ObjUtil objReader = new ObjUtil();

    //function to force focus on user field
    @FXML
    protected void initialize(){
        if(readUserRemembered()){
            Platform.runLater(() -> field_pwd.requestFocus());
            check_remember.setSelected(true);
        }else {
            Platform.runLater(() -> field_user.requestFocus());
        }
    }

    //function to check if the user exits
    @FXML
    protected void checkForUser() {

        String user = field_user.getText();
        String password = field_pwd.getText();

        //check if the user or password has value
        if(user.trim().length()==0 || password.trim().length()==0){
            showMsg("Please, fill in all fields", "red");
        }else{
            //change pass to md5 and check if exists
            String md5pass = hashUtil.hashPassword(password);

            SellersDAO sellersDAO = new SellersDAO();
            Sellers userFinal = sellersDAO.getSellerFromUserAndPass(user, md5pass);

            if(userFinal==null){
                showMsg("The username or password you entered is incorrect. Please try again", "red");
            }else{
                if(check_remember.isSelected()){
                    writeRememberUser(userFinal);
                }else{
                    objReader.cleanRememberUser();
                }

                // if it does exist, we pass the seller to the appviewcontroller so we don't have to do another call
                showMsg("Successful login, redirecting to the next page...", "green");
                changeScene(userFinal);
            }
        }

    }

    //function to write to obj file the user to be remembered
    public void writeRememberUser(Sellers seller){
        if(objReader.writeSellersToObj(seller)) {
            System.out.println("User sucessfully remembered");
        }
    }

    //function to read the user stored in the obj file
    public boolean readUserRemembered(){
        Sellers remembered = objReader.readObj();
        if(remembered!=null){
            field_user.setText(remembered.getCif());
            return true;
        }
        return false;
    }

    //function to change from the scene of login to the scene of appview
    public void changeScene(Sellers seller){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("app_view.fxml"));
            Pane appView = fxmlLoader.load();

            AppViewController appViewController = fxmlLoader.getController();
            appViewController.setSeller(seller);

            Stage stage = (Stage) field_user.getScene().getWindow();

            Scene scene = new Scene(appView);
            stage.setScene(scene);
            stage.setTitle("App View");

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //function to show the user a message, color indicates the type of message (red error, green success, black info)
    public void showMsg(String msg, String color){
        label_msg.setText(msg);
        label_msg.setStyle("-fx-text-fill: " + color + ";");
    }

}
