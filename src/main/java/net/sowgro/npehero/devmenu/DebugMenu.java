package net.sowgro.npehero.devmenu;

import net.sowgro.npehero.Driver;
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
        Button testVol = new Button();
        testVol.setText("print volumes");
        testVol.setOnAction(e -> System.out.println("setc:"+Driver.settingsController.effectsVol+" sndc:"+Driver.soundController.songMediaPlayer.getVolume()));

        primaryPane.getChildren().addAll(testVol);
        
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
