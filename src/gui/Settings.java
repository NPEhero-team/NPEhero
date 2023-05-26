package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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
        musicSlider.setMax(100);
        musicSlider.setMin(0);
        musicSlider.valueProperty().bindBidirectional(Driver.settingsController.musicVol);

        VBox musicBox = new VBox();
        musicBox.getChildren().addAll(musicText, musicSlider);
        musicBox.getStyleClass().add("box");
        musicBox.setPadding(new Insets(10));


        Text SFXText = new Text();
        SFXText.setText("Sound Effects Volume");
        SFXText.getStyleClass().add("t3");

        Slider SFXSlider = new Slider();
        SFXSlider.setMax(100);
        SFXSlider.setMin(0);
        SFXSlider.valueProperty().bindBidirectional(Driver.settingsController.effectsVol);

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
        fullscreen.setOnAction(e -> Driver.primaryStage.setFullScreen(!Driver.primaryStage.isFullScreen()));

        VBox fullBox = new VBox();
        fullBox.getChildren().addAll(fullText,fullscreen);
        fullBox.getStyleClass().add("box");
        fullBox.setPadding(new Insets(10));


        Button devMenu = new Button();
        devMenu.setText("Debug Menu");
        devMenu.setOnAction(e -> Driver.debug.show());

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> Driver.setMenu(new MainMenu()));

        BorderPane buttonBox = new BorderPane();
        buttonBox.setLeft(exit);
        buttonBox.setRight(devMenu);


        VBox options = new VBox();
        options.setSpacing(10);
        options.setAlignment(Pos.CENTER);
        options.getChildren().addAll(musicBox,SFXBox,fullBox,buttonBox);
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
