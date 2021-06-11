package View;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateNewMazeController {

    private int rows;
    private int cols;

    public TextField rowsNum;
    public TextField colsNum;
    public IView view;

    public void setView(IView view) {
        this.view = view;
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void helpWindow(ActionEvent actionEvent) throws IOException {
        Main.mazeHelp();
    }

    public void generateTheMaze(ActionEvent actionEvent) throws IOException {
        boolean flag = generateMaze();

        this.rowsNum.setText("");
        this.colsNum.setText("");

        if( flag == true){
            Main.generateTheMaze();
            view.setRows(rows);
            view.setCols(cols);
            view.loadGeneratedMaze();

        }
    }

    private boolean generateMaze(){
        if(this.rowsNum.getText().equals("") && this.colsNum.getText().equals("")){
            alertErrorMessage("You must fill in number of rows and columns");
            return false;
        }
        else if( this.rowsNum.getText().equals("") ){
            alertErrorMessage("You must fill in number of rows");
            return false;
        }
        else if( this.colsNum.getText().equals("") ){
            alertErrorMessage("You must fill in number of columns");
            return false;
        }
        else if (!isNumber(this.rowsNum.getText()) && !isNumber(this.colsNum.getText())){
            alertErrorMessage("You must fill in an integer number of rows and columns");
            return false;
        }
        else if(!isNumber(this.rowsNum.getText())){
            alertErrorMessage("You must fill in an integer number of rows");
            return false;
        }
        else if(!isNumber(this.colsNum.getText())){
            alertErrorMessage("You must fill in an integer number of columns");
            return false;
        }

        this.rows = Integer.valueOf(this.rowsNum.getText());
        this.cols = Integer.valueOf(this.colsNum.getText());

        if(rows < 2 && cols < 2){
            alertErrorMessage("The minimum maze size can be 2*2, change the number of rows and cols");
            return false;
        }
        else if(rows < 2 && cols > 2){
            alertErrorMessage("The minimum maze size can be 2*2, change the number of rows");
            return false;
        }
        else if(rows > 2 && cols < 2) {
            alertErrorMessage("The minimum maze size can be 2*2, change the number of columns");
            return false;
        }

        return true;
    }

    private void alertErrorMessage(String s){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(s);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
        return;
    }

    private boolean isNumber(String s) {
        for(int i = 0; i < s.length(); i++){
            if(Character.isDigit(s.charAt(i)) == false)
                return false;
        }
        return true;
    }
}
