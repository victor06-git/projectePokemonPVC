package com.projecte;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class ControllerStart extends BuildDatabase implements Initializable {

    @FXML
    private ImageView pokemonImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Pantalla de inicio cargada");

        // Solo cargar imagen, nada de cargar vistas aquí
        String imagePath = "data/pokemonstart.png";
        File file = new File(imagePath);
        
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            pokemonImage.setImage(image);
        } else {
            System.out.println("Imagen no encontrada");
        }
    }

    @FXML
    private void startGame() throws IOException {
        System.out.println("Botón Open Game pulsado");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona la base de datos");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("SQLite Files", "*.sqlite")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String rutaDBAbsoluta = selectedFile.getAbsolutePath();
            // Obtener la ruta del directorio base (puedes cambiar esto según tu estructura)
            String directorioBase = new File("").getAbsolutePath(); // Directorio de trabajo actual
            String rutaRelativa = getRelativePath(directorioBase, rutaDBAbsoluta);
            BuildDatabase.main(rutaDBAbsoluta);
            UtilsViews.setViewAnimating("ViewMenu");
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se ha seleccionado ningún archivo.");
            alert.setContentText("Por favor, selecciona un archivo de base de datos.");
            alert.showAndWait();
        }
    }
    // Método para obtener la ruta relativa
    private String getRelativePath(String basePath, String absolutePath) {
        File base = new File(basePath);
        File file = new File(absolutePath);
        return base.toURI().relativize(file.toURI()).getPath();
    }
}
