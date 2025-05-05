package com.projecte;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;


public class ControllerBattleResult {

    @FXML
    private Button btnMenu = new Button();

    @FXML
    private Label LabelNivel;

    @FXML
    private Label LabelGanador, LaNivel, LabelNpokemons, LabelItems;

    @FXML
    private TextField NomGanador, LevelActual, levelAumentat, NewsPokemons = new TextField();
    
    @FXML
    private TextArea itemsRecogidos = new TextArea();

    @FXML
    private ProgressBar BarraProg = new ProgressBar();

    // Método que se llama al inicializar el controlador
    @FXML
    public void initialize() {
     
    }
    
    
}
