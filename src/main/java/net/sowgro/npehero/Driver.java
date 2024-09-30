package net.sowgro.npehero;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.sowgro.npehero.main.ErrorDisplay;
import net.sowgro.npehero.levelapi.Levels;
import net.sowgro.npehero.main.*;
import net.sowgro.npehero.gui.MainMenu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Stack;


public class Driver extends Application
{
    public static final Image MENU_BACKGROUND = new Image(Driver.class.getResource("mountains.png").toExternalForm());
    public static final File BASE_DIR = new File(".npehero");

    public static Stage primaryStage;
    public static ScrollPane primaryPane = new ScrollPane();
    static ImageView backgroundImage = new ImageView();
    static ImageView backgroundImage2 = new ImageView();

    static Page currentPage = null;

    /*
     * starts javafx
     */
    public static void main(String[] args) {
        launch(args);
    }

    /*
     * sets up game windows and starts controllers
     * (automatically called by javafx on start)
     */
    @Override
    public void start(Stage initStage) {
        Label npehero = new Label("NPEHero");
        Label loading = new Label("Please Wait...           ");
        VBox splashBox = new VBox(npehero, loading);
        splashBox.setPadding(new Insets(30));
        Scene splashScene = new Scene(splashBox);
        initStage.setScene(splashScene);
        initStage.initStyle(StageStyle.UNDECORATED);
        initStage.show();

        Stack<String> errors = new Stack<>();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                System.out.println("Loading .npehero...");
                this.updateMessage("Loading .npehero...");
                try {
                    if (!BASE_DIR.exists() && !BASE_DIR.mkdir()) {
                        throw new IOException();
                    }
                    if (!BASE_DIR.isDirectory()) {
                        throw new IOException();
                    }
                } catch (Exception e) {
                    errors.push("Failed to locate .npehero\n" + e);
                }
                System.out.println("Loading settings...");
                this.updateMessage("Loading settings...");
                try {
                    Settings.read();
                    System.out.println("Settings loaded");
                } catch (Exception e) {
                    errors.push("Failed to load settings from file\n" + e);
                }
                System.out.println("Loading controls...");
                this.updateMessage("Loading controls...");
                try {
                    Control.readFromFile();
                    System.out.println("Controls loaded");
                } catch (Exception e) {
                    errors.push("Failed to load controls from file\n" + e);
                }
                System.out.println("Loading levels...");
                this.updateMessage("Loading levels...");
                try {
                    Levels.readData();
                    System.out.println("Loaded " + Levels.list.size() + " levels (" + Levels.getValidList().size() + " valid)");
                } catch (IOException e) {
                    errors.push("Failed to load levels\n");
                }
                return null;
            }
        };

        task.setOnSucceeded(_ -> {
            initStage.close();

            Page last = new MainMenu();
            while (!errors.empty()) {
                last = new ErrorDisplay(errors.pop(), last);
            }
            Driver.setMenu(last);

            primaryStage = new Stage();

            primaryPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            StackPane root = new StackPane(backgroundImage2, backgroundImage, primaryPane);
            Scene primaryScene = new Scene(root, 800,600);

            primaryPane.scaleXProperty().bind(Settings.guiScale);
            primaryPane.scaleYProperty().bind(Settings.guiScale);
            primaryPane.minHeightProperty().bind(root.heightProperty().divide(Settings.guiScale));
            primaryPane.minWidthProperty() .bind(root.widthProperty() .divide(Settings.guiScale));
            primaryPane.maxHeightProperty().bind(root.heightProperty().divide(Settings.guiScale));
            primaryPane.maxWidthProperty() .bind(root.widthProperty() .divide(Settings.guiScale));

//        Cant figure out how to center this
            backgroundImage.fitHeightProperty().bind(primaryScene.heightProperty());
            backgroundImage2.fitHeightProperty().bind(primaryScene.heightProperty());
            backgroundImage.setPreserveRatio(true);
            backgroundImage2.setPreserveRatio(true);

            primaryScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

            primaryStage.setScene(primaryScene);
            primaryStage.setTitle("NPE Hero");

            primaryPane.getStyleClass().remove("scroll-pane");

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
        });

        loading.textProperty().bind(task.messageProperty());
        new Thread(task).start();
    }

    public static void setMenu(Page p) {
        if (currentPage != null) {
            currentPage.onLeave();
        }
        currentPage = p;
        Pane pane = currentPage.getContent();
        pane.setOpacity(0.0);
        primaryPane.setContent(pane);
        pane.prefWidthProperty().bind(primaryPane.widthProperty()); //makes pane fill the window
        pane.prefHeightProperty().bind(primaryPane.heightProperty());
        primaryPane.requestFocus(); //make the pane itself focused by the keyboard navigation so no button is highlighted by default
        currentPage.onView();
        FadeTransition ft = new FadeTransition(Duration.millis(100), currentPage.getContent());
        ft.setInterpolator(Interpolator.EASE_BOTH);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
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

        ScaleTransition st2 = new ScaleTransition(Duration.seconds(0.2), backgroundImage);
        st2.setInterpolator(Interpolator.LINEAR);
        st2.setFromX(1);
        st2.setFromY(1);
        st2.setToX(1.05);
        st2.setToY(1.05);

        ScaleTransition st = new ScaleTransition(Duration.seconds(0.2), backgroundImage2);
        st.setInterpolator(Interpolator.LINEAR);
        st.setFromX(1.05);
        st.setFromY(1.05);
        st.setToX(1.0);
        st.setToY(1.0);

        ParallelTransition pt = new ParallelTransition(ft, st, st2);
        pt.setDelay(Duration.seconds(0.3));
        pt.play();
        st.setOnFinished(_ -> backgroundImage.setImage(image));
    }

    public static void setMenuBackground() {
        setBackground(MENU_BACKGROUND);
    }

    public static URL getResource(String fileName) {
        return Driver.class.getResource(fileName);
    }
}
