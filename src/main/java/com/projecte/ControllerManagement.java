package com.projecte;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ControllerManagement implements Initializable {

    
    @FXML
    private Button buttonNext = new Button();

    @FXML
    private ChoiceBox<String> pokemons;

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

    @FXML
    public void initialize(URL url, ResourceBundle rb) {

        // Insertar Pokémon con id 1, 4 y 7 en la tabla PlayerPokemon si no existen
        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");

        int[] pokemonIds = {1, 4, 7};
        for (int pokemonId : pokemonIds) {
            ArrayList<HashMap<String, Object>> existingPokemon = db.query(
            String.format("SELECT * FROM PlayerPokemon WHERE pokemon_id = %d;", pokemonId)
            );

            if (existingPokemon.isEmpty()) {
            db.update(
                String.format(
                "INSERT INTO PlayerPokemon (pokemon_id, nickname, max_hp, attack, stamina, unlocked) " +
                "VALUES (%d, '', 100, 50, 50, 1);",
                pokemonId
                )
            );
            }
        }
        // Llenar el ChoiceBox con los nombres de los Pokémon al inicializar
        loadAllPokemons();
        
        // Configurar el evento de selección en el ChoiceBox
        pokemons.setOnAction(event -> {
            String selectedPokemon = pokemons.getValue();
            if (selectedPokemon != null) {
                // Extraer el ID del Pokémon seleccionado (asumiendo que el formato es "#ID Nombre")
                String[] parts = selectedPokemon.split(" ");
                if (parts.length > 0) {
                    try {
                        int selectedId = Integer.parseInt(parts[0].substring(1)); // Quitar el "#" y convertir a entero
                        loadPokemon(selectedId); // Cargar el Pokémon seleccionado
                    } catch (NumberFormatException e) {
                        System.err.println("Error al parsear el ID del Pokémon seleccionado: " + e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Carga todos los Pokémon en el ChoiceBox.
     */
    private void loadAllPokemons() {
        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");

        // Consulta para obtener todos los Pokémon
        ArrayList<HashMap<String, Object>> allPokemons = db.query(
        "SELECT p.id, p.name, p.type, p.icon_path, pp.unlocked " +
        "FROM Pokemon p " +
        "LEFT JOIN PlayerPokemon pp ON p.id = pp.pokemon_id " +
        "ORDER BY p.id ASC;"
    );

        pokemons.getItems().clear(); // Limpiar el ChoiceBox antes de llenarlo

        for (HashMap<String, Object> pokemon : allPokemons) {
            int id = (int) pokemon.get("id");
            String name = (String) pokemon.get("name");
            String type = (String) pokemon.get("type");
            String iconPath = (String) pokemon.get("icon_path");
            boolean isUnlocked = pokemon.get("unlocked") != null && (int) pokemon.get("unlocked") == 1;

            // Agregar al ChoiceBox en el formato "#ID Nombre"
            pokemons.getItems().add("#" + id + " " + name);

            // Aplicar efecto de sombra si el Pokémon no está desbloqueado
            if (isUnlocked) {
                // Si el Pokémon está desbloqueado, mostrar sus datos
                this.labelType.setText(type);
                this.labelName.setText("#" + id + " " + name);
                this.labelStamina.setText("50"); // Ejemplo de valor
                this.labelHp.setText("100");    // Ejemplo de valor
                this.labelNickname.setText(name);
            } else {
                // Si el Pokémon no está desbloqueado, mostrar "?"
                this.labelType.setText("?");
                this.labelName.setText("?");
                this.labelStamina.setText("?");
                this.labelHp.setText("?");
                this.labelNickname.setText("?");
            }

            try {
                String imagePath = "assets/poke-icons/" + iconPath;
                System.out.println("Intentando cargar la imagen desde: " + imagePath);
                java.io.InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
                if (resourceStream == null) {
                    throw new NullPointerException("Recurso no encontrado: " + imagePath);
                }
    
                Image image = new Image(resourceStream);
                imgPokemon.setImage(image);
    
                // Aplicar efecto Shadow si no está desbloqueado
                if (!isUnlocked) {
                    imgPokemon.setEffect(new javafx.scene.effect.Shadow(15, javafx.scene.paint.Color.BLACK));
                } else {
                    imgPokemon.setEffect(null); // Quitar el efecto si está desbloqueado
                }
    
                System.out.println("Imagen cargada correctamente: " + imagePath);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.err.println("Error cargando el recurso: " + iconPath);
                e.printStackTrace();
            }
        }

        // Seleccionar el primer Pokémon por defecto si hay elementos
        if (!pokemons.getItems().isEmpty()) {
            pokemons.setValue(pokemons.getItems().get(0)); // Seleccionar el primero
            String[] parts = pokemons.getValue().split(" ");
            if (parts.length > 0) {
                try {
                    int firstId = Integer.parseInt(parts[0].substring(1)); // Quitar el "#" y convertir a entero
                    loadPokemon(firstId); // Cargar el primer Pokémon
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear el ID del primer Pokémon: " + e.getMessage());
                }
            }
        }
    }

    //Funció per carregar pokemons segons el seu número
    @FXML
    public void loadPokemon(int number){

        this.number = number;

        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");

        ArrayList<HashMap<String, Object>> llistaPokemons = db.query(String.format("SELECT * FROM Pokemon WHERE id = '%d';", this.number));
        if (llistaPokemons.size() == 1) {
            HashMap<String, Object> pokemon = llistaPokemons.get(0);
            String id = String.valueOf(pokemon.get("id"));
            String name = (String) pokemon.get("name");
            String type = (String) pokemon.get("type");
            this.labelType.setText(type);
            this.labelName.setText("#" + id + " " + name);
            String imagePokemon = (String) pokemon.get("icon_path");
            
            try {
                String imagePath = "assets/poke-icons/" + imagePokemon;
                System.out.println("Intentando cargar la imagen desde: " + imagePath);
                java.io.InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
                if (resourceStream == null) {
                    throw new NullPointerException("Recurso no encontrado: " + imagePath);
                }
            
                Image image = new Image(resourceStream);
                imgPokemon.setImage(image);
                System.out.println("Imagen cargada correctamente: " + imagePath);
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
