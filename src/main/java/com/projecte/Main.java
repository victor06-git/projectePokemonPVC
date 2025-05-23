package com.projecte;

import com.utils.UtilsViews;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {


    final int WINDOW_WIDTH = 1920;
    final int WINDOW_HEIGHT = 1080;
    final int MIN_WIDTH = 600;
    final int MIN_HEIGHT = 400;

    @Override
    public void start(Stage stage) throws Exception {


        // Carga vistas
        UtilsViews.parentContainer.setStyle("-fx-font: 14 arial;");
        UtilsViews.addView(getClass(), "ViewStart", "/assets/viewStart.fxml");
        UtilsViews.addView(getClass(), "ViewPokeSettings", "/assets/viewPokeSettings.fxml");
        UtilsViews.addView(getClass(), "ViewBattleAttack", "/assets/viewBattleAttack.fxml");
        UtilsViews.addView(getClass(), "ViewBattleOptions", "/assets/viewBattleOptions.fxml");
        UtilsViews.addView(getClass(), "ViewMenu", "/assets/viewMenu.fxml");
        UtilsViews.addView(getClass(), "ViewAttackResult", "/assets/viewAttackResult.fxml");
        UtilsViews.addView(getClass(), "ViewManagement", "/assets/viewManagement.fxml");
        UtilsViews.addView(getClass(), "ViewBattleResult", "/assets/viewBattleResult.fxml");
        UtilsViews.addView(getClass(), "ViewHistory", "/assets/viewHistory.fxml");

        UtilsViews.setView("ViewStart"); // Vista inicial

        //ControllerManagement ctrl = (ControllerManagement) UtilsViews.getController("ViewManagement");
        //int number = 1; //Número del Pokemon a cargar
        //ctrl.loadPokemon(number); //Definir el número del Pokemon para la obtención de los datos en la vista PokeCard
        
        // Mostrar la finestra
        Scene scene = new Scene(UtilsViews.parentContainer);
        stage.setScene(scene);
        stage.setTitle("Pokémon: Rivalidad Virtual");
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

    @Override
    public void stop() throws Exception {
        AppData db = AppData.getInstance();
        db.close();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
