package org.mzfq2425.finalactivity.finalactivity;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.mzfq2425.finalactivity.finalactivity.dao.SellersDAO;
import org.mzfq2425.finalactivity.finalactivity.model.Sellers;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppViewController {

    @FXML
    private TextField tf_cif;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_business;
    @FXML
    private TextField tf_phone;
    @FXML
    private TextField tf_email;
    @FXML
    private TextField tf_url;
    @FXML
    private TextField tf_password;
    @FXML
    private Label label_msg;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{3}-\\d{3}-\\d{3}$");
    private static final Pattern URL_PATTERN = Pattern.compile("^https?:\\/\\/(www\\.)?[\\w-]+(\\.[\\w-]+)+(\\.[a-z]{2,6})(\\/[\\w-]*)*\\/?$");

    private Sellers seller;

    @FXML
    public void setSeller(Sellers seller) {
        tf_cif.setText(seller.getCif());
        tf_name.setText(seller.getName());
        tf_business.setText(seller.getBusinessName());
        tf_phone.setText(seller.getPhone());
        tf_email.setText(seller.getEmail());
        tf_password.setText(seller.getPlainPassword());
        tf_url.setText(seller.getUrl());
        this.seller = seller;
    }

    // function to reset the values of the seller to the current data
    @FXML
    public void resetSeller() {
        setStyleNull();
        tf_cif.setText(seller.getCif());
        tf_name.setText(seller.getName());
        tf_business.setText(seller.getBusinessName());
        tf_phone.setText(seller.getPhone());
        tf_email.setText(seller.getEmail());
        tf_password.setText(seller.getPlainPassword());
        tf_url.setText(seller.getUrl());
        showMsg("", "black");
    }

    //function to call the update of the seller
    @FXML
    public void callUpdate() {
        showMsg("", "black");
        setStyleNull();

        String name = tf_name.getText();
        String business = tf_business.getText();
        String phone = tf_phone.getText();
        String email = tf_email.getText();
        String password = tf_password.getText();
        String url = tf_url.getText();

        //validation that the user did changed so updating with the same values can be prevented
        boolean somethingChanged = somethingChanged(name, business, phone, email, password, url);

        if (somethingChanged) {
            //set in an array all values and validations at once
            String[][] fieldsToCheck = {
                    {"Name", name, "100", "false", "false", "false", "false"},
                    {"Business Name", business, "100", "false", "false", "true", "false"},
                    {"Phone", phone, "15", "true", "false", "true", "false"},
                    {"Email", email, "90", "false", "true", "true", "false"},
                    {"URL", url, "200", "false", "false", "true", "true"},
                    {"Password", password, "50", "false", "false", "false", "false"},
            };

            //iterating all values
            int i = 0;
            for (String[] field : fieldsToCheck) {
                String fieldName = field[0];
                String value = field[1];
                int maxChar = Integer.parseInt(field[2]);
                boolean checkNumber = Boolean.parseBoolean(field[3]);
                boolean checkMail = Boolean.parseBoolean(field[4]);
                boolean canBeNull = Boolean.parseBoolean(field[5]);
                boolean checkUrl = Boolean.parseBoolean(field[6]);

                if (!checkValues(fieldName, value, maxChar, checkNumber, checkMail, !canBeNull, checkUrl)) {
                    //border red if failed at certain field
                    switch (i) {
                        case 0:
                            tf_name.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                            break;
                        case 1:
                            tf_business.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                            break;
                        case 2:
                            tf_phone.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                            break;
                        case 3:
                            tf_email.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                            break;
                        case 4:
                            tf_url.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                            break;
                        case 5:
                            tf_password.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                            break;
                    }
                    return;
                }

                i++;
            }

            ButtonType action = createDialog("Are you sure you'd like to modify the seller's?");

            if (action == ButtonType.YES) {
                //updating the seller with the new values (and values like cif / seller that can't change)
                Sellers res = SellersDAO.updateSeller(new Sellers(seller.getSellerId(), seller.getCif(), name, business, phone, email, password, hashUtil.hashPassword(password), url, seller.getPro()));
                if(res!=null){
                    showMsg("Seller successfully updated", "green");
                    setSeller(res);
                }else{
                    showMsg("Seller couldn't be updated, please try again later", "red");
                }
            } else if (action == ButtonType.NO || action == ButtonType.CANCEL) {
                //accion cancelada
            } else {
                System.out.println("Accion no controlada: " + action.toString());
            }
        }
    }

    //function to call a dialog to confirm the update operation
    public ButtonType createDialog(String msgDialog) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msgDialog, ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        return alert.getResult();
    }

    //function to call the exit of the app
    @FXML
    public void callExit() {
        System.exit(0);
    }

    //function to call the exit of the app
    @FXML
    public void callOfferView() {
        changeScene(seller);
    }

    //function to change the Scene
    public void changeScene(Sellers seller){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("offer_view.fxml"));
            Pane appView = fxmlLoader.load();

            OfferViewController offerViewController = fxmlLoader.getController();
            offerViewController.setSeller(seller);

            Stage stage = (Stage) tf_cif.getScene().getWindow();

            Scene scene = new Scene(appView);
            stage.setScene(scene);
            stage.setTitle("Offer View");

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //function to delete styles from inputs
    public void setStyleNull() {
        tf_name.setStyle(null);
        tf_business.setStyle(null);
        tf_phone.setStyle(null);
        tf_email.setStyle(null);
        tf_password.setStyle(null);
        tf_url.setStyle(null);
    }

    //function to check all values are different from the original
    public boolean somethingChanged(String name, String business, String phone, String email, String password, String url) {
        boolean didChange = false;

        if (!seller.getName().equals(name)) {
            didChange = true;
        }

        //for null validation
        if (seller.getBusinessName() == null && business != null) {
            didChange = true;
        } else if (seller.getBusinessName() != null && !seller.getBusinessName().equals(business)) {
            didChange = true;
        }

        //for null validation
        if (seller.getPhone() == null && phone != null) {
            didChange = true;
        } else if (seller.getPhone() != null && !seller.getPhone().equals(phone)) {
            didChange = true;
        }

        //for null validation
        if (seller.getEmail() == null && email != null) {
            didChange = true;
        } else if (seller.getEmail() != null && !seller.getEmail().equals(email)) {
            didChange = true;
        }

        if (!seller.getPlainPassword().equals(password)) {
            didChange = true;
        }

        if(!seller.getPassword().equals(password)){
            didChange = true;
        }

        if (!didChange) {
            showMsg("No changes detected. Update not performed.", "black");
            return false;
        } else {
            return true;
        }
    }

    //function to check all values are ok
    public boolean checkValues(String field, String check, int maxChar, boolean checkNumber, boolean checkMail, boolean checkEmpty, boolean checkUrl) {
        if (check == null || check.trim().isEmpty()) {
            if (checkEmpty) {
                showMsg("Please, fill in the empty field: " + field, "red");
                return false;
            } else {
                //in case theres a nullable field
                return true;
            }
        }

        if (check.length() > maxChar) {
            showMsg("The field " + field + " exceeds the maximum length (" + maxChar + ").", "red");
            return false;
        }

        if (checkNumber) {
            Matcher matcher = PHONE_PATTERN.matcher(check);
            if (!matcher.matches()) {
                showMsg("The phone number is not in the valid format (000-000-000).", "red");
                return false;
            }
        }

        if (checkMail) {
            Matcher matcher = EMAIL_PATTERN.matcher(check);
            if (!matcher.matches()) {
                showMsg("The email is not valid.", "red");
                return false;
            }
        }

        if (checkUrl) {
            Matcher matcher = URL_PATTERN.matcher(check);
            if (!matcher.matches()) {
                showMsg("The URL is not valid.", "red");
                return false;
            }
        }

        return true;
    }

    //function to show the user a message, color indicates the type of message (red error, green success, black info)
    public void showMsg(String msg, String color) {
        label_msg.setVisible(true);
        label_msg.setText(msg);
        label_msg.setStyle("-fx-text-fill: " + color + ";");
    }
}