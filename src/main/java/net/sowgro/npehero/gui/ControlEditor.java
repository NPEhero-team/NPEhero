package net.sowgro.npehero.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.Control;
import net.sowgro.npehero.main.SoundController;
import org.w3c.dom.events.Event;

public class ControlEditor extends Pane {
    public ControlEditor() {

        GridPane controls = new GridPane();
        ScrollPane scrollPane = new ScrollPane(controls);
        scrollPane.getStyleClass().remove("scroll-pane");
        scrollPane.getStyleClass().add("box");
        scrollPane.setPadding(new Insets(10));
        controls.setPadding(new Insets(10));
        controls.setHgap(40);
        controls.setVgap(20);


        scrollPane.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.35));
        scrollPane.setMinWidth(400);
        scrollPane.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            SoundController.playSfx(SoundController.BACKWARD);
            Driver.setMenu(new Settings());
        });

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(scrollPane,exit);
        centerBox.setMinWidth(400);

        HBox rootBox = new HBox();
        rootBox.prefWidthProperty().bind(super.prefWidthProperty());
        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().add(centerBox);
        rootBox.setAlignment(Pos.CENTER);

        ToggleGroup tg = new ToggleGroup();
        for (int i = 0; i < Control.values().length; i++) {
            Control control = Control.values()[i];


            // label
            Label label = new Label(control.label);
            controls.add(label, 0, i);

            // control button
            ToggleButton controlButton = new ToggleButton("<err>");
            controlButton.setText(keyToString(control.keyProperty.get()));
            control.keyProperty.addListener(_ -> {
                System.out.println(control.label + " set to " + control.keyProperty.get());
                System.out.println(controlButton.getText());
                controlButton.setText(keyToString(control.keyProperty.get()));
                net.sowgro.npehero.main.Control.writeToFile();
            });
            controlButton.setOnMouseClicked(_ -> {
                EventHandler<KeyEvent> keyListener = new EventHandler<>() {
                    @Override
                    public void handle(KeyEvent k) {
                        control.keyProperty.set(k.getCode());
                        rootBox.removeEventFilter(KeyEvent.KEY_PRESSED, this);
                        controlButton.setSelected(false);
                        k.consume();
                    }
                };
                if (controlButton.isSelected()) {
                    System.out.println("Event registered");
                    rootBox.addEventFilter(KeyEvent.KEY_PRESSED, keyListener);
                }
                else {
                    System.out.println("Event un-registered");
                    rootBox.removeEventFilter(KeyEvent.KEY_PRESSED, keyListener);
                }
            });
            tg.getToggles().add(controlButton);
            controls.add(controlButton, 1, i);

            // label button
            Button resetButton = new Button("Reset");
            resetButton.setOnMouseClicked(_ -> {
                control.keyProperty.set(control.defaultKey);
            });
            controls.add(resetButton, 2, i);

        }

        super.getChildren().add(rootBox);
    }

    public String keyToString(KeyCode key) {
        if (key == null) {
            return "<unset>";
        }
        else {
            return key.toString();
        }
    }
}
