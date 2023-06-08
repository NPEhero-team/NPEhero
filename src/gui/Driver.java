package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
import main.SoundController;
import java.io.File;
import devmenu.DebugMenu;


public class Driver extends Application 
{    
    public static Stage primaryStage;
    static Pane primaryPane = new Pane();

    public static SettingsController settingsController = new SettingsController();
    public static SoundController soundController = new SoundController();
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

        Scene primaryScene = new Scene(primaryPane, 800,600);
        primaryScene.getStylesheets().add("assets/style.css");

        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("NPE Hero");

        
        setMenu(new MainMenu());
        setMenuBackground();

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
    public static void setBackground(Image image) //replaces background with a new one
    {
        primaryPane.setBackground(new Background(
            new BackgroundImage(
                    image,
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                    new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
            )));
    }

    public static void setMenuBackground()
    {
        setBackground(new Image(new File("src/assets/mountains.png").toURI().toString()));
    }

    /**
     * quits the application
     */
    public static void quit()
    {
        Platform.exit();
    }
}
