package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    public static Stage stage;
    public static IModel model;
    public static MyViewModel viewModel;
    public static MyViewController viewController;
    public static Stage stageCreate;
    public static Stage stageOthers;
    public static MediaPlayer mediaPlayer;



    @Override
    public void start(Stage primaryStage) throws Exception{
        Media sound = new Media(new File("resources/Music/startWindow.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        Parent root = FXMLLoader.load(getClass().getResource("../view/StartWindow.fxml"));
        stage = primaryStage;
        primaryStage.setTitle("The Last Dance");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }

    public static void startTheGame() throws IOException {
        mediaPlayer.stop();
        Media sound = new Media(new File("resources/Music/mainWindow.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/MyView.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("The last Dance");
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();

        model = new MyModel();
        viewModel = new MyViewModel(model);
        viewController = fxmlLoader.getController();
        viewController.setViewModel(viewModel);
    }


    public static void createMaze() throws IOException {
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/CreateNewMaze.fxml"));
        Parent root = fxmlLoader.load();
        stageCreate = new Stage();
        stageCreate.setTitle("The last Dance");
        stageCreate.setScene(new Scene(root, 1000, 600));
        stageCreate.show();

        CreateNewMazeController createNewMaze = fxmlLoader.getController();
        createNewMaze.setView(viewController);
    }

    public static void generateTheMaze() throws IOException {
        stageCreate.hide();
        stage.show();
    }

    public static void propertiesMaze() throws IOException {
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/Properties.fxml"));
        Parent root = fxmlLoader.load();
        stageOthers = new Stage();
        stageOthers.setTitle("The last Dance");
        stageOthers.setScene(new Scene(root, 1000, 600));
        stageOthers.show();

        PropertiesController propertiesMaze = fxmlLoader.getController();
        propertiesMaze.setView(viewController);
    }

    public static void backToMain() throws IOException {
        stageOthers.hide();
        stage.show();
    }

    public static void backToMainFromSolved() throws IOException {
        stageOthers.hide();
        mediaPlayer.stop();
        Media sound = new Media(new File("resources/Music/mainWindow.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/MyView.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("The last Dance");
        stage.setScene(new Scene(root, 1000, 600));
        stage.show();

        model = new MyModel();
        viewModel = new MyViewModel(model);
        viewController = fxmlLoader.getController();
        viewController.setViewModel(viewModel);
    }

    public static void mazeSolved() throws IOException {
        mediaPlayer.stop();
        Media sound = new Media(new File("resources/Music/solved.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/MazeSolved.fxml"));
        Parent root = fxmlLoader.load();
        stageOthers = new Stage();
        stageOthers.setTitle("The last Dance");
        stageOthers.setScene(new Scene(root, 600, 600));
        stageOthers.show();
    }

    public static void mazeHelp() throws IOException {
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/Help.fxml"));
        Parent root = fxmlLoader.load();
        stageOthers = new Stage();
        stageOthers.setTitle("The last Dance");
        stageOthers.setScene(new Scene(root, 1000, 600));
        stageOthers.show();
    }

    public static void mazeAbout() throws IOException {
        stage.hide();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("../view/About.fxml"));
        Parent root = fxmlLoader.load();
        stageOthers = new Stage();
        stageOthers.setTitle("The last Dance");
        stageOthers.setScene(new Scene(root, 1000, 600));
        stageOthers.show();
    }

    public void stop() throws Exception{
        if(model != null)
            model.stopServers();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
/*
    @Override
    public void start(Stage primaryStage) throws Exception{


        Parent root = FXMLLoader.load(getClass().getResource("StartWindow.fxml"));
        stage = primaryStage;
        primaryStage.setTitle("The Last Dance");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();

*//*        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("The Last Maze - Another Jordan tale");
        primaryStage.setScene(new Scene(root, 600 , 450));
        primaryStage.show();

        IModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        MyViewController controller = fxmlLoader.getController();
        controller.setViewModel(myViewModel);*//*

*//*
        Main.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("startScene.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("The Last Maze - Another Jordan tale");
        primaryStage.setScene(new Scene(root, 600 , 450));
        primaryStage.show();
*//*

*//*        IModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        MyViewController controller = fxmlLoader.getController();
        controller.setViewModel(myViewModel);*//*
    }

    public static void realStart() throws IOException{


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("The Last Maze - Another Jordan tale");
        stage.setScene(new Scene(root, 600 , 450));
        stage.show();

        IModel model = new MyModel();
        MyViewModel myViewModel = new MyViewModel(model);
        MyViewController controller = fxmlLoader.getController();
        controller.setViewModel(myViewModel);

    }
    public static void main(String[] args) {
        launch(args);
    }
}*/