package net.sowgro.npehero.editor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

public class ErrorDisplay extends Page {

    private HBox content = new HBox();

    /**
     * Error display with a message and Back button
     * @param message The message to display
     * @param prev The destination of the close button
     */
    public ErrorDisplay(String message, Page prev) {
        Label main = new Label(message);
        main.getStyleClass().add("box");

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, exit);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);
    }

    /**
     * Error display with a message and Cancel and Proceed buttons
     * @param message The message to display
     * @param prev The destination of the Cancel button
     * @param next The destination of the Proceed button
     */
    public ErrorDisplay(String message, Page prev, Page next) {
        Label main = new Label(message);
        main.getStyleClass().add("box");
        main.setPadding(new Insets(10));

        Button exit = new Button();
        exit.setText("Cancel");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        Button nextButton = new Button();
        nextButton.setText("Proceed");
        nextButton.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            Driver.setMenu(next);
        });

        HBox bottom = new HBox(exit, nextButton);
        bottom.setAlignment(Pos.CENTER);
        bottom.setSpacing(10);

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, bottom);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);
    }

    @Override
    public Pane getContent() {
        return content;
    }
}
