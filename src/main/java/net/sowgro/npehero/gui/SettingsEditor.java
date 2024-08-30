package net.sowgro.npehero.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.ErrorDisplay;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Settings;
import net.sowgro.npehero.main.Sound;

import java.io.IOException;

public class SettingsEditor extends Page
{
    private final HBox content = new HBox();

    public SettingsEditor()
    {
        Text musicText = new Text();
        musicText.setText("Music Volume");
        musicText.getStyleClass().add("t3");

        Slider musicSlider = new Slider();
        musicSlider.valueProperty().bindBidirectional(Settings.musicVol);
        musicSlider.setMin(0.0);
        musicSlider.setMax(1.0);

        CheckBox enableMenuMusic = new CheckBox("Enable Menu Music");
        enableMenuMusic.selectedProperty().bindBidirectional(Settings.enableMenuMusic);

        VBox musicBox = new VBox();
        musicBox.getChildren().addAll(musicText, musicSlider, enableMenuMusic);
        musicBox.getStyleClass().add("box");
        musicBox.setPadding(new Insets(10));


        Text SFXText = new Text();
        SFXText.setText("Sound Effects Volume");
        SFXText.getStyleClass().add("t3");

        Slider SFXSlider = new Slider();
        SFXSlider.valueProperty().bindBidirectional(Settings.effectsVol);
        SFXSlider.setMin(0.0);
        SFXSlider.setMax(1.0);

        VBox SFXBox = new VBox();
        SFXBox.getChildren().addAll(SFXText, SFXSlider);
        SFXBox.getStyleClass().add("box");
        SFXBox.setPadding(new Insets(10));


        Text fullText = new Text();
        fullText.setText("Fullscreen mode");
        fullText.getStyleClass().add("t3");

        Button fullscreen = new Button();
        fullscreen.setText("Toggle (F11)");
        fullscreen.setOnAction(e -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.primaryStage.setFullScreen(!Driver.primaryStage.isFullScreen());
        });

        VBox fullBox = new VBox();
        fullBox.getChildren().addAll(fullText,fullscreen);
        fullBox.getStyleClass().add("box");
        fullBox.setPadding(new Insets(10));


        Text controlsLabel = new Text("Key Bindings");
        controlsLabel.getStyleClass().add("t3");

        Button controlsButton = new Button();
        controlsButton.setText("Edit");
        controlsButton.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new ControlEditor());
        });

        VBox controlsBox = new VBox();
        controlsBox.getStyleClass().add("box");
        controlsBox.getChildren().addAll(controlsLabel, controlsButton);
        controlsBox.setPadding(new Insets(10));

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            try {
                Settings.save();
            } catch (IOException ex) {
                Driver.setMenu(new ErrorDisplay("Failed to save settings", ex, this));
            }
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(new MainMenu());
        });

        BorderPane buttonBox = new BorderPane();
        buttonBox.setLeft(exit);

        VBox options = new VBox();
        options.setSpacing(10);
        options.setAlignment(Pos.CENTER);
        options.getChildren().addAll(musicBox,SFXBox,fullBox,controlsBox,buttonBox);
        options.maxWidthProperty().bind(content.prefWidthProperty().multiply(0.25));
        options.setMinWidth(400);
        options.prefHeightProperty().bind(content.prefHeightProperty());

        content.getChildren().add(options);
        content.setAlignment(Pos.CENTER);
    }

    @Override
    public Pane getContent() {
        return content;
    }
}

