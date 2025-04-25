package com.projecte;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class ControllerBattleOptions {

    @FXML 
    private Label move1, move2, move3, move4, moveDescriptionLabel;

    @FXML
    private VBox movePanel;
    
    @FXML
    private Label attackNameLabel, attackTypeLabel, attackDamageLabel, attackDescriptionLabel; // Nuevas etiquetas para la información del ataque

    private int currentSelection = 0;

    private Label[] moves;

    @FXML
    public void initialize() {
        moves = new Label[]{move1, move2, move3, move4};
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

    private void updateAttackInfo(int index ) {

        String[] names = {"Quick Attack", "Wing Attack", "Gust", "Focus Energy"};
        String[] types = {"Normal", "Flying", "Flying", "Normal"};
        String[] damages = {"40", "60", "50", "0"}; // 0 para Focus Energy ya que no causa daño
        String[] descriptions = {
            "A fast normal-type attack that always hits first.",
            "A powerful slash with wings.",
            "A gust of wind that may flinch the enemy.",
            "Raises user's critical hit chance."
        };

        // Actualiza el VBox con la información del ataque
        attackNameLabel.setText("➤ " + names[index]);
        attackTypeLabel.setText("Tipo: " + types[index]);
        attackDamageLabel.setText("Daño: " + damages[index]);
        attackDescriptionLabel.setText("Descripción: " + descriptions[index]);

    }


    private void handleAttack(int selectedMove) {

        System.out.println("Using move: " + moves[selectedMove].getText());

    }

}
