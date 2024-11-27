package org.mzfq2425.finalactivity.finalactivity;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.mzfq2425.finalactivity.finalactivity.dao.ProductsDAO;
import org.mzfq2425.finalactivity.finalactivity.dao.Seller_ProductsDAO;
import org.mzfq2425.finalactivity.finalactivity.model.Products;
import org.mzfq2425.finalactivity.finalactivity.model.Seller_products;
import org.mzfq2425.finalactivity.finalactivity.model.Sellers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class OfferViewController {
    private Sellers seller;
    private ProductsDAO productsDAO = new ProductsDAO();
    private Seller_ProductsDAO seller_productsDAO = new Seller_ProductsDAO();
    private List<Products> list = new ArrayList<>();

    @FXML private ComboBox<Products> cb_product;
    @FXML private DatePicker dp_from;
    @FXML private DatePicker dp_to;
    @FXML private TextField tf_disc;
    @FXML private Button btn_add;
    @FXML private Label label_msg;

    //function to force focus on the combobox
    @FXML
    protected void initialize(){
        Platform.runLater(() -> cb_product.requestFocus());
    }

    @FXML
    //function to store seller to avoid more calls to the database when changing scenes
    public void setSeller(Sellers seller) {
        this.seller = seller;
        list = productsDAO.getProductsFromSeller(seller.getSellerId());

        setAllInputs();

        restrictDatePicker(dp_from, LocalDate.now(), null);
        restrictDatePicker(dp_to, LocalDate.now(), null);

        dp_to.setDisable(true);
    }

    //function to call the exit of the app
    @FXML
    public void callExit() {
        System.exit(0);
    }

    @FXML
    public void callAppView() {
        changeScene(seller);
    }

    @FXML
    public void firstDateSelected(){
        LocalDate selectedDate = dp_from.getValue();
        if (selectedDate != null) {
            resetSecondDatePicker(dp_to);
            restrictDatePicker(dp_to, selectedDate, null);
            dp_to.setDisable(false);
        }else{
            dp_to.setDisable(true);
        }
    }

    @FXML
    public void secondDateSelected(){
        LocalDate selectedDate = dp_to.getValue();
        if (selectedDate != null) {
            restrictDatePicker(dp_from, LocalDate.now(), selectedDate);
            dp_from.setDisable(false);
        }
    }

    @FXML
    public void checkOffer(){
        boolean allOk = true;
        String msg = "";

        showMsg("", "black");
        setStyleNull();

        if(cb_product.getValue()==null){
            cb_product.setStyle("-fx-border-color: red; -fx-border-width: 2px");
            allOk = false;
            msg = "Please, select a product";
        }else if(dp_from.getValue() == null){
            dp_from.setStyle("-fx-border-color: red; -fx-border-width: 2px");
            allOk = false;
            if(msg.trim().equals("")){
                msg = "Please, select the start of the offer";
            }
        } else if(dp_to.getValue() == null){
            dp_to.setStyle("-fx-border-color: red; -fx-border-width: 2px");
            allOk = false;
            if(msg.trim().equals("")){
                msg = "Please, select the end of the offer";
            }
        } else {
            msg = checkDiscountValid();

            if (!msg.isEmpty()) {
                tf_disc.setStyle("-fx-border-color: red; -fx-border-width: 2px");
                allOk = false;
            }
        }

        if(!allOk){
            showMsg(msg, "red");
            return;
        }

        // all good, calling the procedure
        boolean hasActiveOffer = true;

        if(seller.getPro()){
            hasActiveOffer = productsDAO.isPRODiscountActive(seller.getSellerId(),dp_from.getValue(), dp_to.getValue());
        }else{
            hasActiveOffer = productsDAO.isDiscountActive(cb_product.getValue().getProductId(), dp_from.getValue(), dp_to.getValue());
        }

        if(!hasActiveOffer){
            //no active discounts for the product on those range of dates, call to create offer
            ButtonType action = createDialogConfirmation("Would you like to create the offer?");

            if (action == ButtonType.YES) {
                //modifying the offer with the new info
                Products product = cb_product.getValue();
                int discount = Integer.parseInt(tf_disc.getText());

                Seller_products sellerProducts = seller_productsDAO.getFromSellerAndProduct(seller.getSellerId(),product.getProductId());

                Double price = sellerProducts.getPrice();
                Double discounted = price * (discount / 100.0);
                Double discountedPrice = price - discounted;
                discountedPrice = Math.round(discountedPrice * 100.0) / 100.0;

                sellerProducts.setOfferPrice(discountedPrice);
                sellerProducts.setOfferStartDate(dp_from.getValue());
                sellerProducts.setOfferEndDate(dp_to.getValue());

                Seller_products res = seller_productsDAO.updateSeller(sellerProducts);

                if(res==null){
                    showMsg("There was an issue updating the offer. " +
                            "Please check the offer data and try again","red");
                }else{
                    showMsg("Offer correctly modified!","green");
                }

            } else if (action == ButtonType.NO || action == ButtonType.CANCEL) {
                //accion cancelada
            } else {
                System.out.println("Error: " + action.toString());
            }
        }else{
            //already active discount, tell user
            if(seller.getPro()){
                showMsg("This sellers has already three active offers\n" +
                        "for the selected date range. Please choose different dates.","red");
            }else {
                showMsg("This product already has an active offer\n" +
                        "for the selected date range. Please choose different dates.", "red");
            }
        }
    }

    //function to show the dialog info for the discounts
    @FXML
    public void createDialogInfo() {
        String msgDialog = "";

        if(seller.getPro()){
            msgDialog = "The discounted price for PRO sellers must match the following requirements:\n\n" +
                    "• For a 30-day period: maximum discount of 10%\n" +
                    "• For a 7-day period: maximum discount of 30%\n" +
                    "• For a 3-day period: maximum discount of 50%\n\n" +
                    "These type of PRO sellers can have three maximum offers at the same time.";
        }else{
            msgDialog = "The discounted price must match the following requirements:\n\n" +
                    "• For a 30-day period: maximum discount of 10%\n" +
                    "• For a 15-day period: maximum discount of 15%\n" +
                    "• For a 7-day period: maximum discount of 20%\n" +
                    "• For a 3-day period: maximum discount of 30%\n" +
                    "• For a 1-day period: maximum discount of 50%";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, msgDialog);
        alert.showAndWait();
    }

    //function to check if the values of the input discount is ok
    public String checkDiscountValid(){
        String discount = tf_disc.getText();
        String message = "";

        if(!discount.equals("") && !discount.trim().isEmpty()){
            try{
                int i = Integer.parseInt(discount);

                //checking the discount
                if(dp_from != null && dp_to != null){
                    int maxDisc = getMaxDiscount(dp_from.getValue(), dp_to.getValue());

                    if (i > maxDisc) {
                        message = "Please, enter a valid discount for the range of dates \n("
                                + getDaysBetween(dp_from.getValue(), dp_to.getValue())
                                 + " days), click the info button for more information";
                        return message;
                    }
                }
            }catch(NumberFormatException e){
                return "Please, enter a valid discount in a whole number format";
            }
        }else{
            return "Please, enter a discount to create the offer";
        }

        return message;
    }

    //function to create a dialog to confirm the operation
    public ButtonType createDialogConfirmation(String msgDialog) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msgDialog, ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        return alert.getResult();
    }

    //function to clean up all red inputs
    public void setStyleNull() {
        cb_product.setStyle(null);
        dp_from.setStyle(null);
        dp_to.setStyle(null);
        tf_disc.setStyle(null);
    }

    // function to count the days between the range of dates selected
    public long getDaysBetween(LocalDate dateFrom, LocalDate dateTo){
        return ChronoUnit.DAYS.between(dateFrom, dateTo) + 1;
    }

    // function to calculate the days between ranges and get the maximum discount
    private int getMaxDiscount(LocalDate dateFrom, LocalDate dateTo){
        int maxDiscount = 0;

        if(dateFrom !=null && dateTo != null){
            long daysBetween = getDaysBetween(dateFrom, dateTo);

            if(!seller.getPro()){
                if(daysBetween > 30){
                    showMsg("The discount can't exceed 30 days", "red");
                    return 0;
                }else if (daysBetween == 30) {
                    maxDiscount = 10;
                } else if (daysBetween < 30 && daysBetween >= 15) {
                    maxDiscount = 15;
                } else if (daysBetween < 15 && daysBetween >=7) {
                    maxDiscount = 20;
                } else if (daysBetween < 7 && daysBetween >= 3) {
                    maxDiscount = 30;
                } else if (daysBetween <= 2) {
                    maxDiscount = 50;
                } else {
                    showMsg("The end date must be after the start date", "red");
                    return 0;
                }
            }else{
                if(daysBetween > 30){
                    showMsg("The discount can't exceed 30 days", "red");
                    return 0;
                }else if (daysBetween == 30) {
                    maxDiscount = 20;
                }else if (daysBetween < 30 && daysBetween >= 7) {
                    maxDiscount = 30;
                }else if (daysBetween < 7 && daysBetween >= 0) {
                    maxDiscount = 50;
                }
            }
        }else{
            showMsg("Please, select a range of dates to enter the discount", "red");
        }

        return maxDiscount;
    }

    // function to format the date selected on the datepicker
    private void setDatePickerFormat(DatePicker datePicker) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) return "";
                return formatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, formatter);
            }
        });
    }

    // function to reset the disabled cells from the datepicker
    private void resetSecondDatePicker(DatePicker datePicker){
        datePicker.getEditor().setDisable(true);
        datePicker.getEditor().setOpacity(1);

        LocalDate today = LocalDate.now();

        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(today)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #F4F4F4;");
                }
            }
        };
        datePicker.setDayCellFactory(dayCellFactory);
    }

    private void restrictDatePicker(DatePicker datePicker, LocalDate minDate, LocalDate maxDate) {
        datePicker.getEditor().setDisable(true);
        datePicker.getEditor().setOpacity(1);

        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                if ((minDate != null && item.isBefore(minDate)) || (maxDate != null && item.isAfter(maxDate))) {
                    setDisable(true);
                    setStyle("-fx-background-color: #F4F4F4;");
                }
            }
        };

        datePicker.setDayCellFactory(dayCellFactory);
    }

    //function to disable or able the inputs in case a seller has no assigned product
    private void disableInputs(boolean isEnable){
        cb_product.setDisable(isEnable);
        tf_disc.setDisable(isEnable);
        btn_add.setDisable(isEnable);

        dp_from.setDisable(isEnable);
        dp_from.getEditor().setDisable(true);
        dp_from.getEditor().setOpacity(1);

        dp_to.setDisable(isEnable);
        dp_to.getEditor().setDisable(true);
        dp_to.getEditor().setOpacity(1);
    }

    //function to change the Scene
    private void changeScene(Sellers seller){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("app_view.fxml"));
            Pane appView = fxmlLoader.load();

            AppViewController appViewController = fxmlLoader.getController();
            appViewController.setSeller(seller);

            Stage stage = (Stage) cb_product.getScene().getWindow();

            Scene scene = new Scene(appView);
            stage.setScene(scene);
            stage.setTitle("App View");

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //function to show the user a message, color indicates the type of message (red error, green success, black info)
    private void showMsg(String msg, String color) {
        label_msg.setVisible(true);
        label_msg.setText(msg);
        label_msg.setStyle("-fx-text-fill: " + color + ";");
    }

    //function to set the combobox with products and set the datepicker format to dd/MM/yyyy
    private void setAllInputs(){
        setDatePickerFormat(dp_from);
        setDatePickerFormat(dp_to);

        if(list.size()>0) {
            disableInputs(false);
            showMsg("","black");

            //fill the combobox with values
            ObservableList<Products> productList = FXCollections.observableArrayList(list);
            cb_product.setItems(productList);

            //force the combobox to show the products' names
            forceNameComboBox();
        }else{
            showMsg("There's no products assigned to this seller yet","black");
            disableInputs(true);
        }
    }

    // function to force combobox names from class products
    public void forceNameComboBox() {
        cb_product.setConverter(new StringConverter<Products>() {
            @Override
            public String toString(Products product) {
                return product != null ? product.getProductName() : "";
            }

            @Override
            public Products fromString(String string) {
                return cb_product.getItems().stream()
                        .filter(product -> product.getProductName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

}
