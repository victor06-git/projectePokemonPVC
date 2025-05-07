package com.projecte;


import java.io.File;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class ControllerBattleAttack {

    @FXML 
    private Label move1, move2, move3, move4, moveDescriptionLabel, computerPokemonLabel, playerPokemonLabel;

    @FXML
    private VBox movePanel;

    @FXML
    private Button fightButton, runButton;

    @FXML
    private ProgressBar enemyHpBar, enemyStaminaBar, playerStaminaBar, playerHpBar;

    @FXML
    private ImageView enemyPokemonImage, playerPokemonImage, backgroundImage;
    
    @FXML
    private Label attackNameLabel, attackTypeLabel, attackDamageLabel, attackDescriptionLabel; 

    private int currentSelection = 0;

    private Label[] moves;

    @FXML
    public void initialize() {
        moves = new Label[]{move1, move2, move3, move4};
        updateSelection();

        loadPokemonImages("data/pokemons/Articuno.gif", enemyPokemonImage);
        loadPokemonImages("data/pokemons/Aerodactyl.gif", playerPokemonImage);  
        loadPokemonImages("data/mapa/mapa2.jpg", backgroundImage);
        
        playerPokemonImage.setScaleX(-1); //Per cambiar la vista del PlayerPokemon
    
        Platform.runLater(() -> movePanel.requestFocus()); // Asegura que el panel tenga foco
    
        movePanel.setOnMouseClicked(e -> movePanel.requestFocus()); // Por si el usuario hace clic
    
        for (int i = 0; i < moves.length; i++) {
            final int index = i;
            moves[i].setOnMouseClicked(e -> {
                currentSelection = index;
                updateSelection();
            });
        }
    
        movePanel.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP -> {
                    currentSelection = (currentSelection - 1 + moves.length) % moves.length;
                    updateSelection();
                    event.consume();
                }
                case DOWN -> {
                    currentSelection = (currentSelection + 1) % moves.length;
                    updateSelection();
                    event.consume();
                }
                case ENTER -> {
                    handleAttack(currentSelection);
                    event.consume();
                }
                default -> {
                    // Do nothing
                }
            }
        });
    }

    

    /**
     * Método para cargar el mapa en el ImageView.
     */
    public void setMap() {
        // Cargar la imagen del mapa desde la ruta proporcionada
        String mapImage = getClass().getResource("data/mapa/mapa2.jpg").toExternalForm();
        backgroundImage.setImage(new Image(mapImage));
    }
        

    private void updateSelection() {
        for (int i = 0; i < moves.length; i++) {
            String moveText = moves[i].getText().replaceAll("➤", "").trim(); // Limpiar cualquier flecha anterior
            if (i == currentSelection) {
                moves[i].setText("➤ " + moveText);
                moves[i].setStyle("-fx-text-fill: yellow; -fx-font-family: 'Courier New'; -fx-font-size: 20px;");
                updateAttackInfo(i);
            } else {
                moves[i].setText("   " + moveText);
                moves[i].setStyle("-fx-text-fill: white; -fx-font-family: 'Courier New'; -fx-font-size: 20px;");
            }
        }
    }

    private void updateAttackInfo(int index) {

        String[] names = {"Quick Attack", "Wing Attack", "Gust", "Focus Energy"};
        String[] types = {"Normal", "Flying", "Flying", "Normal"};
        String[] damages = {"40", "60", "50", "0"}; // 0 para Focus Energy ya que no causa daño
        String[] descriptions = {
            "A fast normal-type attack that always hits first.",
            "A powerful slash with wings.",
            "A gust of wind that may flinch the enemy.",
            "Raises user's critical hit chance."
        };

        // Actualiza el VBox con la información del ataque
        attackNameLabel.setText("➤ " + names[index]);
        attackTypeLabel.setText("Tipo: " + types[index]);
        attackDamageLabel.setText("Daño: " + damages[index]);
        attackDescriptionLabel.setText("Descripción: " + descriptions[index]);

    }

    /**
     * Function to handle the attack action.
     * 
     * @param selectedMove
     */
    private void handleAttack(int selectedMove) {
        System.out.println("Using move: " + moves[selectedMove].getText());

        // Actualizar el panel de información con los detalles del ataque seleccionado
        updateAttackInfo(selectedMove);

        // Mostrar el panel de información si está oculto
        attackNameLabel.getParent().setVisible(true);

        // Resetear el color del botón después de 1 segundo
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> fightButton.setStyle(""));
        pause.play();
        
    }

    /**
     * Function to load Pokémon images from the specified path.
     * 
     * @param imagePath The path to the image file.
     * @param pokemon  The ImageView to display the Pokémon image.
     */
    private void loadPokemonImages(String imagePath, ImageView pokemon) {
        try {
            File file = new File(imagePath);
            Image image = new Image(file.toURI().toString());
            pokemon.setImage(image);
        } catch (NullPointerException e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }
    }

}
