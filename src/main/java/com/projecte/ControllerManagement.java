package com.projecte;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ControllerManagement implements Initializable {

    
    @FXML
    private Button buttonNext = new Button();

    @FXML
    private Button buttonPrevious = new Button();

    @FXML
    private Label labelLevel = new Label();
    
    @FXML
    private Label labelName = new Label();
    
    @FXML
    private Label labelType = new Label();
    
    @FXML
    private Label labelStamina = new Label();
    
    @FXML
    private Label labelHp = new Label();
    
    @FXML
    private Label labelNickname = new Label();

    @FXML
    private ImageView imgBackArrow; //Imagen para retroceder de vista
    
    @FXML
    private ImageView imgPokemon;

    private int number, previousNumber = -1, nextNumber = -1; //Definición de variables para control de pokemons

    @Override
    public void initialize(URL url, ResourceBundle rb) { //Función para inicializar con la imagen de retroceder
        /*
        Path imagePath = null;
        try {
            URL imageURL = getClass().getResource("/assets/images602/arrow-back.png");
            Image image = new Image(imageURL.toExternalForm());
            imgBackArrow.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }
        */
    }

    //Funció per carregar pokemons segons el seu número
    @FXML
    public void loadPokemon(int number){

        this.number = number;

        AppData db = AppData.getInstance();

        ArrayList<HashMap<String, Object>> llistaPokemons = db.query(String.format("SELECT * FROM Pokemon WHERE id = '%d';", this.number));
        if (llistaPokemons.size() == 1) {
            HashMap<String, Object> pokemon = llistaPokemons.get(0);
            String id = (String) pokemon.get("id");
            String name = (String) pokemon.get("name");
            String type = (String) pokemon.get("type");
            this.labelType.setText(type);
            this.labelName.setText("#" + id + " " + name);

            String imagePokemon = (String) pokemon.get("icon-path");
            
            try {
                String imagePath = "assets/poke-icons/" + imagePokemon;
                java.io.InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
                if (resourceStream == null) {
                    throw new NullPointerException("Recurso no encontrado: " + imagePath);
                }
            
                Image image = new Image(resourceStream);
                imgPokemon.setImage(image);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.err.println("Error cargando el recurso: " + imagePokemon);
                e.printStackTrace();
            }
        }

                
        ArrayList<HashMap<String, Object>> llistaPrevious = db.query(String.format("SELECT * FROM Pokemon WHERE id < '%d' ORDER BY id DESC LIMIT 1;", this.number));
    
        if (llistaPrevious.size() == 1) {
            HashMap<String, Object> pokemon_pr = llistaPrevious.get(0);
            this.previousNumber = (int) pokemon_pr.get("id"); 
            buttonPrevious.setDisable(false);          
        } else {
            this.previousNumber = -1;
            this.buttonPrevious.setDisable(true);
        }
        
        ArrayList<HashMap<String, Object>> llistaNextList = db.query(String.format("SELECT * FROM Pokemon WHERE id > '%d' ORDER BY id ASC LIMIT 1;", this.number));

        if (llistaNextList.size() == 1) {
            HashMap<String, Object> pokemon_nxt = llistaNextList.get(0);
            this.nextNumber = (int) pokemon_nxt.get("id");    
            this.buttonNext.setDisable(false);        
        } else {
            this.nextNumber = -1;
            this.buttonNext.setDisable(true);
        }
    }

    //Button edit
    @FXML
    public void editPokemon(ActionEvent event) {
        ControllerPokeSettings ctrl = (ControllerPokeSettings) UtilsViews.getController("ViewPokeSettings");
        UtilsViews.setViewAnimating("ViewPokeSettings");
    }

    //Arrow back
    @FXML
    public void goBack(MouseEvent event) {
        UtilsViews.setViewAnimating("ViewMenu");
    }

    //Button previous
    @FXML
    public void previous(ActionEvent event) {

        if (this.previousNumber != -1) {
            loadPokemon(previousNumber);
        }
    }

    //Button next
    @FXML
    public void next(ActionEvent event) {
        
        if (this.nextNumber != -1) {
            loadPokemon(nextNumber);
        }
    }

}
