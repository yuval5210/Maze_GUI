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

        /*
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();

        stage.setTitle("The last Dance");
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();*/

        Main.startTheGame();

        /*FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MyView.fxml"));

        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController viewController = fxmlLoader.getController();
        viewController.setViewModel(viewModel);

        viewModel.addObserver(viewController);*/
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
