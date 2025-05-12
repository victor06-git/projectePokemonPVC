package com.projecte;


import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerMenu implements Initializable {

    @FXML
    private Label levelInfoLabel, pokemonsCaughtInfoLabel, pointsInfoLabel, battlesWonInfoLabel;

    @FXML
    private Button managementButton, battleHistoryButton, newBattleButton, exitButton;

    @FXML
    private ImageView imgBackground;

    public void initialize(URL url, ResourceBundle rb) {
        Path imagePath = null;
        try {
            URL imageURL = getClass().getResource("/assets/image/background.jpg");
            Image image = new Image(imageURL.toExternalForm());
            imgBackground.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }
    }

    public void toViewManagement(ActionEvent event) {
        UtilsViews.setViewAnimating("ViewManagement");
    }

    public void toViewBattleHistory(ActionEvent event) {
        UtilsViews.setViewAnimating("ViewHistory");
    }
    
    public void toViewNewBattle(ActionEvent event) {
        UtilsViews.setViewAnimating("ViewBattleOptions");
    }

    public void toViewStart(ActionEvent event) {
        UtilsViews.setViewAnimating("ViewStart");
    }

}
