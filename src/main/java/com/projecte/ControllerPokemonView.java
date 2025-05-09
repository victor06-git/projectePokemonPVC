package com.projecte;

import java.io.File;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class ControllerPokemonView {
    @FXML private int vidaMaxima, staminaMaxima;
    @FXML private ProgressBar hpBar, staminaBar;
    @FXML private Label labelName, labelNickname, labelType, labelLevel;
    @FXML private ImageView imgPokemon;

    public void initialize() {
        // Cargar fuentes primero
        loadFonts();
        
        // Aplicar hoja de estilos
        applyStylesheet();
    }

    private void loadFonts() {
        Font pixelFont = Font.loadFont(getClass().getResource("/assets/fonts/PressStart2P-Regular.ttf").toExternalForm(), 10);

        // Cargar fuentes desde archivos locales
        try {
            if (pixelFont != null) {
                labelName.setFont(pixelFont);
                labelNickname.setFont(pixelFont);
                labelType.setFont(pixelFont);
                labelLevel.setFont(pixelFont);
            } else {
                System.err.println("Error: No se pudo cargar la fuente pixel-font.ttf");
            }
        } catch (Exception e) {
            System.err.println("Error cargando fuentes:");
            e.printStackTrace();
        }
    }

    private void applyStylesheet() {
        try {
            Scene scene = hpBar.getScene();
            if (scene != null) {
                String cssPath = getClass().getResource("/assets/pokemonView.css").toExternalForm();
                scene.getStylesheets().clear(); // Limpiar estilos previos
                scene.getStylesheets().add(cssPath);
                
                // Aplicar clases específicas a los componentes
                labelName.getStyleClass().add("label-name");
                labelNickname.getStyleClass().add("label-nickname");
                labelType.getStyleClass().add("label-type");
                labelLevel.getStyleClass().add("label-level");
            }
        } catch (Exception e) {
            System.err.println("Error cargando hoja de estilos:");
            e.printStackTrace();
        }
    }

    public void setVidaMaxima(int value) {
        this.vidaMaxima = value;
    }
    
    public void setStaminaMaxima(int value) {
        this.staminaMaxima = value; 
    }
    
    public void setHP(int value) {
        this.hpBar.setProgress((double) value / vidaMaxima); 
    }

    public void setStamina(int value) {
        this.staminaBar.setProgress((double) value / staminaMaxima); 
    }
    
    public void setName(String value) {
        this.labelName.setText(value); 
    }
    
    public void setNickName(String value) {
        this.labelNickname.setText(value); 
    }
   
    public void setType(String value) {
        this.labelType.setText(value); 
    }
   
    public void setLevel(int value) {
        this.labelLevel.setText(String.valueOf(value)); 
    }

    public void setImatge(String imagePokemon) {
        try {
            String imagePath = "/assets/pokemons/" + imagePokemon;
            if (!imagePath.startsWith("/")) {
                imagePath = "/" + imagePath;
            }
        
            java.net.URL resource = getClass().getResource(imagePath);
            if (resource == null) {
                throw new NullPointerException("Recurso no encontrado: " + imagePath);
            }
        
            String fullPath = resource.toExternalForm();
            Image image = new Image(fullPath);
            imgPokemon.setImage(image);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error cargando el recurso: " + imagePokemon);
            e.printStackTrace();
        }
    }

    @FXML
    public void toViewSettings(MouseEvent event) {
        ControllerPokeSettings ctrl = (ControllerPokeSettings) UtilsViews.getController("ViewPokeSettings");
        UtilsViews.setViewAnimating("ViewPokeSettings");
    }
}
