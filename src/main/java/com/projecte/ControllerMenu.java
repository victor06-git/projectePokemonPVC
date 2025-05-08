package com.projecte;


import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ControllerMenu {

    @FXML
    private Label levelInfoLabel, pokemonsCaughtInfoLabel, pointsInfoLabel, battlesWonInfoLabel;

    @FXML
    private Button managementButton, battleHistoryButton, newBattleButton, exitButton;

    public void toViewManagement() {
        UtilsViews.setViewAnimating("ViewManagement");
    }
    
    public void toViewBattleHistory() {
        UtilsViews.setViewAnimating("ViewBattleHistory");
    }
    
    public void toViewNewBattle() {
        UtilsViews.setViewAnimating("ViewNewBattle");
    } 
    
    public void toViewStart() {
        UtilsViews.setViewAnimating("ViewStart");
    }

}
