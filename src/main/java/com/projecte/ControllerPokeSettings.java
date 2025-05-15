package com.projecte;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import static com.projecte.BuildDatabase.selected_path;
import com.utils.UtilsViews;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ControllerPokeSettings {

    @FXML
    private Label labelName, numberAttack, numberDeffense, numberBottleCap;

    @FXML
    private TextField nicknameText;

    @FXML
    private ImageView attackImage, deffenseImage, bottlecapImage, imgPokemon, imgBackArrow;

    @FXML
    private Button buttonAttack, buttonDeffense, buttonBottle;

    private int currentPokemonId;

    @FXML
    public void setCurrentPokemonId(int id) {
        this.currentPokemonId = id;
    }
    
    // Método que se llama al inicializar el controlador
    @FXML
    public void initialize() {

        Path imagePath = null;
        try {
            URL imageURL = getClass().getResource("/assets/image/arrow-back.gif");
            Image image = new Image(imageURL.toExternalForm());
            imgBackArrow.setImage(image);
        } catch (Exception e) {
            System.err.println("Error loading image asset: " + imagePath);
            e.printStackTrace();
        }
        // Establecer las imágenes en los ImageView
        setImatge(attackImage, "XAttack.png"); // Ruta de la imagen de ataque
        setImatge(deffenseImage, "XDeffense.png"); // Ruta de la imagen de defensa
        setImatge(bottlecapImage, "BottleCap.png"); // Ruta de la imagen de tapa de botella
        setLabelAttack();
        setLabelDeffense();
        setLabelBottleCap();
    }

    @FXML
    public void goBack(MouseEvent event) {
        ControllerManagement ctrl2 = (ControllerManagement) UtilsViews.getController("ViewManagement");
        ctrl2.setLabelNickname(nicknameText.getText());
        ctrl2.setLabelName(labelName.getText());
        ctrl2.setImagePokemon(imgPokemon.getImage());
        UtilsViews.setViewAnimating("ViewManagement");
    }

    // Mostra imatge pokemon
    private void setImatge(ImageView imageView, String imagePath) {
        try {
            String imatge = "assets/items/" + imagePath;
            java.io.InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(imatge);
            if (resourceStream == null) {
                throw new NullPointerException("Recurso no encontrado: " + imagePath);
            }

            Image image = new Image(resourceStream);
            imageView.setImage(image);

        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error cargando el recurso: " + imagePath);
            e.printStackTrace();
        }
    }

    @FXML
    public void setLabelName(String name) {
        labelName.setText(name);
    }

    @FXML
    public void setLabelNickname(String nickname) {
        nicknameText.setText(nickname);
    }

    @FXML
    public void setImagePokemon(Image image) {
        try {
            if (image == null) {
                throw new NullPointerException("La imagen proporcionada es nula.");
            }
            imgPokemon.setImage(image);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println("Error al establecer la imagen del Pokémon.");
            e.printStackTrace();
        }
    }

    @FXML
    public void setLabelAttack() {
        int quantity = getItemQuantityByName("X_Attack");
        numberAttack.setText(String.valueOf(quantity));
    }

    @FXML
    public void setLabelDeffense() {
        int quantity = getItemQuantityByName("X_Defense");
        numberDeffense.setText(String.valueOf(quantity));
    }

    @FXML
    public void setLabelBottleCap() {
        int quantity = getItemQuantityByName("Bottle_Cap");
        numberBottleCap.setText(String.valueOf(quantity));
    }

    private int getItemQuantityByName(String itemName) {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        String query = """
            SELECT COALESCE(ii.quantity, 0) as quantity
            FROM Item i
            LEFT JOIN ItemInventory ii ON i.id = ii.item_id
            WHERE i.name = '%s';
        """.formatted(itemName);

        ArrayList<HashMap<String, Object>> result = db.query(query);
        db.close();

        if (!result.isEmpty()) {
            return ((Number) result.get(0).get("quantity")).intValue();
        }
        return 0;
    }

    @FXML
    private void updatePokemon(ActionEvent event) {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        // Obtener el nuevo nickname desde el TextField
        String newNickname = nicknameText.getText();

        // Validar que el nickname no esté vacío
        if (newNickname == null || newNickname.trim().isEmpty()) {
            showAlert("Error", "El nickname no puede estar vacío.", Alert.AlertType.ERROR);
            return;
        }

        // Extraer el ID del Pokémon desde el labelName
        String labelText = labelName.getText(); // Ejemplo: "#25 Pikachu"
        if (labelText == null || !labelText.startsWith("#")) {
            showAlert("Error", "El formato del labelName no es válido.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Extraer el ID quitando el '#' y tomando la parte numérica
            int currentPokemonId = Integer.parseInt(labelText.substring(1).split(" ")[0]);

            // Realizar la actualización en la base de datos
            db.update(String.format(
                "UPDATE PlayerPokemon SET nickname = '%s' WHERE pokemon_id = %d",
                newNickname, currentPokemonId
            ));

            // Mostrar mensaje de confirmación
            showAlert("Actualización exitosa", "El nickname se ha actualizado correctamente.", Alert.AlertType.CONFIRMATION);

        } catch (NumberFormatException e) {
            showAlert("Error", "No se pudo extraer un ID válido del labelName.", Alert.AlertType.ERROR);
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
    }

    

    @FXML
    private void setAttack(ActionEvent event) {
        // Lógica para manejar el uso del ítem de ataque
        int quantity = getItemQuantityByName("X_Attack");
        if (quantity > 0) {
            System.out.println("Usando X_Attack. Cantidad restante: " + (quantity - 1));
            updateItemQuantity("X_Attack", quantity - 1);
            setLabelAttack(); // Actualizar la etiqueta de cantidad
            addItemEffectToPokemon(currentPokemonId, "X_Attack");
        } else {
            showAlert("Error", "No tienes suficientes X_Attack.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void setDeffense(ActionEvent event) {
        // Lógica para manejar el uso del ítem de defensa
        int quantity = getItemQuantityByName("X_Defense");
        if (quantity > 0) {
            System.out.println("Usando X_Defense. Cantidad restante: " + (quantity - 1));
            updateItemQuantity("X_Defense", quantity - 1);
            setLabelDeffense(); // Actualizar la etiqueta de cantidad
            addItemEffectToPokemon(currentPokemonId, "X_Defense");
        } else {
            showAlert("Error", "No tienes suficientes X_Defense.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void setBottle(ActionEvent event) {
        // Lógica para manejar el uso del ítem Bottle Cap
        int quantity = getItemQuantityByName("Bottle_Cap");
        if (quantity > 0) {
            System.out.println("Usando Bottle_Cap. Cantidad restante: " + (quantity - 1));
            updateItemQuantity("Bottle_Cap", quantity - 1);
            setLabelBottleCap(); // Actualizar la etiqueta de cantidad
            addItemEffectToPokemon(currentPokemonId, "Bottle_Cap");
        } else {
            showAlert("Error", "No tienes suficientes Bottle_Cap.", Alert.AlertType.ERROR);
        }
    }

    private void updateItemQuantity(String itemName, int newQuantity) {
    
        AppData db = AppData.getInstance();
        db.connect(selected_path);
        // Actualiza la cantidad
        db.update("""
            UPDATE ItemInventory
            SET quantity = %d
            WHERE item_id = (SELECT id FROM Item WHERE name = '%s');
        """.formatted(newQuantity, itemName));

        db.close();
    }

    private void addItemEffectToPokemon(int pokemonId, String itemName) {
        AppData db = AppData.getInstance();
        db.connect(selected_path);

        ArrayList<HashMap<String, Object>> result = db.query(
        "SELECT id FROM Item WHERE name = '" + itemName + "'"
        );
        int itemId = -1;
        if (!result.isEmpty()) {
            itemId = ((Number) result.get(0).get("id")).intValue();
        }

        if (itemId != -1) {
            // Insertar o actualizar el efecto en ItemEffect como activo
            db.update("""
                INSERT INTO ItemEffect (player_pokemon_id, item_id, active)
                VALUES (%d, %d, 1)
                ON CONFLICT(player_pokemon_id, item_id) DO UPDATE SET active = 1;
            """.formatted(pokemonId, itemId));
        }

        db.close();
    }

    public void updateItems() {
       AppData db = AppData.getInstance();
        db.connect(selected_path);

        String query = """
            SELECT i.name, COALESCE(ii.quantity, 0) as quantity
            FROM Item i
            LEFT JOIN ItemInventory ii ON i.id = ii.item_id
            WHERE i.name IN ('X_Attack', 'X_Defense', 'Bottle_Cap');
        """;

        ArrayList<HashMap<String, Object>> result = db.query(query);

        for (HashMap<String, Object> row : result) {
            String name = (String) row.get("name");
            int quantity = ((Number) row.get("quantity")).intValue();
            switch (name) {
                case "X_Attack" -> numberAttack.setText(String.valueOf(quantity));
                case "X_Defense" -> numberDeffense.setText(String.valueOf(quantity));
                case "Bottle_Cap" -> numberBottleCap.setText(String.valueOf(quantity));
            }
        }

        db.close();
    }
}