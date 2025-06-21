package com.tp.APP1.controllers;

import com.tp.APP1.dao.ProductDAO;
import com.tp.APP1.dao.ProductDAOImpl;
import com.tp.APP1.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

/**
 * Contrôleur pour le formulaire d'ajout et de modification de produits.
 * Cette classe gère l'interface utilisateur du formulaire dans une fenêtre séparée.
 */
public class ProductFormController {

    @FXML
    private Label titleLabelProForm;

    @FXML
    private TextField textFieldNameProForm;

    @FXML
    private TextField textFieldPriceProForm;


    @FXML
    private TextField textFieldQteProForm;


    @FXML
    private Button saveButtonProForm;

    @FXML
    private Button cancelButtonProForm;

    private ProductDAO productDAO;
    private Product currentProduct;
    private String oldProductName;
    private boolean editMode = false;
    private boolean productSaved = false;

    /**
     * Initialise le contrôleur.
     */
    @FXML
    private void initialize() {
        productDAO = new ProductDAOImpl();
    }

    /**
     * Configure le formulaire pour l'ajout d'un nouveau produit.
     */
    public void setupForAdd() {
        titleLabelProForm.setText("Ajouter un produit");
        editMode = false;
        currentProduct = null;
        oldProductName = null;
        clearFields();
    }

    /**
     * Configure le formulaire pour la modification d'un produit existant.
     * 
     * @param productName Le nom du produit à modifier
     */
    public void setupForEdit(String productName) {
        try {
            titleLabelProForm.setText("Modifier un produit");
            editMode = true;
            oldProductName = productName;
            
            Product product = productDAO.getByName(productName);
            if (product != null) {
                currentProduct = product;
                textFieldNameProForm.setText(product.getName());
                textFieldPriceProForm.setText(String.valueOf(product.getPrice()));
                textFieldQteProForm.setText(String.valueOf(product.getStock()));
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    /**
     * Gère l'action du bouton Enregistrer.
     */
    @FXML
    private void handleSave() {
        try {
            String name = textFieldNameProForm.getText();
            Double price = parseDouble(textFieldPriceProForm.getText());
            Integer quantity = parseInteger(textFieldQteProForm.getText());

            if (isInputValid(name, price, quantity)) {
                Product product = new Product(name, price, quantity);

                if (editMode) {
                    // Mode modification
                    if (!name.equals(oldProductName) && productDAO.exists(name)) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Nom déjà utilisé", 
                                "Un autre produit avec ce nom existe déjà.");
                        return;
                    }

                    if (productDAO.update(oldProductName, product)) {
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Modification réussie", 
                                "Le produit a été modifié avec succès.");
                        productSaved = true;
                        closeDialog();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Modification échouée", 
                                "Le produit n'a pas pu être modifié.");
                    }
                } else {
                    // Mode ajout
                    if (productDAO.exists(name)) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Produit existant", 
                                "Ce produit existe déjà dans la base de données.");
                        return;
                    }

                    if (productDAO.add(product)) {
                        showAlert(Alert.AlertType.INFORMATION, "Succès", "Ajout réussi", 
                                "Veuillez attendre la confirmation par l'admine");
                        productSaved = true;
                        closeDialog();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout échoué", 
                                "Le produit n'a pas pu être ajouté.");
                    }
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Entrée invalide", 
                        "Veuillez remplir tous les champs correctement.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Entrée invalide", 
                    "Veuillez entrer des nombres valides pour le prix et la quantité.");
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    /**
     * Gère l'action du bouton Annuler.
     */
    @FXML
    private void handleCancel() {
        closeDialog();
    }

    /**
     * Ferme la fenêtre de dialogue.
     */
    private void closeDialog() {
        Stage stage = (Stage) cancelButtonProForm.getScene().getWindow();
        stage.close();
    }

    /**
     * Vérifie si un produit a été sauvegardé.
     * 
     * @return true si un produit a été sauvegardé, false sinon
     */
    public boolean isProductSaved() {
        return productSaved;
    }

    /**
     * Efface les champs du formulaire.
     */
    private void clearFields() {
        textFieldNameProForm.clear();
        textFieldPriceProForm.clear();
        textFieldQteProForm.clear();
    }

    /**
     * Vérifie si les entrées sont valides.
     */
    private boolean isInputValid(String name, Double price, Integer quantity) {
        return name != null && !name.isEmpty() && price != null && price > 0 
                && quantity != null && quantity > 0;
    }

    /**
     * Convertit une chaîne en double.
     */
    private Double parseDouble(String text) {
        try {
            return Double.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Convertit une chaîne en entier.
     */
    private Integer parseInteger(String text) {
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            return null;
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