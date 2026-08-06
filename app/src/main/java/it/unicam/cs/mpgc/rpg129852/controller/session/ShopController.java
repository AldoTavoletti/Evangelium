package it.unicam.cs.mpgc.rpg129852.controller.session;

import it.unicam.cs.mpgc.rpg129852.dto.book.Book;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.shop.ShopService;
import it.unicam.cs.mpgc.rpg129852.ui.common.AlertHelper;
import it.unicam.cs.mpgc.rpg129852.ui.shop.BookRowComponent;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

import java.util.List;

public class ShopController {

    private static final String CART_ICON_PATH = "/images/shopping-cart.png";
    private static final String BUY_TITLE = "Acquista libro";
    private static final String BUY_HEADER = "Stai per acquistare il libro: ";
    private static final String BUY_CONTENT = "Vuoi continuare?";
    private static final String EMPTY_BOOKS_MSG = "Tutti i libri sono stati acquistati.";
    private static final String ERR_BUY_MSG = "Impossibile comprare il libro";

    private final ShopService shopService;
    private final ViewRouter sceneManager;
    private Image cartImageCache;

    @FXML
    private VBox booksContainer;

    @FXML
    private Label currentVirtuesLabel;

    public ShopController(ShopService shopService, ViewRouter sceneManager) {
        this.shopService = shopService;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        cartImageCache = ImageUtils.loadImage(CART_ICON_PATH);
        updateVirtuesDisplay();
        refreshBooksList();
    }

    private void updateVirtuesDisplay() {
        currentVirtuesLabel.setText(shopService.getAvailableVirtues().toString());
    }

    private void refreshBooksList() {
        booksContainer.getChildren().clear();

        List<Book> availableBooks = shopService.getAvailableBooks();

        if (availableBooks.isEmpty()) {
            booksContainer.getChildren().add(new Label(EMPTY_BOOKS_MSG));
            return;
        }

        List<BookRowComponent> bookRows = generateBookRows(availableBooks);

        booksContainer.getChildren().addAll(bookRows);
    }

    private List<BookRowComponent> generateBookRows(List<Book> availableBooks) {
        return availableBooks.stream()
                .map(book -> new BookRowComponent(
                        book,
                        shopService.canAfford(book),
                        cartImageCache,
                        this::confirmAndBuyBook
                ))
                .toList();
    }

    private void confirmAndBuyBook(Book book) {
        boolean confirmed = AlertHelper.askConfirmation(BUY_TITLE, BUY_HEADER + book.displayName(), BUY_CONTENT);

        if (confirmed) {
            executeBuy(book);
        }
    }

    private void executeBuy(Book book) {
        try {
            shopService.buy(book);
            updateVirtuesDisplay();
            refreshBooksList();
        } catch (IllegalStateException e) {
            AlertHelper.showError(ERR_BUY_MSG, e.getMessage());
        }
    }

    @FXML
    void onReturnToMenuAction(ActionEvent event) {
        sceneManager.switchScene(ViewRoute.PLAYER_MENU);
    }
}