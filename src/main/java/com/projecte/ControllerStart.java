package com.projecte;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

public class ControllerStart implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Pantalla de inicio cargada");
        
        //Afegim les vistes
        try {
            UtilsViews.addView(getClass(), "StartView", "/assets/startView.fxml");
            UtilsViews.addView(getClass(), "ViewMenu", "/assets/viewMenu.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void empezarJuego() {
        System.out.println("Botón Open Game pulsado");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona la base de datos");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("SQLite Files", "*.sqlite")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {

            //Connexió a la DB
            String rutaDB = selectedFile.getAbsolutePath();
            System.out.println("Archivo seleccionado: " + rutaDB);
            AppData.getInstance().connect(rutaDB);
            
            // Cambiar la vista menu
            UtilsViews.setViewAnimating("ViewMenu");
        } else {
            // Mostrar un missatge si no es troba
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se ha seleccionado ningún archivo.");
            alert.setContentText("Por favor, selecciona un archivo de base de datos.");
            alert.showAndWait();
        }
    }
}
