
package com.tp.APP1.controllers;

import com.tp.APP1.dao.ProductDAO;
import com.tp.APP1.dao.ProductDAOImpl;
import com.tp.APP1.dao.UserDAO;
import com.tp.APP1.dao.UserDAOImpl;
import com.tp.APP1.models.Product;
import com.tp.APP1.models.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProductController {

    @FXML
    private TextField textFieldNameProView;
    @FXML
    private GridPane gridPane;
    @FXML
    private Label welcomeLabelproview;
    @FXML
    private Button clientsButtonProView;
    @FXML
    private Button reintialiserbutton;
    @FXML
    private Button productAttent;

    private final ProductDAO productDAO;
    private User currentUser;
    private String status; // nouveau champ
    @FXML
    private Label pendingCountLabel;
    @FXML
    private Button validatePurchasesButton;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductController() {
        this.productDAO = new ProductDAOImpl();
    }

    @FXML
    private ImageView roleImageViewProduct;

    @FXML
    private Label roleLabelviewpro;

    @FXML
    private Label roleDescriptionLabel;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabelproview.setText("Bienvenue " + user.getUsername() + " !");
        clientsButtonProView.setVisible(isAdmin());
        reintialiserbutton.setVisible(isAdmin());
        productAttent.setVisible(isAdmin());
        updateRoleSpecificUI();
        deferOrApplyRoleStyle();
        updatePendingNotification();
        validatePurchasesButton.setVisible(isAdmin());

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
        dlg.getDialogPane().getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/AjouterUser.css").toExternalForm());
        dlg.getDialogPane().getStyleClass().add("dialog-pane");
        g.getStyleClass().add("grid-pane");
        dlg.showAndWait().ifPresent(user -> {
            try {
                new UserDAOImpl().add(user);
                showAlert(Alert.AlertType.INFORMATION, "Succès", null, "Utilisateur créé.");
            } catch (SQLException e) {
                handleDatabaseError(e);
            }
        });

    }

    private void updatePendingNotification() {
        if (!isAdmin()) {
            pendingCountLabel.setVisible(false);
            return;
        }
        try {
            List<Product> pending = productDAO.getByStatus("en_attente");
            int count = pending.size();

            if (count > 0) {
                pendingCountLabel.setText(String.valueOf(count));
                pendingCountLabel.setVisible(true);
            } else {
                pendingCountLabel.setVisible(false);
            }
        } catch (SQLException e) {
            pendingCountLabel.setVisible(false);
            handleDatabaseError(e);
        }
    }

    private void updateRoleSpecificUI() {
        if (isAdmin()) {
            // Set admin-specific UI elements
            roleImageViewProduct.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/tp/APP1/images/admin_icon.svg"))));
            roleLabelviewpro.setText("Rôle: Administrateur");
            roleDescriptionLabel.setText("En tant qu'administrateur, vous pouvez gérer les produits, les clients et les utilisateurs ainsi que les Achats.");
        } else {
            // Set client-specific UI elements
            roleImageViewProduct.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/tp/APP1/images/client_icon.svg"))));
            roleLabelviewpro.setText("Rôle: User");
            roleDescriptionLabel.setText("En tant que User, vous pouvez consulter et Ajouter les produits.");
        }
    }

    private void deferOrApplyRoleStyle() {
        if (gridPane.getScene() != null) {
            applyRoleStyle();
        } else {
            gridPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) applyRoleStyle();
            });
        }
    }

    private void applyRoleStyle() {
        Platform.runLater(() -> {
            BorderPane root = (BorderPane) gridPane.getScene().getRoot();

            if (isAdmin()) {
                // Apply admin-specific styles
                root.getStyleClass().add("admin-background");
                welcomeLabelproview.getStyleClass().add("admin-welcome");
                roleLabelviewpro.getStyleClass().add("admin-role");
                roleDescriptionLabel.getStyleClass().add("admin-role-description");
//                createUserButtonProView.getStyleClass().add("create-user-button");
            } else {
                // Apply client-specific styles
                root.getStyleClass().add("client-background");
                welcomeLabelproview.getStyleClass().add("client-welcome");
                roleLabelviewpro.getStyleClass().add("client-role");
                roleDescriptionLabel.getStyleClass().add("client-role-description");
            }
        });
    }

    private boolean isAdmin() {
        return currentUser != null && "admin".equals(currentUser.getRole());
    }

    @FXML
    private void initialize() {
        clearInputFields();
        loadProducts();
        updatePendingNotification();

        // On cache le bouton "Valider Achats" tant qu'on n'a pas le rôle
        if (validatePurchasesButton != null) {
            validatePurchasesButton.setVisible(false);
        }
    }

    @FXML
    public void loadProducts() {
        try {
            String searchTerm = textFieldNameProView.getText();
            List<Product> products;

            if (searchTerm != null && !searchTerm.isEmpty()) {
                products = productDAO.searchByName(searchTerm);
            } else {
                products = productDAO.getAll();
            }

            // 🔒 Si l'utilisateur est un client, filtrer les produits non validés
            if (!isAdmin()) {
                products.removeIf(p -> !"validé".equalsIgnoreCase(p.getStatus()));
            }

            if (searchTerm != null && !searchTerm.isEmpty() && products.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Recherche", null,
                        "Aucun produit ne correspond à votre recherche.");
                clearInputFields();
                products = productDAO.getAll();

                if (!isAdmin()) {
                    products.removeIf(p -> !"validé".equalsIgnoreCase(p.getStatus()));
                }
            }

            displayProductsInGrid(products);
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void displayProductsInGrid(List<Product> products) {
        if (isAdmin()) {
            gridPane.getChildren().clear();
            // En-têtes
            String[] headers = {"Nom", "Prix", "Quantité"};
            for (int i = 0; i < headers.length; i++) {
                Label hdr = new Label(headers[i]);
                hdr.getStyleClass().add("column-header");
                gridPane.add(hdr, i, 0);
            }
            int row = 1;
            for (Product p : products) {
                Label name = new Label(p.getName());
                Label price = new Label(String.valueOf(p.getPrice()));
                Label qty = new Label(String.valueOf(p.getStock()));
                name.getStyleClass().add("data-row");
                price.getStyleClass().add("data-row");
                qty.getStyleClass().add("data-row");
                gridPane.add(name, 0, row);
                gridPane.add(price, 1, row);
                gridPane.add(qty, 2, row);

                Button btnDel = new Button("Supprimer");
                btnDel.getStyleClass().add("delete-button");
                btnDel.setOnAction(e -> deleteProduct(p));
                gridPane.add(btnDel, 3, row);

                Button btnEdit = new Button("Modifier");
                btnEdit.getStyleClass().add("edit-button");
                btnEdit.setOnAction(e -> prepareProductForEdit(p));
                gridPane.add(btnEdit, 4, row);

                row++;
            }
        } else {
            gridPane.getChildren().clear();

            String[] headers = {"Nom", "Prix", "Quantité"};
            for (int i = 0; i < headers.length; i++) {
                Label hdr = new Label(headers[i]);
                hdr.getStyleClass().add("column-header");
                gridPane.add(hdr, i, 0);
            }
            int row = 1;
            for (Product p : products) {
                Label name = new Label(p.getName());
                Label price = new Label(String.valueOf(p.getPrice()));
                Label qty = new Label(String.valueOf(p.getStock()));

                name.getStyleClass().add("data-row");
                price.getStyleClass().add("data-row");
                qty.getStyleClass().add("data-row");

                gridPane.add(name, 0, row);
                gridPane.add(price, 1, row);
                gridPane.add(qty, 2, row);


                row++;
            }
        }
    }

    @FXML
    public void addProduct() {
        loadProductDialog(null);
        updatePendingNotification();
    }

    private void deleteProduct(Product product) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Suppression");
        confirm.setHeaderText(null);
        confirm.setContentText("Supprimer le produit '" + product.getName() + "' ?");
        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                productDAO.delete(product.getName());
                loadProducts();
            } catch (SQLException e) {
                handleDatabaseError(e);
            }
        }
    }

    private void prepareProductForEdit(Product product) {
        loadProductDialog(product);
    }

    private void loadProductDialog(Product toEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/tp/APP1/views/ProductFormDialog.fxml"));
            Parent root = loader.load();
            ProductFormController ctrl = loader.getController();
            if (toEdit == null) ctrl.setupForAdd();
            else ctrl.setupForEdit(toEdit.getName());

            Stage dlg = new Stage();
            dlg.initModality(Modality.WINDOW_MODAL);
            dlg.setTitle(toEdit == null ? "Ajouter un produit" : "Modifier un produit");
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource("/com/tp/APP1/styles/application.css").toExternalForm());
            dlg.setScene(scene);
            dlg.showAndWait();
            if (ctrl.isProductSaved()) loadProducts();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", null,
                    "Impossible d'ouvrir le dialogue: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }

    private void clearInputFields() {
        textFieldNameProView.clear();
    }

    private void handleDatabaseError(Exception e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "BDD", null, e.getMessage());
    }

    @FXML
    private void manageClients() {
        if (!isAdmin()) {
            showAlert(Alert.AlertType.WARNING, "Accès refusé", null,
                    "Seuls les admins peuvent gérer les clients.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/tp/APP1/views/ClientView.fxml"));
            Parent root = loader.load();
            ClientController ctrl = loader.getController();
            ctrl.setCurrentUser(currentUser);
            gridPane.getScene().setRoot(root);
        } catch (Exception e) {
            handleDatabaseError(e);
        }
    }

    @FXML
    private void logout() {
        Stage st = (Stage) gridPane.getScene().getWindow();
        st.close();
        try {
            new com.tp.APP1.StockManagementApp().start(new Stage());
        } catch (Exception e) {
            handleDatabaseError(e);
        }
    }

    @FXML
    public void loadPendingProducts() {
        if (!isAdmin()) {
            showAlert(Alert.AlertType.WARNING, "Accès refusé", null,
                    "Seuls les admins peuvent valider les produits.");
            return;
        }
        try {
            List<Product> pendingProducts = productDAO.getByStatus("en_attente");
            displayProductsInGridWithValidation(pendingProducts);
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    private void displayProductsInGridWithValidation(List<Product> products) {
        gridPane.getChildren().clear();

        String[] headers = {"Nom", "Prix", "Quantité", "Actions"};
        for (int i = 0; i < headers.length; i++) {
            Label hdr = new Label(headers[i]);
            hdr.getStyleClass().add("column-header");
            gridPane.add(hdr, i, 0);
        }

        int row = 1;
        for (Product p : products) {
            Label name = new Label(p.getName());
            Label price = new Label(String.valueOf(p.getPrice()));
            Label qty = new Label(String.valueOf(p.getStock()));

            name.getStyleClass().add("data-row");
            price.getStyleClass().add("data-row");
            qty.getStyleClass().add("data-row");

            gridPane.add(name, 0, row);
            gridPane.add(price, 1, row);
            gridPane.add(qty, 2, row);

            Button btnValidate = new Button("Valider");
            btnValidate.getStyleClass().add("validate-button");
            btnValidate.setOnAction(e -> validateProduct(p));

            Button btnReject = new Button("Refuser");
            btnReject.getStyleClass().add("reject-button");
            btnReject.setOnAction(e -> rejectProduct(p));

            HBox actions = new HBox(10, btnValidate, btnReject); // espacement entre les deux
            gridPane.add(actions, 3, row);

            row++;
        }
    }

    private void rejectProduct(Product product) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Refus");
        confirm.setHeaderText(null);
        confirm.setContentText("Refuser le produit '" + product.getName() + "' ?");

        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                boolean success = productDAO.updateStatus(product.getName(), "refusé");
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", null,
                            "Produit refusé avec succès.");
                    loadPendingProducts();
                    updatePendingNotification();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", null,
                            "Impossible de refuser le produit.");
                }
            } catch (SQLException e) {
                handleDatabaseError(e);
            }
        }
    }

    private void validateProduct(Product product) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Validation");
        confirm.setHeaderText(null);
        confirm.setContentText("Valider le produit '" + product.getName() + "' ?");
        Optional<ButtonType> res = confirm.showAndWait();

        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                boolean success = productDAO.updateStatus(product.getName(), "validé");
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Succès", null,
                            "Produit validé avec succès.");
                    loadPendingProducts();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", null,
                            "Impossible de valider le produit.");
                }
            } catch (SQLException e) {
                handleDatabaseError(e);
            }
        }
        updatePendingNotification();
    }

    @FXML
    private void goToAchatValidation() {
        if (!isAdmin()) {
            showAlert(Alert.AlertType.WARNING, "Accès refusé", null,
                    "Seuls les admins peuvent valider les achats.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tp/APP1/views/AdminAchatsValidationView.fxml"));
            Parent root = loader.load();

            // Crée une nouvelle scène avec le root chargé
            Scene newScene = new Scene(root, 900, 600);

            // Applique la feuille de style CSS ici
            newScene.getStylesheets().add(getClass().getResource("/com/tp/APP1/styles/AchatsValidationStyle.css").toExternalForm());

            // Contrôleur
            AdminAchatValidationController ctrl = loader.getController();
            ctrl.setCurrentUser(currentUser);

            // Applique la nouvelle scène à la fenêtre
            Stage currentStage = (Stage) gridPane.getScene().getWindow();
            currentStage.setScene(newScene);

        } catch (Exception e) {
            handleDatabaseError(e);
        }
    }

}

