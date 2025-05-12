package com.projecte;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;

public class ControllerHistory extends BuildDatabase implements Initializable {
    @FXML
    private VBox historyList;

    @FXML
    private ImageView imgBackArrow;

    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {

        // Insertar Pokémon con id 1, 4 y 7 en la tabla PlayerPokemon si no existen
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        Path imagePath = null;
        try {
            URL imageURL = getClass().getResource("/assets/image/arrow-back.gif");
            Image image = new Image(imageURL.toExternalForm());
            imgBackArrow.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }
    }

    public void toViewStart() {
        UtilsViews.setViewAnimating("ViewStart");
    }


}
