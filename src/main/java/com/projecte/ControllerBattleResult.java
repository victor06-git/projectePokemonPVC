package com.projecte;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.utils.UtilsViews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerBattleResult {

    public static final String STATUS_BATTLE_STARTED = "battle_started";
    public static final String STATUS_BATTLE_PREP = "battle_prep";
    public static final String STATUS_BATTLE_ENDED = "battle_ended";
    
    @FXML
    private Label labelRewards, labelWinner;
    
    @FXML
    private Button buttonContinue;

    @FXML
    private ImageView imgPokemon1, imgPokemon2, item1;

    @FXML
    private ProgressBar nivellBar;

    private int round = -1;
    private String winner;
    
    @FXML
    private void toContinue(ActionEvent event) {
        // Acción al hacer clic en el botón "Recoger recompensas"
        UtilsViews.setView("ViewMenu");
        ControllerBattleOptions ctrl = (ControllerBattleOptions) UtilsViews.getController("ViewBattleOptions");
        ctrl.setBattleStatus(STATUS_BATTLE_ENDED, round);

    }

    public void setRound(int round) {
        this.round = round;
    }

    public void setWinner(String winner) {
        this.winner = winner;
        labelWinner.setText("Ganador: " + winner); // Mostrar el ganador en la etiqueta
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
        db.connect("./data/pokemons.sqlite");

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
            db.connect("./data/pokemons.sqlite");
        
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
                        db.update("INSERT INTO ItemInventory (item_id, quantity) VALUES (" + itemId + ", 1) " +
                                  "ON CONFLICT(item_id) DO UPDATE SET quantity = quantity + 1;");
        
                        // Establecer la imagen del ítem en `item1` dependiendo del id del ítem
                        if (itemId == 3) {
                            setPokemonImage(item1, "assets/items/BottleCap.png");
                        } else if (itemId == 1) {
                            setPokemonImage(item1, "assets/items/XAttack.png");
                        } else  {
                            setPokemonImage(item1, "assets/items/XDeffense.png");
                        } 
        
                        System.out.println("Ítem desbloqueado: " + itemName);
                        anyUnlocked = true;
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
            AppData db = AppData.getInstance();
            db.connect("./data/pokemons.sqlite");
    
            // Generar un número aleatorio entre 500 y 1000
            Random random = new Random();
            int randomXP = random.nextInt(501) + 500; // 501 para incluir 1000 como límite superior
    
            // Actualizar la tabla GameStats
            db.update("UPDATE GameStats " +
                      "SET total_experience = total_experience + " + randomXP + ", " +
                      "battles_played = battles_played + 1 " +
                      "WHERE id = 1;");
    
            System.out.println("GameStats actualizado: +" + randomXP + " XP, +1 batalla jugada.");
    
            db.close();
        }


}

