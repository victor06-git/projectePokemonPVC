package com.projecte;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.projecte.BuildDatabase.selected_path;
import com.utils.UtilsViews;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class ControllerBattleAttack {

    @FXML
    private StackPane rootPane;

    @FXML 
    private Label move1, move2, move3, move4, moveDescriptionLabel, computerPokemonLabel, playerPokemonLabel;

    @FXML
    private VBox movePanel, attackInfoPanel;

    @FXML
    private Button fightButton, runButton;

    @FXML
    private ProgressBar enemyHpBar, enemyStaminaBar, playerStaminaBar, playerHpBar;

    @FXML
    private ImageView enemyPokemonImage, playerPokemonImage, backgroundImage;
    
    @FXML
    private Label attackNameLabel, attackTypeLabel, attackDamageLabel, estaminaLabel, hpComputer, estaminePlayer, estamineComputer, hpPlayer; 

    private int currentSelection = 0;

    private Label[] moves;

    private int round = -1; // Variable to track the current round

    private int idPokemon; // ID del Pokémon jugador
    List<String> pokemonTypes; // Lista de tipos del Pokémon jugador
    private List<Integer> playerPokemonIds; // Para almacenar los IDs de los Pokémon del jugador
    private HashMap<Integer, Boolean> playerPokemonStatus = new HashMap<>();

    private HashMap<Integer, String> enemyPokemons; // Para almacenar los Pokémon enemigos
    private List<Integer> enemyPokemonIds; // Para almacenar los IDs de los Pokémon enemigos
    public static final String STATUS_BATTLE_STARTED = "battle_started";
    public static final String STATUS_BATTLE_PREP = "battle_prep";
    public static final String STATUS_BATTLE_ENDED = "battle_ended";
    private String[] attackNames;
    private String[] attackTypes;
    private String[] attackDamages;
    private String[] attackStaminaCosts;
    private int battleId; // ID de la batalla actual
    private boolean run; // Variable para indicar si el jugador ha huido
    private Double typeEffectiveness = 1.0; // Variable para almacenar la efectividad del tipo

    @FXML
    public void initialize() {
        moves = new Label[]{move1, move2, move3, move4};
        loadAttacksFromDatabase();
        updateSelection();
    
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
     * Método para mostrar una alerta.
     * @param message Mensaje a mostrar en la alerta.
     * @param type Tipo de alerta (INFORMATION, WARNING, etc.).
     */
    public void setComputerPokemonLabel(String text) {
        this.computerPokemonLabel.setText(text);
    }

    public void setPlayerPokemonLabel(String text) {
        this.playerPokemonLabel.setText(text);
    }

    public void setBattleId(int battleId) {
        this.battleId = battleId;
    }

    /**
     * Método para establecer el ID del Pokémon.
     * @param idPokemon El ID del Pokémon a establecer.
     */
    public void setIdPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
        
        // Obtener los tipos del Pokémon de la base de datos
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        // Obtenemos el tipo (puede ser combinado)
        ArrayList<HashMap<String, Object>> results = db.query(
            "SELECT type FROM Pokemon WHERE id = " + idPokemon + ";");
        
        List<String> types = new ArrayList<>();
        if (!results.isEmpty()) {
            String type = (String) results.get(0).get("type");
            
            // Manejar tipos combinados (ejemplo: "fire/water")
            if (type != null) {
                String[] splitTypes = type.split("/"); // Separar tipos
                for (String t : splitTypes) {
                    if (!types.contains(t)) {
                        types.add(t); // Agregar tipo si no está ya en la lista
                    }
                }
            }
        }
        
        db.close();
        
        System.out.println("Parsed types for Pokémon ID " + idPokemon + ": " + types); // Debugging output
        setPokemonTypes(types);
    }

    public void setPokemonTypes(List<String> types) {
        this.pokemonTypes = new ArrayList<>(types);
        System.out.println("Tipos del Pokémon: " + this.pokemonTypes); // Debugging output
        loadAttacksFromDatabase();
    }

    /**
     * Método para establecer el número de ronda actual.
     * @param round El número de ronda a establecer.
     */
    public void setRound(int round){
        this.round = round;
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

    public void setEstaminaPlayer(String estamina) {
        this.estaminePlayer.setText(estamina);
    }

    public String getEstaminaPlayer(){
        return this.estaminePlayer.getText();
    }

    public void setHpPlayer(String hp){
        this.hpPlayer.setText(hp);
    }

    public String getHpPlayer(){
        return this.hpPlayer.getText();
    }

    public void setHpComputer(String hp){
        this.hpComputer.setText(hp);
    }

    public String getHpComputer(){
        return this.hpComputer.getText();
    }

    public void setEstaminaComputer(String estamina){
        this.estamineComputer.setText(estamina);
    }

    public String getEstaminaComputer(){
        return this.estamineComputer.getText();
    }

    /**
     * Método para establecer la barra de vida del enemigo.
     * @param enemyHpBar
     */
    public void setEnemyHpBar(double hp) {
        this.enemyHpBar.setProgress(hp);
    }

    /**
     * Método para obtener la barra de vida del enemigo.
     * @return ProgressBar de la barra de vida del enemigo.
     */
    public ProgressBar getEnemyHpBar() {
        return enemyHpBar;
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
    public void setEnemyStaminaBar(double stamina) {
        this.enemyStaminaBar.setProgress(stamina);
    }

    /**
     * Método para obtener la barra de stamina del jugador.
     * @return ProgressBar de la barra de stamina del jugador.
     */
    public double getPlayerStaminaBar() {
        return playerStaminaBar.getProgress();
    }

    /**
     * Método para establecer la barra de stamina del jugador.
     * @param playerStaminaBar
     */
    public void setPlayerStaminaBar(double stamina) {
        this.playerStaminaBar.setProgress(stamina);
    }

    /**
     * Método para obtener la barra de vida del jugador.
     * @return ProgressBar de la barra de vida del jugador. 
     */
    public double getPlayerHpBar() {
        return playerHpBar.getProgress();
    }
    /**
     * Método para establecer la barra de vida del jugador.
     * @param playerHpBar
     */
    public void setPlayerHpBar(double hp) {
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
    public void setEstaminaLabel(String estaminaLabel) {
        this.estaminaLabel.setText(estaminaLabel);
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
     * Método para cargar los ataques del pokemon según sus tipos.
     * Para Pokémon con dos tipos: 2 ataques de cada tipo.
     * Para Pokémon con un tipo: 4 ataques de ese tipo.
     */
    public void loadAttacksFromDatabase() {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        ArrayList<HashMap<String, Object>> allAttacks = new ArrayList<>();

        // Primero intentamos obtener los ataques definidos en PokemonAttack
        if (idPokemon > 0) {
            String query = """
                SELECT a.name, a.type, a.damage, a.stamina_cost
                FROM PokemonAttack pa
                JOIN Attack a ON pa.attack_id = a.id
                WHERE pa.pokemon_id = ?
            """;
            ArrayList<HashMap<String, Object>> definedAttacks = db.query(
                query.replace("?", String.valueOf(idPokemon))
            );

            if (!definedAttacks.isEmpty()) {
                allAttacks.addAll(definedAttacks);
                System.out.println("Attacks defined in PokemonAttack: " + allAttacks);
            }
        }

        // Si no hay ataques definidos, usar el comportamiento anterior
        if (allAttacks.isEmpty()) {
            if (pokemonTypes != null && !pokemonTypes.isEmpty()) {
                int attacksPerType = pokemonTypes.size() > 1 ? 2 : 4;
                for (String type : pokemonTypes) {
                    ArrayList<HashMap<String, Object>> typeAttacks = db.query(
                        "SELECT name, type, damage, stamina_cost FROM Attack " +
                        "WHERE type = '" + type + "' " +
                        "ORDER BY damage DESC " +
                        "LIMIT " + attacksPerType + ";");
                    allAttacks.addAll(typeAttacks);
                }
                if (allAttacks.size() < 4) {
                    String excludedTypes = pokemonTypes.stream()
                        .map(t -> "'" + t + "'")
                        .collect(Collectors.joining(","));
                    ArrayList<HashMap<String, Object>> otherAttacks = db.query(
                        "SELECT name, type, damage, stamina_cost FROM Attack " +
                        "WHERE type NOT IN (" + excludedTypes + ") " +
                        "ORDER BY damage DESC " +
                        "LIMIT " + (4 - allAttacks.size()) + ";");
                    allAttacks.addAll(otherAttacks);
                }
            } else {
                allAttacks = db.query(
                    "SELECT name, type, damage, stamina_cost FROM Attack " +
                    "ORDER BY RANDOM() " +
                    "LIMIT 4;");
            }
        }

        int numAttacks = Math.min(allAttacks.size(), 4);
        attackNames = new String[numAttacks];
        attackTypes = new String[numAttacks];
        attackDamages = new String[numAttacks];
        attackStaminaCosts = new String[numAttacks];

        for (int i = 0; i < numAttacks; i++) {
            HashMap<String, Object> attack = allAttacks.get(i);
            attackNames[i] = (String) attack.get("name");
            attackTypes[i] = (String) attack.get("type");
            attackDamages[i] = String.valueOf(attack.get("damage"));
            attackStaminaCosts[i] = String.valueOf(attack.get("stamina_cost"));
        }

        // setMove1(attackNames[0]);
        // setMove2(attackNames[1]);
        // setMove3(attackNames[2]);
        // setMove4(attackNames[3]);

        db.close();

        if (numAttacks > 0) setMove1(attackNames[0]);
        if (numAttacks > 1) setMove2(attackNames[1]);
        if (numAttacks > 2) setMove3(attackNames[2]);
        if (numAttacks > 3) setMove4(attackNames[3]);
        //System.out.println(numAttacks + " attacks loaded: " + attackNames[0] + ", " + attackNames[1] + ", " + attackNames[2] + ", " + attackNames[3]);
    }
        /**
         * Actualiza la información mostrada para el ataque seleccionado
         * @param index Índice del ataque seleccionado (0-3)
         */
        private void updateAttackInfo(int index) {
            if (attackNames == null || attackNames.length <= index) {
                return; // Asegurarse de que hay datos cargados
            }
            
            // Actualiza el VBox con la información del ataque
            attackNameLabel.setText("➤ " + attackNames[index]);
            attackTypeLabel.setText("Tipo: " + attackTypes[index]);
            attackDamageLabel.setText("Daño: " + attackDamages[index]);
            estaminaLabel.setText("Estamina: " + attackStaminaCosts[index]);
        }

    /**
     * Function to handle the attack action.
     * 
     * @param selectedMove
     */
    private void handleAttack(int selectedMove) {

        Random random = new Random();
        // 40% de probabilidad de fallo
        boolean attackFails = random.nextDouble() < 0.4;
        if (attackFails) {
            showAlert("¡El ataque ha fallado!", AlertType.INFORMATION);
            computerAttack(); // El enemigo ataca igualmente
            return;
        }

        System.out.println("Using move: " + moves[selectedMove].getText());
        updateAttackInfo(selectedMove);

        int enemyID = enemyPokemonIds.get(0);
        String enemyType = getPokemonTypes(enemyID).get(0);
        String attackType = attackTypes[selectedMove];

        this.typeEffectiveness = getTypeEffectiveness(attackType, enemyType);
        if (this.typeEffectiveness != null) {
            System.out.println("Efectividad del ataque: " + this.typeEffectiveness);
        } else {
            System.out.println("No se pudo calcular la efectividad del ataque.");
        }
        
        String damageStr = attackDamageLabel.getText().replace("Daño: ", "").trim();
        String staminaStr = estaminaLabel.getText().replace("Estamina: ", "").trim();
        double damage = Double.parseDouble(damageStr) * this.typeEffectiveness;
        double staminaConsumed = Double.parseDouble(staminaStr);
        
        double currentEnemyHp = enemyHpBar.getProgress();
        double newEnemyHp = currentEnemyHp - (damage / 100.0);
        newEnemyHp = Math.max(newEnemyHp, 0);
        enemyHpBar.setProgress(newEnemyHp);
        setHpComputer((int)(newEnemyHp * 100) + "/ 100");
        
        double currentPlayerStamina = playerStaminaBar.getProgress();
        double newPlayerStamina = currentPlayerStamina - (staminaConsumed / 30.0);
        newPlayerStamina = Math.max(newPlayerStamina, 0);
        playerStaminaBar.setProgress(newPlayerStamina);
        setEstaminaPlayer((int)(newPlayerStamina * 30) + "/ 30");
    
        attackNameLabel.getParent().setVisible(true);

        // Si el jugador tiene menos del 25% de estamina, puede atacar otra vez (50% de probabilidad)
        if (newPlayerStamina < 0.25 && random.nextBoolean()) {
            int randomMove;
            do {
                randomMove = random.nextInt(attackNames.length);
            } while (randomMove == selectedMove && attackNames.length > 1); // Asegura que sea distinto si hay más de uno

            selectedMove = randomMove;

            // Actualiza la info del nuevo ataque seleccionado
            updateAttackInfo(selectedMove);
            damageStr = attackDamageLabel.getText().replace("Daño: ", "").trim();
            staminaStr = estaminaLabel.getText().replace("Estamina: ", "").trim();
            damage = Double.parseDouble(damageStr);
            staminaConsumed = Double.parseDouble(staminaStr);
        }
    
        // Verificar si el Pokémon enemigo fue derrotado
        if (computerPokemonDead() || computerPokemonOutOfStamina()) {
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                switchToNextEnemyPokemon();
                // Si ya no quedan enemigos, termina la batalla
                if (enemyPokemonIds.isEmpty()) {
                    // Aquí termina la batalla, no vuelvas a llamar a handleAttack
                    return;
                }
                // Si el nuevo enemigo tampoco puede combatir, vuelve a cambiar
                if (computerPokemonDead() || computerPokemonOutOfStamina()) {
                    // Cambia al siguiente sin recursión infinita
                    switchToNextEnemyPokemon();
                    // Si después de cambiar ya no quedan enemigos, termina la batalla
                    if (enemyPokemonIds.isEmpty()) return;
                } else {
                    computerAttack();
                }
            });
            pause.play();
        } else {
            computerAttack();
        }
    }

    @FXML
    public void fightButtonAction(ActionEvent event) {

        fightButton.setStyle("-fx-background-color: red;");
        ControllerAttackResult ctrl = (ControllerAttackResult) UtilsViews.getController("ViewAttackResult");
        //ctrl.setRun(false); // Reiniciar la variable run al hacer clic en el botón de ataque
        // AppData db = AppData.getInstance();
        // db.setRun(false);
        handleAttack(currentSelection); // Mostrar el ataque seleccionado
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event2 -> {
        fightButton.setStyle("-fx-background-color: #ffcc00; -fx-effect: dropshadow(gaussian, #ffffff, 2, 0.5, 0.0, 0.0); -fx-font-weight: bold;");
        }); // Resetear el estilo después de 1 segundo
        pause.play();
        System.out.println("Fight button clicked! Current move: " + moves[currentSelection].getText());
        // Crear una transición para simular el cooldown
        PauseTransition cooldown = new PauseTransition(Duration.seconds(1.5));

        // Durante el cooldown, reducir gradualmente la barra de vida del jugador
        cooldown.setOnFinished(event1 -> {
            updatePlayerStatus(); // Cambiar la vista después de que la vida llegue a 0
            ctrl.setRound(round);
            ctrl.setBattleId(battleId);
            UtilsViews.setViewAnimating("ViewAttackResult");
            round += 1;
        });

        // Iniciar la transición
        cooldown.play();

    }

    // public void setRun(boolean run) {
    //     this.run = run;
    // }

    @FXML
    public void runButtonAction(ActionEvent event) {
        // Cambiar el estilo del botón al hacer clic
        runButton.setStyle("-fx-background-color: blue;");
        
        // Resetear el estilo después de 1 segundo
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event2 -> {
            runButton.setStyle("-fx-background-color: #ffcc00; -fx-effect: dropshadow(gaussian, #ffffff, 2, 0.5, 0.0, 0.0); -fx-font-weight: bold;");

            ControllerAttackResult ctrl = (ControllerAttackResult) UtilsViews.getController("ViewAttackResult");
            ControllerBattleOptions ctrl2 = (ControllerBattleOptions) UtilsViews.getController("ViewBattleOptions");
            ctrl2.setBattleStatus(STATUS_BATTLE_ENDED, round);
            AppData db = AppData.getInstance();
            db.setRun(true);
            this.run = db.isRun();

            ctrl.setWinner("Computer");
            ctrl.setBattleId(battleId);
            ctrl.setFinalBattle(true); // Esto hará que se muestre BattleResult y no se sume XP
            System.out.println("Valor run: " + this.run);
            if (this.run) {
                showAlert("¡Has huido de la batalla!", AlertType.INFORMATION);
            } else {
                showAlert("¡No has podido huir de la batalla!", AlertType.INFORMATION);
            }
            // Cambiar a la vista de resultados de batalla
            UtilsViews.setView("ViewAttackResult");
        });
        pause.play();
    }

    /**
     * Método para actualizar el estado del player.
     * 
     */
    public void updatePlayerStatus() {
        // Verificar si el jugador perdió o no quedan Pokémon enemigos
        if (playerPokemonDead() || playerPokemonOutOfStamina() || !hasMoreEnemyPokemons()) {
            ControllerAttackResult ctrl = (ControllerAttackResult) UtilsViews.getController("ViewAttackResult");
            ControllerBattleOptions ctrlBattle = (ControllerBattleOptions) UtilsViews.getController("ViewBattleOptions");
            // Marca el Pokémon como muerto si se quedó sin vida o estamina
            
            if (playerPokemonDead() || playerPokemonOutOfStamina()) {
                ctrlBattle.markPokemonAsDead(idPokemon);
            }
            ControllerBattleOptions ctrlBattleOptions = (ControllerBattleOptions) UtilsViews.getController("ViewBattleOptions");
            if (ctrlBattleOptions.areAllPokemonsDead()) {
                ctrlBattleOptions.endGame(); // Finaliza el juego si todos los Pokémon están muertos
                return; // Salir del método
            }

            if (computerPokemonDead() || computerPokemonOutOfStamina()) {
                switchToNextEnemyPokemon();
                return; // Importante: salir para evitar doble cambio de vista
            }  
            // Configurar los datos para la vista de resultados
            ctrl.setHpLabel(getHpComputer());
            ctrl.setEstaminaLabel(getEstaminaComputer());
            ctrl.setRound(round);
            ctrl.setHpPlayer(getHpPlayer());
            ctrl.setEstaminaPlayer(getEstaminaPlayer());
            ctrl.setStatsLabel("Ataque: " + moves[currentSelection].getText().replace("➤", "").trim());
            ctrl.setPokemonLabel(playerPokemonLabel.getText());
            
        } else {
            // Continuar la batalla normalmente
            ControllerAttackResult ctrl = (ControllerAttackResult) UtilsViews.getController("ViewAttackResult");
            ctrl.setHpLabel(getHpComputer());
            ctrl.setEstaminaLabel(getEstaminaComputer());
            ctrl.setRound(round);
            ctrl.setEquipLabel(playerPokemonDead() ? "Computer" : "Player");
            ctrl.setHpPlayer(getHpPlayer());
            ctrl.setEstaminaPlayer(getEstaminaPlayer());
            ctrl.setStatsLabel("Ataque: " + moves[currentSelection].getText().replace("➤", "").trim());
            ctrl.setPokemonLabel(playerPokemonLabel.getText());
        }
    }

    /**
     * Método para verificar si el Pokémon atacante está muerto.
     * 
     * @return true si el Pokémon atacante tiene 0 o menos HP, de lo contrario false.
     */
    private boolean playerPokemonDead() {
        double playerHp = playerHpBar.getProgress();
        return playerHp <= 0.0;
    }

    /**
     * Método para verificar si el Pokémon atacante se quedó sin estamina.
     * 
     * @return true si el Pokémon atacante tiene 0 o menos estamina, de lo contrario false.
     */
    private boolean playerPokemonOutOfStamina() {
        double playerStamina = playerStaminaBar.getProgress();
        return playerStamina <= 0.0; // Verifica si la estamina es 0 o menos
    }

    /**
     * Método para verificar si el Pokémon enemigo está muerto.
     * 
     * @return true si el Pokémon enemigo tiene 0 o menos HP, de lo contrario false.
     */
    private boolean computerPokemonDead() {
        double computerHp = enemyHpBar.getProgress();
        return computerHp <= 0.0;
    }

    /**
     * Método para verificar si el Pokémon enemigo se quedó sin estamina.
     * 
     * @return true si el Pokémon enemigo tiene 0 o menos estamina, de lo contrario false.
     */
    private boolean computerPokemonOutOfStamina() {
        double computerStamina = enemyStaminaBar.getProgress();
        return computerStamina <= 0.0;
    }

    // Método para establecer los Pokémon enemigos
    public void setEnemyPokemons(HashMap<Integer, String> enemyPokemons, List<Integer> enemyPokemonIds) {
        this.enemyPokemons = enemyPokemons;
        this.enemyPokemonIds = enemyPokemonIds;
        // Aquí puedes agregar lógica para actualizar la interfaz con los Pokémon enemigos
        updateEnemyPokemonDisplay();
    }
    // Método para actualizar la interfaz con los Pokémon enemigos
    private void updateEnemyPokemonDisplay() {
        // Lógica para mostrar los Pokémon enemigos en la interfaz
        // Por ejemplo, puedes establecer las imágenes y nombres de los Pokémon enemigos
        if (enemyPokemonIds != null && !enemyPokemonIds.isEmpty()) {
            Integer firstEnemyId = enemyPokemonIds.get(0);
            String enemyName = enemyPokemons.get(firstEnemyId);
            if (enemyName != null) {
                // Actualiza la interfaz con el nombre y la imagen del primer Pokémon enemigo
                System.out.println("Primer Pokémon enemigo: " + enemyName);
                // Aquí iría la lógica para actualizar la interfaz gráfica
            } else {
                System.out.println("No se encontró un Pokémon enemigo con ID: " + firstEnemyId);
            }
        } else {
            System.out.println("No hay Pokémon enemigos disponibles.");
        }
    }

    public void setPlayerPokemons(List<Integer> pokemonIds) {
        ControllerBattleOptions ctrl = (ControllerBattleOptions) UtilsViews.getController("ViewBattleOptions");
        this.playerPokemonIds = pokemonIds;
        //System.out.println("IDs de Pokémon del jugador: " + playerPokemonIds);
        this.playerPokemonStatus = new HashMap<>();
        for (Integer id : pokemonIds) {
            playerPokemonStatus.put(id, true); // Todos los Pokémon comienzan vivos
        }
        ctrl.setPokemonStatus(playerPokemonStatus);
        System.out.println("Estado de los Pokémon del jugador: " + playerPokemonStatus);
    }

    /**
     * Método para que el Pokémon enemigo ataque al jugador.
     */
    public void computerAttack() {
        // Si el Pokémon enemigo está muerto o sin estamina, cambiar al siguiente
        if (computerPokemonDead() || computerPokemonOutOfStamina()) {
            switchToNextEnemyPokemon();
            return;
        }

        Random random = new Random();

        // 40% de probabilidad de fallo
        boolean attackFails = random.nextDouble() < 0.4;
        if (attackFails) {
            showAlert("¡El ataque del enemigo ha fallado!", AlertType.INFORMATION);
            return;
        }

        int enemyID = enemyPokemonIds.get(0);
        String enemyType = getPokemonTypes(enemyID).get(0); // Obtener el primer tipo del Pokémon enemigo
        String attackType = getPokemonTypes(idPokemon).get(0); // Obtener el tipo del ataque
        // Aquí puedes agregar lógica para calcular el daño basado en tipos

        this.typeEffectiveness = getTypeEffectiveness(attackType, enemyType);
        
        if (this.typeEffectiveness != null) {
            System.out.println("Efectividad del ataque: " + this.typeEffectiveness);
        } else {
            System.out.println("No se pudo calcular la efectividad del ataque.");
        }
        // Daño aleatorio entre 50 y 100
        int damage = random.nextInt(51) + 50;
        Double finalDamage = damage * this.typeEffectiveness;
        System.out.println("Daño infligido: " + finalDamage);
        // Estamina consumida proporcional al daño (puedes ajustar la fórmula)
        double staminaConsumed = damage / 2.5;

        double currentPlayerHp = playerHpBar.getProgress();
        double newPlayerHp = currentPlayerHp - (finalDamage / 100.0);
        newPlayerHp = Math.max(newPlayerHp, 0);
        playerHpBar.setProgress(newPlayerHp);
        setHpPlayer((int)(newPlayerHp * 100) + "/ 100");

        double currentEnemyStamina = enemyStaminaBar.getProgress();
        double newEnemyStamina = currentEnemyStamina - (staminaConsumed / 30.0);
        newEnemyStamina = Math.max(newEnemyStamina, 0);
        enemyStaminaBar.setProgress(newEnemyStamina);
        setEstaminaComputer((int)(newEnemyStamina * 30) + "/ 30");
    }

    /**
     * Método para cambiar al siguiente Pokémon enemigo disponible.
     */
    private void switchToNextEnemyPokemon() {
        if (enemyPokemonIds == null || enemyPokemonIds.isEmpty()) {
            showAlert("¡Todos los Pokémon enemigos han sido derrotados!", AlertType.INFORMATION);
            ControllerBattleOptions ctrl = (ControllerBattleOptions) UtilsViews.getController("ViewBattleOptions");
            ctrl.setBattleStatus(STATUS_BATTLE_ENDED, round);
            ControllerAttackResult ctrlResult = (ControllerAttackResult) UtilsViews.getController("ViewAttackResult");
            UtilsViews.setViewAnimating("ViewAttackResult");
            ctrlResult.setFinalBattle(true);
            // Aquí podrías llamar a un método para finalizar la batalla
            return;
        }

        // Eliminar el Pokémon actual de la lista (el primero)
        int defeatedPokemonId = enemyPokemonIds.remove(0);
        enemyPokemons.remove(defeatedPokemonId);

        // Verificar si quedan más Pokémon
        if (enemyPokemonIds.isEmpty()) {
             // Mostrar alerta de victoria
            showAlert("¡Has derrotado a todos los Pokémon enemigos!", AlertType.INFORMATION);

            // Cambiar a la vista de resultados
            Platform.runLater(() -> {
                ControllerAttackResult ctrlResult = (ControllerAttackResult) UtilsViews.getController("ViewAttackResult");
                ctrlResult.setRound(round);
                ctrlResult.setFinalBattle(true);
                ctrlResult.setEquipLabel("Player"); // Indica que el jugador ganó
                ctrlResult.setHpLabel(getHpComputer());
                ctrlResult.setEstaminaLabel(getEstaminaComputer());
                ctrlResult.setHpPlayer(getHpPlayer());
                ctrlResult.setEstaminaPlayer(getEstaminaPlayer());
                ctrlResult.setStatsLabel("Ataque: " + moves[currentSelection].getText().replace("➤", "").trim());
                ctrlResult.setPokemonLabel(playerPokemonLabel.getText());

                // Cambiar a la vista de resultados
                UtilsViews.setViewAnimating("ViewAttackResult");
            });
            return;
        }

        // Obtener el siguiente Pokémon
        int nextEnemyId = enemyPokemonIds.get(0);
        String nextEnemyName = enemyPokemons.get(nextEnemyId);

        // Actualizar la interfaz con el nuevo Pokémon
        setEnemyPokemonImage("/assets/pokemons/normal/" + String.format("%03d", nextEnemyId) + ".gif");
        setComputerPokemonLabel(nextEnemyName);
        
        // Resetear las barras de vida y estamina para el nuevo Pokémon
        setEnemyHpBar(1.0);
        setEnemyStaminaBar(1.0);
        setHpComputer("100/100");
        setEstaminaComputer("30/30");

        showAlert("¡El enemigo ha cambiado a " + nextEnemyName + "!", AlertType.INFORMATION);
    }

    public boolean hasMoreEnemyPokemons() {
        return enemyPokemonIds != null && !enemyPokemonIds.isEmpty();
    }

    /**
     * Método para mostrar una alerta con un mensaje específico.
     * 
     * @param message El mensaje a mostrar en la alerta.
     * @param type El tipo de alerta a mostrar.
     */
    private void showAlert(String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type == AlertType.ERROR ? "Error" : "Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Método para insertar los Pokémon de la batalla en la base de datos.
     * Este método se llama al finalizar la batalla.
     */
    public void insertBattlePokemons() {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        // Obtener el último battle_id insertado
        ArrayList<HashMap<String, Object>> result = db.query("SELECT id FROM Battle ORDER BY id DESC LIMIT 1;");
        if (result.isEmpty()) {
            System.err.println("No hay batallas registradas.");
            db.close();
            return;
        }
        
        int battleId = ((Number) result.get(0).get("id")).intValue();

        // Insertar los Pokémon del jugador
        for (Integer playerPokemonId : playerPokemonStatus.keySet()) {
            db.update("INSERT INTO BattlePokemon (battle_id, is_player, pokemon_id) VALUES (" +
                    battleId + ", 1, " + playerPokemonId + ");");
        }

        // Insertar los Pokémon enemigos
        for (Integer enemyPokemonId : enemyPokemons.keySet()) {
            db.update("INSERT INTO BattlePokemon (battle_id, is_player, pokemon_id) VALUES (" +
                    battleId + ", 0, " + enemyPokemonId + ");");
        }

        db.close();
    }

    //Pruebas tipo efectividad
    private List<String> getPokemonTypes(int pokemonId) {
        AppData db = AppData.getInstance();
        db.connect(selected_path);
        ArrayList<HashMap<String, Object>> result = db.query(
            "SELECT type FROM Pokemon WHERE id = " + pokemonId + ";"
        );
        db.close();
        List<String> types = new ArrayList<>();
        if (!result.isEmpty()) {
            String typeStr = (String) result.get(0).get("type");
            if (typeStr != null) {
                for (String t : typeStr.split("/")) {
                    types.add(t.trim());
                }
            }
        }
        return types;
    }

    private double getTypeEffectiveness(String attackType, String defenderType) {
        AppData db = AppData.getInstance();
        db.connect(selected_path);
        String query = String.format(
            "SELECT multiplier FROM typeEffectiveness WHERE attack_type = '%s' AND target_type = '%s';",
            attackType, defenderType
        );
        ArrayList<HashMap<String, Object>> result = db.query(query);
        db.close();
        if (!result.isEmpty()) {
            return ((Number) result.get(0).get("multiplier")).doubleValue();
        }
        return 1.0; //Default effectiveness
    }
    
    /**
     * Método para reiniciar el estado de la batalla.
     * Este método se llama al finalizar la batalla o al iniciar una nueva.
     */
    public void resetBattleAttackState() {
            round = 0;
            idPokemon = -1;
            playerPokemonIds = new ArrayList<>();
            playerPokemonStatus = new HashMap<>();
            enemyPokemons = new HashMap<>();
            enemyPokemonIds = new ArrayList<>();
            typeEffectiveness = 1.0;
            AppData db = AppData.getInstance();
            db.setRun(false);
            //run = false;
            // Resetea barras y labels si es necesario
            if (playerHpBar != null) playerHpBar.setProgress(1.0);
            if (playerStaminaBar != null) playerStaminaBar.setProgress(1.0);
            if (enemyHpBar != null) enemyHpBar.setProgress(1.0);
            if (enemyStaminaBar != null) enemyStaminaBar.setProgress(1.0);
    }

    /**
     * Method to apply item effects to player Pokémon.
     * @param playerPokemonIds
     */
    public void applyItemEffectsToPlayerPokemons(List<Integer> playerPokemonIds) {
       AppData db = AppData.getInstance();
        db.connect(selected_path);

        for (Integer pokemonId : playerPokemonIds) {
            // Consulta los items activos para este Pokémon
            String query = """
                SELECT i.name
                FROM ItemEffect ie
                JOIN Item i ON ie.item_id = i.id
                WHERE ie.player_pokemon_id = %d AND ie.active = 1
            """.formatted(pokemonId);

        ArrayList<HashMap<String, Object>> effects = db.query(query);

        int extraDamage = 0;
        for (HashMap<String, Object> effect : effects) {
            String itemName = (String) effect.get("name");
            if ("X_Attack".equals(itemName)) {
                extraDamage += 20;
            } else if ("Bottle_Cap".equals(itemName)) {
                extraDamage += 30;
            }
        }

        if (extraDamage > 0) {
            // Sube el daño de todos los ataques definidos para este Pokémon
            String updateDamage = """
                UPDATE Attack
                SET damage = damage + %d
                WHERE id IN (
                    SELECT attack_id FROM PokemonAttack WHERE pokemon_id = %d
                );
            """.formatted(extraDamage, pokemonId);
            db.update(updateDamage);
        }

        // Desactiva los efectos después de aplicarlos
        String query2 = """
            UPDATE ItemEffect
            SET active = 0
            WHERE player_pokemon_id = %d AND active = 1
        """.formatted(pokemonId);
        db.update(query2);
    }
    db.close();
    }

    /**
     * Method to remove effects from player Pokémon.
     * This method is called when the battle ends.
     * @param playerPokemonIds
     */
    public void putOutEffectsToPlayerPokemons(List<Integer> playerPokemonIds) {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        for (Integer pokemonId : playerPokemonIds) {
            // Consulta los items activos para este Pokémon
            String query = """
                UPDATE PlayerPokemon
                SET attack = 50, max_hp = 100, stamina = 30
                WHERE id = %d
            """.formatted(pokemonId);
            db.update(query);
        }
        db.close();
    }

}
