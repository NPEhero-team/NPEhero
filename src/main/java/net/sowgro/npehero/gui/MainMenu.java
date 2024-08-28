package net.sowgro.npehero.gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.editor.LevelList;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;


public class MainMenu extends Page {

    private final HBox content = new HBox();

    public MainMenu() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(50.0);
        dropShadow.setColor(Color.WHITE);
        dropShadow.setBlurType(BlurType.GAUSSIAN);

        Text npehero = new Text();
        npehero.setBoundsType(TextBoundsType.VISUAL);
        npehero.setText("NPE HERO");
        npehero.getStyleClass().add("t0");
        npehero.setEffect(dropShadow);

        Text lessthan = new Text("<");
        lessthan.setBoundsType(TextBoundsType.VISUAL);
        lessthan.getStyleClass().add("t0e");

        Text greaterthan = new Text(">");
        greaterthan.setBoundsType(TextBoundsType.VISUAL);
        greaterthan.getStyleClass().add("t0e");
        HBox title = new HBox(lessthan, npehero, greaterthan);
        title.setSpacing(20);
        title.setAlignment(Pos.CENTER);

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
            Platform.exit();
        });

        VBox buttonBox = new VBox();
        buttonBox.getChildren().addAll(play, settings, levelEdit, exit);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(title, buttonBox);
        centerBox.setSpacing(30);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);
    }

    @Override
    public Pane getContent() {
        return content;
    }
}
