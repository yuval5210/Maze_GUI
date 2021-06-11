package ViewModel;

import Model.*;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;

    public MyViewModel(IModel model){
        this.model = model;
        this.model.assignObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public void generateMaze(int rows, int cols){
        model.generateMaze(rows, cols);
    }

    public Maze getMaze(){
        return model.getMaze();
    }

    public void solveMaze(){
        model.solveMaze();
    }

    public Solution getSolution(){
        return model.getSolution();
    }

    public void movePlayer(KeyEvent keyEvent){
        MoveMentDirection direction;
        switch (keyEvent.getCode()){
            case UP, NUMPAD8 -> direction = MoveMentDirection.UP;
            case DOWN , NUMPAD2-> direction = MoveMentDirection.DOWN;
            case LEFT , NUMPAD4-> direction = MoveMentDirection.LEFT;
            case RIGHT , NUMPAD6 -> direction = MoveMentDirection.RIGHT;
/*            case NUMPAD9 -> direction = MoveMentDirection.RIGHTU;
            case NUMPAD7 -> direction = MoveMentDirection.LEFTU;
            case NUMPAD1 -> direction = MoveMentDirection.LEFTD;
            case NUMPAD3 -> direction = MoveMentDirection.RIGHTD;*/

            default -> {
                return;
            }
        }
        model.updatePlayerLocation(direction);
    }

/*    public void movePlayer(KeyEvent keyCode){
        KeyCode k = keyCode.getCode();
        model.updatePlayerLocation(k);
*//*
     *//**//*        MoveMentDirection direction;
        switch (keyEvent.getCode()){
            case UP , NUMPAD8-> direction = MoveMentDirection.UP;
            case DOWN , NUMPAD2-> direction = MoveMentDirection.DOWN;
            case LEFT , NUMPAD4-> direction = MoveMentDirection.LEFT;
            case RIGHT , NUMPAD6 -> direction = MoveMentDirection.RIGHT;
            case NUMPAD9 -> direction = MoveMentDirection.NUMPAD9;
            case NUMPAD7 -> direction = MoveMentDirection.NUMPAD7;
            case NUMPAD1 -> direction = MoveMentDirection.NUMPAD1;
            case NUMPAD3 -> direction = MoveMentDirection.NUMPAD3;

            default -> {
                return;*//**//*
            }
        }
        model.updatePlayerLocation(direction);*//*
    }*/


    public int getPlayerRow(){
        return model.getPlayerRow();
    }

    public int getPlayerCol(){
        return model.getPlayerCol();
    }

    public void movePlayer(MouseEvent movement, double startX, double startY){
        model.updatePlayerLocation(movement,startX, startY);
    }

}
