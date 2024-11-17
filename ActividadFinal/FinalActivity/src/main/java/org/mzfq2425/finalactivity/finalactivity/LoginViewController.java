package org.mzfq2425.finalactivity.finalactivity;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.mzfq2425.finalactivity.finalactivity.model.Sellers;

import java.io.IOException;

public class LoginViewController {
    @FXML private TextField field_user;
    @FXML private PasswordField field_pwd;
    @FXML private Label label_msg;

    static SessionFactory sessionFactory = null;
    static Session session = null;

    //function to force focus on user fileld
    @FXML
    protected void initialize(){
        Platform.runLater(() -> field_user.requestFocus());
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
            Sellers userFinal = checkIfSellerExist(user, md5pass);

            if(userFinal==null){
                showMsg("The username or password you entered is incorrect. Please try again", "red");
            }else{
                // if it does exist, we pass the seller to the appviewcontroller so we dont have to do another call
                showMsg("Successful login, redirecting to the next page...", "green");
                changeScene(userFinal);
            }
        }

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

    //function to check if seller info from login is correct
    public Sellers checkIfSellerExist(String user, String password){
        Sellers userRes = null;

        try {
            openCon();
            String query = "SELECT s FROM Sellers s WHERE s.cif = :cif AND s.password = :password";

            userRes = session.createQuery(query, Sellers.class)
                    .setParameter("cif", user)
                    .setParameter("password", password)
                    .uniqueResult();
        }catch(Exception e){
            System.out.println(e.getMessage());
        } finally{
            closeCon();
        }

        return userRes;
    }

    //function to open connection
    public static void openCon(){
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Sellers.class)
                    .buildSessionFactory();

            session = sessionFactory.openSession();

            if (session != null) {
                System.out.println("Session successfully opened!");
            } else {
                System.out.println("Error opening session!");
            }
        } catch (Exception e) {
            System.err.println("Error initializing Hibernate: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    //function to close connection
    public static void closeCon(){
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
            }
        } catch (Exception e) {
            System.err.println("Error closing connection: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
