package gui;

import java.util.HashMap;

import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Driver extends Application 
{

    static HashMap<String,Pane> menus = new HashMap<String,Pane>();
    static Pane primaryPane = new Pane();

    public static void main(String[] args) 
    {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) 
    {
        menus.put("MainMenu", new MainMenu());
        menus.put("LevelSelector", new LevelSelector());
        menus.put("Settings", new Settings());
        menus.put("Leaderboard", new Leaderboard());

        for (Pane value : menus.values()) {
            value.minWidthProperty().bind(primaryStage.widthProperty()); 
            value.minHeightProperty().bind(primaryStage.heightProperty());
        }

        primaryPane.getChildren().add(menus.get("MainMenu"));
        primaryPane.minWidthProperty().bind(primaryStage.widthProperty()); 
        primaryPane.minHeightProperty().bind(primaryStage.heightProperty());
        setBackground("assets/water.png");

        Scene primaryScene = new Scene(primaryPane, 800, 600);
        primaryScene.getStylesheets().add("gui/style.css");

        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("NPE Hero");
        primaryStage.show();
        primaryStage.setFullScreen(true);
    }


    public static void switchMenu(String name)
    {
        primaryPane.getChildren().remove(0);
        primaryPane.getChildren().add(menus.get(name));
    }

    public static void setBackground(String url)
    {
        primaryPane.setBackground(new Background(
            new BackgroundImage(
                    new Image(url),
                    BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                    new BackgroundPosition(Side.LEFT, 0, true, Side.BOTTOM, 0, true),
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, true)
            )));
    }
}
