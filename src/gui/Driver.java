package gui;

import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.LevelController;
import main.SettingsController;
import org.json.simple.parser.ParseException;


public class Driver extends Application 
{
    public static Stage primaryStage;
    static Pane primaryPane = new Pane();

    public static SettingsController settingsController = new SettingsController();
    public static LevelController levelController = new LevelController();
    public static DebugMenu debug = new DebugMenu();

    /*
     * starts javafx
     */
    public static void main(String[] args) 
    {
        launch(args);
    }
    
    /*
     * sets up game windows and starts controllers
     * (automatically called by javafx on start)
     */
    @Override
    public void start(Stage newPrimaryStage)
    {   
        primaryStage = newPrimaryStage;

        Scene primaryScene = new Scene(primaryPane, 800, 600);
        primaryScene.getStylesheets().add("gui/style.css");

        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("NPE Hero");

        setMenu(new MainMenu());
        setBackground("assets/water.png");

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> { //full screen stuff
            if (KeyCode.F11.equals(event.getCode())) {
                primaryStage.setFullScreen(!primaryStage.isFullScreen());
            }
        });
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreenExitHint("");
        primaryStage.show();
    }

    /**
     * Replaces/adds a new pane to the primaryPane
     * @param pane  the new pane
     */
    public static void setMenu(Pane pane)
    {
        if (! primaryPane.getChildren().isEmpty())
        {
            primaryPane.getChildren().remove(0);
        }
        primaryPane.getChildren().add(pane);
        pane.prefWidthProperty().bind(primaryPane.widthProperty()); //makes pane fill the window
        pane.prefHeightProperty().bind(primaryPane.heightProperty());
        primaryPane.requestFocus(); //make the pane itself focused by the keyboard naviagtion so no button is highlighted by default
    }

    /**
     * @return the current pane in primaryPane
     */
    public static Pane getMenu(){
        return (Pane) primaryPane.getChildren().get(0);
    }

    /**
     * replaces the background image with a new one
     * @param url   the url of the image to set
     */
    public static void setBackground(String url) //replaces background with a new one
    {
        // Image image1;
        // Image image2;
        // ImageView imageView;
        // KeyFrame keyFrame1On = new KeyFrame(Duration.seconds(0), new KeyValue(imageView.imageProperty(), image1));
        // KeyFrame startFadeOut = new KeyFrame(Duration.seconds(0.2), new KeyValue(imageView.opacityProperty(), 1.0));
        // KeyFrame endFadeOut = new KeyFrame(Duration.seconds(0.5), new KeyValue(imageView.opacityProperty(), 0.0));
        // KeyFrame keyFrame2On = new KeyFrame(Duration.seconds(0.5), new KeyValue(imageView.imageProperty(), image2));
        // KeyFrame endFadeIn = new KeyFrame(Duration.seconds(0.8), new KeyValue(imageView.opacityProperty(), 1.0));
        // Timeline timelineOn = new Timeline(keyFrame1On, startFadeOut, endFadeOut, keyFrame2On, endFadeIn);

        primaryPane.setBackground(new Background(
            new BackgroundImage(
                    new Image(url),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                    new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
            )));
    }

    /**
     * quits the application
     */
    public static void quit()
    {
        Platform.exit();
    }
}
