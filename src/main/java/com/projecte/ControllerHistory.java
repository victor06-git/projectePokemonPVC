package com.projecte;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;

public class ControllerHistory extends BuildDatabase implements Initializable {
    @FXML
    private VBox historyList;

    @FXML
    private ImageView imgBackArrow;

    private String imgPlayer1, imgPlayer2, imgPlayer3;
    private String imgCPU1, imgCPU2, imgCPU3;
    
    @FXML
    public void initialize(URL url, ResourceBundle rb) {

        // Insertar Pokémon con id 1, 4 y 7 en la tabla PlayerPokemon si no existen
        AppData db = AppData.getInstance();
        db.connect(selected_path);
        
        Path imagePath = null;
        try {
            URL imageURL = getClass().getResource("/assets/image/arrow-back.gif");
            Image image = new Image(imageURL.toExternalForm());
            imgBackArrow.setImage(image);

            loadHistory();

        } catch (Exception e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }
        db.close();
    }

    @FXML
    private void loadHistory() throws IOException {
        // Cargar la historia desde la base de datos
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        URL resource = this.getClass().getResource("/assets/miniHistoryView.fxml");


        String query = "SELECT * FROM Pokemon p " +
            "JOIN PlayerPokemon pp ON p.id = pp.pokemon_id " +
            "JOIN BattlePokemon bp ON pp.pokemon_id = bp.pokemon_id " +
            "JOIN Battle b ON bp.battle_id = b.id ";

        ArrayList<HashMap<String, Object>> history = db.query(query);

        System.out.println("History: " + history);

        historyList.getChildren().clear(); // Limpiar la lista de historia antes de cargar nuevos elementos
        
        List<String> pokeImgs = new ArrayList<>();

        for (HashMap<String, Object> row : history) {
            String icon_path = (String) row.get("icon_path");
            String date = (String) row.get("date");
            String map = (String) row.get("map");
            String winner = (String) row.get("winner");
            Integer is_player = (Integer) row.get("is_player");

            pokeImgs.add(icon_path);
        }


            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerMiniHistory ctrl = loader.getController(); 

            ctrl.setBattleInfo(date, map, winner);

            for (int i = 0; i < pokeImgs.size(); i++) {
                String icon_path = pokeImgs.get(i);
                ctrl.setPokeCPU(icon_path, imgPokemon+(i+1));
            }
            
            historyList.getChildren().add(itemTemplate);

        


        System.out.println("History: " + history);

        db.close();
    }

    public void toViewMenu() {
        UtilsViews.setViewAnimating("ViewMenu");
    }


}
