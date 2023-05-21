package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Level;

public class DebugMenu 
{
    public Stage primaryStage = new Stage();
    public DebugMenu()
    {
        Button wallpaperTest = new Button();
        wallpaperTest.setText("wallpaper trees");
        wallpaperTest.setOnAction(e -> Driver.setBackground("assets/trees.png"));

        Button wallpaperTest2 = new Button();
        wallpaperTest2.setText("wallpaper water");
        wallpaperTest2.setOnAction(e -> Driver.setBackground("assets/water.png"));

        Button wallpaperTest3 = new Button();
        wallpaperTest3.setText("wallpaper pico");
        wallpaperTest3.setOnAction(e -> Driver.setBackground("assets/pico.png"));

        Button testfinish = new Button();
        testfinish.setText("launch game end");
        Level temp = new Level();
        temp.title = "Title";
        temp.aritst = "artist";
        testfinish.setOnAction(e -> Driver.setMenu(new GameOver(300, new Settings(), temp, "Easy")));

        VBox primaryPane = new VBox();
        primaryPane.getChildren().addAll(wallpaperTest,wallpaperTest2,wallpaperTest3,testfinish);
        
        Scene primaryScene = new Scene(primaryPane);
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("debug");
        primaryStage.show();
    }
}
