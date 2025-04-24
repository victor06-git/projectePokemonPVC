package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

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
        Image img = new Image(getClass().getResourceAsStream("/assets/images0602/marca.png"));
        imgMarca.setImage(img);
    }

    public void loadList() {
        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");

        // Llistar les 10 últimes ciutats utilitzant ArrayList i HashMap
        ArrayList<HashMap<String, Object>> llistaPokemons = db.query("SELECT * FROM pokemons;");
        try {
            setList(llistaPokemons);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void setList(ArrayList<HashMap<String, Object>> llistaPokemons) throws IOException {

        URL resource = this.getClass().getResource("/assets/pokemonView.fxml");

        // Netejar el contingut existent
        list.getChildren().clear();

        // Iterar sobre els elements del JSONArray 'jsonInfo' (ja carregat a initialize)
        for (int i = 0; i < llistaPokemons.size(); i++) {
            // Obtenir l'objecte JSON individual (animal)
            HashMap<String, Object> pokemon = llistaPokemons.get(i);

            // Extreure la informació necessària del JSON
            int number = (int) pokemon.get("number");
            String name = (String) pokemon.get("name");
            String type = (String) pokemon.get("type");
            String imagePath = (String) pokemon.get("image");

            // Carregar el template
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerPokemonView itemController = loader.getController();
            /* 
            // Assignar els valors als controls del template
            itemController.setNumber(number);
            itemController.setTitle(name);
            itemController.setSubtitle(type);
            itemController.setImatge(imagePath);
            */
            // Afegir el nou element a 'yPane'
            list.getChildren().add(itemTemplate);
        }
    }

    
}