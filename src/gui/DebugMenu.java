package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DebugMenu 
{
    public Stage primaryStage = new Stage();

    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    VBox primaryPane = new VBox();
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

        Button testVol = new Button();
        testVol.setText("print volumes");
        testVol.setOnAction(e -> System.out.println("sfx:"+Driver.settingsController.effectsVol+" msc:"+Driver.settingsController.musicVol));

        primaryPane.getChildren().addAll(wallpaperTest,wallpaperTest2,wallpaperTest3,testVol);
        
        Scene primaryScene = new Scene(primaryPane);
        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("debug");
    }

    public void show()
    {
        primaryStage.show();
    }

    public void addButton(Button b)
    {
        primaryPane.getChildren().add(b);
    }
}
