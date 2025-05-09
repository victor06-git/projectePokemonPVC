package com.projecte;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ControllerAttackResult {

    @FXML
    private Label roundLabel, equipLabel, pokemonLabel, statsLabel, hpLabel, estaminaLabel, hpPlayer, estaminaPlayer;
    
    @FXML
    private Button buttonContinue;

    private int round = -1;

    @FXML
    private void initialize() {
        
    }

    public void setRound(int round) {
        this.round = round;
        setRoundLabel(this.round);
    }

    public void setHpPlayer(String hp) {
        hpPlayer.setText(hp);
    }
    
    public void setEstaminaPlayer(String estamina) {
        estaminaPlayer.setText(estamina);
    }

    public void setRoundLabel(int round) {
        roundLabel.setText("Round " + round);
    }   

    public void setEquipLabel(String equip) {
        equipLabel.setText(equip);
    }

    public void setPokemonLabel(String pokemon) {
        pokemonLabel.setText(pokemon);
    }

    public void setStatsLabel(String stats) {
        statsLabel.setText(stats);
    }

    public void setHpLabel(String hp) {
        hpLabel.setText(hp);
    }

    public void setEstaminaLabel(String estamina) {
        estaminaLabel.setText(estamina);
    }

    /**
     * Método para continuar la batalla.
     * 
     * @param event El evento de acción del botón continuar.
     */
    @FXML
    public void toContinue(javafx.event.ActionEvent event) {
        
        ControllerBattleOptions ctrl = (ControllerBattleOptions) UtilsViews.getController("ViewBattleOptions");
        ControllerBattleAttack ctrlAttack = (ControllerBattleAttack) UtilsViews.getController("ViewBattleAttack");
        
        if (ctrlAttack.getPlayerHpBar() == 0.0 || ctrlAttack.getPlayerStaminaBar() == 0.0) {
            UtilsViews.setViewAnimating("ViewBattleOptions");
        } else {  
            UtilsViews.setViewAnimating("ViewBattleAttack");

        }
    }
    
}
