package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.Book;
import it.unicam.cs.mpgc.rpg129852.model.Game;
import it.unicam.cs.mpgc.rpg129852.model.Inventory;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.BookCatalog;
import it.unicam.cs.mpgc.rpg129852.service.ShopService;
import it.unicam.cs.mpgc.rpg129852.ui.AlertHelper;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class ShopController {


    private static final String CART_ICON_PATH = "/images/shopping-cart.png";
    private static final double ICON_SIZE = 24.0;
    private static final String BUY_BTN_CLASS = "buy-btn";

    private static final String BUY_TITLE = "Acquista libro";
    private static final String BUY_HEADER = "Stai per acquistare il libro: ";
    private static final String BUY_CONTENT = "Vuoi continuare?";

    private static final String EMPTY_BOOKS_MSG = "Tutti i libri sono stati acquistati.";
    private static final String ERR_BUY_MSG = "Impossibile comprare il libro";

    private Image cartImageCache;

    private final ShopService shopService;

    @FXML
    private VBox booksContainer;

    @FXML
    private Label currentVirtuesLabel;

    private final ViewRouter sceneManager;
    private final BookCatalog bookCatalog;

    public ShopController(BookCatalog bookCatalog, ShopService shopService, ViewRouter sceneManager) {
        this.sceneManager = sceneManager;
        this.bookCatalog = bookCatalog;
        this.shopService = shopService;
    }

    @FXML
    public void initialize() {
        // for performance reasons
        cartImageCache = ImageUtils.loadImage(CART_ICON_PATH);
        currentVirtuesLabel.setText(shopService.getAvailableVirtues().toString());
        refreshBooksList();
    }

    private void refreshBooksList() {
        booksContainer.getChildren().clear();

        // get the books
        List<Book> alreadyBoughtBooks = shopService.getBoughtBooks();

        List<Book> availableBooks = bookCatalog.getNotBoughtBooks(alreadyBoughtBooks);

        if (availableBooks.isEmpty()) {
            booksContainer.getChildren().add(new Label(EMPTY_BOOKS_MSG));
            return;
        }

        for (Book book : availableBooks) {
            booksContainer.getChildren().add(createBookRow(book));
        }
    }

    private HBox createBookRow(Book book) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("book-row");

        Label nameLabel = new Label(book.displayName());
        nameLabel.getStyleClass().add("book-title");

        Label priceLabel = new Label("Costo: " + book.price());
        priceLabel.getStyleClass().add("book-price");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button buyButton = createBuyButton(book);

        row.getChildren().addAll(nameLabel, priceLabel, spacer, buyButton);
        return row;
    }

    private Button createBuyButton(Book book) {
        Button buyButton = new Button();
        buyButton.getStyleClass().add(BUY_BTN_CLASS);

        if (cartImageCache != null && !cartImageCache.isError()) {
            ImageView cartIcon = new ImageView(cartImageCache);
            cartIcon.setFitWidth(ICON_SIZE);
            cartIcon.setFitHeight(ICON_SIZE);
            buyButton.setGraphic(cartIcon);
        } else {
            buyButton.setText("Buy");
        }

        if (book.price().compareTo(shopService.getAvailableVirtues()) > 0)
            buyButton.setDisable(true);

        buyButton.setOnAction(event -> confirmAndBuyBook(book));

        return buyButton;
    }

    private void confirmAndBuyBook(Book book) {
        if (AlertHelper.askConfirmation(BUY_TITLE, BUY_HEADER + book.displayName(), BUY_CONTENT)) {
            executeBuy(book);
        }
    }

    private void executeBuy(Book book) {
        try {
            shopService.buy(book);
            //todo: add book to inventory and take out spent virtues
            refreshBooksList();
        } catch (Exception e) {
            AlertHelper.showError(ERR_BUY_MSG, e.getMessage());
        }
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.PLAYER_MENU);
    }


}
