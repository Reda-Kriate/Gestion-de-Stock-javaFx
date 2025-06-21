package com.tp.APP1.controllers;

import com.tp.APP1.dao.ClientDAO;
import com.tp.APP1.dao.ClientDAOImpl;
import com.tp.APP1.models.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Contrôleur pour le formulaire d'ajout et de modification de clients.
 * Cette classe gère l'interface utilisateur du formulaire dans une fenêtre séparée.
 */
public class ClientFormController {

    @FXML
    private Label titleLabelCliForm;

    @FXML
    private TextField textFieldNameCliForm;

    @FXML
    private TextField textFieldEmailCliForm;

    @FXML
    private TextField textFieldPhoneCliForm;

    @FXML
    private TextField textFieldAddressCliForm;
    @FXML
    private TextField textFieldPasswordCliForm;

    @FXML
    private Button saveButtonCliForm;

    @FXML
    private Button cancelButtonCliForm;

    private ClientDAO clientDAO;
    private Client currentClient;
    private int currentClientId;
    private boolean editMode = false;
    private boolean clientSaved = false;

    @FXML
    private void initialize() {
        clientDAO = new ClientDAOImpl();
    }

    public void setupForAdd() {
        titleLabelCliForm.setText("Ajouter un client");
        editMode = false;
        currentClient = null;
        currentClientId = -1;
        clearFields();
    }

    public void setupForEdit(int clientId) {
        try {
            titleLabelCliForm.setText("Modifier un client");
            editMode = true;
            currentClientId = clientId;
            
            Client client = clientDAO.getById(clientId);
            if (client != null) {
                currentClient = client;
                textFieldNameCliForm.setText(client.getName());
                textFieldEmailCliForm.setText(client.getEmail());
                textFieldPhoneCliForm.setText(client.getPhone());
                textFieldAddressCliForm.setText(client.getAddress());
                textFieldPasswordCliForm.setText(client.getPassword());
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    @FXML
    private void handleSave() {
        try {
            String name = textFieldNameCliForm.getText();
            String email = textFieldEmailCliForm.getText();
            String phone = textFieldPhoneCliForm.getText();
            String address = textFieldAddressCliForm.getText();
            String password = textFieldPasswordCliForm.getText();

            if (isInputValid(name)) {
                Client client = new Client(name, email, phone, address, password);

                if (editMode) {
                    // Mode modification
                    client.setId(currentClientId);
                    if (clientDAO.update(client)) {
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Modification réussie", 
                                "Le client a été modifié avec succès.");
                        clientSaved = true;
                        closeDialog();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Modification échouée", 
                                "Le client n'a pas pu être modifié.");
                    }
                } else {
                    // Mode ajout
                    if (clientDAO.add(client)) {
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout réussi", 
                                "Le client a été ajouté avec succès.");
                        clientSaved = true;
                        closeDialog();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout échoué", 
                                "Le client n'a pas pu être ajouté.");
                    }
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Entrée invalide", 
                        "Veuillez remplir au moins le champ nom.");
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelButtonCliForm.getScene().getWindow();
        stage.close();
    }


    public boolean isClientSaved() {
        return clientSaved;
    }

    private void clearFields() {
        textFieldNameCliForm.clear();
        textFieldEmailCliForm.clear();
        textFieldPhoneCliForm.clear();
        textFieldAddressCliForm.clear();
    }

    private boolean isInputValid(String name) {
        return name != null && !name.isEmpty();
    }


    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void handleDatabaseError(Exception e) {
        System.err.println("Erreur de base de données: " + e.getMessage());
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Erreur de base de données", 
                "Une erreur est survenue lors de l'accès à la base de données", 
                "Détails: " + e.getMessage());
    }
}