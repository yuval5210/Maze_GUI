package View;

import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.io.IOException;

public interface IView {

    void displayMaze(Maze maze);
    void setRows(int rows);
    void setCols(int cols);
    void loadGeneratedMaze();
    void mouseClick(MouseEvent mouseEvent);
    void solveMaze(ActionEvent actionEvent);
    void keyPressed(KeyEvent keyEvent);
    void dragDetected(MouseEvent mouseEvent);
    void mousePressed(MouseEvent mouseEvent);
    void mouseDragged(MouseEvent mouseEvent);
    void mouseRelesed(MouseEvent mouseEvent);
    void they_see_me_scrolling(ScrollEvent scrollEvent);
    void createNewMaze(ActionEvent actionEvent) throws IOException;
    void backToMain(ActionEvent actionEvent) throws IOException;
    void exit(ActionEvent actionEvent) throws Exception;
    void loadMaze(ActionEvent actionEvent);
    void loadMazeFromOtherPlace(ActionEvent actionEvent) throws IOException;
    void saveMaze(ActionEvent actionEvent);
    void propertiesMaze(ActionEvent actionEvent) throws IOException;
    void helpWindow(ActionEvent actionEvent) throws IOException;
    void aboutWindow(ActionEvent actionEvent) throws IOException;
    void toNewMaze(ActionEvent actionEvent) throws IOException;
    void toProperties(ActionEvent actionEvent) throws IOException;
    void toHelp(ActionEvent actionEvent) throws IOException;
    void toAbout(ActionEvent actionEvent) throws IOException;
    void backToMainSolve(ActionEvent actionEvent) throws IOException;
    void primInfo(MouseEvent mouseEvent);

}
