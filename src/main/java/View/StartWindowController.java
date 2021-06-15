package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

public class StartWindowController {

    //public Button startButton;

    @FXML
    private void startTheGame(ActionEvent actionEvent) throws Exception{
        Main.startTheGame();

    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
