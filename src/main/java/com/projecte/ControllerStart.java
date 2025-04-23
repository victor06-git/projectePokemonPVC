package com.projecte;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

public class ControllerStart implements Initializable {


        @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Pantalla de inicio cargada");
    }

    @FXML
    private void empezarJuego() {
        System.out.println("Boton Open Game pulsado");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Inicio del Juego");
        alert.setHeaderText(null);
        alert.setContentText("Aqui comenzaría el juego o se cambiaria de escena.");
        alert.showAndWait();
    }
}