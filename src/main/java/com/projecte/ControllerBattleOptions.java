package com.projecte;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ControllerBattleOptions implements Initializable {

    public static final String STATUS_BATTLE_STARTED = "battle_started";
    public static final String STATUS_BATTLE_PREP = "battle_prep";
    public static final String STATUS_BATTLE_ENDED = "battle_ended";
    private List<String> mapPaths = new ArrayList<>();
    private int currentMapIndex = 0;

    private HashMap<Integer, String> enemyPokemons = new HashMap<>();
    private List<Integer> enemyPokemonIds = new ArrayList<>();


    @FXML 
    private ImageView imgArrowBack;

    @FXML
    private Button startButton, continueButton;
    
    @FXML
    private VBox mapListPanel, teamPanel;

    @FXML
    private ChoiceBox<String> choicePokemon1, choicePokemon2, choicePokemon3;

    @FXML
    private ImageView imgPokemon1, imgPokemon2, imgPokemon3;

    @FXML
    private ImageView imgSelectedMap;

    @FXML
    private Label labelSelectedMap;

    @FXML
    private Button pokemon1, pokemon2, pokemon3;

    @FXML
    private Label pickPokemon, nextMap, previousMap;

    private int currentMapSelection = 0;
    private int idPokemon = -1;
    private Label[] maps = new Label[0];
    private int round = 1; // Variable to track the current round

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Path imagePath = null;
        try {
            URL imageURL = getClass().getResource("/assets/image/arrow-back.gif");
            Image image = new Image(imageURL.toExternalForm());
            imgArrowBack.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }

        loadMapPaths();
        if (!mapPaths.isEmpty()) {
            showCurrentMap();
        }

        // Cargar los Pokémon desbloqueados en los ChoiceBox y sus imágenes
        loadUnlockedPokemons();
    }
    
    // Getters
    public VBox getMapListPanel() {
        return mapListPanel;
    }

    public VBox getTeamPanel() {
        return teamPanel;
    }

    public ChoiceBox<String> getChoicePokemon1() {
        return choicePokemon1;
    }

    public ChoiceBox<String> getChoicePokemon2() {
        return choicePokemon2;
    }

    public ChoiceBox<String> getChoicePokemon3() {
        return choicePokemon3;
    }

    public ImageView getImgPokemon1() {
        return imgPokemon1;
    }

    public ImageView getImgPokemon2() {
        return imgPokemon2;
    }

    public ImageView getImgPokemon3() {
        return imgPokemon3;
    }

    public ImageView getImgSelectedMap() {
        return imgSelectedMap;
    }

    public Label getLabelSelectedMap() {
        return labelSelectedMap;
    }

    public javafx.scene.control.Button getStartButton() {
        return startButton;
    }

    public int getCurrentMapSelection() {
        return currentMapSelection;
    }

  
    public ImageView getImgArrowBack() {
        return imgArrowBack;
    }

    // Setters
    public void setRound(int round) {
        this.round = round;
    }

    public void setMapListPanel(VBox mapListPanel) {
        this.mapListPanel = mapListPanel;
    }

    public void setTeamPanel(VBox teamPanel) {
        this.teamPanel = teamPanel;
    }

    public void setChoicePokemon1(ChoiceBox<String> choicePokemon1) {
        this.choicePokemon1 = choicePokemon1;
    }

    public void setChoicePokemon2(ChoiceBox<String> choicePokemon2) {
        this.choicePokemon2 = choicePokemon2;
    }

    public void setChoicePokemon3(ChoiceBox<String> choicePokemon3) {
        this.choicePokemon3 = choicePokemon3;
    }

    public void setImgPokemon1(ImageView imgPokemon1) {
        this.imgPokemon1 = imgPokemon1;
    }

    public void setImgPokemon2(ImageView imgPokemon2) {
        this.imgPokemon2 = imgPokemon2;
    }

    public void setImgPokemon3(ImageView imgPokemon3) {
        this.imgPokemon3 = imgPokemon3;
    }

    public void setImgSelectedMap(ImageView imgSelectedMap) {
        this.imgSelectedMap = imgSelectedMap;
    }

    public void setLabelSelectedMap(Label labelSelectedMap) {
        this.labelSelectedMap = labelSelectedMap;
    }

    public void setStartButton(javafx.scene.control.Button startButton) {
        this.startButton = startButton;
    }

    public void setCurrentMapSelection(int currentMapSelection) {
        this.currentMapSelection = currentMapSelection;
    }

    public void setMaps(Label[] maps) {
        this.maps = maps;
    }

    public void setImgArrowBack(ImageView imgArrowBack) {
        this.imgArrowBack = imgArrowBack;
    }

    public void toViewMenu() {
        // Change to the Menu view
        UtilsViews.setViewAnimating("ViewMenu");
    }

    //Funció per canviar de vista
    @FXML
    public void toViewBattle(MouseEvent event) {
        if (idPokemon == -1) {
            // Mostrar alerta si no se ha seleccionado un Pokémon
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Selección de Pokémon");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un Pokémon activo antes de continuar a la batalla.");
            alert.showAndWait();
            return; // Salir del método si no hay Pokémon seleccionado
        }

        // Verificar si los Pokémon seleccionados son diferentes
        if (!arePokemonsDifferent()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Selección de Pokémon");
            alert.setHeaderText(null);
            alert.setContentText("Los Pokémon seleccionados deben ser diferentes.");
            alert.showAndWait();
            return; // Salir del método si no son diferentes
        }
        // Cambiar a la vista de batalla
        setBattleStatus(STATUS_BATTLE_STARTED, round);

        ControllerBattleAttack ctrl = (ControllerBattleAttack) UtilsViews.getController("ViewBattleAttack");
        ctrl.setMap(mapPaths.get(currentMapIndex));
        ctrl.setRound(round);
        
        // Generar Pokémon enemigos aleatorios solo si no hay Pokémon enemigos existentes
        if (enemyPokemonIds.isEmpty()) {
            Set<Integer> enemyPokemonSet = new HashSet<>();
            Random random = new Random();
            
            // Generar 3 IDs únicos de Pokémon (1-251)
            while (enemyPokemonSet.size() < 3) {
                enemyPokemonSet.add(random.nextInt(251) + 1);
            }
            
            // Obtener nombres desde la BD
            AppData db = AppData.getInstance();
            db.connect("./data/pokemons.sqlite");
            
            enemyPokemonIds.clear();
            enemyPokemons.clear();
            
            for (int enemyId : enemyPokemonSet) {
                String pokemonName = db.query("SELECT name FROM Pokemon WHERE id = " + enemyId)
                                      .get(0).get("name").toString();
                enemyPokemons.put(enemyId, pokemonName + " Level 1");
                enemyPokemonIds.add(enemyId);
            }
        
            // Configurar primer enemigo
            int firstEnemyId = enemyPokemonIds.get(0);
            ctrl.setEnemyPokemonImage("/assets/pokemons/normal/" + String.format("%03d", firstEnemyId) + ".gif");
            ctrl.setComputerPokemonLabel(enemyPokemons.get(firstEnemyId));
        
        
        ctrl.setEnemyPokemons(enemyPokemons, enemyPokemonIds);

        // Establecer la imagen y el nombre del Pokémon del jugador
        ctrl.setPlayerPokemonImage("/assets/pokemons/back/" + (idPokemon > 100 ? idPokemon + ".gif" : "0" + idPokemon + ".gif"));
        
        String query2 = "SELECT name FROM Pokemon WHERE id = " + idPokemon;
        String query3 = "SELECT max_hp, stamina FROM PlayerPokemon WHERE pokemon_id = " + idPokemon;
        ArrayList<HashMap<String, Object>> result2 = db.query(query2);
        ArrayList<HashMap<String, Object>> result3 = db.query(query3);
        String pokemonNamePlayer = "";
        int maxHp = (int) result3.get(0).get("max_hp");
        int stamina = (int) result3.get(0).get("stamina");
        
        if (!result2.isEmpty()) {
            pokemonNamePlayer = result2.get(0).get("name").toString();
        }
        if (!result3.isEmpty()) {
            
        }
        db.close();
        ctrl.setPlayerPokemonLabel(pokemonNamePlayer + " Level 1");
        
        
        // Configurar estamina y HP
        ctrl.setEstaminaComputer("30/30");
        ctrl.setEstaminaPlayer(stamina + "/30");
        ctrl.setHpPlayer(maxHp + "/100");
        ctrl.setHpComputer("100/100");
        ctrl.setEnemyHpBar(1.0);
        ctrl.setEnemyStaminaBar(1.0);
        ctrl.setPlayerHpBar(1.0);
        ctrl.setPlayerStaminaBar(1.0);
    }
        // Cambiar a la vista de batalla
        UtilsViews.setViewAnimating("ViewBattleAttack");
        
    }

    @FXML
    public void toContinueBattle(MouseEvent event) {
        if (idPokemon == -1) {
            // Mostrar alerta si no se ha seleccionado un Pokémon
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Selección de Pokémon");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, selecciona un Pokémon activo antes de continuar a la batalla.");
            alert.showAndWait();
            return; // Salir del método si no hay Pokémon seleccionado
        }

        ControllerBattleAttack ctrl = (ControllerBattleAttack) UtilsViews.getController("ViewBattleAttack");
        ctrl.setMap(mapPaths.get(currentMapIndex));
        ctrl.setRound(round);

        // Verificar si ya existen Pokémon enemigos
        if (!enemyPokemonIds.isEmpty()) {
            // Establecer la imagen y el nombre del primer Pokémon enemigo
            int firstEnemyId = enemyPokemonIds.get(0); // Obtener el primer Pokémon
            ctrl.setEnemyPokemonImage("/assets/pokemons/normal/" + String.format("%03d", firstEnemyId) + ".gif");
            ctrl.setComputerPokemonLabel(enemyPokemons.get(firstEnemyId));
        } else {
            // Si no hay Pokémon enemigos, mostrar alerta o manejar el caso
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No hay Pokémon enemigos disponibles.");
            alert.showAndWait();
            return;
        }

        // Establecer la imagen y el nombre del Pokémon del jugador
        ctrl.setPlayerPokemonImage("/assets/pokemons/back/" + (idPokemon > 100 ? idPokemon + ".gif" : "0" + idPokemon + ".gif"));
        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");
        String query2 = "SELECT name FROM Pokemon WHERE id = " + idPokemon;
        ArrayList<HashMap<String, Object>> result2 = db.query(query2);
        String pokemonNamePlayer = "";
        if (!result2.isEmpty()) {
            pokemonNamePlayer = result2.get(0).get("name").toString();
        }
        ctrl.setPlayerPokemonLabel(pokemonNamePlayer + " Level 1");
        db.close();

        // Configurar estamina y HP
        ctrl.setEstaminaComputer("30/30");
        ctrl.setEstaminaPlayer("30/30");
        ctrl.setHpPlayer("100/100");
        ctrl.setHpComputer("100/100");
        ctrl.setEnemyHpBar(1.0);
        ctrl.setEnemyStaminaBar(1.0);
        ctrl.setPlayerHpBar(1.0);
        ctrl.setPlayerStaminaBar(1.0);

        // Cambiar a la vista de batalla
        UtilsViews.setViewAnimating("ViewBattleAttack");
    }

    /**
     * Método para verificar si los Pokémon seleccionados son diferentes.
     * @return true si son diferentes, false si hay duplicados.
     */
    private boolean arePokemonsDifferent() {
        Set<String> selectedPokemons = new HashSet<>();
        selectedPokemons .add(choicePokemon1.getValue());
        selectedPokemons.add(choicePokemon2.getValue());
        selectedPokemons.add(choicePokemon3.getValue());
        return selectedPokemons.size() == 3; // Deben ser tres diferentes
    }


    

    private void loadMapPaths() {
        try {
            // Ruta de la carpeta de mapas
            String mapFolderPath = "assets/mapa";
            java.nio.file.Path folderPath = java.nio.file.Paths.get(getClass().getClassLoader().getResource(mapFolderPath).toURI());

            // Obtener todos los archivos de la carpeta
            java.io.File folder = folderPath.toFile();
            java.io.File[] mapFiles = folder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif"));

            if (mapFiles != null) {
                for (java.io.File mapFile : mapFiles) {
                    mapPaths.add(mapFolderPath + "/" + mapFile.getName());
                }
            } else {
                System.err.println("No se encontraron mapas en la carpeta: " + mapFolderPath);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar las rutas de los mapas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showCurrentMap() {
    if (!mapPaths.isEmpty() && currentMapIndex >= 0 && currentMapIndex < mapPaths.size()) {
        String currentMapPath = mapPaths.get(currentMapIndex);
        imgSelectedMap.setImage(new Image(getClass().getClassLoader().getResourceAsStream(currentMapPath)));
        labelSelectedMap.setText(currentMapPath.substring(currentMapPath.lastIndexOf("/") + 1, currentMapPath.lastIndexOf(".")).toUpperCase());}
    }

    @FXML
    private void showPreviousMap(MouseEvent event) {
        if (currentMapIndex > 0) {
            currentMapIndex--;
            showCurrentMap();
        } else {
            System.out.println("No hay mapas anteriores.");
        }
    }

    @FXML
    private void showNextMap(MouseEvent event) {
        if (currentMapIndex < mapPaths.size() - 1) {
            currentMapIndex++;
            showCurrentMap();
        } else {
            System.out.println("No hay mapas siguientes.");
        }
    }

    private void loadUnlockedPokemons() {
        AppData db = AppData.getInstance();
        db.connect("./data/pokemons.sqlite");

        // Consulta para obtener los Pokémon desbloqueados
        String query = """
            SELECT p.id, p.name, p.icon_path 
            FROM Pokemon p 
            INNER JOIN PlayerPokemon pp ON p.id = pp.pokemon_id 
            WHERE pp.unlocked = 1 
            ORDER BY p.id ASC;
        """;

        // Ejecutar la consulta
        ArrayList<HashMap<String, Object>> unlockedPokemons = db.query(query);

        // Limpiar los ChoiceBox antes de llenarlos
        choicePokemon1.getItems().clear();
        choicePokemon2.getItems().clear();
        choicePokemon3.getItems().clear();

        // Crear un mapa para asociar nombres de Pokémon con sus rutas de imagen
        HashMap<String, Object> pokemonImages = new HashMap<>();

        // Agregar los Pokémon desbloqueados a los ChoiceBox y almacenar las rutas de las imágenes
        for (HashMap<String, Object> pokemon : unlockedPokemons) {
            int id = Integer.parseInt(pokemon.get("id").toString());
            String name = pokemon.get("name").toString();
            String iconPath = pokemon.get("icon_path").toString();
            String formattedPokemon = "#" + id + " " + name;

            choicePokemon1.getItems().add(formattedPokemon);
            choicePokemon2.getItems().add(formattedPokemon);
            choicePokemon3.getItems().add(formattedPokemon);

            // Asociar el nombre del Pokémon con su ruta de imagen
            pokemonImages.put(formattedPokemon, "assets/poke-icons/" + iconPath);
            System.out.println("Pokemon: " + formattedPokemon + ", Icon Path: " + iconPath);
        }

        // Seleccionar el primer Pokémon por defecto si hay elementos
        if (!choicePokemon1.getItems().isEmpty()) {
            choicePokemon1.setValue(choicePokemon1.getItems().get(0));
            updatePokemonImage(choicePokemon1.getValue(), imgPokemon1, pokemonImages);
        }
        if (!choicePokemon2.getItems().isEmpty()) {
            choicePokemon2.setValue(choicePokemon2.getItems().get(0));
            updatePokemonImage(choicePokemon2.getValue(), imgPokemon2, pokemonImages);
        }
        if (!choicePokemon3.getItems().isEmpty()) {
            choicePokemon3.setValue(choicePokemon3.getItems().get(0));
            updatePokemonImage(choicePokemon3.getValue(), imgPokemon3, pokemonImages);
        }

            // Manejar cambios en los ChoiceBox para actualizar las imágenes
            choicePokemon1.setOnAction(event -> updatePokemonImage(choicePokemon1.getValue(), imgPokemon1, pokemonImages));
            choicePokemon2.setOnAction(event -> updatePokemonImage(choicePokemon2.getValue(), imgPokemon2, pokemonImages));
            choicePokemon3.setOnAction(event -> updatePokemonImage(choicePokemon3.getValue(), imgPokemon3, pokemonImages));

            db.close();
        }

       private void updatePokemonImage(String selectedPokemon, ImageView imageView, HashMap<String, Object> pokemonImages) {
            if (imageView == null) {
                System.out.println("imageView is null for " + selectedPokemon);
                return;
            }

            if (selectedPokemon != null && pokemonImages.containsKey(selectedPokemon)) {
                String iconPath1 = (String) pokemonImages.get(selectedPokemon);
                try {
                    String imagePath = iconPath1;
                    java.io.InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
                    if (resourceStream == null) {
                        throw new NullPointerException("Recurso no encontrado: " + imagePath);
                    }

                    Image image = new Image(resourceStream);
                    imageView.setImage(image);

                } catch (NullPointerException | IllegalArgumentException e) {
                    System.err.println("Error cargando el recurso: " + iconPath1);
                    e.printStackTrace();
                }
            } else {
                imageView.setImage(null); // Si no hay imagen, limpiar el ImageView
                imageView.setEffect(null); // Asegurarse de que no haya efecto si no hay imagen
            }
        }

        @FXML	
        public void selectPokemon1(MouseEvent event) {
            disableButton(pokemon1);
            enableButton(pokemon2);
            enableButton(pokemon3);
            String selectedPokemon = choicePokemon1.getValue();
            this.idPokemon = Integer.parseInt(selectedPokemon.substring(1, selectedPokemon.indexOf(' ')));
            pickPokemon.setText("Has elegido como pokemon activo: " + selectedPokemon.substring(selectedPokemon.indexOf(' ') + 1));
        }
        
        @FXML
        public void selectPokemon2(MouseEvent event) {
            disableButton(pokemon2);
            enableButton(pokemon1);
            enableButton(pokemon3);
            String selectedPokemon = choicePokemon2.getValue();
            this.idPokemon = Integer.parseInt(selectedPokemon.substring(1, selectedPokemon.indexOf(' ')));
            pickPokemon.setText("Has elegido como pokemon activo: " + selectedPokemon.substring(selectedPokemon.indexOf(' ') + 1));
        }
        
        @FXML
        public void selectPokemon3(MouseEvent event) {
            disableButton(pokemon3);
            enableButton(pokemon1);
            enableButton(pokemon2);
            String selectedPokemon = choicePokemon3.getValue();
            this.idPokemon = Integer.parseInt(selectedPokemon.substring(1, selectedPokemon.indexOf(' ')));
            pickPokemon.setText("Has elegido como pokemon activo: " + selectedPokemon.substring(selectedPokemon.indexOf(' ') + 1));
        }
        
        private void disableButton(Button button) {
            button.setDisable(true);
        }
        
        private void enableButton(Button button) {
            button.setDisable(false);
        }

        public void setBattleStatus(String status, int round) {
            this.round = round;
            ControllerAttackResult ctrl = (ControllerAttackResult) UtilsViews.getController("ViewAttackResult");
        
            // Deshabilitar la selección de mapa y Pokémon según el estado de la batalla
            switch (status) {
                case STATUS_BATTLE_STARTED:
                    imgArrowBack.setDisable(true);
                    startButton.setDisable(true);
                    continueButton.setDisable(false);
                    //Hacer que los labels nextMap i previousMap de escoger mapa no sean visibles
                    nextMap.setText("");
                    previousMap.setText("");
                    disablePokemonSelection(true); // Deshabilitar selección de Pokémon
                    break;
                case STATUS_BATTLE_PREP:
                    imgArrowBack.setDisable(false);
                    startButton.setDisable(false);
                    continueButton.setDisable(true);
                    disablePokemonSelection(false); // Habilitar selección de Pokémon
                    break;
                case STATUS_BATTLE_ENDED:
                    imgArrowBack.setDisable(false);
                    startButton.setDisable(false);
                    continueButton.setDisable(true);
                    disablePokemonSelection(false); // Habilitar selección de Pokémon
                    //showBattleResults(); // Mostrar resultados de la batalla
                    break;
                default:
                    break;
            }
        }
        
        /**
         * Método para deshabilitar o habilitar la selección de Pokémon.
         * @param disable true para deshabilitar, false para habilitar.
         */
        private void disablePokemonSelection(boolean disable) {
            choicePokemon1.setDisable(disable);
            choicePokemon2.setDisable(disable);
            choicePokemon3.setDisable(disable);
            pokemon1.setDisable(disable);
            pokemon2.setDisable(disable);
            pokemon3.setDisable(disable);
        }      

}
