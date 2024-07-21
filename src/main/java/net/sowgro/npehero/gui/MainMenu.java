package net.sowgro.npehero.gui;

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
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;


public class MainMenu extends Page {

    @Override
    public Pane getContent() {

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
        play.setOnAction(_ -> {
            Driver.setMenu(new LevelSelector());
            Sound.playSfx(Sound.FORWARD);
        });

        Button settings = new Button();
        settings.setText("Settings");
        settings.setOnAction(_ -> {
            Driver.setMenu(new SettingsEditor());
            Sound.playSfx(Sound.FORWARD);
        });

        Button levelEdit = new Button("Level Editor");
        levelEdit.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new LevelList());
        });

        Button exit = new Button();
        exit.setText("Quit");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
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
        rootBox.setAlignment(Pos.CENTER);
        rootBox.getChildren().add(centerBox);

        return rootBox;
    }
}
