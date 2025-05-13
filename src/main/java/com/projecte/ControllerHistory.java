package com.projecte;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;

public class ControllerHistory extends BuildDatabase implements Initializable {
    @FXML
    private VBox historyList;

    @FXML
    private ImageView imgBackArrow;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        try {
            URL imageURL = getClass().getResource("/assets/image/arrow-back.gif");
            Image image = new Image(imageURL.toExternalForm());
            imgBackArrow.setImage(image);

            //loadHistory();
        } catch (Exception e) {
            System.err.println("Error loading image asset.");
            e.printStackTrace();
        } 
        
    }

    @FXML
    private void loadHistory() throws IOException {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        URL resource = this.getClass().getResource("/assets/miniHistoryView.fxml");

        String query = "SELECT b.id as battle_id, b.date, b.map, b.winner, " +
                       "p.icon_path, bp.is_player " +
                       "FROM Battle b " +
                       "JOIN BattlePokemon bp ON b.id = bp.battle_id " +
                       "JOIN Pokemon p ON bp.pokemon_id = p.id " +
                       "ORDER BY b.date DESC";

        ArrayList<HashMap<String, Object>> history = db.query(query);
        System.out.println("Raw history data: " + history);

        // Usamos LinkedHashMap para mantener orden de inserción (orden de fecha)
        Map<Integer, BattleRecord> battleMap = new LinkedHashMap<>();

        for (HashMap<String, Object> row : history) {
            Integer battleId = (Integer) row.get("battle_id");
            String date = (String) row.get("date");
            String map = (String) row.get("map");
            String winner = (String) row.get("winner");
            String iconPath = (String) row.get("icon_path");
            Integer isPlayer = (Integer) row.get("is_player");

            BattleRecord battle = battleMap.get(battleId);
            if (battle == null) {
                battle = new BattleRecord(date, map, winner);
                battleMap.put(battleId, battle);
            }

            if (isPlayer != null && isPlayer == 1) {
                if (battle.playerPokemonImages.size() < 3) {
                    battle.playerPokemonImages.add(iconPath);
                }
            } else {
                if (battle.cpuPokemonImages.size() < 3) {
                    battle.cpuPokemonImages.add(iconPath);
                }
            }
        }

        historyList.getChildren().clear();

        for (BattleRecord battle : battleMap.values()) {
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerMiniHistory ctrl = loader.getController();

            ctrl.setBattleInfo(battle.date, battle.map, battle.winner);

            // Asignar imágenes Pokémon jugador
            for (int i = 0; i < 3; i++) {
                if (battle.playerPokemonImages.size() > i) {
                    ctrl.setPlayerPokemonImage(i, battle.playerPokemonImages.get(i));
                } else {
                    ctrl.setPlayerPokemonImage(i, "default.png");
                }
            }

            // Asignar imágenes Pokémon CPU
            for (int i = 0; i < 3; i++) {
                if (battle.cpuPokemonImages.size() > i) {
                    ctrl.setCpuPokemonImage(i, battle.cpuPokemonImages.get(i));
                } else {
                    ctrl.setCpuPokemonImage(i, "default.png");
                }
            }

            historyList.getChildren().add(itemTemplate);
        }

        db.close();
    }

    public void toViewMenu() {
        UtilsViews.setViewAnimating("ViewMenu");
    }

    private static class BattleRecord {
        String date;
        String map;
        String winner;
        List<String> playerPokemonImages = new ArrayList<>();
        List<String> cpuPokemonImages = new ArrayList<>();

        BattleRecord(String date, String map, String winner) {
            this.date = date;
            this.map = map;
            this.winner = winner;
        }
    }
}
