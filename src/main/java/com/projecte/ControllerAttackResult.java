package com.projecte;

import java.net.URL;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerAttackResult implements Initializable{

    @FXML
    private Label roundLabel, equipLabel, pokemonLabel, statsLabel, hpLabel, estaminaLabel, hpPlayer, estaminaPlayer;
    
    @FXML
    private ImageView imgPokeAttackResult;

    @FXML
    private Button buttonContinue;

    private int round = -1;

    private Boolean finalBattle = false;

    private String winner;

    private int battleId;

    private boolean run;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar la vista
        URL imageURL = getClass().getResource("/assets/image/pokemonattackresult.png");
        Image image = new Image(imageURL.toExternalForm());
        imgPokeAttackResult.setImage(image);
        
    }

    public void setRound(int round) {
        this.round = round;
        setRoundLabel(this.round);
    }

    public void setHpPlayer(String hp) {
        hpPlayer.setText(hp);
    }

    public void setBattleId(int battleId) {
        this.battleId = battleId;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    /**
     * Método para establecer si es la batalla final.
     * 
     * @param finalBattle true si es la batalla final, false en caso contrario.
     */
    public void setFinalBattle(Boolean finalBattle) {
        this.finalBattle = finalBattle;
        if (finalBattle) {
            buttonContinue.setText("Finalizar");
            //System.out.println("Valor run " + run);
            UtilsViews.setView("ViewBattleResult");
            ControllerBattleResult ctrl = (ControllerBattleResult) UtilsViews.getController("ViewBattleResult");
            ctrl.setRound(this.round);
            ctrl.setRun(this.run);
            if (!run) {
                ctrl.unlockTwoRandomPokemons();
                ctrl.unlockRandomItem();
                ctrl.updateGameStatsWithRandomXP();
            } else {
                int level = ctrl.getCurrentLevelFromDB();
                ctrl.setLevelProgressBar(level);
            }
            
        } else {
            buttonContinue.setText("Continuar");
        }
    }

    public void setWinner(String winner) {
        this.winner = winner;
        ControllerBattleResult ctrl = (ControllerBattleResult) UtilsViews.getController("ViewBattleResult");
        ctrl.setWinner(winner);
        if (winner.equals("Player")) {
            hpPlayer.setText("Ganador: " + winner);
        } else {
            hpPlayer.setText("Perdedor: " + winner);
        }
        ctrl.setBattleId(battleId);
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
        
        if (ctrlAttack.getPlayerHpBar() == 0.0 || ctrlAttack.getPlayerStaminaBar() == 0.0 || finalBattle) {
            this.round += 1;
            ctrl.setRound(this.round);
            if (finalBattle) {
                UtilsViews.setView("ViewBattleResult");

            } else {
            UtilsViews.setViewAnimating("ViewBattleOptions");
        }

        } else {  
            this.round += 1;
            ctrl.setRound(this.round);
            UtilsViews.setViewAnimating("ViewBattleAttack");
        }
    }
    


}
