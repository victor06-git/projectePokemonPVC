package com.projecte;


import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static com.projecte.BuildDatabase.selected_path;
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
    private  Label levelInfoLabel, pokemonsCaughtInfoLabel, pointsInfoLabel, battlesPlayedInfoLabel, maxConsecutiveWinsInfoLabel;

    @FXML
    private Button managementButton, battleHistoryButton, newBattleButton, exitButton;

    @FXML
    private ImageView imgBackground;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Path imagePath = null;

        setGameStats();

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
        UtilsViews.setViewAnimating("ViewBattleHistory");
    }
    
    public void toViewNewBattle(ActionEvent event) {
        UtilsViews.setViewAnimating("ViewBattleOptions");
    }

    public void toViewStart(ActionEvent event) {
        UtilsViews.setViewAnimating("ViewStart");
    }

    public void setGameStats()  {
        BuildDatabase data = new BuildDatabase();
        HashMap<String, String> gameStats = data.getGameStats();

        System.out.println(gameStats);
        if (levelInfoLabel != null) {
            levelInfoLabel.setText(gameStats.get("level"));
        }
        if (pokemonsCaughtInfoLabel != null) {
            pokemonsCaughtInfoLabel.setText(gameStats.get("pokemons_caught"));
        }
        if (pointsInfoLabel != null) {
            pointsInfoLabel.setText(gameStats.get("points"));
        }
        if (battlesPlayedInfoLabel != null) {
            battlesPlayedInfoLabel.setText(gameStats.get("battles_played"));
        }
        if (maxConsecutiveWinsInfoLabel != null) {
            maxConsecutiveWinsInfoLabel.setText(gameStats.get("max_consecutive_wins"));
        }
    }



}
