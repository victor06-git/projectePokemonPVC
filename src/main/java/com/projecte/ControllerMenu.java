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
    private Label levelInfoLabel, pokemonsCaughtInfoLabel, pointsInfoLabel, battlesPlayedInfoLabel, maxConsecutiveWinsInfoLabel;

    private int level, pokemonsCaught, total_experience, battlesPlayed, maxWinStreak;

    @FXML
    private Button managementButton, battleHistoryButton, newBattleButton, exitButton;

    @FXML
    private ImageView imgBackground;

    public static final String STATUS_BATTLE_STARTED = "battle_started";
    public static final String STATUS_BATTLE_PREP = "battle_prep";
    public static final String STATUS_BATTLE_ENDED = "battle_ended";
    private int round = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Path imagePath = null;
        loadGameStats();

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
        ControllerHistory ctrl = (ControllerHistory) UtilsViews.getController("ViewHistory");
        ctrl.loadHistory();
        UtilsViews.setViewAnimating("ViewHistory");
    }
    
    public void toViewNewBattle(ActionEvent event) {
        ControllerBattleOptions ctrl = (ControllerBattleOptions) UtilsViews.getController("ViewBattleOptions");
        ctrl.setBattleStatus(STATUS_BATTLE_PREP, round=1);
        UtilsViews.setViewAnimating("ViewBattleOptions");
    }

    public void toViewStart(ActionEvent event) {
        ControllerStart ctrl = (ControllerStart) UtilsViews.getController("ViewStart");
        UtilsViews.setViewAnimating("ViewStart");
        ctrl.setVisibleContinueButton(true);
    }

    public void setlevelInfoLabel(String level) {
        levelInfoLabel.setText(level);
    }

    public void setPokemonsCaughtInfoLabel(String pokemonsCaught) {
        pokemonsCaughtInfoLabel.setText(pokemonsCaught);
    }

    public void setPointsInfoLabel(String points) {
        pointsInfoLabel.setText(points);
    }

    public void setBattlesPlayedInfoLabel(String battlesPlayed) {
        battlesPlayedInfoLabel.setText(battlesPlayed);
    }
    
    public void setMaxConsecutiveWinsInfoLabel(String maxConsecutiveWins) {
        maxConsecutiveWinsInfoLabel.setText(maxConsecutiveWins);
    }

    public void loadGameStats() {
            AppData db = AppData.getInstance();
            db.connect(selected_path);

            ArrayList<HashMap<String, Object>> stats = db.query(
                "SELECT total_experience, battles_played, max_win_streak FROM GameStats WHERE id = 1"
            );
            ArrayList<HashMap<String, Object>> caught = db.query(
                "SELECT COUNT(*) as total_caught FROM PlayerPokemon WHERE unlocked = 1"
            );

            if (!stats.isEmpty()) {
                HashMap<String, Object> el = stats.get(0);
                total_experience = ((Number) el.get("total_experience")).intValue();
                battlesPlayed = ((Number) el.get("battles_played")).intValue();
                maxWinStreak = ((Number) el.get("max_win_streak")).intValue();
                level = total_experience / 1000;
            }
            if (!caught.isEmpty()) {
                pokemonsCaught = ((Number) caught.get(0).get("total_caught")).intValue();
            }
            setBattlesPlayedInfoLabel(String.valueOf(battlesPlayed));
            setlevelInfoLabel(String.valueOf(level));
            setPokemonsCaughtInfoLabel(String.valueOf(pokemonsCaught));
            setPointsInfoLabel(String.valueOf(total_experience));
            setMaxConsecutiveWinsInfoLabel(String.valueOf(maxWinStreak));


            db.close();
        }



}
