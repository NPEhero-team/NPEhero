package gui;

import fallTest.Hbox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LevelSurround extends Pane
{
    //will have param (Level l)
    public LevelSurround()
    {
        Button exit = new Button();
        exit.setText("Exit");
        exit.setOnAction(e -> Driver.setMenu("LevelSelector"));

        Button pause = new Button();
        pause.setText("Pause");

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(exit,pause);
        buttonBox.setAlignment(Pos.TOP_LEFT);
        buttonBox.setSpacing(10);

        Text title = new Text();
        title.setText("Test level 1");
        title.setFill(Color.WHITE);
        title.setFont(new Font(50));

        Text diff = new Text();
        diff.setText("Easy");
        diff.setFill(Color.WHITE);

        VBox textBox = new VBox();
        textBox.setAlignment(Pos.TOP_RIGHT);
        textBox.getChildren().addAll(title,diff);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(buttonBox);
        topBar.setRight(textBox);
        topBar.setPadding(new Insets(10));
        
        Text title = new Text();
        title.setText("Test level 1");
        title.setFill(Color.WHITE);
        title.setFont(new Font(50));

        Text diff = new Text();
        diff.setText("Easy");
        diff.setFill(Color.WHITE);

        VBox textBox = new VBox();
        textBox.setAlignment(Pos.TOP_RIGHT);
        textBox.getChildren().addAll(title,diff);

        HBox centerBox = new HBox();
        centerBox.getStyleClass().add("textBox");

        StackPane root = new StackPane();
        root.getChildren().addAll(topBar,centerBox);

        super.getChildren().add(root);
        root.minWidthProperty().bind(super.minWidthProperty());
        root.minHeightProperty().bind(super.minHeightProperty());
    }
}