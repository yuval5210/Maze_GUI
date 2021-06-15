package Model;

import Client.Client;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.*;
import Client.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;


public class MyModel extends Observable implements IModel {


    private Maze maze;
    private Solution solution;
    private static int playerRow;
    private static int playerCol;
    //private static final Logger Logger = LogManager.getLogger();


    private Server generateServer;
    private Server solverServer;



    public MyModel() {
        maze = null;
        solution = null;

        generateServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        generateServer.start();


        solverServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        solverServer.start();

    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void updatePlayerLocation(MoveMentDirection direction) {
        int row = playerRow;
        int col = playerCol;

        switch (direction) {
            case UP, NUMPAD8 -> {
                if (row > 0 && maze.getCellValue(row - 1, col) == 0) {
                    row--;
                }
            }
            case DOWN, NUMPAD2 -> {
                if (row < maze.getRows() - 1 && maze.getCellValue(row + 1, col) == 0) {
                    row++;

                }

            }
            case LEFT, NUMPAD4 -> {
                if (col > 0 && maze.getCellValue(row, col - 1) == 0) {
                    col--;

                }
            }

            case RIGHT, NUMPAD6 -> {
                if (col < maze.getColumns() - 1 && maze.getCellValue(row, col + 1) == 0) {
                    col++;

                }
            }

            case NUMPAD7 -> {
                if (row > 0 && col > 0 && maze.getCellValue(row - 1, col - 1) == 0) {
                    row--;
                    col--;
                }
            }
            case NUMPAD9 -> {
                if (row > 0 && col < maze.getColumns() - 1 && maze.getCellValue(row - 1, col + 1) == 0) {
                    row--;
                    col++;
                }
            }
            case NUMPAD1 -> {
                if (row < maze.getRows() - 1 && col > 0 && maze.getCellValue(row + 1, col - 1) == 0) {
                    row++;
                    col--;
                }
            }
            case NUMPAD3 -> {
                if ((row < maze.getRows() - 1) && (col < maze.getColumns() - 1) && (maze.getCellValue(row + 1, col + 1) == 0)) {
                    row++;
                    col++;
                }
            }
            default -> {
                return;
            }
        }
        playerRow = row;
        playerCol = col;

        setChanged();
        if((playerRow == this.maze.getGoalPosition().getRowIndex()) && (playerCol == this.maze.getGoalPosition().getColumnIndex())){
            notifyObservers("Player Finished");
            return;
        }
        notifyObservers("player moved");
    }


    @Override
    public int getPlayerRow() {
        return playerRow;
    }

    @Override
    public int getPlayerCol() {
        return playerCol;
    }

    @Override
    public void generateMaze(int rows, int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        byte[] compressedMaze = (byte[]) fromServer.readObject();
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(rows * cols * 4) + 24]; // may need to change the size
                        is.read(decompressedMaze);
                        Maze maze = new Maze(decompressedMaze);
                        setMaze(maze);
                        //maze.print();
                        updateLocation(maze.getStartPosition());
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
            //Logger.info("Client accepted: " + InetAddress.getLocalHost().toString() + " rows = " + this.maze.getRows() + ", columns = " + this.maze.getColumns());
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

        setChanged();
        notifyObservers("maze generated");
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        this.solution = null;
        playerRow = maze.getStartPosition().getRowIndex();
        playerCol = maze.getStartPosition().getColumnIndex();
        //setChanged();
        //notifyObservers();

    }

    @Override
    public void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new
                    IClientStrategy() {
                        @Override
                        public void clientStrategy(InputStream inFromServer,
                                                   OutputStream outToServer) {
                            try {
                                ObjectOutputStream toServer = new
                                        ObjectOutputStream(outToServer);
                                ObjectInputStream fromServer = new
                                        ObjectInputStream(inFromServer);
                                toServer.flush();
                                toServer.writeObject(maze); //send maze to server
                                toServer.flush();
                                setSolution((Solution) fromServer.readObject()); //read generated maze (compressed with MyCompressor) from server


                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    });
            client.communicateWithServer();
            //Logger.info("Client accepted: " + InetAddress.getLocalHost().toString() + " The searching algorithm is: " + Configurations.getMazeSearchingAlgorithm());
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }
    }



    private void setSolution(Solution solution) {
        this.solution = solution;
        //setChanged();
        //notifyObservers(solution);

    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void updatePlayerLocation(MouseEvent movement, double startX, double startY ,double cellWidth ,double cellHegiht ) {
        if (maze != null) {

            int row = playerRow;
            int col = playerCol;
            if (!movement.isControlDown()) {
                if (movement.getY() < startY && (Math.abs(movement.getY() - startY) >= cellHegiht)) {
                    if (movement.getX() < startX && (Math.abs(movement.getX() - startX) >= cellWidth)){
                        if(maze.getCellValue(row-1,col-1) ==0) {
                            row--;
                            col--;
                        }
                    }
                    else if (movement.getX() > startX && (Math.abs(movement.getX() - startX) >= cellWidth)){
                        if(maze.getCellValue(row-1,col+1) ==0) {
                            row--;
                            col++;
                        }
                    }
                    else { /*(maze.getCellValue(row - 1, col) == 0)*/
                        if(maze.getCellValue(row-1,col)==0) {
                            row--;
                        }
                    }


                } else if (movement.getY() > startY && (Math.abs(movement.getY() - startY) >= cellHegiht)) {
                    if(movement.getX() < startX && (Math.abs(movement.getX() - startX) >= cellWidth)){
                        if(maze.getCellValue(row+1,col-1)==0) {
                            row++;
                            col--;
                        }
                    }
                    else if(movement.getX() > startX && (Math.abs(movement.getX() - startX) >= cellWidth)){
                        if(maze.getCellValue(row+1,col+1)==0) {
                            row++;
                            col++;
                        }
                    }
                    else { /*(maze.getCellValue(row + 1, col) == 0)*/
                        if(maze.getCellValue(row+1,col)==0) {
                            row++;
                        }
                    }

                } else if (movement.getX() > startX &&(Math.abs(movement.getX() - startX) >= cellWidth)) {
                    if (maze.getCellValue(row, col + 1) == 0)
                        col++;

                }

                else if (movement.getX() < startX && (Math.abs(movement.getX() - startX) >= cellWidth)) {
                    if (maze.getCellValue(row, col - 1) == 0)
                        col--;

                }
            }
            playerRow = row;
            playerCol = col;

            setChanged();
            if((playerRow == this.maze.getGoalPosition().getRowIndex()) && (playerCol == this.maze.getGoalPosition().getColumnIndex())){
                notifyObservers("Player Finished");
                return;
            }
            notifyObservers("player moved");
        }
    }

    @Override
    public void saveGame(File chosen) {
        try{
            OutputStream out = new MyCompressorOutputStream(new FileOutputStream(chosen));
            out.write(maze.toByteArray());
            out.flush();
            out.close();
            setChanged();
            notifyObservers("Maze Saved");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void loadGame(File chosen) {
        try{
            InputStream in = new MyDecompressorInputStream(new FileInputStream(chosen));
            byte[] mazeToLoad = new byte[1000000];
            in.read(mazeToLoad);
            in.close();
            this.maze = new Maze(mazeToLoad);
            playerRow = this.maze.getStartPosition().getRowIndex();
            playerCol = this.maze.getStartPosition().getColumnIndex();
            setChanged();
            notifyObservers("Maze Loaded");
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void updateLocation(Position playerPosition) {
        playerRow = playerPosition.getRowIndex();
        playerCol = playerPosition.getColumnIndex();
        setChanged();
        notifyObservers("player moved");
    }


    @Override
    public void stopServers(){
        generateServer.stop();
        solverServer.stop();
    }


}
