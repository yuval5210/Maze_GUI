package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {
//Yuval :)
    private Maze maze;
    private Solution solution;
    private double startX;
    private double startY;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameGoal = new SimpleStringProperty();
    StringProperty imageFileNameHoop = new SimpleStringProperty();
    StringProperty imageFileNameSolPath = new SimpleStringProperty();

    public String getImageFileNameSolPath() {
        return imageFileNameSolPath.get();
    }


    public void setImageFileNameSolPath(String imageFileNameSolPath) {
        this.imageFileNameSolPath.set(imageFileNameSolPath);
    }

    StringProperty imageFileNameSolution = new SimpleStringProperty();

    public String getImageFileNameHoop() {
        return imageFileNameHoop.get();
    }



    public void setImageFileNameHoop(String imageFileNameHoop) {
        this.imageFileNameHoop.set(imageFileNameHoop);
    }

    private double zoomFactor;
    private double zoomDelta;


    public double canvasHeight;
    public double canvasWidth;
    public int rows;
    public int cols;
    public double cellHeight;
    public double cellWidth;
    private volatile Object zoomLock;
    private volatile Object CHWL;

    public MazeDisplayer(){
        canvasHeight = getHeight();
        canvasWidth = getWidth();
        zoomFactor = 0;
        zoomDelta = 15;
        zoomLock = new Object();
        CHWL = new Object();

    }


    public void updateCanvasProperties() {
        synchronized (CHWL) {
            canvasHeight = getHeight();
            canvasWidth = getWidth();
        }
        draw();
    }

    private void setCellHeightAndWidth(double canvasHeight , double canvasWidth){
        synchronized (CHWL) {
            if (maze != null) {
                synchronized (zoomLock) {
                    setCellHeight((canvasHeight + zoomFactor) /  maze.getRows());
                    setCellWidth((canvasWidth + zoomFactor) /  maze.getColumns());
                }
            }
        }
    }


    public String getImageFileNameGoal() {
        return imageFileNameGoal.get();
    }


    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.imageFileNameGoal.set(imageFileNameGoal);
    }

    private int PlayerRow;
    private int PlayerCol;

    public Maze getMaze() {
        return maze;
    }

    public Solution getSolution() {
        return solution;
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }


    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }


    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public int getPlayerRow() {
        return PlayerRow;
    }

    public void setPlayerRow(int playerRow) {
        PlayerRow = playerRow;
    }

    public int getPlayerCol() {
        return PlayerCol;
    }

    public void setPlayerCol(int playerCol) {
        PlayerCol = playerCol;
    }

    public void setPlayerPos(int row, int col) {
        this.PlayerRow = row;
        this.PlayerCol = col;
        draw();
    }

    public void setPlayerPosition(int row, int column) {
        this.PlayerRow = row;
        this.PlayerCol = column;
        draw();


    }

    public void setSolution(Solution arg) {

    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        drawMaze(maze);
    }

    public void drawMaze(Maze maze) {
        this.maze = maze;
        setPlayerPosition(maze.getStartPosition().getRowIndex(), maze.getStartPosition().getColumnIndex());

        draw();
    }

    public double getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(double cellHeight) {
        this.cellHeight = cellHeight;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(double cellWidth) {
        this.cellWidth = cellWidth;
    }

    private void draw() {

        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.getRows();
            int cols = maze.getColumns();
            cellHeight = canvasHeight / rows;
            cellWidth = canvasWidth / cols;

            setCellHeightAndWidth(canvasHeight,canvasWidth);

            GraphicsContext graphicsContext = getGraphicsContext2D();

            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext,rows,cols,cellHeight,cellWidth);

            if (solution != null){
                //drawSolution(graphicsContext,cellHeight,cellWidth);
            }
            drawMazePlayer(graphicsContext,cellHeight,cellWidth);
            drawMazeGoal(graphicsContext,cellHeight,cellWidth);


        }
    }

    public void drawMazeGoal(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {

        Image net = null;
        try {
            net = new Image(new FileInputStream(getImageFileNameHoop()));
        } catch (FileNotFoundException e) {
            System.out.println("Where the hell is jordan?");
        }

        double x = maze.getGoalPosition().getColumnIndex() * cellWidth;
        double y = maze.getGoalPosition().getRowIndex() * cellHeight;

        graphicsContext.setFill(Color.GREEN);

        if (net == null)
            graphicsContext.fillRect(x,y,cellWidth,cellHeight);
        else
            graphicsContext.drawImage(net,x,y,cellWidth,cellHeight);

    }



    private void drawMazePlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        Image jordan = null;
        try {
            jordan = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("Where the hell is jordan?");
        }

        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;

        graphicsContext.setFill(Color.GREEN);

        if (jordan == null)
            graphicsContext.fillRect(x,y,cellWidth,cellHeight);
        else
            graphicsContext.drawImage(jordan,x,y,cellWidth,cellHeight);

    }


    private void drawMazeWalls(GraphicsContext graphicsContext, int rows, int cols, double cellHeight, double cellWidth) {
        Image ball = null;
        try {
            ball = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no  wall Image");
        }

        graphicsContext.setFill(Color.RED);
        for (int i = 0; i < maze.getRows(); i++) {
            for (int j = 0; j < maze.getColumns(); j++) {
                if (maze.getCellValue(i, j) == 1) {
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if (ball == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellWidth);
                    else
                        graphicsContext.drawImage(ball,x,y,cellWidth,cellHeight);
                }
            }
        }
    }

    public void drawSolution(Solution sol) {

        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int rows = maze.getRows();
        int cols = maze.getColumns();
        double cellHeight = canvasHeight / rows;
        double cellWidth = canvasWidth / cols;

        GraphicsContext graphicsContext = getGraphicsContext2D();

        graphicsContext.setFill(Color.GREEN);

        Image X = null;
        try {
            X = new Image(new FileInputStream(getImageFileNameSolPath()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no path Image");
        }


        ArrayList<AState> solutionPath = sol.getSolutionPath();
        for (AState state : solutionPath)
        {
            MazeState mazeState = (MazeState) state;
            Position p = mazeState.getPosition();
            int thisRow = p.getRowIndex();
            int thisCol = p.getColumnIndex();

            if(((this.getPlayerRow() == thisRow) && (this.getPlayerCol() == thisCol)) || (maze.getGoalPosition().getRowIndex() == thisRow && maze.getGoalPosition().getColumnIndex() == thisCol))
            {
                continue;
            }
            double x = thisCol * cellWidth;
            double y = thisRow * cellHeight;
            if(X == null)
                graphicsContext.fillRect(x, y, cellWidth, cellHeight);
            graphicsContext.drawImage(X,x,y,cellWidth,cellHeight);
        }
    }

    public void changeCursorsPlace(double xTo, double yTo){
        if(maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.getRows();
            int cols = maze.getColumns();
            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            double extraMoveX = (cellWidth * maze.getColumns()) / canvasWidth;
            double extraMoveY = (cellHeight * maze.getRows()) / canvasHeight;
            if (xTo < 0)
                extraMoveX = -extraMoveX;
            if (yTo < 0)
                extraMoveY = -extraMoveY;
            startX += xTo + extraMoveX;
            startY += yTo + extraMoveY;
            draw();
        }
    }

    public void backToNormalZoom(){
        synchronized (zoomLock){
            zoomFactor = 0;
            draw();
        }
    }


    public void zoom(double deltaY ,double getX) {
        synchronized (zoomLock) {
            if (deltaY > 0) {
                zoomFactor += zoomDelta * 2;
            } else
                zoomFactor -= zoomDelta * 2;
            draw();
        }
    }
}