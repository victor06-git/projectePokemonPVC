package com.projecte;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

public class ControllerMenu {

    @FXML
    private Button btnTeam, btnHistory, btnNbattle, btnExit = new Button();
    
    @FXML
    private Label LabelLevel, LabelPoints, LabelBwons, LabelMaxcw, LabelPokcaught;

    @FXML
    private Label titt, Level, Points, Battlewons, MaxCw, Pokecaught;

    @FXML
    private ImageView imgentrenador; 

    // Método que se llama al inicializar el controlador
    @FXML
    public void initialize() {
        // Establecer las imágenes en los ImageView
        setImatge(imgentrenador, "data/entrenador.png"); // Ruta de la imagen de ataque
    }
    // Mostra imatge pokemon
    private void setImatge(ImageView imageView, String imagePath) {
        try {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
        } catch (NullPointerException e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }
    }
    // Método para establecer el texto de los labels
    public void setLabelText(String level, String points, String battleWons, String maxCw, String pokecaught) {
        LabelLevel.setText(level);
        LabelPoints.setText(points);
        LabelBwons.setText(battleWons);
        LabelMaxcw.setText(maxCw);
        LabelPokcaught.setText(pokecaught);
    }

}