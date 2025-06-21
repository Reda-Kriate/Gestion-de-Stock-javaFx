package com.tp.APP1.controllers;

import com.tp.APP1.dao.AchatDAO;
import com.tp.APP1.dao.AchatDAOImpl;
import com.tp.APP1.dao.ProductDAO;
import com.tp.APP1.dao.ProductDAOImpl;
import com.tp.APP1.models.Achat;
import com.tp.APP1.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class AdminAchatValidationController {

    @FXML
    private TableView<Achat> achatTable;

    @FXML
    private TableColumn<Achat, String> clientColumn;

    @FXML
    private TableColumn<Achat, String> productColumn;

    @FXML
    private TableColumn<Achat, Integer> quantityColumn;

    @FXML
    private TableColumn<Achat, String> statusColumn;

    @FXML
    private TableColumn<Achat, Void> actionColumn;

    private final AchatDAO achatDAO = new AchatDAOImpl();
    private final ObservableList<Achat> achatList = FXCollections.observableArrayList();
    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        // ici, tu peux mettre le code pour mettre à jour l’UI selon l’utilisateur
    }

    @FXML
    public void initialize() {
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        addActionButtons();

        loadAchatsEnAttente();
    }

    private void loadAchatsEnAttente() {
        try {
            achatList.setAll(achatDAO.getAchatsEnAttente());
            achatTable.setItems(achatList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btnValider = new Button("Valider");
            private final Button btnRefuser = new Button("Refuser");
            private final HBox hBox = new HBox(10, btnValider, btnRefuser);

            {
                btnValider.setOnAction(e -> {
                    Achat achat = getTableView().getItems().get(getIndex());
                    validerAchat(achat);
                });

                btnRefuser.setOnAction(e -> {
                    Achat achat = getTableView().getItems().get(getIndex());
                    refuserAchat(achat);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hBox);
                }
            }
        });
    }
    private final ProductDAO productDAO = new ProductDAOImpl();

    private void validerAchat(Achat achat) {
        try {
            boolean success = productDAO.validatePurchase(achat.getId());

            if (success) {
                showAlert("Succès", "Achat validé et stock mis à jour.");
            } else {
                showAlert("Erreur", "Impossible de valider l’achat. Vérifie le stock ou l’ID.");
            }

            loadAchatsEnAttente(); // Refresh la table
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//    private void validerAchat(Achat achat) {
//        try {
//            achatDAO.updateStatus(achat.getId(), "validé");
//            loadAchatsEnAttente();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    private void refuserAchat(Achat achat) {
        try {
            achatDAO.updateStatus(achat.getId(), "refusé");
            loadAchatsEnAttente();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goBackToAdminView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/ProductView.fxml"));
            Parent root = loader.load();
            ProductController ctrl = loader.getController();
            ctrl.setCurrentUser(currentUser); // Assure-toi d’avoir currentUser dans ce contrôleur aussi

            Scene scene = achatTable.getScene();
            scene.setRoot(root);

            // Ajouter la feuille de style à la scène existante
            scene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/application.css").toExternalForm());

        } catch (Exception e) {
            e.printStackTrace();
            // Ou gérer l’erreur proprement
        }
    }

}
