package com.tp.APP1.controllers;

import com.tp.APP1.dao.ClientDAO;
import com.tp.APP1.dao.ClientDAOImpl;
import com.tp.APP1.models.Client;
import com.tp.APP1.models.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginClientController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final ClientDAO clientDAO = new ClientDAOImpl();

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if(email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        try {
            Client client = clientDAO.getClientByEmailAndPassword(email, password);

            if(client != null) {
                openClientHome(client); // accès autorisé
            } else {
                showError("Email ou mot de passe incorrect.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de la connexion.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void openClientHome(Client client) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/ClientHomeView.fxml"));
            BorderPane root = loader.load();

            ClientHomeController controller = loader.getController();
            controller.setCurrentClient(client);

            Stage stage = (Stage) emailField.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/ClientHomePageStyle.css").toExternalForm());
            stage.setScene(scene);


            stage.setTitle("Accueil Client");


        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de l'accueil.");
        }
    }
    @FXML
    private void retourButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/LoginView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root,900,600);
            scene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/application.css").toExternalForm());

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Connexion");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors du retour à la page de connexion.");
        }
    }

}
