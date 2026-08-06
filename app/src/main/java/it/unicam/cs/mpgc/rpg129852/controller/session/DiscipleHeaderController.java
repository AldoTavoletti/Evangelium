package it.unicam.cs.mpgc.rpg129852.controller.session;

import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DiscipleHeaderController {

    @FXML
    private Label discipleLabel;

    @FXML
    private ImageView discipleImageView;

    @FXML
    private Label faithLabel;

    @FXML
    private Label hopeLabel;

    @FXML
    private Label loveLabel;

    public void initData(DiscipleData data, Image image) {

        Virtues virtues = data.getVirtues();
        fillVirtuesLabels(virtues);

        fillDiscipleLabel(data.getName(), data.getJob());

        setDiscipleImage(image);

    }

    private void fillVirtuesLabels(Virtues virtues) {
        faithLabel.setText("Fede: " + virtues.faith());
        hopeLabel.setText("Speranza: " + virtues.hope());
        loveLabel.setText("Carità: " + virtues.love());
    }

    private void fillDiscipleLabel(String discipleName, String discipleJob) {
        discipleLabel.setText(discipleName + " | " + discipleJob);
    }

    private void setDiscipleImage(Image image) {
        if (image != null) {
            discipleImageView.setImage(image);
        }
    }
}