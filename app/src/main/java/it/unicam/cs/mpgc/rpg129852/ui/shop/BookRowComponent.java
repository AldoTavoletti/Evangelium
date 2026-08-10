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

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A custom UI component representing a single row in the shop interface.
 * It displays a book's details and provides a button to purchase it,
 * which dynamically enables or disables based on the player's affordability.
 */
public class BookRowComponent extends HBox {

    private static final double SPACING = 15.0;
    private static final double ICON_SIZE = 24.0;

    private static final String BUY_BTN_CLASS = "buy-btn";
    private static final String FALLBACK_BUY_TEXT = "Buy";
    private static final String PRICE_PREFIX = "Costo: ";

    private static final String TITLE_CLASS = "book-title";
    private static final String PRICE_CLASS = "book-price";
    private static final String ROW_CLASS = "book-row";

    /**
     * Constructs a new book row component.
     *
     * @param book        the book data to display
     * @param canAfford   true if the player has enough currency to buy the book, false otherwise
     * @param cartIcon    the icon to display on the buy button; if null or broken, a text fallback is used
     * @param onBuyAction the callback triggered when the buy button is clicked
     * @throws NullPointerException if the book or the onBuyAction callback is null
     */
    public BookRowComponent(Book book, boolean canAfford, Image cartIcon, Consumer<Book> onBuyAction) {
        super(SPACING);

        Objects.requireNonNull(book, "The book must not be null.");
        Objects.requireNonNull(onBuyAction, "The onBuyAction callback must not be null.");

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

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button buyButton = buildBuyButton(book, canAfford, cartIcon, onBuyAction);

        this.getChildren().addAll(nameLabel, priceLabel, spacer, buyButton);
    }

    private Button buildBuyButton(Book book, boolean canAfford, Image cartIcon, Consumer<Book> onBuyAction) {
        Button buyButton = new Button();
        buyButton.getStyleClass().add(BUY_BTN_CLASS);

        applyIconOrFallbackText(buyButton, cartIcon);

        buyButton.setDisable(!canAfford);
        buyButton.setOnAction(event -> onBuyAction.accept(book));

        return buyButton;
    }

    private void applyIconOrFallbackText(Button button, Image iconImage) {
        if (iconImage != null && !iconImage.isError()) {
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitWidth(ICON_SIZE);
            iconView.setFitHeight(ICON_SIZE);
            button.setGraphic(iconView);
        } else {
            button.setText(FALLBACK_BUY_TEXT);
        }
    }
}