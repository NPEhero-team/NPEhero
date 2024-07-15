package net.sowgro.npehero.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.devmenu.LevelList;
import net.sowgro.npehero.main.SoundController;


public class MainMenu extends Pane
{
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public MainMenu()
    {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(50.0);
        dropShadow.setColor(Color.WHITE);
        dropShadow.setBlurType(BlurType.GAUSSIAN);

        Text title = new Text();
        title.setText("NPE Hero");
        title.getStyleClass().add("t0");
        title.setEffect(dropShadow);

        Button play = new Button();
        play.setText("Play");
        play.setOnAction(e -> {
            Driver.setMenu(new LevelSelector());
            SoundController.playSfx(SoundController.FORWARD);
        });

        Button settings = new Button();
        settings.setText("Settings");
        settings.setOnAction(_ -> {
            Driver.setMenu(new Settings());
            SoundController.playSfx(SoundController.FORWARD);
        });

        Button levelEdit = new Button("Level Editor");
        levelEdit.setOnAction(e -> {
            SoundController.playSfx(SoundController.FORWARD);
            Driver.setMenu(new LevelList());
        });

        Button exit = new Button();
        exit.setText("Quit");
        exit.setOnAction(e -> {
            SoundController.playSfx(SoundController.BACKWARD);
//            Driver.quit();
//            Platform.exit();
            System.exit(0);
        });

        VBox buttonBox = new VBox();
        buttonBox.getChildren().addAll(play, settings, levelEdit, exit);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(title, buttonBox);
        centerBox.setSpacing(10);

        VBox rootBox = new VBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty()); 
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.setAlignment(Pos.CENTER);
        rootBox.getChildren().add(centerBox);

        super.getChildren().add(rootBox);
    }
}
