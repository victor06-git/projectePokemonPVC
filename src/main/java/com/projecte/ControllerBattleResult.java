package com.projecte;


import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;

import static com.projecte.BuildDatabase.selected_path;
import com.utils.UtilsViews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerBattleResult implements  Initializable{

    public static final String STATUS_BATTLE_STARTED = "battle_started";
    public static final String STATUS_BATTLE_PREP = "battle_prep";
    public static final String STATUS_BATTLE_ENDED = "battle_ended";
    
    @FXML
    private Label labelRewards, labelWinner, item1, item2, item3, levelLabel;
    
    @FXML
    private Button buttonContinue;

    @FXML
    private ImageView imgPokemon1, imgPokemon2, backgroundImage;

    @FXML
    private ProgressBar levelBar;

    @FXML
    private Label xpLabel;

    private int round = -1;
    private String winner;
    private int battleId;
    private boolean run;
    private int randomXP = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //updateBattleWinner(winner);
        
        URL imageURL = null;
        try {
            imageURL = getClass().getResource("/assets/result/finish.jpg");
            Image image = new Image(imageURL.toExternalForm());
            backgroundImage.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image asset: " + imageURL);
            e.printStackTrace();
        }
    }

    @FXML
    private void toContinue(ActionEvent event) {

        // Limpiar imágenes
        imgPokemon1.setImage(null);
        imgPokemon2.setImage(null);

        // Limpiar labels
        labelRewards.setText("");
        labelWinner.setText("");
        item1.setText("");
        item2.setText("");
        item3.setText("");
        levelLabel.setText("");
        xpLabel.setText("");
        
        // Acción al hacer clic en el botón "Recoger recompensas"
        ControllerAttackResult ctrl_2 = (ControllerAttackResult) UtilsViews.getController("ViewAttackResult");
        ControllerBattleOptions ctrl = (ControllerBattleOptions) UtilsViews.getController("ViewBattleOptions");
        ControllerBattleAttack ctrlAttack = (ControllerBattleAttack) UtilsViews.getController("ViewBattleAttack");
        ControllerMenu ctrlMenu = (ControllerMenu) UtilsViews.getController("ViewMenu");
        ctrl.setBattleStatus(STATUS_BATTLE_ENDED, round);
        ctrl.setBattleStatus(STATUS_BATTLE_PREP, round = 1);
        ctrl.resetBattleState();
        ctrlAttack.resetBattleAttackState();
        setRun(false);
        ctrl_2.setFinalBattle(false);
        UtilsViews.setView("ViewMenu");
        ctrlMenu.loadGameStats();

    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setBattleId(int battleId) {
        this.battleId = battleId;
    }

    public void setRun(boolean run) {
        if (!run) {
            buttonContinue.setText("Recoger recompensas");
            this.run = run;
        } else if (run && winner.equals("Computer")) {
            buttonContinue.setText("Te quedas sin nada por huir");
            this.run = run;
        }
    }

    public void setWinner(String winner) {
        this.winner = winner;
        updateBattleWinner(winner); // <-- Actualiza el ganador en la tabla
        labelWinner.setText(winner); // Mostrar el ganador en la etiqueta
    }

    /**
     * Establece la imagen de un Pokémon en un ImageView.
     *
     * @param imageView El ImageView donde se establecerá la imagen.
     * @param iconPath  La ruta del icono del Pokémon (relativa a assets/poke-icons).
     */
    @FXML
    private void setPokemonImage(ImageView imageView, String iconPath) {
        try {
            String imagePath = "assets/poke-icons/" + iconPath;
            System.out.println("Cargando imagen desde: " + imagePath);
            java.io.InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            if (resourceStream == null) {
                throw new NullPointerException("Recurso no encontrado: " + imagePath);
            }

            Image image = new Image(resourceStream);
            imageView.setImage(image);
            System.out.println("Imagen establecida correctamente: " + imagePath);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error al cargar la imagen: " + iconPath);
            e.printStackTrace();
        }
    }

    /**
     * Selecciona dos Pokémon aleatorios que no estén desbloqueados (isUnlocked = 0)
     * y actualiza la tabla PlayerPokemon para marcarlos como desbloqueados (isUnlocked = 1).
     *
     * @return Una lista con los dos Pokémon seleccionados.
     */
    public ArrayList<HashMap<String, Object>> unlockTwoRandomPokemons() {

        AppData db = AppData.getInstance();
        db.connect(selected_path);

        // Consulta para obtener todos los Pokémon no desbloqueados
        ArrayList<java.util.HashMap<String, Object>> lockedPokemons = db.query(
            "SELECT p.id, p.name, p.type, p.icon_path " +
            "FROM Pokemon p " +
            "INNER JOIN PlayerPokemon pp ON p.id = pp.pokemon_id " +
            "WHERE pp.unlocked = 0;"
        );

        ArrayList<HashMap<String, Object>> selectedPokemons = new ArrayList<>();
        Random random = new Random();

        if (lockedPokemons.size() >= 2) {
            // Seleccionar dos Pokémon aleatorios
            int firstIndex = random.nextInt(lockedPokemons.size());
            HashMap<String, Object> firstPokemon = lockedPokemons.get(firstIndex);
            selectedPokemons.add(firstPokemon);

            int secondIndex;
            do {
                secondIndex = random.nextInt(lockedPokemons.size());
            } while (secondIndex == firstIndex); // Asegurarse de que no sea el mismo Pokémon

            HashMap<String, Object> secondPokemon = lockedPokemons.get(secondIndex);
            selectedPokemons.add(secondPokemon);

            // Actualizar la tabla PlayerPokemon para marcar los Pokémon seleccionados como desbloqueados
            db.update("UPDATE PlayerPokemon SET unlocked = 1 WHERE pokemon_id = " + firstPokemon.get("id") + ";");
            db.update("UPDATE PlayerPokemon SET unlocked = 1 WHERE pokemon_id = " + secondPokemon.get("id") + ";");

            setPokemonImage(imgPokemon1, (String) firstPokemon.get("icon_path"));
            setPokemonImage(imgPokemon2, (String) secondPokemon.get("icon_path"));

            System.out.println("Pokémon desbloqueados: " + firstPokemon.get("name") + " y " + secondPokemon.get("name"));
        } else {
            System.err.println("No hay suficientes Pokémon no desbloqueados para seleccionar dos.");
        }

        db.close();
        return selectedPokemons;
        }
        
        /**
         * Desbloquea un ítem aleatorio y lo añade al inventario del jugador.
         * 
         * @return El ítem desbloqueado.
         */
        public void unlockRandomItem() {

            AppData db = AppData.getInstance();
            db.connect(selected_path);
        
            Random random = new Random();
        
            // Obtener todos los ítems con su id, nombre e icon_path
            ArrayList<HashMap<String, Object>> items = db.query(
                "SELECT id, name FROM Item;"
            );
        
            if (!items.isEmpty()) {
                boolean anyUnlocked = false;
        
                for (HashMap<String, Object> item : items) {
                    int itemId = (int) item.get("id");
                    String itemName = (String) item.get("name");
        
                    // Para cada item, 50% de probabilidad de desbloquearlo
                    if (random.nextBoolean()) {
                        // Actualizar el inventario de ítems
                        db.update("UPDATE ItemInventory SET quantity = quantity + 1 WHERE item_id = " + itemId + ";");

                        if (itemName.equalsIgnoreCase("X_Attack")) {
                            item1.setText("XAttack + 1");
                        } else if (itemName.equalsIgnoreCase("X_Defense") || itemName.equalsIgnoreCase("XDefense")) {
                            item2.setText("XDefense + 1");
                        } else if (itemName.equalsIgnoreCase("Bottle_Cap") || itemName.equalsIgnoreCase("Bottle_Cap")) {
                            item3.setText("BottleCap + 1");
                        } else {
                            item1.setText("XAttack + 0");
                            item2.setText("XDefense + 0");
                            item3.setText("BottleCap + 0");
                        }
            
                        System.out.println("Ítem desbloqueado: " + itemName);
                        anyUnlocked = true;
                        break;
                    }
                }
        
                if (!anyUnlocked) {
                    System.out.println("No se desbloqueó ningún ítem tras evaluar la probabilidad para cada uno.");
                }
            } else {
                System.err.println("No hay ítems disponibles en la tabla Item.");
            }
        
            db.close();
        }        

        public void updateGameStatsWithRandomXP() {

            if (run){
                //Hazme la query de la tabla GameStats para conseguir el nivel actual
                AppData db = AppData.getInstance();
                db.connect(selected_path);
                ArrayList<HashMap<String, Object>> stats = db.query(
                    "SELECT total_experience FROM GameStats WHERE id = 1"
                );
                if (!stats.isEmpty()) {
                    int totalXP = ((Number) stats.get(0).get("total_experience")).intValue();
                    setLevelProgressBar(totalXP);
                }

                xpLabel.setText("Sin XP");
                System.out.println(run);
                return;
            } //No consigue nada si ha huido

            // Conectar a la base de datos
            AppData db = AppData.getInstance();
            db.connect(selected_path);

            // Generar un número aleatorio entre 500 y 1000
            Random random = new Random();
            
            this.randomXP = random.nextInt(501) + 500; // 501 para incluir 1000 como límite superior
            
             
            // Obtener el último resultado de la tabla Battle
            ArrayList<HashMap<String, Object>> battles = db.query(
                "SELECT winner FROM Battle ORDER BY id DESC LIMIT 2"
            );

            // Leer la racha actual y máxima
            ArrayList<HashMap<String, Object>> stats = db.query(
                "SELECT current_win_streak, max_win_streak FROM GameStats WHERE id = 1"
            );

            ArrayList<HashMap<String, Object>> exp = db.query(
                "SELECT total_experience FROM GameStats WHERE id = 1"
            );
            if (!stats.isEmpty() && !run) {
                int totalXP = ((Number) exp.get(0).get("total_experience")).intValue();
                setLevelProgressBar(totalXP);
            } else {
                int totalXP = ((Number) exp.get(0).get("total_experience")).intValue();
                setLevelProgressBar(totalXP);
            }

            int currentStreak = 0;
            int maxStreak = 0;
            if (!stats.isEmpty() && !run) {
                currentStreak = ((Number) stats.get(0).get("current_win_streak")).intValue();
                maxStreak = ((Number) stats.get(0).get("max_win_streak")).intValue();
            }

            String lastWinner = null;
            String prevWinner = null;
            if (!battles.isEmpty()) {
                lastWinner = (String) battles.get(0).get("winner");
                if (battles.size() > 1) {
                    prevWinner = (String) battles.get(1).get("winner");
                }
            }

            // Lógica de racha
            if ("Player".equals(lastWinner)) {
                if ("Player".equals(prevWinner)) {
                    currentStreak += 1;
                } else {
                    currentStreak = 1; // Si antes era Computer o no hay anterior, empieza nueva racha
                }
                if (currentStreak > maxStreak) {
                    maxStreak = currentStreak;
                }
            } else {
                currentStreak = 0; // Si pierde, se resetea la racha
            }

            //Conseguir el valor de battles_played con un count en Battle
            ArrayList<HashMap<String, Object>> battlesPlayed = db.query(
                "SELECT COUNT(*) AS battles_played FROM Battle;"
            );
            int battlesCount = 0;
            if (!battlesPlayed.isEmpty()) {
                battlesCount = ((Number) battlesPlayed.get(0).get("battles_played")).intValue();
            }
            // Si no hay batallas, se inicializa a 0
            if (battlesCount == 0) {
                battlesCount = 0;
            }

            // Actualizar la tabla GameStats
            db.update("UPDATE GameStats " +
                    "SET total_experience = total_experience + " + this.randomXP + ", " +
                    "battles_played = " + (battlesCount + 1) + ", " +
                    "current_win_streak = " + currentStreak + ", " +
                    "max_win_streak = " + maxStreak + " " +
                    "WHERE id = 1;");

            System.out.println("GameStats actualizado: +" + this.randomXP + " XP, +1 batalla jugada, racha actual: " + currentStreak + ", racha máxima: " + maxStreak);

            db.close();
        }

        public void setLevelProgressBar(int totalExperience) {
            // Cada 1000 XP es un nivel
            int level = (totalExperience / 1000) % 1000; // Si llega a 999, vuelve a 0
            int xpInLevel = totalExperience % 1000;
            double progress = xpInLevel / 1000.0; // Valor entre 0 y 1

            levelBar.setProgress(progress);
            levelLabel.setText("Nivel: " + level);            
        }

        public void updateBattleWinner(String winner) {
            AppData db = AppData.getInstance();
            db.connect(selected_path);

            // Obtener el id de la última batalla registrada
            // if (run){
            //     winner = "Computer";
            // } else {
            //     winner = "Player";
            // }
            ArrayList<HashMap<String, Object>> result = db.query("SELECT id FROM Battle ORDER BY id DESC LIMIT 1;");
            if (!result.isEmpty()) {
                int lastBattleId = ((Number) result.get(0).get("id")).intValue();
                String update = String.format(
                    "UPDATE Battle SET winner = '%s' WHERE id = %d;",
                    winner, lastBattleId
                );
                db.update(update);
            }

            db.close();
        }

        public int getCurrentLevelFromDB() {
            AppData db = AppData.getInstance();
            db.connect(selected_path);

            int level = 0;
            ArrayList<HashMap<String, Object>> result = db.query(
                "SELECT total_experience FROM GameStats WHERE id = 1"
            );
            if (!result.isEmpty()) {
                int totalXP = ((Number) result.get(0).get("total_experience")).intValue();
                level = totalXP / 1000; // Cada 1000 XP es un nivel
            }

            db.close();
            return level;
        }

}

