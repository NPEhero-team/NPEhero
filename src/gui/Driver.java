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
import main.LevelController;

public class Driver extends Application 
{

    static Stage primaryStage;
    static Pane primaryPane = new Pane();

    public static void main(String[] args) 
    {
        launch(args);
    }
    
    @Override
    public void start(Stage newPrimaryStage) 
    {
        new LevelController();
        primaryStage = newPrimaryStage;

        Scene primaryScene = new Scene(primaryPane, 800, 600);
        primaryScene.getStylesheets().add("gui/style.css");

        primaryStage.setScene(primaryScene);
        primaryStage.setTitle("NPE Hero");

        setMenu(new MainMenu());
        setBackground("assets/water.png");

        primaryStage.show();
    }

    public static void setMenu(Pane pane)
    {
        if (! primaryPane.getChildren().isEmpty())
        {
            primaryPane.getChildren().remove(0);
        }
        pane.prefWidthProperty().bind(primaryPane.widthProperty()); 
        pane.prefHeightProperty().bind(primaryPane.heightProperty());
        primaryPane.getChildren().add(pane);
        primaryPane.requestFocus();
    }

    public static Pane getMenu(){
        return (Pane) primaryPane.getChildren().get(0);
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
