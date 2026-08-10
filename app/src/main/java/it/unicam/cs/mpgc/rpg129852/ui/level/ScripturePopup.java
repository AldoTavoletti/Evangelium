package it.unicam.cs.mpgc.rpg129852.ui.level;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;

import java.net.URL;

public class ScripturePopup extends Popup {

    private static final double POPUP_WIDTH = 350.0;
    private static final double CONTENT_SPACING = 10.0;
    private static final double Y_AXIS_OFFSET = 5.0;

    private static final String DEFAULT_CSS_PATH = "/css/Gameplay.css";
    private static final String TITLE_STYLE_CLASS = "popup-source";
    private static final String CONTENT_STYLE_CLASS = "popup-text";
    private static final String PANE_STYLE_CLASS = "popup-pane";

    private final Label popupTitle;
    private final Label popupContent;

    public ScripturePopup() {
        this.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_LEFT);

        this.popupTitle = buildConfiguredLabel(TITLE_STYLE_CLASS);
        this.popupContent = buildConfiguredLabel(CONTENT_STYLE_CLASS);

        VBox pane = new VBox(popupTitle, popupContent);
        pane.setSpacing(CONTENT_SPACING);
        pane.getStyleClass().add(PANE_STYLE_CLASS);

        applyStylesheet(pane);

        this.getContent().add(pane);
    }

    public void showAboveNode(Node anchorNode, String content, String title) {
        popupTitle.setText(title);
        popupContent.setText(content);

        Bounds bounds = anchorNode.localToScreen(anchorNode.getBoundsInLocal());

        if (bounds != null) {
            this.show(anchorNode, bounds.getMinX(), bounds.getMinY() - Y_AXIS_OFFSET);
        }
    }

    private Label buildConfiguredLabel(String styleClass) {
        Label label = new Label();
        label.setWrapText(true);
        label.setPrefWidth(POPUP_WIDTH);
        label.getStyleClass().add(styleClass);
        return label;
    }

    private void applyStylesheet(VBox pane) {
        URL cssResource = getClass().getResource(DEFAULT_CSS_PATH);

        if (cssResource != null) {
            pane.getStylesheets().add(cssResource.toExternalForm());
        } else {
            System.err.println("UI Warning: Could not find stylesheet at " + DEFAULT_CSS_PATH);
        }
    }
}