package View;

import Server.Configurations;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.search.ISearchingAlgorithm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.lang.module.Configuration;

public class PropertiesController {

    public ChoiceBox numOfThreads;
    public ChoiceBox generatingAlgo;
    public ChoiceBox solvingAlgo;

    public Button confirm;
    public Button cancel;

    ObservableList<String> threads = FXCollections.observableArrayList("3", "5", "10");
    ObservableList<String> generating = FXCollections.observableArrayList("EmptyMazeGenerator", "SimpleMazeGenerator", "MyMazeGenerator");
    ObservableList<String> solving = FXCollections.observableArrayList("BestFirstSearch", "BreadthFirstSearch", "DepthFirstSearch");

    private IView view;

    public PropertiesController() {
        Object[] prop = Configurations.getInstance().LoadProperties();

        int numThreads = (int)prop[0];
        this.numOfThreads.setValue(numThreads + "");
        this.numOfThreads.setItems(threads);



        IMazeGenerator generator = (IMazeGenerator) prop[1];
        String generatorName = generator.getClass().toString();
        //generatorName = generatorName.substring()

        generatingAlgo.setValue(generatorName);
        generatingAlgo.setItems(generating);

        ISearchingAlgorithm searcher = (ISearchingAlgorithm) prop[2];
        String searcherName = searcher.getClass().toString();
        //searcherName = searcherName.substring()

        solvingAlgo.setValue(searcherName);
        solvingAlgo.setItems(solving);
    }

    public void backToMain(ActionEvent actionEvent) throws IOException {
        Main.backToMain();
    }


    public void confirmProperties(ActionEvent actionEvent) throws IOException {
        String threads = (String) numOfThreads.getValue();
        String generating = (String) generatingAlgo.getValue();
        String solving = (String) solvingAlgo.getValue();

        Configurations config = Configurations.getInstance();
        config.setProp(Integer.valueOf(threads), generating, solving);

        Main.backToMain();
    }

    public void setView(IView view) {
        this.view = view;
    }
}
