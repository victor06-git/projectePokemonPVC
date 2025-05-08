package com.projecte;

import java.net.URL;
import java.nio.file.Path;
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
        UtilsViews.setViewAnimating("ViewStart");
    }

    //Funció per canviar de vista
    @FXML
    public void toViewBattle(MouseEvent event) {
        ControllerBattleAttack ctrl = (ControllerBattleAttack) UtilsViews.getController("ViewBattleAttack");
        ctrl.setEnemyHpBar(0.6);
        ctrl.setMap("/assets/mapa/mapa2.jpg");
        ctrl.setEstaminaComputer("100/200");
        ctrl.setEstaminaPlayer("200/20");
        ctrl.setHpPlayer("50/50");
        ctrl.setHpComputer("100/50");
        ctrl.setEnemyPokemonImage("/assets/pokemons/normal/005.gif");
        ctrl.setPlayerPokemonImage("/assets/pokemons/back/020.gif");
        ctrl.setEnemyStaminaBar(0.5);
        ctrl.setPlayerHpBar(1.0);
        ctrl.setPlayerStaminaBar(1.0);
        UtilsViews.setViewAnimating("ViewBattleAttack");
    }

   
}
