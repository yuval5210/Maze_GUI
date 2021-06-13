package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.util.Observer;

public interface IModel {

    void generateMaze(int rows , int cols);
    Maze getMaze();
    void solveMaze();
    Solution getSolution();
    void updatePlayerLocation(MoveMentDirection direction);
    int getPlayerRow();
    int getPlayerCol();
    void assignObserver(Observer o);
    void stopServers();
    void updatePlayerLocation(MouseEvent movement, double startX, double startY, double cellWidth, double cellHegiht);
    void saveGame(File chosen);
    void loadGame(File chosen);
    void setMaze(Maze maze);

}
