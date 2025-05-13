package com.projecte;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static com.projecte.BuildDatabase.*;
import com.sun.jdi.IntegerValue;
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
    private Label levelInfoLabel, pokemonsCaughtInfoLabel, pointsInfoLabel, battlesPlayedInfoLabel, maxConsecutiveWinsInfoLabel;

    @FXML
    private Button managementButton, battleHistoryButton, newBattleButton, exitButton;

    @FXML
    private ImageView imgBackground;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Path imagePath = null;


        try {
            URL imageURL = getClass().getResource("/assets/image/background.jpg");
            Image image = new Image(imageURL.toExternalForm());
            imgBackground.setImage(image);

            setGameStats();

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

    private void setGameStats() throws IOException {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        String query1 = """
            SELECT 
            total_experience, 
            battles_played, 
            max_win_streak 
            FROM GameStats
        """;

        ArrayList<HashMap<String, Object>> result1 = db.query(query1);

        String query2 = """
            SELECT 
            COUNT(*) as total_caught 
            FROM PlayerPokemon 
            WHERE unlocked = 1
        """;

        ArrayList<HashMap<String, Object>> result2 = db.query(query2);

      
        // Check if the result is empty
        if (result1.isEmpty() || result2.isEmpty()) {
            System.err.println("No data returned from the querys.");
            return; // Exit the method if no data is found
        }


        for (HashMap<String, Object> el : result1) {
            Integer totalExperience = (Integer) el.get("total_experience");
            Integer level = totalExperience / 1000;
            Integer points = totalExperience;
            Integer battlesPlayed = (Integer) el.get("battles_played");
            Integer maxConsecutiveWins = (Integer) el.get("max_win_streak");

            levelInfoLabel.setText(String.valueOf(level));
            pointsInfoLabel.setText(String.valueOf(points));
            battlesPlayedInfoLabel.setText(String.valueOf(battlesPlayed));
            maxConsecutiveWinsInfoLabel.setText(String.valueOf(maxConsecutiveWins));
        }

        for (HashMap<String, Object> el : result2) { 
            int totalCaught = (int) el.get("total_caught");
            pokemonsCaughtInfoLabel.setText(String.valueOf(totalCaught));
        }


        db.close();
    }



}
