package com.projecte;

import com.utils.UtilsViews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ControllerRewards {
    
    @FXML
    private Label labelRewards;
    
    @FXML
    private Button buttonContinue;
    
    public void initialize() {
        // Inicialización de la vista
        labelRewards.setText("¡Has ganado!");
        buttonContinue.setText("Recoger recompensas");
    }
    
    @FXML
    private void toContinue(ActionEvent event) {
        // Acción al hacer clic en el botón "Recoger recompensas"
        UtilsViews.setView("ViewMenu");
    }
}
