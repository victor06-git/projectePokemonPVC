package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;

public class ControllerBattleResult {

    @FXML
    private Button btnMenu;

    @FXML
    private Label LabelNivel;

    @FXML
    private Label LabelGanador, LaNivel, LabelNpokemons, LabelItems;

    @FXML
    private TextField NomGanador, LevelActual, levelAumentat, NewsPokemons;
    
    @FXML
    private TextArea itemsRecogidos;

    @FXML
    private ProgressBar BarraProg;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private VBox mainContainer;

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // Set initial styles and properties
        setupUI();
        
        // Set up button actions
        btnMenu.setOnAction(e -> handleMenuButton());
    }

    /**
     * Sets up the initial UI state
     */
    private void setupUI() {
        // Set progress bar style
        BarraProg.setStyle("-fx-accent: #3b4cca;");
        
        // Disable editing for text fields and area
        NomGanador.setEditable(false);
        LevelActual.setEditable(false);
        levelAumentat.setEditable(false);
        NewsPokemons.setEditable(false);
        itemsRecogidos.setEditable(false);
    }

    /**
     * Handles the menu button action
     */
    private void handleMenuButton() {
    }

    /**
     * Updates the battle result information
     * @param winnerName Name of the winning Pokémon/trainer
     * @param currentLevel Current level before battle
     * @param newLevel New level after battle
     * @param newPokemons String of newly caught Pokémon
     * @param collectedItems String of collected items
     * @param expProgress Experience progress (0.0 to 1.0)
     */
    public void updateBattleResults(String winnerName, int currentLevel, int newLevel, 
                                  String newPokemons, String collectedItems, double expProgress) {
        NomGanador.setText(winnerName);
        LevelActual.setText(String.valueOf(currentLevel));
        levelAumentat.setText(String.valueOf(newLevel));
        NewsPokemons.setText(newPokemons);
        itemsRecogidos.setText(collectedItems);
        BarraProg.setProgress(expProgress);
    }

    /**
     * Sets the background image
     * @param imagePath Path to the image file
     */
    public void setBackgroundImage(String imagePath) {
        try {
            backgroundImage.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.err.println("Error loading background image: " + imagePath);
            e.printStackTrace();
        }
    }

    /**
     * Clears all result fields
     */
    public void clearResults() {
        NomGanador.clear();
        LevelActual.clear();
        levelAumentat.clear();
        NewsPokemons.clear();
        itemsRecogidos.clear();
        BarraProg.setProgress(0);
    }
}