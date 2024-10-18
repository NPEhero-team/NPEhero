package net.sowgro.npehero.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.*;

import java.io.IOException;

public class SettingsEditor extends Page
{
    private final HBox content = new HBox();
    private final CheckBox forceDefaultColors;
    private final Slider gameOpacity;
    private final ColorPicker[] colorPickers;

    record OptionEntry(String label, Node action) { }

    public SettingsEditor()
    {
        Slider musicSlider = new Slider(0.0, 1.0, 0);
        musicSlider.valueProperty().bindBidirectional(Settings.musicVol);

        CheckBox enableMenuMusic = new CheckBox("Enable Menu Music");
        enableMenuMusic.selectedProperty().bindBidirectional(Settings.enableMenuMusic);

        VBox musicBox = new VBox(musicSlider, enableMenuMusic);

        Slider SFXSlider = new Slider(0.0, 1.0, 0);
        SFXSlider.valueProperty().bindBidirectional(Settings.effectsVol);

        Button fullscreen = new Button();
        fullscreen.setText("Toggle (F11)");
        fullscreen.setOnAction(e -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.primaryStage.setFullScreen(!Driver.primaryStage.isFullScreen());
        });

        Button controlsButton = new Button();
        controlsButton.setText("Edit");
        controlsButton.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(new ControlEditor());
        });

        ToggleButton[] scaleOptions = new ToggleButton[4];
        for (int i = 0; i < scaleOptions.length; i++) {
            var val = i * 0.5 + 0.5;
            ToggleButton tb = new ToggleButton((int)(val * 100) + "%");
            tb.setUserData(val);
            tb.getStyleClass().remove("radio-button");
            tb.getStyleClass().add("button");
            if (val == Settings.guiScale.get()) {
                tb.setSelected(true);
            }
            scaleOptions[i] = tb;
        }

        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(scaleOptions);
        tg.selectedToggleProperty().addListener((_, _, newt) -> {
            Settings.guiScale.set((Double)newt.getUserData());
        });
        HBox scaleSlider = new HBox(scaleOptions);

        colorPickers = new ColorPicker[] {
                new ColorPicker(Settings.defaultColors[0]),
                new ColorPicker(Settings.defaultColors[1]),
                new ColorPicker(Settings.defaultColors[2]),
                new ColorPicker(Settings.defaultColors[3]),
                new ColorPicker(Settings.defaultColors[4])
        };
        for (ColorPicker cp : colorPickers) {
            cp.getStyleClass().add("button");
            cp.setMinHeight(60);
            cp.setMinWidth(60);
        }

        HBox colorPickerBox = new HBox();
        colorPickerBox.getChildren().addAll(colorPickers);
        colorPickerBox.setSpacing(10);

        forceDefaultColors = new CheckBox("Force Default Colors");
        forceDefaultColors.setSelected(Settings.forceDefaultColors);

        Button resetColors = new Button("Reset");
        resetColors.setOnAction(_ -> {
            for (int i = 0; i < colorPickers.length; i++) {
                colorPickers[i].setValue(Settings.DEFAULT_DEFAULT_COLORS[i]);
            }
        });

        VBox defaultColors = new VBox(colorPickerBox, forceDefaultColors, resetColors);
        defaultColors.setSpacing(10);

        gameOpacity = new Slider(0.5, 1.0, 0.75);
        gameOpacity.setMajorTickUnit(0.25);
        gameOpacity.setMinorTickCount(1);
        gameOpacity.setSnapToTicks(true);

        OptionEntry[][] optionEntries = {
                {
                        new OptionEntry("Music Volume", musicBox),
                        new OptionEntry("Sound Effects Volume", SFXSlider),
                        new OptionEntry("Fullscreen Mode", fullscreen),
                        new OptionEntry("Key Bindings", controlsButton),
                },
                {
                        new OptionEntry("GUI Scale", scaleSlider),
                        new OptionEntry("Default Block Colors", defaultColors),
                        new OptionEntry("Game Opacity", gameOpacity)
                }
        };
        HBox options = new HBox();
        options.setSpacing(10);
        for (OptionEntry[] col : optionEntries) {
            VBox colBox = new VBox();
            colBox.setSpacing(10);
            colBox.setPrefWidth(420);

            for (OptionEntry option : col) {
                VBox optionBox = new VBox(new Label(option.label), option.action);
                optionBox.setPadding(new Insets(10));
                optionBox.setSpacing(5);
                optionBox.getStyleClass().add("box");
                colBox.getChildren().add(optionBox);
            }
            options.getChildren().add(colBox);
        }
        ScrollPane optionsScroll = new ScrollPane(options);
        optionsScroll.getStyleClass().remove("scroll-pane");
        optionsScroll.setFitToWidth(true);
//        optionsScroll.setPrefWidth(450);

        HBox main = new HBox();
        main.getChildren().addAll(optionsScroll);
        main.setSpacing(10);
        main.maxWidthProperty().bind(content.widthProperty().multiply(0.95));
        main.maxHeightProperty().bind(content.heightProperty().multiply(0.75));

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(new MainMenu());
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, exit);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);
    }

    @Override
    public Pane getContent() {
        return content;
    }

    @Override
    public void onLeave() {
        Settings.forceDefaultColors = forceDefaultColors.isSelected();
        Settings.defaultColors[0] = colorPickers[0].getValue();
        Settings.defaultColors[1] = colorPickers[1].getValue();
        Settings.defaultColors[2] = colorPickers[2].getValue();
        Settings.defaultColors[3] = colorPickers[3].getValue();
        Settings.defaultColors[4] = colorPickers[4].getValue();
        Settings.gameOpacity = gameOpacity.getValue();
        try {
            Settings.save();
        } catch (IOException ex) {
            Driver.setMenu(new ErrorDisplay("Failed to save settings", ex, this));
        }
    }
}

