package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ControllerAttackResult {

    @FXML
    private Label roundLabel, equipLabel, pokemonLabel, statsLabel, hpLabel, estaminaLabel;
    
    @FXML
    private Button buttonContinue;

    @FXML
    private void initialize() {

    }
    
    @FXML
    private void handleContinueButtonAction() {

    }

    private void setRoundLabel(String round) {
        roundLabel.setText(round);
    }   

    private void setEquipLabel(String equip) {
        equipLabel.setText(equip);
    }

    private void setPokemonLabel(String pokemon) {
        pokemonLabel.setText(pokemon);
    }

    private void setStatsLabel(String stats) {
        statsLabel.setText(stats);
    }

    private void setHpLabel(String hp) {
        hpLabel.setText(hp);
    }

    private void setEstaminaLabel(String estamina) {
        estaminaLabel.setText(estamina);
    }

}
