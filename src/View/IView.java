package View;

import algorithms.mazeGenerators.Maze;

public interface IView {

    void displayMaze(Maze maze);

    void setRows(int rows);
    void setCols(int cols);

    void loadGeneratedMaze();
}
