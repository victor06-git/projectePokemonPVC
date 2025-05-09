package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ControllerManagement {

    
    @FXML
    private VBox list = new VBox();

    @FXML
    private ImageView imgMarca;

    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void loadList() {
        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");

        // Llistar les 10 últimes ciutats utilitzant ArrayList i HashMap
        ArrayList<HashMap<String, Object>> llistaPokemons = db.query("SELECT * FROM Pokemon;");
        try {
            setList(llistaPokemons);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void setList(ArrayList<HashMap<String, Object>> llistaPokemons) throws IOException {
        // Ruta al archivo FXML del diseño de cada Pokémon
        URL resource = this.getClass().getResource("/assets/pokemonView.fxml");
    
        // Limpiar el contenido existente del VBox
        list.getChildren().clear();
    
        // Iterar sobre la lista de Pokémon
        for (HashMap<String, Object> pokemon : llistaPokemons) {
            // Extraer la información necesaria del HashMap
            int number = (int) pokemon.get("number"); //Cambiar
            String name = (String) pokemon.get("name"); //Cambiar
            String type = (String) pokemon.get("type"); //Cambiar
            String imagePath = (String) pokemon.get("image"); //Cambiar
    
            // Cargar el diseño de pokemonView.fxml
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
    
            // Obtener el controlador de la vista cargada
            ControllerPokemonView itemController = loader.getController();
    
            // Asignar los valores a los controles del template
            itemController.setVidaMaxima(100);
            itemController.setStaminaMaxima(100);
            itemController.setHP(100);   
            itemController.setName(name);
            itemController.setType(type);
            itemController.setImatge(imagePath);
            itemController.setNickName(name);
            itemController.setLevel(1);
            itemController.setStamina(100);
    
            // Agregar el nuevo elemento al VBox
            list.getChildren().add(itemTemplate);
        }
    }

    
}