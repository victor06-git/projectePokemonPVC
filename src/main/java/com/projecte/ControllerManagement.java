package com.projecte;

import java.io.InputStream;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ControllerManagement implements Initializable {

    
    @FXML
    private Button buttonNext;

    @FXML
    private ChoiceBox<String> pokemons;

    @FXML
    private CheckBox showUnlockedOnly;

    @FXML
    private Button buttonPrevious;

    @FXML
    private Button buttonSettings;

    @FXML
    private Label labelLevel;
    
    @FXML
    private Label labelName;
    
    @FXML
    private Label labelType;
    
    @FXML
    private Label labelStamina;
    
    @FXML
    private Label labelHp;
    
    @FXML
    private Label labelNickname;

    @FXML
    private ImageView imgBackArrow; //Imagen para retroceder de vista
    
    @FXML
    private ImageView imgPokemon;

    private int number, previousNumber = -1, nextNumber = -1; //Definición de variables para control de pokemons

    
    /**
     * Método que se ejecuta al inicializar la vista.
     * @param url URL de la vista.
     * @param rb ResourceBundle para la localización.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Path imagePath = null;
        try {
            URL imageURL = getClass().getResource("/assets/image/arrow-back.gif");
            Image image = new Image(imageURL.toExternalForm());
            imgBackArrow.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }

        showUnlockedOnly.setOnAction(event -> loadAllPokemons());

        // Llenar el ChoiceBox con los nombres de los Pokémon al inicializar
        loadAllPokemons();
        
        // Configurar el evento de selección en el ChoiceBox
        pokemons.setOnAction(event -> {
        String selectedPokemon = pokemons.getValue();
        if (selectedPokemon != null && !selectedPokemon.isEmpty()) {
            String[] parts = selectedPokemon.split(" ");
            if (parts.length > 0) {
                try {
                    int selectedId = Integer.parseInt(parts[0].substring(1));
                    loadPokemon(selectedId);
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear el ID del Pokémon seleccionado: " + e.getMessage());
                }
            }
        }
    });
    }

    @FXML
    public void setLabelNickname(String nickname) {
        this.labelNickname.setText(nickname);
    }
    @FXML
    public void setLabelName(String name) {
        this.labelName.setText(name);
    }
    @FXML
    public void setImagePokemon(Image image) {
        this.imgPokemon.setImage(image);
    }

    /**
     * Carga todos los Pokémon en el ChoiceBox.
     */
    private void loadAllPokemons() {
        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");

        // Determinar si se deben mostrar solo los desbloqueados
        boolean showOnlyUnlocked = showUnlockedOnly.isSelected();

        // Consulta para obtener los Pokémon
        String query = "SELECT p.id, p.name, p.type, p.icon_path, pp.unlocked " +
                   "FROM Pokemon p " +
                   "LEFT JOIN PlayerPokemon pp ON p.id = pp.pokemon_id ";
        if (showOnlyUnlocked) {
            query += "WHERE pp.unlocked = 1 ";
        }
            query += "ORDER BY p.id ASC;";

        // Consulta para obtener todos los Pokémon
        ArrayList<HashMap<String, Object>> allPokemons = db.query(query);     
        db.close();   

        pokemons.getItems().clear(); // Limpiar el ChoiceBox antes de llenarlo

        for (HashMap<String, Object> pokemon : allPokemons) {
            int id = (int) pokemon.get("id");
            String name = (String) pokemon.get("name");
            String iconPath = (String) pokemon.get("icon_path");
            boolean isUnlocked = pokemon.get("unlocked") != null && (int) pokemon.get("unlocked") == 1;

            // Agregar al ChoiceBox en el formato "#ID Nombre"
            pokemons.getItems().add("#" + id + " " + name);

           try {
                String imagePath = "assets/poke-icons/" + iconPath;
                java.io.InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
                if (resourceStream != null) {
                    Image image = new Image(resourceStream);
                    imgPokemon.setImage(image);
                    if (!isUnlocked) {
                        imgPokemon.setEffect(new javafx.scene.effect.Shadow(15, javafx.scene.paint.Color.BLACK));
                    } else {
                        imgPokemon.setEffect(null);
                    }
                } else {
                    imgPokemon.setImage(null);
                }
            } catch (Exception e) {
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
        } else {
            // Si no hay pokémon, limpia los labels e imagen
            labelType.setText("");
            labelName.setText("");
            labelStamina.setText("");
            labelHp.setText("");
            labelNickname.setText("");
            imgPokemon.setImage(null);
            buttonSettings.setDisable(true);
        }
    }

    //Funció per carregar pokemons segons el seu número
    @FXML
    public void loadPokemon(int number) {
    this.number = number;

    AppData db = AppData.getInstance();
    db.connect("./data/pokemons.sqlite");

    ArrayList<HashMap<String, Object>> llistaPokemons = db.query(String.format("SELECT p.id, p.name, p.type, p.icon_path, pp.stamina, pp.max_hp, pp.unlocked FROM Pokemon p LEFT JOIN PlayerPokemon pp ON p.id = pp.pokemon_id WHERE p.id = '%d';", this.number));
    if (llistaPokemons.size() == 1) {
        HashMap<String, Object> pokemon = llistaPokemons.get(0);
        String id = String.valueOf(pokemon.get("id"));
        String name = (String) pokemon.get("name");
        String type = (String) pokemon.get("type");
        String iconPath = (String) pokemon.get("icon_path");
        String stamina = String.valueOf(pokemon.get("stamina"));
        String maxHp = String.valueOf(pokemon.get("max_hp"));

        boolean isUnlocked = pokemon.get("unlocked") != null && (int) pokemon.get("unlocked") == 1;
        db.close();
        // Si el Pokémon está desbloqueado, mostrar sus datos
        if (isUnlocked) {
                // Si el Pokémon está desbloqueado, mostrar sus datos
                this.labelType.setText(type);
                this.labelName.setText("#" + id + " " + name);
                this.labelStamina.setText(stamina);
                this.labelHp.setText(maxHp);
                this.labelNickname.setText(name);
            } else {
                // Si el Pokémon no está desbloqueado, mostrar "?"
                this.labelType.setText("?");
                this.labelName.setText("?");
                this.labelStamina.setText("?");
                this.labelHp.setText("?");
                this.labelNickname.setText("?");
            }

            buttonSettings.setDisable(!isUnlocked);

        try {
            String imagePath = "assets/poke-icons/" + iconPath;
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            if (resourceStream == null) {
                throw new NullPointerException("Recurso no encontrado: " + imagePath);
            }

            Image image = new Image(resourceStream);
            imgPokemon.setImage(image);

            // Aplicar o quitar el efecto Shadow según el estado de desbloqueo
            if (!isUnlocked) {
                imgPokemon.setEffect(new javafx.scene.effect.Shadow(15, javafx.scene.paint.Color.BLACK));
            } else {
                imgPokemon.setEffect(null); // Quitar el efecto si está desbloqueado
            }

        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error cargando el recurso: " + iconPath);
            e.printStackTrace();
        }
     }

        ArrayList<HashMap<String, Object>> llistaPrevious = db.query(String.format("SELECT id FROM Pokemon WHERE id < %d ORDER BY id DESC LIMIT 1;", this.number));
    
        if (llistaPrevious.size() == 1) {
            HashMap<String, Object> pokemon_pr = llistaPrevious.get(0);
            this.previousNumber = (int) pokemon_pr.get("id");
            buttonPrevious.setDisable(false);
        } else {
            this.previousNumber = -1;
            this.buttonPrevious.setDisable(true);
        }

        ArrayList<HashMap<String, Object>> llistaNextList = db.query(String.format("SELECT id FROM Pokemon WHERE id > %d ORDER BY id ASC LIMIT 1;", this.number));

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
        ctrl.setLabelName(labelName.getText());
        ctrl.setLabelNickname(labelNickname.getText());
        ctrl.setImagePokemon(imgPokemon.getImage());
        ctrl.setCurrentPokemonId(number);
        UtilsViews.setViewAnimating("ViewPokeSettings");
    }

    //Arrow back
    @FXML
    public void goBack(MouseEvent event) {
        UtilsViews.setViewAnimating("ViewMenu");
    }


    // Button previous
    @FXML
    public void previous(ActionEvent event) {
        if (this.number != -1) {
            // Verificar si el CheckBox está activado
            boolean showOnlyUnlocked = showUnlockedOnly.isSelected();

            // Si el CheckBox está activado, buscar el Pokémon desbloqueado anterior
            if (showOnlyUnlocked) {
                ArrayList<HashMap<String, Object>> llistaPrevious = AppData.getInstance().query(
                    String.format("SELECT p.id, p.name, p.type, p.icon_path FROM Pokemon p INNER JOIN PlayerPokemon pp ON p.id = pp.pokemon_id WHERE pp.unlocked = 1 AND p.id < '%d' ORDER BY p.id DESC LIMIT 1;", this.number)
                );

                if (llistaPrevious.size() == 1) {
                    HashMap<String, Object> pokemon_pr = llistaPrevious.get(0);
                    this.number = (int) pokemon_pr.get("id"); // Actualizar el número actual
                    loadPokemon(this.number);
                    updateChoiceBox(this.number);
                } else {
                    // No hay más Pokémon desbloqueados anteriores
                    this.buttonPrevious.setDisable(true);
                }
            } else {
                // Cargar el Pokémon anterior normalmente
                loadPokemon(previousNumber + 2);
                System.out.println("previousNumber: " + (previousNumber - 1));
                System.out.println("number: " + number);
                updateChoiceBox(previousNumber - 1);
            }
        }
    }

    // Button next
    @FXML
    public void next(ActionEvent event) {
        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");
        // Verificar si el número actual es válido
        if (this.number != -1) {
            // Verificar si el CheckBox está activado
            boolean showOnlyUnlocked = showUnlockedOnly.isSelected();

            // Si el CheckBox está activado, buscar el Pokémon desbloqueado siguiente
            if (showOnlyUnlocked) {
                ArrayList<HashMap<String, Object>> llistaNext = db.query(
                    String.format("SELECT p.id, p.name, p.type, p.icon_path FROM Pokemon p INNER JOIN PlayerPokemon pp ON p.id = pp.pokemon_id WHERE pp.unlocked = 1 AND p.id > '%d' ORDER BY p.id ASC LIMIT 1;", this.number)
                );

                if (llistaNext.size() == 1) {
                    HashMap<String, Object> pokemon_nxt = llistaNext.get(0);
                    this.number = (int) pokemon_nxt.get("id"); // Actualizar el número actual
                    loadPokemon(this.number);
                    updateChoiceBox(this.number);
                } else {
                    // No hay más Pokémon desbloqueados siguientes
                    this.buttonNext.setDisable(true);
                }
            } else {
                // Cargar el Pokémon siguiente normalmente
                loadPokemon(nextNumber);
                updateChoiceBox(nextNumber - 1);
            }
        }
        db.close();
    }

    // Método auxiliar para actualizar el ChoiceBox
    private void updateChoiceBox(int pokemonId) {
        for (String item : pokemons.getItems()) {
            if (item.startsWith("#" + pokemonId + " ")) {
                pokemons.setValue(item);
                break;
                }
            }
        }
    }