package it.unicam.cs.mpgc.rpg129852.ui.shop;

import it.unicam.cs.mpgc.rpg129852.dto.book.Book;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.function.Consumer;

public class BookRowComponent extends HBox {

    private static final double SPACING = 15.0;
    private static final double ICON_SIZE = 24.0;
    private static final String BUY_BTN_CLASS = "buy-btn";
    private static final String FALLBACK_BUY_TEXT = "Buy";
    private static final String PRICE_PREFIX = "Costo: ";
    private static final String TITLE_CLASS = "book-title";
    private static final String PRICE_CLASS = "book-price";
    private static final String ROW_CLASS = "book-row";

    public BookRowComponent(Book book, boolean canAfford, Image cartIcon, Consumer<Book> onBuyAction) {
        super(SPACING);

        setupContainer();
        buildContent(book, canAfford, cartIcon, onBuyAction);
    }

    private void setupContainer() {
        this.setAlignment(Pos.CENTER_LEFT);
        this.getStyleClass().add(ROW_CLASS);
    }

    private void buildContent(Book book, boolean canAfford, Image cartIcon, Consumer<Book> onBuyAction) {
        Label nameLabel = new Label(book.displayName());
        nameLabel.getStyleClass().add(TITLE_CLASS);

        Label priceLabel = new Label(PRICE_PREFIX + book.price());
        priceLabel.getStyleClass().add(PRICE_CLASS);

        // Spacer pushes the buy button to the far right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button buyButton = buildBuyButton(book, canAfford, cartIcon, onBuyAction);

        this.getChildren().addAll(nameLabel, priceLabel, spacer, buyButton);
    }

    private Button buildBuyButton(Book book, boolean canAfford, Image cartIcon, Consumer<Book> onBuyAction) {
        Button buyButton = new Button();
        buyButton.getStyleClass().add(BUY_BTN_CLASS);

        if (cartIcon != null && !cartIcon.isError()) {
            ImageView cartImageView = new ImageView(cartIcon);
            cartImageView.setFitWidth(ICON_SIZE);
            cartImageView.setFitHeight(ICON_SIZE);
            buyButton.setGraphic(cartImageView);
        } else {
            buyButton.setText(FALLBACK_BUY_TEXT);
        }

        buyButton.setDisable(!canAfford);

        // Pass the book back to the caller when clicked
        buyButton.setOnAction(event -> onBuyAction.accept(book));

        return buyButton;
    }
}