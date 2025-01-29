package com.comicland.comicland;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;


public class HelloController {
    private ComicLandSystem system = new ComicLandSystem();
    private Map<ComicLandSystem.ComicBook, Integer> cart = new HashMap<>();  // Cart to store selected items and their quantities

    @FXML
    private HBox displayContainer;

    @FXML
    private Button cartButton;

    @FXML
    private HBox cartContainer;

    @FXML
    private ToggleButton bestSellingFilter;

    @FXML
    private ToggleButton actionFilter;

    @FXML
    private ToggleButton adventureFilter;

    @FXML
    private ToggleButton fantasyFilter;

    @FXML
    private ToggleButton sciFiFilter;

    @FXML
    public void initialize() {
        system.initializeInventory();
        displayInventory();

        // Add listeners to the filter checkboxes
        bestSellingFilter.selectedProperty().addListener((observable, oldValue, newValue) -> displayInventory());
        actionFilter.selectedProperty().addListener((observable, oldValue, newValue) -> displayInventory());
        adventureFilter.selectedProperty().addListener((observable, oldValue, newValue) -> displayInventory());
        fantasyFilter.selectedProperty().addListener((observable, oldValue, newValue) -> displayInventory());
        sciFiFilter.selectedProperty().addListener((observable, oldValue, newValue) -> displayInventory());

        // Apply stylesheets and align the cart button
        displayContainer.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());
            }
            cartContainer.setAlignment(Pos.BASELINE_RIGHT);
        });
    }

    private void displayInventory() {
        displayContainer.getChildren().clear();
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(5);
        grid.setStyle("-fx-padding: 10;");
        grid.setPrefWidth(600);
        grid.setMaxWidth(Double.MAX_VALUE);

        int row = 0;
        int col = 0;

        boolean filterBestSelling = bestSellingFilter.isSelected();
        boolean filterAction = actionFilter.isSelected();
        boolean filterAdventure = adventureFilter.isSelected();
        boolean filterFantasy = fantasyFilter.isSelected();
        boolean filterSciFi = sciFiFilter.isSelected();



        for (ComicLandSystem.ComicBook comic : system.getInventory()) {
            // Filter by genre
            boolean matchesGenre = false;
            if (filterAction && comic.getGenre().equals("Action")) matchesGenre = true;
            else if (filterAdventure && comic.getGenre().equals("Sci-Fi")) matchesGenre = true;
            else if (filterFantasy && comic.getGenre().equals("Superhero")) matchesGenre = true;
            else if (filterSciFi && comic.getGenre().equals("Violence")) matchesGenre = true;
            else if (!filterAction && !filterAdventure && !filterFantasy && !filterSciFi) matchesGenre = true;

            // Skip the comic if it does not match the selected genre filter
            if (filterBestSelling && !comic.isBestSelling() || !matchesGenre) continue;

            // Create the comic display card
            VBox card = new VBox();
            card.setSpacing(5);
            card.setStyle(" -fx-padding: 10; -fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 2, 2);");

            Image comicImage = new Image(getClass().getResource(comic.getImagePath()).toExternalForm());
            ImageView imageView = new ImageView(comicImage);
            imageView.setFitWidth(120);
            imageView.setPreserveRatio(true);

            Label titleLabel = new Label(comic.getTitle());
            Label priceLabel = new Label("Price: ₱" + comic.getPrice());
            Label stockLabel = new Label("Stock: " + comic.getStock());

            titleLabel.setStyle("-fx-font-weight: Bold; -fx-font-size: 14px;  -fx-font-family: Poppins;");
            priceLabel.setStyle("-fx-font-family: Poppins; -fx-font-size: 12px; -fx-text-fill: #777;");
            stockLabel.setStyle("-fx-font-family: Poppins; -fx-font-size: 12px; -fx-text-fill: #777;");

            Button selectButton = new Button("Add to Cart");
            selectButton.setStyle("-fx-cursor:hand; -fx-padding: 10; -fx-background-color: #0ea5e9; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 10px; -fx-background-radius: 5px; -fx-font-weight: Bold; -fx-font-family: Poppins;");
            selectButton.setOnAction(event -> openQuantityWindow(comic));
            selectButton.setMaxWidth(Double.MAX_VALUE);

            card.getChildren().addAll(imageView, titleLabel, priceLabel, stockLabel, selectButton);
            grid.add(card, col, row);

            col++;
            if (col > 9) {
                col = 0;
                row++;
            }
        }
        displayContainer.getChildren().add(grid);
    }

    private void updateCartButtonText() {
        int cartSize = cart.size();  // Number of items in the cart
        cartButton.setText("My Cart (" + cartSize + ")");
    }

    private void openQuantityWindow(ComicLandSystem.ComicBook comic) {
        Stage quantityStage = new Stage();
        quantityStage.setTitle("Select Quantity");

        // Label for Quantity
        Label quantityLabel = new Label("Quantity: 1");

        // Minus and plus buttons
        Button minusButton = new Button("-");
        Button plusButton = new Button("+");

        int[] quantity = {1};

        // Event handler for minus button
        minusButton.setOnAction(event -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                quantityLabel.setText("Quantity: " + quantity[0]);
            }
        });

        // Event handler for plus button
        plusButton.setOnAction(event -> {
            quantity[0]++;
            quantityLabel.setText("Quantity: " + quantity[0]);
        });

        // HBox
        HBox quantityBox = new HBox(10, minusButton, quantityLabel, plusButton);
        quantityBox.setStyle("-fx-alignment: center;");

        // Add to cart button
        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(event -> {
            if (quantity[0] >= 1) {
                // Add comic to cart with specified quantity
                cart.put(comic, cart.getOrDefault(comic, 0) + quantity[0]);

                System.out.println("Added " + quantity[0] + " copies of " + comic.getTitle() + " to the cart.");

                updateCartButtonText();

                quantityStage.close();  // Close the quantity window
            } else {
                System.out.println("Please select a valid quantity.");
            }
        });

        // Vbox
        VBox layout = new VBox(20, quantityBox, addToCartButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 300, 150);
        quantityStage.setScene(scene);

        quantityStage.show();

        minusButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #d1d5db; -fx-text-fill: black; -fx-border: none; -fx-padding: 10 10; -fx-cursor: hand; -fx-font-family: Poppins;");
        plusButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-color: #d1d5db; -fx-text-fill: black; -fx-border: none; -fx-padding: 10 10; -fx-cursor: hand; -fx-font-family: Poppins;");
        quantityLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: Poppins;");
        addToCartButton.setStyle("-fx-font-size: 14px; -fx-background-color: #0ea5e9; -fx-font-weight: bold; -fx-text-fill: white; -fx-font-family: Poppins; -fx-border-radius: 10px; -fx-cursor: hand;");
    }

    @FXML
    private void openCartWindow() {
        Stage cartStage = new Stage();
        cartStage.setTitle("My Cart");
        cartStage.setResizable(false);
        cartStage.setWidth(550);
        cartStage.setHeight(800);

        VBox cartLayout = new VBox(10);
        cartLayout.setStyle("-fx-padding: 20; -fx-background-color: #f4f4f4;");

        if (cart.isEmpty()) {
            Label emptyLabel = new Label("Cart is empty");
            emptyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #777; -fx-font-family: Poppins;");
            cartLayout.getChildren().add(emptyLabel);
        } else {
            // Create a label for total price
            double[] totalPrice = {0.0};
            Label totalLabel = new Label("Total Price: ₱" + totalPrice[0]);
            totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333; -fx-font-family: Poppins;");

            // Total Price
            HBox totalBox = new HBox();
            totalBox.setStyle("-fx-alignment: center-right;");
            totalBox.getChildren().add(totalLabel);
            cartLayout.getChildren().add(totalBox);  // Add total label to the top of the VBox

            // Grid
            GridPane gridPane = new GridPane();
            gridPane.setVgap(10);
            gridPane.setHgap(10);
            gridPane.setStyle("-fx-background-color: transparent;");
            gridPane.setAlignment(Pos.CENTER);

            int row = 0;
            int column = 0;

            for (Map.Entry<ComicLandSystem.ComicBook, Integer> entry : cart.entrySet()) {
                ComicLandSystem.ComicBook comic = entry.getKey();
                int quantity = entry.getValue();


                HBox itemCard = new HBox(15);
                itemCard.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-alignment: center-left; "
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 2, 2); -fx-font-family: Poppins;");

                // Comic Image
                Image comicImage = new Image(getClass().getResource(comic.getImagePath()).toExternalForm());
                ImageView imageView = new ImageView(comicImage);
                imageView.setFitHeight(150);
                imageView.setFitWidth(120);
                imageView.setPreserveRatio(true);

                // VBox for labels
                VBox labelBox = new VBox(5);
                labelBox.setStyle("-fx-alignment: top-left;");
                Label titleLabel = new Label(comic.getTitle());
                titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333; -fx-font-family: Poppins;");

                Label quantityLabel = new Label("Quantity: x" + quantity);
                quantityLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #777;");

                Label priceLabel = new Label("Price: ₱" + comic.getPrice() + " each");
                priceLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #777;");

                labelBox.getChildren().addAll(titleLabel, quantityLabel, priceLabel);

                // Remove Button
                VBox buttonBox = new VBox();
                buttonBox.setStyle("-fx-alignment: bottom-left; -fx-padding: 5 0 0 0;");
                Button removeButton = new Button("Remove");
                removeButton.setStyle("-fx-background-color: #ef4444;  -fx-font-family: Poppins; -fx-border-radius: 10px; -fx-text-fill: white; -fx-padding: 5 10;");
                removeButton.setStyle("-fx-background-color: #ef4444;  -fx-cursor: hand; -fx-font-family: Poppins; -fx-border-radius: 10px; -fx-text-fill: white; -fx-padding: 5 10;");
                removeButton.setOnAction(event -> {
                    cart.remove(comic);
                    updateCartButtonText();
                    openCartWindow();
                    cartStage.close();
                });
                buttonBox.getChildren().add(removeButton);

                VBox titleBox = new VBox(10);
                titleBox.getChildren().addAll(labelBox, buttonBox);

                itemCard.getChildren().addAll(imageView, titleBox);


                gridPane.add(itemCard, column, row);
                totalPrice[0] += comic.getPrice() * quantity;

                column++;
                if (column > 1) {
                    column = 0;
                    row++;
                }
            }

            cartLayout.getChildren().add(gridPane);

            // Update total price label
            totalLabel.setText("Total Price: ₱" + String.format("%.2f", totalPrice[0]));

            // Create buttons for "Clear Cart" and "Proceed"
            Button clearCartButton = new Button("Clear Cart");
            clearCartButton.setStyle("-fx-cursor: hand; -fx-background-color: #d1d5db; -fx-text-fill: black; -fx-font-size:14px; -fx-font-weight: Bold; -fx-padding: 10 20; -fx-font-family: Poppins; -fx-border-radius: 10px;");
            clearCartButton.setOnAction(event -> {
                cart.clear();  // Clear the entire cart
                updateCartButtonText();
                cartStage.close();  // Close the cart window
            });

            Button proceedButton = new Button("Proceed");
            proceedButton.setStyle("-fx-cursor: hand; -fx-background-color: #0ea5e9; -fx-text-fill: white; -fx-font-size:14px; -fx-font-weight: Bold; -fx-padding: 10 20; -fx-font-family: Poppins; -fx-border-radius: 10px;");
            proceedButton.setOnAction(event -> {
                generateReceipt(totalPrice[0], cartStage);  // Generate receipt and close cart
            });

            // Add buttons in an HBox aligned to the bottom-right corner
            HBox buttonBox = new HBox(10, clearCartButton, proceedButton);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);  // Align buttons to the right
            cartLayout.getChildren().add(buttonBox);  // Add the buttons to the VBox


            Scene cartScene = new Scene(cartLayout);
            cartStage.setScene(cartScene);
            cartStage.show();
        }
    }

    private void generateReceipt(double totalPrice, Stage cartStage) {
        Stage receiptStage = new Stage();
        receiptStage.setTitle("Receipt");

        // Prevent the window from being resized
        receiptStage.setResizable(false);

        VBox receiptLayout = new VBox(10);
        receiptLayout.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: #f4f4f4;");

        Label receiptTitleLabel = new Label("ComicLand");
        receiptTitleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #333; -fx-font-family: Poppins;");
        receiptLayout.getChildren().add(receiptTitleLabel);

        // Loop through the cart to display items (without reducing stock)
        for (Map.Entry<ComicLandSystem.ComicBook, Integer> entry : cart.entrySet()) {
            ComicLandSystem.ComicBook comic = entry.getKey();
            int quantity = entry.getValue();

            // Display item details in the receipt
            Label itemLabel = new Label(comic.getTitle() + " - " + quantity + " pcs @ ₱" + comic.getPrice() + " each");
            itemLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
            receiptLayout.getChildren().add(itemLabel);
        }

        // Total Price
        Label totalLabel = new Label("Total Price: ₱" + totalPrice);
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");
        receiptLayout.getChildren().add(totalLabel);

        // Load and display barcode image
        Image barcodeImage = new Image(getClass().getResource("/images/barcode.png").toExternalForm());
        ImageView barcodeImageView = new ImageView(barcodeImage);
        barcodeImageView.setFitWidth(200);
        barcodeImageView.setPreserveRatio(true);
        receiptLayout.getChildren().add(barcodeImageView);

        // Close Button - Exits the application
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-cursor: hand; -fx-background-color: #0ea5e9; -fx-text-fill: white; -fx-padding: 10 20; -fx-font-weight: Bold; -fx-font-family: Poppins;");
        closeButton.setOnAction(event -> Platform.exit());  // Closes the entire application

        // Button layout
        HBox buttonLayout = new HBox(10, closeButton);
        buttonLayout.setAlignment(Pos.CENTER);
        receiptLayout.getChildren().add(buttonLayout);

        // Scene and Stage setup
        Scene scene = new Scene(receiptLayout, 500, 800);
        receiptStage.setScene(scene);
        receiptStage.show();

        // Clear the cart after showing the receipt and closing the cart stage
        cart.clear();
        updateCartButtonText();
        cartStage.close();  // Close the cart window after generating the receipt
    }




    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (Map.Entry<ComicLandSystem.ComicBook, Integer> entry : cart.entrySet()) {
            ComicLandSystem.ComicBook comic = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += comic.getPrice() * quantity;
        }

        return totalPrice;
    }
}
