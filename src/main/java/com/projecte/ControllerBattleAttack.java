package com.projecte;


import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
    private Label attackNameLabel, attackTypeLabel, attackDamageLabel, estaminaLabel; 

    private int currentSelection = 0;

    private Label[] moves;

    @FXML
    public void initialize() {
        moves = new Label[]{move1, move2, move3, move4};
        updateSelection();
        
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
     * Método para establecer el texto del primer movimiento.
     * @param moveText
     */
    public void setMove1(String moveText) {
        move1.setText(moveText);
    }
    
    /**
     * Método para establecer el texto del segundo movimiento.
     * @param moveText
     */
    public void setMove2(String moveText) {
        move2.setText(moveText);
    }

    /**
     * Método para establecer el texto del tercer movimiento.
     * @param moveText
     */
    public void setMove3(String moveText) {
        move3.setText(moveText);
    }
    
    /**
     * Método para establecer el texto del cuarto movimiento.
     * @param moveText
     */
    public void setMove4(String moveText) {
        move4.setText(moveText);
    }

    /**
     * Método para obtener el texto del primer movimiento.
     * @return Texto del primer movimiento.
     */
    public String getMove1() {
        return move1.getText();
    }

    /**
     * Método para obtener el texto del segundo movimiento.
     * @return Texto del segundo movimiento.
     */
    public String getMove2() {
        return move2.getText();
    }

    /**
     * Método para obtener el texto del tercer movimiento.
     * @return Texto del tercer movimiento.
     */
    public String getMove3() {
        return move3.getText();
    }
    
    /**
     * Método para obtener el texto del cuarto movimiento.
     * @return Texto del cuarto movimiento.
     */
    public String getMove4 () {
        return move4.getText();
    }

    /**
     * Método para obtener la barra de vida del enemigo.
     * @return ProgressBar de la barra de vida del enemigo.
     */
    public ProgressBar getEnemyHpBar() {
        return enemyHpBar;
    }

    /**
     * Método para establecer la barra de vida del enemigo.
     * @param enemyHpBar
     */
    public void setEnemyHpBar(Double hp) {
        this.enemyHpBar.setProgress(hp);
    }

    /**
     * Método para obtener la barra de stamina del enemigo.
     * @return ProgressBar de la barra de stamina del enemigo.
     */
    public ProgressBar getEnemyStaminaBar() {
        return enemyStaminaBar;
    }

    /**
     * Método para establecer la barra de stamina del enemigo.
     * @param enemyStaminaBar
     */
    public void setEnemyStaminaBar(Double stamina) {
        this.enemyStaminaBar.setProgress(stamina);
    }

    /**
     * Método para obtener la barra de stamina del jugador.
     * @return ProgressBar de la barra de stamina del jugador.
     */
    public ProgressBar getPlayerStaminaBar() {
        return playerStaminaBar;
    }

    /**
     * Método para establecer la barra de stamina del jugador.
     * @param playerStaminaBar
     */
    public void setPlayerStaminaBar(Double stamina) {
        this.playerStaminaBar.setProgress(stamina);
    }

    /**
     * Método para obtener la barra de vida del jugador.
     * @return ProgressBar de la barra de vida del jugador. 
     */
    public ProgressBar getPlayerHpBar() {
        return playerHpBar;
    }
    /**
     * Método para establecer la barra de vida del jugador.
     * @param playerHpBar
     */
    public void setPlayerHpBar(Double hp) {
        this.playerHpBar.setProgress(hp);
    }

    /**
     * Método para obtener la imagen del Pokémon enemigo.
     * @return ImageView de la imagen del Pokémon enemigo.
     */
    public ImageView getEnemyPokemonImage() {
        return enemyPokemonImage;
    }

    /**
     * Método para establecer la imagen del Pokémon enemigo.
     * @param enemyPokemonImage
     */
    public void setEnemyPokemonImage(String imagePath) {
        try {
            // Asegúrate de que la ruta comience con "/"
            if (!imagePath.startsWith("/")) {
                imagePath = "/" + imagePath;
            }
    
            // Obtén el recurso
            java.net.URL resource = getClass().getResource(imagePath);
            if (resource == null) {
                throw new NullPointerException("Recurso no encontrado: " + imagePath);
            }
    
            // Carga la imagen
            String fullPath = resource.toExternalForm();
            Image image = new Image(fullPath);
            enemyPokemonImage.setImage(image);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error cargando el recurso: " + imagePath);
            e.printStackTrace();
        }
    }

    /**
     * Metodo para conseguir la imagen del Pokémon jugador.
     * @return ImageView de la imagen del Pokémon jugador.
     */
    public ImageView getPlayerPokemonImage() {
        return playerPokemonImage;
    }

    /**
     * Método para establecer la imagen del Pokémon jugador.
     * @param playerPokemonImage
     */
    public void setPlayerPokemonImage(String imagePath) {
        try {
            // Asegúrate de que la ruta comience con "/"
            if (!imagePath.startsWith("/")) {
                imagePath = "/" + imagePath;
            }
    
            // Obtén el recurso
            java.net.URL resource = getClass().getResource(imagePath);
            if (resource == null) {
                throw new NullPointerException("Recurso no encontrado: " + imagePath);
            }
    
            // Carga la imagen
            String fullPath = resource.toExternalForm();
            Image image = new Image(fullPath);
            playerPokemonImage.setImage(image);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error cargando el recurso: " + imagePath);
            e.printStackTrace();
        }
    }
    
    /**
     * Método para establecer la imagen del Pokémon jugador.
     * @return ImageView de la imagen del background.
     */
    public ImageView getBackgroundImage() {
        return backgroundImage;
    }

    /**
     * Método para establecer la imagen de fondo.
     * @param backgroundImage
     */
    public void setMap(String imagePath) {
        try {
            // Asegúrate de que la ruta comience con "/"
            if (!imagePath.startsWith("/")) {
                imagePath = "/" + imagePath;
            }
    
            // Obtén el recurso
            java.net.URL resource = getClass().getResource(imagePath);
            if (resource == null) {
                throw new NullPointerException("Recurso no encontrado: " + imagePath);
            }
    
            // Carga la imagen
            String fullPath = resource.toExternalForm();
            Image image = new Image(fullPath);
            backgroundImage.setImage(image);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error cargando el recurso: " + imagePath);
            e.printStackTrace();
        }
    }

    /**
     * Método para obtener el nombre del ataque.
     * @return Label que contiene el nombre del ataque.
     */
    public Label getAttackNameLabel() {
        return attackNameLabel;
    }


    /**
     * Método para establecer el nombre del ataque.
     * @param attackNameLabel Label que contiene el nombre del ataque.
     */
    public void setAttackNameLabel(Label attackNameLabel) {
        this.attackNameLabel = attackNameLabel;
    }

    /**
     * Método para obtener el tipo de ataque.
     * @return Label que contiene el tipo de ataque.
     */
    public Label getAttackTypeLabel() {
        return attackTypeLabel;
    }

    /**
     * Método para establecer el tipo de ataque.
     * @param attackTypeLabel
     */
    public void setAttackTypeLabel(Label attackTypeLabel) {
        this.attackTypeLabel = attackTypeLabel;
    }

    /**
     * Método para obtener el daño del ataque.
     * @return Label que contiene el daño del ataque.
     */
    public Label getAttackDamageLabel() {
        return attackDamageLabel;
    }

    /**
     * Método para establecer el daño del ataque.
     * @param attackDamageLabel Label que contiene el daño del ataque.
     */
    public void setAttackDamageLabel(Label attackDamageLabel) {
        this.attackDamageLabel = attackDamageLabel;
    }

    /**
     * Método para obtener la estamina.
     * @return Label que contiene la estamina.
     */
    public Label getEstaminaLabel() {
        return estaminaLabel;
    }

    /**
     * Método para establecer la estamina.
     * @param estaminaLabel Label que contiene la estamina.
     */
    public void setEstaminaLabel(Label estaminaLabel) {
        this.estaminaLabel = estaminaLabel;
    }
        
    /**
     * Método para actualizar la selección de movimientos.
     */
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

    /**
     * Método para actualizar la información del ataque.
     * 
     * @param index
     */
    private void updateAttackInfo(int index) {

        String[] names = {"Quick Attack", "Wing Attack", "Gust", "Focus Energy"};
        String[] types = {"Normal", "Flying", "Flying", "Normal"};
        String[] damages = {"40", "60", "50", "20"}; 
        String[] estaminas = {
            "5",
            "3",
            "4",
            "10"
        };

        // Actualiza el VBox con la información del ataque
        attackNameLabel.setText("➤ " + names[index]);
        attackTypeLabel.setText("Tipo: " + types[index]);
        attackDamageLabel.setText("Daño: " + damages[index]);
        estaminaLabel.setText("Estamina: " + estaminas[index]);

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

    @FXML
    public void fightButtonAction(ActionEvent event) {

        fightButton.setStyle("-fx-background-color: red;");

        handleAttack(currentSelection); // Mostrar el ataque seleccionado
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event2 -> {
                fightButton.setStyle("-fx-background-color: #ffcc00; -fx-effect: dropshadow(gaussian, #ffffff, 2, 0.5, 0.0, 0.0); -fx-font-weight: bold;");
            }); // Resetear el estilo después de 1 segundo
            pause.play();
            System.out.println("Fight button clicked! Current move: " + moves[currentSelection].getText());
    }

    @FXML
    public void runButtonAction(ActionEvent event) {
        // Cambiar el estilo del botón al hacer clic
        runButton.setStyle("-fx-background-color: blue;");
        
        // Resetear el estilo después de 1 segundo
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event2 -> {
            runButton.setStyle("-fx-background-color: #ffcc00; -fx-effect: dropshadow(gaussian, #ffffff, 2, 0.5, 0.0, 0.0); -fx-font-weight: bold;");
        });
        pause.play();
    }
}
