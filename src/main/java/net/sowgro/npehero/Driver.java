package net.sowgro.npehero;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sowgro.npehero.main.Control;
import net.sowgro.npehero.gui.MainMenu;
import net.sowgro.npehero.main.Levels;
import net.sowgro.npehero.main.Settings;
import net.sowgro.npehero.main.Sound;

import java.net.URL;


public class Driver extends Application
{
    public static final Image MENU_BACKGROUND = new Image(Driver.class.getResource("mountains.png").toExternalForm());;

    public static Stage primaryStage;
    static ScrollPane primaryPane = new ScrollPane();
    static ImageView backgroundImage = new ImageView();
    static ImageView backgroundImage2 = new ImageView();

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
        Settings.read();
        Levels.readData();
        Control.readFromFile();

        primaryStage = newPrimaryStage;

        StackPane root = new StackPane(backgroundImage2, backgroundImage, primaryPane);
        Scene primaryScene = new Scene(root, 800,600);

//        Cant figure out how to center this
        backgroundImage.fitHeightProperty().bind(primaryScene.heightProperty());
        backgroundImage2.fitHeightProperty().bind(primaryScene.heightProperty());
        backgroundImage.setPreserveRatio(true);
        backgroundImage2.setPreserveRatio(true);

        primaryScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("NPE Hero");

        primaryPane.getStyleClass().remove("scroll-pane");
        
        setMenu(new MainMenu());
        setMenuBackground();

        Sound.playSong(Sound.MENU_SONG);

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
        primaryPane.setContent(pane);
        pane.prefWidthProperty().bind(primaryPane.widthProperty()); //makes pane fill the window
        pane.prefHeightProperty().bind(primaryPane.heightProperty());
        primaryPane.requestFocus(); //make the pane itself focused by the keyboard naviagtion so no button is highlighted by default
    }

    /**
     * @return the current pane in primaryPane
     */
    public static Pane getMenu(){
        return (Pane) primaryPane.getContent();
    }

    /**
     * Replaces the background image with a new one.
     * @param image The image to set.
     */
    public static void setBackground(Image image) //replaces background with a new one
    {
        // TODO center on screen
        if (image == backgroundImage.getImage()) {
            return;
        }
        backgroundImage2.setImage(image);
        FadeTransition ft = new FadeTransition(Duration.seconds(0.2), backgroundImage);
        ft.setInterpolator(Interpolator.EASE_BOTH);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(_ -> {
            backgroundImage.setImage(image);
        });

        ScaleTransition st = new ScaleTransition(Duration.seconds(0.2), backgroundImage2);
        st.setInterpolator(Interpolator.EASE_BOTH);
        st.setFromX(1.05);
        st.setFromY(1.05);
        st.setToX(1.0);
        st.setToY(1.0);

        ParallelTransition pt = new ParallelTransition(ft, st);
        pt.play();
    }

    public static void setMenuBackground()
    {
        setBackground(MENU_BACKGROUND);
    }

    public static URL getResource(String fileName) {
        return Driver.class.getResource(fileName);
    }
}
