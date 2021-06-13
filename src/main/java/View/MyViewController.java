package View;

import ViewModel.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import com.sun.javafx.menu.MenuItemBase;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Window;


import java.awt.*;
import java.io.File;
import java.lang.Object;
import java.net.URISyntaxException;
import java.util.Observable;
import java.util.Observer;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MyViewController implements Initializable,Observer, IView{

    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public Label lbl_player_row;
    public Label lbl_player_col;
    public BorderPane borderPane;
    public MenuItem solveBar;
    public MenuItem saveBar;
    private MyViewModel viewModel;
    @FXML
    private MazeDisplayer mazeDisplayer;

    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();

    private double mouseX;
    private double mouseY;
    private volatile double preX = 0;
    private volatile double preY = 0;

    private boolean drag_start;

    private int rows;
    private int cols;


    public void setViewModel(MyViewModel myViewModel){
        this.viewModel = myViewModel;
        this.viewModel.addObserver(this);
    }

    @Override
    public void displayMaze(Maze maze) {
        mazeDisplayer.setMaze(maze);
        int PlayerRow = viewModel.getPlayerRow();
        int PlayerCol = viewModel.getPlayerCol();
        mazeDisplayer.setPlayerPosition(PlayerRow,PlayerCol);
        this.characterPositionRow.set(""+PlayerRow);
        this.characterPositionColumn.set(""+PlayerCol);
    }

    @Override
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public void setCols(int cols) {
        this.cols = cols;
    }

    @Override
    public void loadGeneratedMaze(){
        try{
            viewModel.generateMaze(rows, cols);
        }catch(Exception e){
            alertErrorMessage(e.getMessage());
        }
    }

    private void alertErrorMessage(String s){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(s);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
        return;
    }

    @Override
    public void update(Observable o, Object arg) {
        String change = (String) arg;

        switch (change){
            case "player moved" -> playerMoved();
            case "Player Finished" -> {
                try {
                    playerFinished();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case "maze generated" -> mazeGenerated();
            case "maze Solved" -> mazeSolved();
            case "Maze Saved" -> mazeSavedAlert();
            case "Maze Loaded" -> mazeLoadedAlert();
            default -> System.out.println("There is not implemented change: " + change);
        }
    }

    private void playerFinished() throws IOException {
        updatePosition(this.mazeDisplayer.getMaze().getStartPosition());
        Main.mazeSolved();
    }

    private void mazeLoadedAlert() {
        mazeGenerated();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("The Maze loaded successfully");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    private void mazeSavedAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("The Maze saved successfully");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    private void mazeSolved() {
        viewModel.solveMaze();
        mazeDisplayer.backToNormalZoom();
        this.mazeDisplayer.drawSolution(this.viewModel.getSolution());
    }

    private void mazeGenerated() {
        this.solveBar.setDisable(false);
        this.saveBar.setDisable(false);
        characterPositionRow.set("" + viewModel.getMaze().getStartPosition().getRowIndex());
        characterPositionColumn.set("" + viewModel.getMaze().getStartPosition().getColumnIndex());
        mazeDisplayer.drawMaze(viewModel.getMaze());
    }

    private void playerMoved() {
        characterPositionRow.set(""+ viewModel.getPlayerRow());
        characterPositionColumn.set(""+ viewModel.getPlayerCol());
        mazeDisplayer.setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol());
    }

    private void updatePosition(Position arg) {
        characterPositionRow.set(""+ arg.getRowIndex());
        characterPositionColumn.set(""+ arg.getColumnIndex());
        mazeDisplayer.setPlayerPosition(arg.getRowIndex(),arg.getColumnIndex());
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (lbl_player_col != null && lbl_player_row != null) {
            lbl_player_row.textProperty().bind(characterPositionRow);
            lbl_player_col.textProperty().bind(characterPositionColumn);
        }
    }

    public void generateMaze(ActionEvent actionEvent) {

        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());
        //this.mazeDisplayer.deleteSol();
        viewModel.generateMaze(rows,cols);
    }

    @Override
    public void mouseClick(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void solveMaze(ActionEvent actionEvent) {
        viewModel.solveMaze();
        mazeDisplayer.backToNormalZoom();
        mazeDisplayer.drawSolution(this.viewModel.getSolution());
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        viewModel.movePlayer(keyEvent);
        keyEvent.consume();
    }

    @Override
    public void dragDetected(MouseEvent mouseEvent) { // need to understand this part....
        this.mouseX = mouseEvent.getX();
        this.mouseY = mouseEvent.getY();
        this.drag_start = true;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.drag_start && enoughForMovement(mouseEvent, this.mouseX,this.mouseY)){
            this.viewModel.movePlayer(mouseEvent,this.mouseX,this.mouseY , mazeDisplayer.cellWidth,mazeDisplayer.cellHeight);
            this.mouseX = mouseEvent.getX();
            this.mouseY = mouseEvent.getY();
        }
    }

    private boolean enoughForMovement(MouseEvent mouseEvent, double startX, double startY) {
        double cell_height = mazeDisplayer.getCellHeight();
        double cell_width = mazeDisplayer.getCellWidth();
        boolean leftOrRight = Math.abs(mouseEvent.getX()-startX) >=cell_width ;
        boolean upOrDown = Math.abs(mouseEvent.getY()-startY) >= cell_height;
        return leftOrRight || upOrDown;
    }

    @Override
    public void mouseRelesed(MouseEvent mouseEvent) {
        this.drag_start = false;
        dragDone(mouseEvent);
        mouseEvent.consume();
    }

    private void dragDone(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();

    }

    @Override
    public void they_see_me_scrolling(ScrollEvent scrollEvent) {
        mazeDisplayer.zoom(scrollEvent.getDeltaY() ,scrollEvent.getX());
        scrollEvent.consume();
    }

    @Override
    public void createNewMaze(ActionEvent actionEvent) throws IOException {
        Main.createMaze();
    }

    @Override
    public void backToMain(ActionEvent actionEvent) throws IOException {
        Main.backToMain();
    }

    @Override
    public void exit(ActionEvent actionEvent) throws Exception {
        if(Main.model != null)
            Main.model.stopServers();
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void loadMaze(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Maze");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)" , new String[]{"*.maze"}));
        fileChooser.setInitialDirectory(new File("./resources/SavedMaze"));
        File chosen = fileChooser.showOpenDialog((Window) null);
        viewModel.loadGame(chosen);
    }

    @Override
    public void loadMazeFromOtherPlace(ActionEvent actionEvent) throws IOException {
        Main.backToMain();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Maze");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)" , new String[]{"*.maze"}));
        fileChooser.setInitialDirectory(new File("./resources/SavedMaze"));
        File chosen = fileChooser.showOpenDialog((Window) null);
        viewModel.loadGame(chosen);
    }

    @Override
    public void saveMaze(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Maze");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)" , new String[]{"*.maze"}));
        fileChooser.setInitialDirectory(new File("./resources/SavedMaze"));
        File chosen = fileChooser.showSaveDialog((Window) null);
        viewModel.saveGame(chosen);
    }

    @Override
    public void propertiesMaze(ActionEvent actionEvent) throws IOException {
        Main.propertiesMaze();
    }

    @Override
    public void helpWindow(ActionEvent actionEvent) throws IOException {
        Main.mazeHelp();
    }

    @Override
    public void aboutWindow(ActionEvent actionEvent) throws IOException {
        Main.mazeAbout();
    }

    @Override
    public void toNewMaze(ActionEvent actionEvent) throws IOException {
        Main.backToMain();
        Main.createMaze();
    }

    @Override
    public void toProperties(ActionEvent actionEvent) throws IOException {
        Main.backToMain();
        Main.propertiesMaze();
    }

    @Override
    public void toHelp(ActionEvent actionEvent) throws IOException {
        Main.backToMain();
        Main.mazeHelp();
    }

    @Override
    public void toAbout(ActionEvent actionEvent) throws IOException {
        Main.backToMain();
        Main.mazeAbout();
    }

    @Override
    public void backToMainSolve(ActionEvent actionEvent) throws IOException {
        Main.backToMainFromSolved();
    }

    @Override
    public void primInfo(MouseEvent mouseEvent) {
        try{
            Desktop.getDesktop().browse(new URL("https://en.wikipedia.org/wiki/Maze_generation_algorithm").toURI());
        }catch(URISyntaxException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



