package com.tp.APP1.controllers;

import com.tp.APP1.dao.ClientDAO;
import com.tp.APP1.dao.ClientDAOImpl;
import com.tp.APP1.models.Client;
import com.tp.APP1.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur pour la gestion des clients dans l'interface utilisateur.
 * Cette classe gère les interactions entre l'interface graphique et les données
 * en utilisant le pattern DAO pour l'accès aux données.
 * Seuls les utilisateurs avec le rôle 'admin' peuvent gérer les clients.
 */
public class ClientController {

    @FXML
    private TextField textFieldName;

    @FXML
    private GridPane gridPane;

    private final ClientDAO clientDAO;

    private User currentUser;

    /**
     * Constructeur par défaut qui initialise le DAO.
     */
    public ClientController() {
        this.clientDAO = new ClientDAOImpl();
    }

    /**
     * Initialise le contrôleur. Cette méthode est appelée automatiquement
     * après le chargement du fichier FXML.
     */
    @FXML
    private void initialize() {
        System.out.println("Chargement des données clients depuis la base de données...");
        clearInputFields();
        loadClients(); // Charger les données après ouverture de l'application

    }

    /**
     * Définit l'utilisateur courant pour le contrôle d'accès.
     * 
     * @param user L'utilisateur actuellement connecté
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Vérifie si l'utilisateur courant a le rôle admin.
     * 
     * @return true si l'utilisateur est admin, false sinon
     */
    private boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }

    /**
     * Charge et affiche tous les clients dans le GridPane.
     */
    @FXML
    public void loadClients() {
        try {
            String searchTerm = textFieldName.getText();
            List<Client> clients;

            if (searchTerm != null && !searchTerm.isEmpty()) {
                clients = clientDAO.searchByName(searchTerm);
                if (clients.isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "Information", "Recherche", 
                            "Aucun client ne correspond à votre recherche.");
                    clearInputFields();
                    clients = clientDAO.getAll();
                }
            } else {
                clients = clientDAO.getAll();
            }

            displayClientsInGrid(clients);
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    /**
     * Affiche les clients dans le GridPane.
     * 
     * @param clients Liste des clients à afficher
     */
    private void displayClientsInGrid(List<Client> clients) {
        // Nettoyer le GridPane
        gridPane.getChildren().clear();

        // Ajout des en-têtes
        Label nameHeader = new Label("Nom");
        Label emailHeader = new Label("Email");
        Label phoneHeader = new Label("Téléphone");
        Label addressHeader = new Label("Adresse");
        Label actionsHeader = new Label("Actions");

        // Appliquer la classe CSS pour les en-têtes
        nameHeader.getStyleClass().add("column-header");
        emailHeader.getStyleClass().add("column-header");
        phoneHeader.getStyleClass().add("column-header");
        addressHeader.getStyleClass().add("column-header");
        actionsHeader.getStyleClass().add("column-header");

        gridPane.add(nameHeader, 0, 0);
        gridPane.add(emailHeader, 1, 0);
        gridPane.add(phoneHeader, 2, 0);
        gridPane.add(addressHeader, 3, 0);
        gridPane.add(actionsHeader, 4, 0);

        int rowIndex = 1; // Démarrer à la deuxième ligne (après les en-têtes)

        // Ajouter chaque client au GridPane
        for (Client client : clients) {
            // Créer les labels pour les données du client
            Label nameLabel = new Label(client.getName());
            Label emailLabel = new Label(client.getEmail());
            Label phoneLabel = new Label(client.getPhone());
            Label addressLabel = new Label(client.getAddress());

            // Appliquer la classe CSS pour les données
            nameLabel.getStyleClass().add("data-row");
            emailLabel.getStyleClass().add("data-row");
            phoneLabel.getStyleClass().add("data-row");
            addressLabel.getStyleClass().add("data-row");

            gridPane.add(nameLabel, 0, rowIndex);
            gridPane.add(emailLabel, 1, rowIndex);
            gridPane.add(phoneLabel, 2, rowIndex);
            gridPane.add(addressLabel, 3, rowIndex);

            // Boutons d'action (seulement pour les admins)
            if (isAdmin()) {
                // Bouton de suppression
                Button deleteButton = new Button("Supprimer");
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> deleteClient(client.getId()));
                gridPane.add(deleteButton, 4, rowIndex);

                // Bouton de modification
                Button modifyButton = new Button("Modifier");
                modifyButton.getStyleClass().add("edit-button");
                modifyButton.setOnAction(event -> prepareClientForEdit(client.getId()));
                gridPane.add(modifyButton, 5, rowIndex);
            }

            rowIndex++;
        }
    }

    /**
     * Ouvre une fenêtre de dialogue pour ajouter un nouveau client.
     * Cette action n'est disponible que pour les administrateurs.
     */
    @FXML
    public void addClient() {
        if (!isAdmin()) {
            showAlert(Alert.AlertType.WARNING, "Accès refusé", "Permission insuffisante", 
                    "Seuls les administrateurs peuvent ajouter des clients.");
            return;
        }

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
            dialogStage.setTitle("Ajouter un client");
            dialogStage.initModality(Modality.WINDOW_MODAL); // Bloque l'interaction avec la fenêtre principale
            dialogStage.setScene(scene);

            // Ajouter la feuille de style CSS
            scene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/application.css").toExternalForm());

            // Afficher la fenêtre et attendre qu'elle soit fermée
            dialogStage.showAndWait();

            // Vérifier si un client a été ajouté
            if (controller.isClientSaved()) {
                loadClients(); // Recharger la liste des clients
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur d'interface", 
                    "Impossible d'ouvrir la fenêtre d'ajout de client: " + e.getMessage());
        }
    }

    /**
     * Supprime un client de la base de données.
     * Cette action n'est disponible que pour les administrateurs.
     * 
     * @param clientId L'identifiant du client à supprimer
     */
    private void deleteClient(int clientId) {
        if (!isAdmin()) {
            showAlert(Alert.AlertType.WARNING, "Accès refusé", "Permission insuffisante", 
                    "Seuls les administrateurs peuvent supprimer des clients.");
            return;
        }

        try {
            Client client = clientDAO.getById(clientId);
            if (client == null) {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Client introuvable",
                        "Le client que vous essayez de supprimer n'existe pas.");
                return;
            }

            //  Créer une boîte de dialogue de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText("Voulez-vous vraiment supprimer ce client ?");
            confirmationAlert.setContentText("Client : " + client.getName());

            //  Attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            //  Si l'utilisateur confirme
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (clientDAO.delete(clientId)) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Suppression réussie",
                            "Le client a été supprimé avec succès.");
                    loadClients();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Suppression échouée",
                            "Le client n'a pas pu être supprimé.");
                }
            } else {
                System.out.println("Suppression annulée par l'utilisateur.");
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    /**
     * Ouvre une fenêtre de dialogue pour modifier un client existant.
     * Cette action n'est disponible que pour les administrateurs.
     * 
     * @param clientId L'identifiant du client à modifier
     */
    private void prepareClientForEdit(int clientId) {
        if (!isAdmin()) {
            showAlert(Alert.AlertType.WARNING, "Accès refusé", "Permission insuffisante", 
                    "Seuls les administrateurs peuvent modifier des clients.");
            return;
        }

        try {
            // Charger le fichier FXML du formulaire
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/ClientFormDialog.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur et configurer pour la modification
            ClientFormController controller = loader.getController();
            controller.setupForEdit(clientId);

            // Créer une nouvelle scène et fenêtre
            Scene scene = new Scene(root);
            Stage dialogStage = new Stage();

            // Configurer la fenêtre
            dialogStage.setTitle("Modifier un client");
            dialogStage.initModality(Modality.WINDOW_MODAL); // Bloque l'interaction avec la fenêtre principale
            dialogStage.setScene(scene);

            // Ajouter la feuille de style CSS
            scene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/application.css").toExternalForm());

            // Afficher la fenêtre et attendre qu'elle soit fermée
            dialogStage.showAndWait();

            // Vérifier si un client a été modifié
            if (controller.isClientSaved()) {
                loadClients(); // Recharger la liste des clients
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur d'interface", 
                    "Impossible d'ouvrir la fenêtre de modification de client: " + e.getMessage());
        }
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

    /**
     * Efface les champs de texte.
     */
    private void clearInputFields() {
        textFieldName.clear();
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

    /**
     * Retourne à la vue des produits.
     */
    @FXML
    private void backToProducts() {
        try {
            // Charger le fichier FXML de la vue des produits
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/ProductView.fxml"));
            BorderPane root = loader.load();

            // Obtenir le contrôleur de la vue chargée
            ProductController productController = loader.getController();

            // Passer l'utilisateur courant (admin) au contrôleur des produits
            productController.setCurrentUser(currentUser);

            // Obtenir la scène actuelle
            Scene scene = gridPane.getScene();

            // Remplacer le contenu de la scène
            scene.setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur de navigation",
                    "Impossible de retourner à la vue des produits: " + e.getMessage());
        }
    }

}
