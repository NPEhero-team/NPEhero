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

    public static void quit()
    {
        try {
            Platform.exit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
