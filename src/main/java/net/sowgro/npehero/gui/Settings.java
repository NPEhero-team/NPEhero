package net.sowgro.npehero.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.devmenu.LevelList;

public class Settings extends Pane
{
    /*
     * this class is a layout class, most of its purpose is to place UI elements like Buttons within Panes like VBoxes.
     * the creation of these UI elements are mostly not commented due to their repetitive and self explanatory nature.
     * style classes are defined in the style.css file.
     */
    public Settings()
    {
        Text musicText = new Text();
        musicText.setText("Music Volume");
        musicText.getStyleClass().add("t3");

        Slider musicSlider = new Slider();
        musicSlider.valueProperty().bindBidirectional(Driver.settingsController.musicVol);
        musicSlider.setMin(0.0);
        musicSlider.setMax(1.0);

        VBox musicBox = new VBox();
        musicBox.getChildren().addAll(musicText, musicSlider);
        musicBox.getStyleClass().add("box");
        musicBox.setPadding(new Insets(10));


        Text SFXText = new Text();
        SFXText.setText("Sound Effects Volume");
        SFXText.getStyleClass().add("t3");

        Slider SFXSlider = new Slider();
        SFXSlider.valueProperty().bindBidirectional(Driver.settingsController.effectsVol);
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
        fullscreen.getStyleClass().remove("toggle-button");
        fullscreen.getStyleClass().add("button");
        fullscreen.setOnAction(e -> {
            Driver.soundController.playSfx("forward");
            Driver.primaryStage.setFullScreen(!Driver.primaryStage.isFullScreen());
        });

        VBox fullBox = new VBox();
        fullBox.getChildren().addAll(fullText,fullscreen);
        fullBox.getStyleClass().add("box");
        fullBox.setPadding(new Insets(10));


        Text devLabel = new Text("Advanced");
        devLabel.getStyleClass().add("t3");
        
        Button levelEdit = new Button("Level Utility");
        levelEdit.setOnAction(e -> {
            Driver.soundController.playSfx("forward");
            new LevelList();
        });

        Button devMenu = new Button();
        devMenu.setText("Debug Menu");
        devMenu.setOnAction(e -> {
            Driver.soundController.playSfx("forward");
//            Driver.debug.show();
        });

        VBox devBox = new VBox();
        devBox.getStyleClass().add("box");
        devBox.getChildren().addAll(devLabel,levelEdit,devMenu);
        devBox.setVisible(false);
        devBox.setManaged(false);
        devBox.setPadding(new Insets(10));

        ToggleButton advanced = new ToggleButton("Advanced");
        advanced.getStyleClass().remove("toggle-button");
        advanced.getStyleClass().add("button");
        advanced.selectedProperty().bindBidirectional(devBox.managedProperty());
        advanced.selectedProperty().bindBidirectional(devBox.visibleProperty());

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Driver.settingsController.write();
            Driver.soundController.playSfx("backward");
            Driver.setMenu(new MainMenu());
        });

        BorderPane buttonBox = new BorderPane();
        buttonBox.setLeft(exit);
        buttonBox.setRight(advanced);


        VBox options = new VBox();
        options.setSpacing(10);
        options.setAlignment(Pos.CENTER);
        options.getChildren().addAll(musicBox,SFXBox,fullBox,devBox,buttonBox);
        options.maxWidthProperty().bind(super.prefWidthProperty().multiply(0.25)); 
        options.setMinWidth(400);
        options.prefHeightProperty().bind(super.prefHeightProperty());

        HBox rootBox = new HBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty()); 
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().add(options);
        rootBox.setAlignment(Pos.CENTER);
        super.getChildren().add(rootBox);
    }
}
