package com.projecte;

import com.utils.UtilsViews;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    final int WINDOW_WIDTH = 600;
    final int WINDOW_HEIGHT = 450;
    final int MIN_WIDTH = 600;
    final int MIN_HEIGHT = 400;

    @Override
    public void start(Stage stage) throws Exception {

        // Carga vistas
        UtilsViews.parentContainer.setStyle("-fx-font: 14 arial;");
        UtilsViews.addView(getClass(), "ViewStart", "/assets/viewStart.fxml");
<<<<<<< HEAD
        UtilsViews.addView(getClass(), "ViewMenu", "/assets/viewMenu.fxml");


=======
        UtilsViews.addView(getClass(), "ViewPokeSettings", "/assets/viewPokeSettings.fxml");
        UtilsViews.addView(getClass(), "ViewBattleAttack", "/assets/viewBattleAttack.fxml");
        UtilsViews.addView(getClass(), "pokemonView", "/assets/pokemonView.fxml");
        UtilsViews.addView(getClass(), "ViewBattleOptions", "/assets/viewBattleOptions.fxml");

        UtilsViews.setView("ViewBattleAttack");

        // Mostrar la finestra
>>>>>>> 223cae3f978c513d783b46f6a7a3f769027d549b
        Scene scene = new Scene(UtilsViews.parentContainer);
        stage.setScene(scene);
        stage.setTitle("Pokémons PvP");
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.show();

        // Icono
        if (!System.getProperty("os.name").contains("Mac")) {
            Image icon = new Image("file:icons/icon.png");
            stage.getIcons().add(icon);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
