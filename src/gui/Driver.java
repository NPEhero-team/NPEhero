package gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class Driver extends Application 
{
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) 
    {
        primaryStage.setScene(new MainMenu(primaryStage));
        primaryStage.show();
    }
}
