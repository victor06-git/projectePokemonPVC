package com.projecte;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ControllersubHistory {
    
    @FXML
    private Label labelData;

    @FXML
    private ImageView imgPlayer1, imgPlayer2, imgPlayer3, imgComputer1, imgComputer2, imgComputer3, imgWinner;

    @FXML
    private void initialize() {

        //Agregar la imagen de la arrow

    }
    
    public void setLabelData(String data) {
        labelData.setText(data);
    }

    public void setImgPlayer1(ImageView img) {
        imgPlayer1.setImage(img.getImage());
    }


    public void setImgPlayer2(ImageView img) {
        imgPlayer2.setImage(img.getImage());
    }


    public void setImgPlayer3(ImageView img) {
        imgPlayer3.setImage(img.getImage());
    }


    public void setImgComputer1(ImageView img) {
        imgComputer1.setImage(img.getImage());
    }


    public void setImgComputer2(ImageView img) {
        imgComputer2.setImage(img.getImage());
    }

    
    public void setImgComputer3(ImageView img) {
        imgComputer3.setImage(img.getImage());
    }
    
    
    public void setImgWinner(ImageView img) {
        imgWinner.setImage(img.getImage());
    }
}
    