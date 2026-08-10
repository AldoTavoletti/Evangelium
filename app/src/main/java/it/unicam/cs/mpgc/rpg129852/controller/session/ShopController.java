package it.unicam.cs.mpgc.rpg129852.controller.session;

import it.unicam.cs.mpgc.rpg129852.dto.book.Book;
import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRoute;
import it.unicam.cs.mpgc.rpg129852.navigation.ViewRouter;
import it.unicam.cs.mpgc.rpg129852.service.disciple.DiscipleProfileService;
import it.unicam.cs.mpgc.rpg129852.service.shop.ShopService;
import it.unicam.cs.mpgc.rpg129852.ui.common.AlertHelper;
import it.unicam.cs.mpgc.rpg129852.ui.shop.BookRowComponent;
import it.unicam.cs.mpgc.rpg129852.util.ImageUtils;
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
    private DiscipleHeaderController discipleHeaderController;
    @FXML
    private VBox booksContainer;
    @FXML
    private Label currentVirtuesLabel;
    @FXML
    private DiscipleProfileService discipleProfile;

    public ShopController(DiscipleProfileService discipleProfile, ShopService shopService, ViewRouter sceneManager) {
        this.shopService = shopService;
        this.discipleProfile = discipleProfile;
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        cartImageCache = ImageUtils.loadImage(CART_ICON_PATH);
        restartUI();
    }

    @FXML
    void onBackToMenuButtonClicked() {
        sceneManager.switchScene(ViewRoute.PLAYER_MENU);
    }

    private void restartUI() {
        initializeDiscipleHeader();
        refreshBooksList();
    }

    private void initializeDiscipleHeader() {
        DiscipleData data = discipleProfile.getCurrentData();
        String gifPath = discipleProfile.getGifPath();
        Image gif = ImageUtils.loadImage(gifPath);

        discipleHeaderController.initData(data, gif);
    }


    private void refreshBooksList() {
        booksContainer.getChildren().clear();

        List<Book> availableBooks = shopService.getAvailableBooks();

        if (availableBooks.isEmpty()) {
            displayEmptyShopMessage();
        } else {
            populateShopWithBooks(availableBooks);
        }
    }

    private void displayEmptyShopMessage() {
        booksContainer.getChildren().add(new Label(EMPTY_BOOKS_MSG));
    }

    private void populateShopWithBooks(List<Book> availableBooks) {
        List<BookRowComponent> bookRows = availableBooks.stream()
                .map(this::createBookRowComponent)
                .toList();

        booksContainer.getChildren().addAll(bookRows);
    }

    private BookRowComponent createBookRowComponent(Book book) {
        return new BookRowComponent(
                book,
                shopService.canAfford(book),
                cartImageCache,
                this::confirmAndBuyBook
        );
    }

    private void confirmAndBuyBook(Book book) {
        boolean isConfirmed = AlertHelper.askConfirmation(BUY_TITLE, BUY_HEADER + book.displayName(), BUY_CONTENT);

        if (isConfirmed) {
            attemptPurchase(book);
        }
    }

    private void attemptPurchase(Book book) {
        try {
            shopService.buy(book);
            restartUI();
        } catch (IllegalStateException e) {
            AlertHelper.showError(ERR_BUY_MSG, e.getMessage());
        }
    }
}