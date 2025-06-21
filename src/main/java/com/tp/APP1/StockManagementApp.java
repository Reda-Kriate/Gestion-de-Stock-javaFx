package com.tp.APP1;

import com.tp.APP1.controllers.AuthenticationController;
import com.tp.APP1.controllers.ProductController;
import com.tp.APP1.models.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class StockManagementApp extends Application {

    private User authenticatedUser;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Afficher l'écran de connexion d'abord
            if (!showLoginScreen()) {
                // Si la connexion échoue, fermer l'application
                Platform.exit();
                return;
            }

            // Charger le fichier FXML principal après connexion réussie
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/ProductView.fxml"));
            BorderPane root = loader.load();

            // Obtenir le contrôleur et lui passer l'utilisateur authentifié
            ProductController controller = loader.getController();
            controller.setCurrentUser(authenticatedUser);

            // Créer la scène
            Scene scene = new Scene(root, 900, 600);

            // Ajouter la feuille de style CSS
            scene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/application.css").toExternalForm());



            // Configurer la fenêtre principale
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestion de Stock - Utilisateur: " + authenticatedUser.getUsername());
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(500);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'interface: " + e.getMessage());
            e.printStackTrace();

            // Afficher une alerte en cas d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de démarrer l'application");
            alert.setContentText("Une erreur est survenue lors du chargement de l'interface: " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Affiche l'écran de connexion et attend que l'utilisateur se connecte.
     *
     * @return true si la connexion est réussie, false sinon
     */
    private boolean showLoginScreen() {
        try {
            // Charger le fichier FXML de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/LoginView.fxml"));
            BorderPane root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root, 800, 600);

            // Ajouter la feuille de style CSS
            scene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/application.css").toExternalForm());

            // Créer une nouvelle fenêtre
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.setTitle("Connexion");
            loginStage.setResizable(false);
            loginStage.initModality(Modality.APPLICATION_MODAL);

            // Obtenir le contrôleur
            AuthenticationController controller = loader.getController();

            // Afficher la fenêtre et attendre qu'elle soit fermée
            loginStage.showAndWait();

            // Vérifier si la connexion a réussi
            if (controller.isLoginSuccessful()) {
                authenticatedUser = controller.getAuthenticatedUser();
                return true;
            }

            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'écran de connexion: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Méthode principale qui lance l'application.
     *
     * @param args Arguments de ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
