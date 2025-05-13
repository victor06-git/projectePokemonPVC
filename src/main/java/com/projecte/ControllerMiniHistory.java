package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerMiniHistory extends BuildDatabase {

    @FXML
    private ImageView imgPokemon1; // Jugador 1
    @FXML
    private ImageView imgPokemon2; // Jugador 2
    @FXML
    private ImageView imgPokemon3; // Jugador 3

    @FXML
    private ImageView imgPokemon4; // CPU 1
    @FXML
    private ImageView imgPokemon5; // CPU 2
    @FXML
    private ImageView imgPokemon6; // CPU 3

    @FXML
    private ImageView imgWin;

    @FXML
    private Label dateLabel;

    @FXML
    private Label mapLabel;

    /**
     * Asigna la imagen de un Pokémon del jugador en la posición indicada.
     * @param index Posición entre 0 y 2.
     * @param iconName Nombre del archivo de la imagen (sin path).
     */
    public void setPlayerPokemonImage(int index, String iconName) {
        Image image = loadPokemonImage(iconName);
        switch (index) {
            case 0 -> imgPokemon1.setImage(image);
            case 1 -> imgPokemon2.setImage(image);
            case 2 -> imgPokemon3.setImage(image);
            default -> throw new IllegalArgumentException("Índice fuera de rango para Pokémon jugador: " + index);
        }
    }

    /**
     * Asigna la imagen de un Pokémon de la CPU en la posición indicada.
     * @param index Posición entre 0 y 2.
     * @param iconName Nombre del archivo de la imagen (sin path).
     */
    public void setCpuPokemonImage(int index, String iconName) {
        Image image = loadPokemonImage(iconName);
        switch (index) {
            case 0 -> imgPokemon4.setImage(image);
            case 1 -> imgPokemon5.setImage(image);
            case 2 -> imgPokemon6.setImage(image);
            default -> throw new IllegalArgumentException("Índice fuera de rango para Pokémon CPU: " + index);
        }
    }

    /**
     * Carga la imagen de un Pokémon dado su nombre de archivo.
     * @param iconName Nombre del archivo de la imagen.
     * @return Imagen cargada.
     */
    private Image loadPokemonImage(String iconName) {
        String path = "/assets/poke-icons/" + iconName.toLowerCase().trim();
        try {
            return new Image(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + path + ", se carga imagen por defecto.");
            return new Image(getClass().getResourceAsStream("/assets/poke-icons/default.png"));
        }
    }

    /**
     * Establece la información de la batalla en las etiquetas y la imagen de ganador.
     * @param date Fecha de la batalla.
     * @param map Mapa donde ocurrió la batalla.
     * @param winner Ganador ("player", "computer", etc).
     */
    public void setBattleInfo(String date, String map, String winner) {
        dateLabel.setText(date);
        mapLabel.setText(map);

        String winnerImagePath;
        if ("player".equalsIgnoreCase(winner.trim())) {
            winnerImagePath = "/assets/image/winner.png";
        } else if ("computer".equalsIgnoreCase(winner.trim())) {
            winnerImagePath = "/assets/image/defeat.png";
        } else {
            winnerImagePath = "/assets/image/defeat.png";
        }

        try {
            imgWin.setImage(new Image(getClass().getResourceAsStream(winnerImagePath)));
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen del ganador: " + winnerImagePath);
        }
    }
}
