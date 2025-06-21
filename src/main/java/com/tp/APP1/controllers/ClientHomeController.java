package com.tp.APP1.controllers;

import com.tp.APP1.dao.ProductDAO;
import com.tp.APP1.dao.ProductDAOImpl;
import com.tp.APP1.models.Client;
import com.tp.APP1.models.Product;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class ClientHomeController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, Integer> quantityColumn;
    @FXML private TableColumn<Product, Void> actionColumn;

    private Client currentClient;
    private ProductDAO productDAO = new ProductDAOImpl();

//    public ClientHomeController(GridPane gridPane) {
//        this.gridPane = gridPane;
//    }

    public void setCurrentClient(Client client) {
        this.currentClient = client;
        loadProducts();
    }

    private void loadProducts() {
        try {
            ObservableList<Product> products = FXCollections.observableArrayList(productDAO.getAllValidatedProducts());
            productTable.setItems(products);

            nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
            priceColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPrice()));
            quantityColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStock()));

            addBuyButtonToTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
        actionColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(null));
    }

    private void addBuyButtonToTable() {
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final TextField quantityField = new TextField();
            private final Button buyButton = new Button("Acheter");

            {
                quantityField.setPrefWidth(50);

                buyButton.setOnAction(e -> {
                    //le probleme ici !!
                    Product product = getTableView().getItems().get(getIndex());
                    System.out.println(product);
                    try {
                        int quantity = Integer.parseInt(quantityField.getText());
                        if (quantity > 0 && quantity <= product.getStock()) {
                            if (product.getId() > 0){
                                boolean success = productDAO.addPurchase(currentClient, product, quantity);
                                  if (success) {
                                showAlert("Demande d'achat envoyée. En_attente de validation.");
                                loadProducts();  // <- Ici on rafraîchit la table après achat
                                 } else {
                                showAlert("Erreur lors de la demande d'achat.");}
                            }else{
                                showAlert("Erreur ID non valide.");
                            }

                        } else {
                            showAlert("Quantité invalide.");
                        }
                    } catch (NumberFormatException ex) {
                        showAlert("Entrez un nombre valide.");
                    } catch (SQLException ex) {
                        showAlert("Erreur base de données : " + ex.getMessage());
                    }
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, quantityField, buyButton);
                    setGraphic(box);
                }
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Achat");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/LoginView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/application.css").toExternalForm());

            Stage stage = (Stage) productTable.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

