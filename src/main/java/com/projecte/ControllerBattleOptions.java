package com.projecte;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class ControllerBattleOptions implements Initializable {

    @FXML 
    private ImageView imgArrowBack;

    @FXML
    private Button startButton;
    
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

    private int currentMapSelection = 0;
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
        ControllerBattleAttack ctrl = (ControllerBattleAttack) UtilsViews.getController("ViewBattleAttack");
        ctrl.setMap(mapPaths.get(currentMapIndex));
        ctrl.setEnemyPokemonImage("/assets/pokemons/normal/005.gif");
        ctrl.setPlayerPokemonImage("/assets/pokemons/back/020.gif");
        ctrl.setEstaminaComputer("100/200");
        ctrl.setEstaminaPlayer("200/20");
        ctrl.setHpPlayer("50/50");
        ctrl.setHpComputer("100/50");
        ctrl.setEnemyHpBar(0.6);
        ctrl.setEnemyStaminaBar(0.5);
        ctrl.setPlayerHpBar(0.8);
        ctrl.setPlayerStaminaBar(1.0);
        UtilsViews.setViewAnimating("ViewBattleAttack");
    }

    private List<String> mapPaths = new ArrayList<>();
    private int currentMapIndex = 0;

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

}
