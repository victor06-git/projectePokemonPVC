package com.projecte;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

public class ControllerStart extends BuildDatabase implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView pokemonImage;

    @FXML

    private Button continueButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //System.out.println("Pantalla de inicio cargada");

        // Solo cargar imagen, nada de cargar vistas aquí
        String imagePath = "data/pokemonstart.png";
        File file = new File(imagePath);
        
       // pokemonImage.fitWidthProperty().bind(rootPane.widthProperty());
       // pokemonImage.fitHeightProperty().bind(rootPane.heightProperty());
        
        if (file.exists()) {
            Image image = new Image(file.toURI().toString());
            pokemonImage.setImage(image);
        } else {
            System.out.println("Imagen no encontrada");
        }
    }

    @FXML
    private void openGame() throws IOException {
        System.out.println("Botón Open Game pulsado");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona la base de datos");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("SQLite Files", "*.sqlite")
        );
        fileChooser.setInitialDirectory(new File(Path.of("data").toAbsolutePath().toString()));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            String rutaDBAbsoluta = selectedFile.getAbsolutePath();
            // Obtener la ruta del directorio base (puedes cambiar esto según tu estructura)
            String directorioBase = new File("").getAbsolutePath(); // Directorio de trabajo actual
            String rutaRelativa = getRelativePath(directorioBase, rutaDBAbsoluta);
            BuildDatabase.main(rutaDBAbsoluta);
            UtilsViews.setViewAnimating("ViewMenu");
            ControllerMenu ctrl = (ControllerMenu) UtilsViews.getController("ViewMenu");
            ctrl.loadGameStats();
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

    @FXML
    private void exitGame() {
        System.exit(0);
    }

    @FXML
    private void newGame() {
        // Mostrar diálogo para pedir el nombre de la partida
        TextInputDialog dialog = new TextInputDialog("Nueva Partida");
        dialog.setTitle("Nueva Partida");
        dialog.setHeaderText("Introduce un nombre para tu nueva partida");
        dialog.setContentText("Nombre:");

        // Mostrar diálogo y procesar la respuesta
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            // Limpiar el nombre para que sea válido como nombre de archivo
            String cleanName = name.replaceAll("[^a-zA-Z0-9_]", "_");
            if (cleanName.isEmpty()) {
                cleanName = "nueva_partida";
            }

            // Configurar FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar nueva partida");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("SQLite Files", "*.sqlite")
            );
            
            // Establecer directorio data
            File dataDir = new File(Path.of("data").toAbsolutePath().toString());
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            fileChooser.setInitialDirectory(dataDir);
            
            // Generar nombre único inicial
            String baseName = cleanName;
            String fileName = baseName + ".sqlite";
            File file = new File(dataDir, fileName);
            int counter = 1;
            
            while (file.exists()) {
                fileName = baseName + "_" + counter + ".sqlite";
                file = new File(dataDir, fileName);
                counter++;
            }
            
            fileChooser.setInitialFileName(fileName);
            
            // Guardar automáticamente sin mostrar el diálogo
            File selectedFile = file;
            // Asegurarse de que tiene la extensión correcta
            if (!selectedFile.getName().toLowerCase().endsWith(".sqlite")) {
                selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + ".sqlite");
            }
                
                BuildDatabase.main(selectedFile.getAbsolutePath());
                BuildDatabase.insertBaseStats();
                UtilsViews.setViewAnimating("ViewMenu");
                ControllerMenu ctrl = (ControllerMenu) UtilsViews.getController("ViewMenu");
                ctrl.loadGameStats();
            }
        );
    }

    @FXML
    private void continueGame() {
        UtilsViews.setViewAnimating("ViewMenu");
        ControllerMenu ctrl = (ControllerMenu) UtilsViews.getController("ViewMenu");
        ctrl.loadGameStats();
    }

    @FXML 
    public void setVisibleContinueButton(Boolean visible) {
        continueButton.setVisible(visible);
          
    }
}
