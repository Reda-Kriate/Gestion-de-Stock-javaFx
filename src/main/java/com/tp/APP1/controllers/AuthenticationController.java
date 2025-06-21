package com.tp.APP1.controllers;

import com.tp.APP1.dao.UserDAO;
import com.tp.APP1.dao.UserDAOImpl;
import com.tp.APP1.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Contrôleur pour la gestion de l'authentification des utilisateurs.
 * Cette classe gère les interactions entre l'interface de connexion et les données
 * en utilisant le pattern DAO pour l'accès aux données.
 */
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

    /**
     * Constructeur par défaut qui initialise le DAO.
     */
    public AuthenticationController() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Initialise le contrôleur. Cette méthode est appelée automatiquement
     * après le chargement du fichier FXML.
     */
    @FXML
    private void initialize() {
        // Configuration des événements
        loginButton.setOnAction(event -> login());
    }

    /**
     * Gère le processus de connexion lorsque l'utilisateur clique sur le bouton de connexion.
     */
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
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Identifiants invalides", 
                        "Le nom d'utilisateur ou le mot de passe est incorrect.");
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    @FXML
    private void createUser() {
        javafx.scene.control.Dialog<User> dlg = new javafx.scene.control.Dialog<>();
        dlg.setTitle("Créer utilisateur");
        javafx.scene.control.ButtonType ok = new javafx.scene.control.ButtonType("Créer", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dlg.getDialogPane().getButtonTypes().addAll(ok, javafx.scene.control.ButtonType.CANCEL);

        javafx.scene.layout.GridPane g = new javafx.scene.layout.GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new javafx.geometry.Insets(20));

        TextField u = new TextField();
        u.setPromptText("Nom");
        PasswordField p = new PasswordField();
        p.setPromptText("Mot de passe");
        javafx.scene.control.ComboBox<String> cb = new javafx.scene.control.ComboBox<>(
                javafx.collections.FXCollections.observableArrayList("client", "admin")
        );
        cb.setValue("client");

        g.add(new javafx.scene.control.Label("Nom:"), 0, 0);
        g.add(u, 1, 0);
        g.add(new javafx.scene.control.Label("Pass:"), 0, 1);
        g.add(p, 1, 1);
        g.add(new javafx.scene.control.Label("Rôle:"), 0, 2);
        g.add(cb, 1, 2);

        dlg.getDialogPane().setContent(g);
        javafx.application.Platform.runLater(u::requestFocus);

        dlg.setResultConverter(btn -> {
            if (btn == ok) return new User(u.getText(), p.getText(), cb.getValue());
            return null;
        });

        dlg.showAndWait().ifPresent(user -> {
            try {
                new UserDAOImpl().add(user);
                showAlert(Alert.AlertType.INFORMATION, "Succès", null, "Utilisateur créé.");
            } catch (SQLException e) {
                handleDatabaseError(e);
            }
        });
    }

    /**
     * Vérifie si la connexion a réussi.
     * 
     * @return true si l'utilisateur s'est connecté avec succès, false sinon
     */
    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    /**
     * Obtient l'utilisateur authentifié.
     * 
     * @return L'utilisateur authentifié ou null si aucun utilisateur n'est connecté
     */
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    /**
     * Affiche une boîte de dialogue d'alerte.
     */
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

    /**
     * Gère les erreurs de base de données.
     */
    private void handleDatabaseError(Exception e) {
        System.err.println("Erreur de base de données: " + e.getMessage());
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Erreur de base de données", 
                "Une erreur est survenue lors de l'accès à la base de données", 
                "Détails: " + e.getMessage());
    }
}