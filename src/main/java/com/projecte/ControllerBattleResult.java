package com.projecte;


import com.utils.UtilsViews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;

public class ControllerBattleResult {
    
    @FXML
    private Label labelRewards, labelWinner;
    
    @FXML
    private Button buttonContinue;

    @FXML
    private ImageView imgPokemon1, imgPokemon2, item1;

    @FXML
    private ProgressBar nivellBar;
    
    @FXML
    private void toContinue(ActionEvent event) {
        // Acción al hacer clic en el botón "Recoger recompensas"
        UtilsViews.setView("ViewMenu");
    }

    
}
