package com.projecte;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;

public class ControllerMiniHistory extends BuildDatabase  {

    @FXML
    private ImageView imgPokemon1 = null;
    @FXML
    private ImageView imgPokemon2 = null;
    @FXML
    private ImageView imgPokemon3 = null;
    @FXML
    private ImageView imgPokemon4 = null;
    @FXML
    private ImageView imgPokemon5 = null;
    @FXML
    private ImageView imgPokemon6 = null;

    @FXML
    private ImageView imgWin = null;

    @FXML
    private Label dateLabel = null;
    
    @FXML
    private Label mapLabel = null;



    public void setPokePlayer(String icon_name) {
        String path = "/assets/poke-icons/" + icon_name.toLowerCase().trim();
        imgPokemon1.setImage(new Image(getClass().getResourceAsStream(path)));
        imgPokemon2.setImage(new Image(getClass().getResourceAsStream(path)));
        imgPokemon3.setImage(new Image(getClass().getResourceAsStream(path)));
    }

   
    public void setPokeCPU(String icon_name, ImageView img) {
        String path = "/assets/poke-icons/" + icon_name.toLowerCase().trim();
        img.setImage(new Image(getClass().getResourceAsStream(path)));
       }   


    public void setBattleInfo(String date, String map, String winner) {
        dateLabel.setText(date);
        mapLabel.setText(map);
        switch (winner.toLowerCase().trim()) {
            case "player" -> imgWin.setImage(new Image(getClass().getResourceAsStream("/assets/image/winner.png")));
            case "computer" -> imgWin.setImage(new Image(getClass().getResourceAsStream("/assets/image/defeat.png")));
            default -> imgWin.setImage(new Image(getClass().getResourceAsStream("/assets/image/defeat.png")));
        }
    }


}
