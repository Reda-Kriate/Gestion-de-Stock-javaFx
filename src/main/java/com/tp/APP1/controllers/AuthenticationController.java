package com.tp.APP1.controllers;

import com.tp.APP1.dao.UserDAO;
import com.tp.APP1.dao.UserDAOImpl;
import com.tp.APP1.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Consumer;

public class AuthenticationController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    private final UserDAO userDAO;
    private User authenticatedUser;
    private boolean loginSuccessful = false;

    public AuthenticationController() {
        this.userDAO = new UserDAOImpl();
    }

    @FXML
    private void initialize() {
        // Configuration des événements
        loginButton.setOnAction(event -> login());
    }
    private Consumer<User> onLoginSuccess;

    public void setOnLoginSuccess(Consumer<User> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }
    @FXML
    private void login() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validation des champs
        if (username == null || username.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Nom d'utilisateur requis", 
                    "Veuillez entrer votre nom d'utilisateur.");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Mot de passe requis", 
                    "Veuillez entrer votre mot de passe.");
            return;
        }

        try {
            // Tentative d'authentification
            authenticatedUser = userDAO.authenticate(username, password);

            if (authenticatedUser != null) {
                loginSuccessful = true;
                // Fermer la fenêtre de connexion
                ((Stage) loginButton.getScene().getWindow()).close();
            }


            else {
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Identifiants invalides", 
                        "Le nom d'utilisateur ou le mot de passe est incorrect.");
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }
    @FXML
    public void addClient() {
        try {
            // Charger le fichier FXML du formulaire
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/ClientFormDialog.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur et configurer pour l'ajout
            ClientFormController controller = loader.getController();
            controller.setupForAdd();

            // Créer une nouvelle scène et fenêtre
            Scene scene = new Scene(root);
            Stage dialogStage = new Stage();

            // Configurer la fenêtre
            dialogStage.setTitle("Ajouter un Client");
            dialogStage.initModality(Modality.WINDOW_MODAL); // Bloque l'interaction avec la fenêtre principale
            dialogStage.setScene(scene);

            // Ajouter la feuille de style CSS
            scene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/application.css").toExternalForm());

            // Afficher la fenêtre et attendre qu'elle soit fermée
            dialogStage.showAndWait();


        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur d'interface",
                    "Impossible d'ouvrir la fenêtre d'ajout de client: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleClientLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/ClientLoginPage.fxml"));
            VBox clientRoot = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(clientRoot, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/ClientLoginStyle.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Connexion Client");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDatabaseError(Exception e) {
        System.err.println("Erreur de base de données: " + e.getMessage());
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Erreur de base de données", 
                "Une erreur est survenue lors de l'accès à la base de données", 
                "Détails: " + e.getMessage());
    }
}