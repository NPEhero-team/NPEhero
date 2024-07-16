package net.sowgro.npehero.gui;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.Control;
import net.sowgro.npehero.main.SoundController;
import org.w3c.dom.events.Event;

import java.util.List;
import java.util.Map;

public class ControlEditor extends Pane {
    public ControlEditor() {

        GridPane controls = new GridPane();
        ScrollPane scrollPane = new ScrollPane(controls);
        scrollPane.getStyleClass().remove("scroll-pane");
        scrollPane.getStyleClass().add("box");
        scrollPane.setPadding(new Insets(10));
        controls.setPadding(new Insets(10));
        controls.setVgap(20);
        controls.setHgap(40);


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
        int i = 0;
        for (Map.Entry<String, List<Control>> section : Control.sections) {
            // section header
            Label sectionLabel = new Label(section.getKey());
            sectionLabel.getStyleClass().add("gray");
            BorderPane sectionBox = new BorderPane();
            sectionBox.setCenter(sectionLabel);
            controls.add(sectionBox, 0, i, 3, 1);
            i++;

            for (Control control : section.getValue()) {

                // label
                Label label = new Label(control.label);
                controls.add(label, 0, i);

                // control button
                ToggleButton controlButton = new ToggleButton("<err>");
                controlButton.setText(keyToString(control.keyProperty.get()));
                control.keyProperty.addListener(_ -> {
                    controlButton.setText(keyToString(control.keyProperty.get()));
                    Control.writeToFile();
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
                        rootBox.addEventFilter(KeyEvent.KEY_PRESSED, keyListener);
                    } else {
                        rootBox.removeEventFilter(KeyEvent.KEY_PRESSED, keyListener);
                    }
                });
                tg.getToggles().add(controlButton);
                controls.add(controlButton, 1, i);

                // reset button
                Button resetButton = new Button("Reset");
                resetButton.setOnMouseClicked(_ -> {
                    control.keyProperty.set(control.defaultKey);
                });
                controls.add(resetButton, 2, i);
                i++;
            }

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
