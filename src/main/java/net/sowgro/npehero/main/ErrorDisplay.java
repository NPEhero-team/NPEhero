package net.sowgro.npehero.main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.Driver;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorDisplay extends Page {

    private final HBox content = new HBox();

    /**
     * Error display with a message and Back button
     * @param message The message to display
     * @param prev The destination of the close button
     */
    public ErrorDisplay(String message, Page prev) {
        Label main = new Label(message);
        main.getStyleClass().add("box");
        main.setPadding(new Insets(10));
        main.setWrapText(true);

        Button exit = new Button();
        exit.setText("Ok");
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
     * Error display with a message, exception and Back button
     * @param message The message to display
     * @param e The exception that occurred
     * @param prev The destination of the close button
     */
    public ErrorDisplay(String message, Exception e, Page prev) {
        Label title = new Label(message);
        title.setWrapText(true);

        Label exView = new Label(e.toString());
        exView.getStyleClass().add("red");

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString(); // stack trace as a string

        Label stackTrace = new Label(sStackTrace);
        stackTrace.getStyleClass().addAll("red", "box");
        stackTrace.setVisible(false);
        stackTrace.setManaged(false);

        Button exit = new Button();
        exit.setText("Ok");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        Button printStack = new Button("Print to console");
        printStack.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            e.printStackTrace();
        });

        ToggleButton showStack = new ToggleButton("Show Stack Trace");
        stackTrace.managedProperty().bind(showStack.selectedProperty());
        stackTrace.visibleProperty().bind(showStack.selectedProperty());

        HBox buttonBox = new HBox(exit, showStack, printStack);
        buttonBox.setSpacing(10);

        VBox main = new VBox(title, exView, stackTrace);
        main.getStyleClass().add("box");
        main.setPadding(new Insets(10));
        main.setSpacing(10);

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, buttonBox);
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
        main.setWrapText(true);

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
