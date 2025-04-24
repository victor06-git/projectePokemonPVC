package com.projecte;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerPokeSettings {

    @FXML
    private Label labelName, nicknamLabel, numberAttack, numberDeffense, numberBottleCap;

    @FXML
    private ImageView attackImage, deffenseImage, bottlecapImage;

    // Método que se llama al inicializar el controlador
    @FXML
    public void initialize() {
        // Establecer las imágenes en los ImageView
        setImatge(attackImage, "data/XAttack.png"); // Ruta de la imagen de ataque
        setImatge(deffenseImage, "data/XDeffense.png"); // Ruta de la imagen de defensa
        setImatge(bottlecapImage, "data/BottleCap.png"); // Ruta de la imagen de tapa de botella
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
}