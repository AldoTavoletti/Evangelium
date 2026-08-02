package it.unicam.cs.mpgc.rpg129852.controller;

import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DiscipleHeaderController {

    @FXML private Label discipleNameLabel;
    @FXML private ImageView discipleImageView;
    @FXML private Label faithLabel;
    @FXML private Label hopeLabel;
    @FXML private Label loveLabel;

    public void initData(DiscipleData data, Image image) {
        discipleNameLabel.setText(data.getName() + " | " + data.getJob());
        faithLabel.setText("Fede: " + data.getVirtues().faith());
        hopeLabel.setText("Speranza: " + data.getVirtues().hope());
        loveLabel.setText("Carità: " + data.getVirtues().love());

        if (image != null) {
            discipleImageView.setImage(image);
        }
    }
}