package com.projecte;

import com.utils.UtilsViews;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;


public class ControllerBattleOptions {

    @FXML
    private VBox mapListPanel, teamPanel;

    @FXML
    private ChoiceBox<String> choicePokemon1, choicePokemon2, choicePokemon3;

    @FXML
    private ImageView imgSelectedMap;

    @FXML
    private Label labelSelectedMap ;

    @FXML
    private javafx.scene.control.Button startButton;

    private int currentMapSelection = 0;
    private Label[] maps;

   
    @FXML
    private void initialize() {

    }

    public VBox getMapListPanel() {
        return mapListPanel;
    }

    public void setMapListPanel(VBox mapListPanel) {
        this.mapListPanel = mapListPanel;
    }

    public VBox getTeamPanel() {
        return teamPanel;
    }

    public void setTeamPanel(VBox teamPanel) {
        this.teamPanel = teamPanel;
    }

    public ChoiceBox<String> getChoicePokemon1() {
        return choicePokemon1;
    }

    public void setChoicePokemon1(ChoiceBox<String> choicePokemon1) {
        this.choicePokemon1 = choicePokemon1;
    }

    public ChoiceBox<String> getChoicePokemon2() {
        return choicePokemon2;
    }

    public void setChoicePokemon2(ChoiceBox<String> choicePokemon2) {
        this.choicePokemon2 = choicePokemon2;
    }

    public ChoiceBox<String> getChoicePokemon3() {
        return choicePokemon3;
    }

    public void setChoicePokemon3(ChoiceBox<String> choicePokemon3) {
        this.choicePokemon3 = choicePokemon3;
    }

    public ImageView getImgSelectedMap() {
        return imgSelectedMap;
    }

    public void setImgSelectedMap(ImageView imgSelectedMap) {
        this.imgSelectedMap = imgSelectedMap;
    }

    public Label getLabelSelectedMap() {
        return labelSelectedMap;
    }

    public void setLabelSelectedMap(Label labelSelectedMap) {
        this.labelSelectedMap = labelSelectedMap;
    }

    public javafx.scene.control.Button getStartButton() {
        return startButton;
    }

    public void setStartButton(javafx.scene.control.Button startButton) {
        this.startButton = startButton;
    }

    public int getCurrentMapSelection() {
        return currentMapSelection;
    }

    public void setCurrentMapSelection(int currentMapSelection) {
        this.currentMapSelection = currentMapSelection;
    }

    public Label[] getMaps() {
        return maps;
    }

    public void setMaps(Label[] maps) {
        this.maps = maps;
    }

    //Funció per canviar de vista
    @FXML
    public void toViewBattle(MouseEvent event) {
        ControllerBattleAttack ctrl = (ControllerBattleAttack) UtilsViews.getController("ViewBattleAttack");
        ctrl.setMap("data/mapa/mapa2.jpg");
        ctrl.setEnemyPokemonImage("data/pokemons/Hitmonchan.gif");
        ctrl.setPlayerPokemonImage("data/pokemons/Mew.gif");
        UtilsViews.setViewAnimating("ViewBattleAttack");
    }
    
    

}
