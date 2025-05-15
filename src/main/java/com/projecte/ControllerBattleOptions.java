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

import static com.projecte.BuildDatabase.selected_path;
import com.utils.UtilsViews;

import javafx.application.Platform;
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
    private HashMap<Integer, Boolean> pokemonStatus = new HashMap<>(); // true = vivo, false = muerto
    private int currentMapIndex = 0;

    private HashMap<Integer, String> enemyPokemons = new HashMap<>();
    private List<Integer> selectedPokemonIds = new ArrayList<>();
    private List<Integer> enemyPokemonIds = new ArrayList<>();
    private Set<Integer> enemyPokemonSet = new HashSet<>();


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
    private String winner;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Path imagePath = null;
        // Cargar los Pokémon desbloqueados en los ChoiceBox y sus imágenes
        loadUnlockedPokemons();
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

    public void setPokemonStatus(HashMap<Integer, Boolean> pokemonStatus) {
        this.pokemonStatus = pokemonStatus;
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
        ctrl.setIdPokemon(idPokemon);
        ctrl.setRound(round);

        selectedPokemonIds.clear();
        selectedPokemonIds.add(getPokemonIdFromChoiceBox(choicePokemon1));
        selectedPokemonIds.add(getPokemonIdFromChoiceBox(choicePokemon2));
        selectedPokemonIds.add(getPokemonIdFromChoiceBox(choicePokemon3));

        ctrl.setPlayerPokemons(selectedPokemonIds);
        
        // Generar Pokémon enemigos aleatorios solo si no hay Pokémon enemigos existentes
        if (enemyPokemonIds.isEmpty()) {
            Random random = new Random();
            
            // Generar 3 IDs únicos de Pokémon (1-251)
            while (enemyPokemonSet.size() < 3) {
                enemyPokemonSet.add(random.nextInt(251) + 1);
            }
            
            // Obtener nombres desde la BD
            AppData db = AppData.getInstance();
            db.connect(selected_path);
            
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
        ctrl.insertBattlePokemons();
        UtilsViews.setViewAnimating("ViewBattleAttack");
        
    }

    /**
     * Método para obtener el ID del Pokémon seleccionado en un ChoiceBox.
     * 
     * @param choiceBox El ChoiceBox que contiene los Pokémon.
     * @return El ID del Pokémon seleccionado.
     */
    private int getPokemonIdFromChoiceBox(ChoiceBox<String> choiceBox) {
        String selectedPokemon = choiceBox.getValue();
        return Integer.parseInt(selectedPokemon.substring(1, selectedPokemon.indexOf(' ')));
    }

    /**
     * Método para continuar la batalla.
     * 
     * @param event El evento de acción del botón continuar.
     */
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

        if (!arePokemonsDifferent()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Selección de Pokémon");
            alert.setHeaderText(null);
            alert.setContentText("Los Pokémon seleccionados deben ser diferentes.");
            alert.showAndWait();
            return; // Salir del método si no son diferentes
        }

        ControllerBattleAttack ctrl = (ControllerBattleAttack) UtilsViews.getController("ViewBattleAttack");
        ctrl.setMap(mapPaths.get(currentMapIndex));
        ctrl.setIdPokemon(idPokemon);
        ctrl.setRound(round);

        // Obtener nombres desde la BD
        AppData db = AppData.getInstance();
        db.connect(selected_path);        

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
        double enemyHp = ctrl.getEnemyHpBar().getProgress();
        double enemyStamina = ctrl.getEnemyStaminaBar().getProgress();
        String computerHp = ctrl.getHpComputer();
        String computerStamina = ctrl.getEstaminaComputer();
        ctrl.setEstaminaComputer(computerStamina);
        ctrl.setEstaminaPlayer(stamina + "/30");
        ctrl.setHpPlayer(maxHp + "/100");
        ctrl.setHpComputer(computerHp);
        ctrl.setEnemyHpBar(enemyHp);
        ctrl.setEnemyStaminaBar(enemyStamina);
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
        selectedPokemons.add(choicePokemon1.getValue());
        selectedPokemons.add(choicePokemon2.getValue());
        selectedPokemons.add(choicePokemon3.getValue());
        return selectedPokemons.size() == 3; // Deben ser tres diferentes
    }

    /**
     * Método para cargar las rutas de los mapas desde la carpeta assets/mapa.
     */
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

    /**
     * Método para mostrar el mapa anterior.
     * @param event El evento de acción del mouse.
     */
    @FXML
    private void showPreviousMap(MouseEvent event) {
        if (currentMapIndex > 0) {
            currentMapIndex--;
            showCurrentMap();
        } else {
            System.out.println("No hay mapas anteriores.");
        }
    }

    /**
     * Método para mostrar el siguiente mapa.
     * @param event El evento de acción del mouse.
     */
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
        db.connect(selected_path);

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
        //System.out.println("Pokémon desbloqueados encontrados: " + unlockedPokemons.size());

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

            disableDeadPokemons();

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
            String selectedPokemon = choicePokemon1.getValue();
            int selectedId = Integer.parseInt(selectedPokemon.substring(1, selectedPokemon.indexOf(' ')));

            if (pokemonStatus.getOrDefault(selectedId, true)) { // Verifica si el Pokémon seleccionado está vivo
                disableButton(pokemon1);
                enableButton(pokemon2);
                enableButton(pokemon3);
                this.idPokemon = selectedId; // Actualiza el idPokemon al seleccionado
                pickPokemon.setText("Has elegido como pokemon activo: " + selectedPokemon.substring(selectedPokemon.indexOf(' ') + 1));
            } else {
                // Si el Pokémon está muerto, mostrar un mensaje
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Pokémon Muerto");
                alert.setHeaderText(null);
                alert.setContentText("Este Pokémon está muerto y no puede ser seleccionado.");
                alert.showAndWait();
                imgPokemon1.setEffect(new javafx.scene.effect.Shadow(10, javafx.scene.paint.Color.BLACK));
            }
        }

        @FXML
        public void selectPokemon2(MouseEvent event) {
            String selectedPokemon = choicePokemon2.getValue();
            int selectedId = Integer.parseInt(selectedPokemon.substring(1, selectedPokemon.indexOf(' ')));

            if (pokemonStatus.getOrDefault(selectedId, true)) { // Verifica si el Pokémon seleccionado está vivo
                disableButton(pokemon2);
                enableButton(pokemon1);
                enableButton(pokemon3);
                this.idPokemon = selectedId; // Actualiza el idPokemon al seleccionado
                pickPokemon.setText("Has elegido como pokemon activo: " + selectedPokemon.substring(selectedPokemon.indexOf(' ') + 1));
            } else {
                // Si el Pokémon está muerto, mostrar un mensaje
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Pokémon Muerto");
                alert.setHeaderText(null);
                alert.setContentText("Este Pokémon está muerto y no puede ser seleccionado.");
                alert.showAndWait();
                imgPokemon2.setEffect(new javafx.scene.effect.Shadow(10, javafx.scene.paint.Color.BLACK));
            }
        }

        @FXML
        public void selectPokemon3(MouseEvent event) {
            String selectedPokemon = choicePokemon3.getValue();
            int selectedId = Integer.parseInt(selectedPokemon.substring(1, selectedPokemon.indexOf(' ')));

            if (pokemonStatus.getOrDefault(selectedId, true)) { // Verifica si el Pokémon seleccionado está vivo
                disableButton(pokemon3);
                enableButton(pokemon1);
                enableButton(pokemon2);
                this.idPokemon = selectedId; // Actualiza el idPokemon al seleccionado
                pickPokemon.setText("Has elegido como pokemon activo: " + selectedPokemon.substring(selectedPokemon.indexOf(' ') + 1));
            } else {
                // Si el Pokémon está muerto, mostrar un mensaje
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Pokémon Muerto");
                alert.setHeaderText(null);
                alert.setContentText("Este Pokémon está muerto y no puede ser seleccionado.");
                alert.showAndWait();
                imgPokemon3.setEffect(new javafx.scene.effect.Shadow(10, javafx.scene.paint.Color.BLACK));
            }
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
                    resetBattleState(); // Reiniciar el estado de la batalla
                    break;
                default:
                    break;
            }
        }

        // Método para restaurar el estado de la batalla para una nueva partida
        public void resetBattleState() {
            // Reiniciar el estado de los Pokémon
            pokemonStatus.clear();
            selectedPokemonIds.clear();
            enemyPokemonIds.clear();
            enemyPokemonSet.clear();
            enemyPokemons.clear();

            nextMap.setText(">>");
            previousMap.setText("<<");

            // Reiniciar los ChoiceBox
            choicePokemon1.getItems().clear();
            choicePokemon2.getItems().clear();
            choicePokemon3.getItems().clear();

            // Reiniciar las imágenes de los Pokémon
            imgPokemon1.setImage(null);
            imgPokemon2.setImage(null);
            imgPokemon3.setImage(null);

            // Reiniciar el texto de selección de Pokémon
            pickPokemon.setText("Selecciona un Pokémon activo");

            // Reiniciar el ID del Pokémon seleccionado
            idPokemon = -1;

            // Reiniciar el mapa seleccionado
            currentMapIndex = 0;
            showCurrentMap();

            // Habilitar los botones y ChoiceBox
            enableButton(pokemon1);
            enableButton(pokemon2);
            enableButton(pokemon3);
            disablePokemonSelection(false);

            // Reiniciar las variables de la batalla
            round = 1;
            winner = null;
            loadUnlockedPokemons(); // Recargar los Pokémon desbloqueados
        }
        
        /**
         * Método para deshabilitar o habilitar la selección de Pokémon.
         * @param disable true para deshabilitar, false para habilitar.
         */
        private void disablePokemonSelection(boolean disable) {
            choicePokemon1.setDisable(disable);
            choicePokemon2.setDisable(disable);
            choicePokemon3.setDisable(disable);
        }      

        private void disableDeadPokemons() {
            // Deshabilitar los botones de Pokémon que están muertos
            if (choicePokemon1.getItems().size() > 0) {
                int id1 = Integer.parseInt(choicePokemon1.getValue().substring(1, choicePokemon1.getValue().indexOf(' ')));
                if (!pokemonStatus.getOrDefault(id1, true)) {
                    disableButton(pokemon1);
                } else {
                    enableButton(pokemon1);
                }
            }
            if (choicePokemon2.getItems().size() > 0) {
                int id2 = Integer.parseInt(choicePokemon2.getValue().substring(1, choicePokemon2.getValue().indexOf(' ')));
                if (!pokemonStatus.getOrDefault(id2, true)) {
                    disableButton(pokemon2);
                } else {
                    enableButton(pokemon2);
                }
            }
            if (choicePokemon3.getItems().size() > 0) {
                int id3 = Integer.parseInt(choicePokemon3.getValue().substring(1, choicePokemon3.getValue().indexOf(' ')));
                if (!pokemonStatus.getOrDefault(id3, true)) {
                    disableButton(pokemon3);
                } else {
                    enableButton(pokemon3);
                }
            }
        }

        public void markPokemonAsDead(int pokemonId) {
            pokemonStatus.put(pokemonId, false); // Marca el Pokémon como muerto
            disableDeadPokemons(); // Deshabilita los botones de Pokémon muertos
            System.out.println("Pokemon " + pokemonId + " marcado como muerto.");
            this.idPokemon = -1; // Reinicia el idPokemon al no tener Pokémon activo
            pickPokemon.setText("No has elegido ningún Pokémon activo.");
        
            // Verificar si todos los Pokémon están muertos
            if (areAllPokemonsDead()) {
                endGame(); // Llama a un método para finalizar el juego
            }
        }
        
        // Método para verificar si todos los Pokémon están muertos
        // Método para verificar si todos los Pokémon están muertos
        public boolean areAllPokemonsDead() {
            System.out.println("Estado de los Pokémon: " + pokemonStatus);

            // Verificar si todos los valores en pokemonStatus son false
            for (boolean isAlive : pokemonStatus.values()) {
                if (isAlive) {
                    // Si al menos un Pokémon está vivo, devolver false
                    return false;
                }
            }

            // Si todos los Pokémon están muertos, devolver true
            return true;
        }
        
        
        // Método para finalizar el juego
        public void endGame() {
            Platform.runLater(() -> {
                
                ControllerBattleAttack ctrl = (ControllerBattleAttack) UtilsViews.getController("ViewBattleAttack");
                if (areAllPokemonsDead()) {
                    winner = "Computer"; // El jugador perdió
                } else if (!ctrl.hasMoreEnemyPokemons()) {
                    winner = "Player"; // El jugador ganó
                }

                try {
                    AppData db = AppData.getInstance();
                    db.connect(selected_path);

                    // Obtener el nombre del mapa jugado
                    String mapName = "";
                    if (!mapPaths.isEmpty() && currentMapIndex >= 0 && currentMapIndex < mapPaths.size()) {
                        String mapPath = mapPaths.get(currentMapIndex);
                        mapName = mapPath.substring(mapPath.lastIndexOf("/") + 1, mapPath.lastIndexOf("."));
                    }

                    // Obtener fecha y hora actual
                    java.time.LocalDateTime now = java.time.LocalDateTime.now();
                    String dateTime = now.toString(); // Formato ISO

                    // Determinar el ganador
                    String battleWinner = winner != null ? winner : "Computer";

                    // Insertar en la tabla Battle
                    String insertBattle = String.format(
                        "INSERT INTO Battle (date, map, winner) VALUES ('%s', '%s', '%s');",
                        dateTime, mapName, battleWinner
                    );
                    db.update(insertBattle);

                    db.close();
                } catch (Exception e) {
                    System.err.println("Error al guardar la batalla en la base de datos:");
                    e.printStackTrace();
                }

                ControllerAttackResult ctrlResult = (ControllerAttackResult) UtilsViews.getController("ViewAttackResult");
                
                
                ctrlResult.setWinner(winner); // Establecer el ganador en el controlador de resultados
                ctrlResult.setRound(round);
                ctrlResult.setFinalBattle(true);
                ctrlResult.setEquipLabel("Player"); // Indica que el jugador ganó
                ctrlResult.setHpLabel(ctrl.getHpComputer());
                ctrlResult.setEstaminaLabel(ctrl.getEstaminaComputer());
                ctrlResult.setHpPlayer(ctrl.getHpPlayer());
                ctrlResult.setEstaminaPlayer(ctrl.getEstaminaPlayer());
                //ctrlResult.setStatsLabel("Ataque: " + moves[currentSelection].getText().replace("➤", "").trim());
                //ctrlResult.setPokemonLabel(playerPokemonLabel.getText());

                // Cambiar a la vista de resultados
                UtilsViews.setViewAnimating("ViewAttackResult");
            });
        }

        private void showAlert(String message, Alert.AlertType alertType) {
            Alert alert = new Alert(alertType);
            alert.setTitle("Fin de la partida");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        }        
}
