package net.sowgro.npehero.gui;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.editor.ErrorDisplay;
import net.sowgro.npehero.main.Control;
import net.sowgro.npehero.main.Page;
import net.sowgro.npehero.main.Sound;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ControlEditor extends Page {

    private final HBox content = new HBox();

    public ControlEditor() {

        GridPane controls = new GridPane();
        ScrollPane scrollPane = new ScrollPane(controls);
        scrollPane.getStyleClass().remove("scroll-pane");
        scrollPane.getStyleClass().add("box");
        scrollPane.setPadding(new Insets(10));
        controls.setPadding(new Insets(10));
        controls.setVgap(20);
        controls.setHgap(40);

        scrollPane.setPrefWidth(700);
        scrollPane.prefHeightProperty().bind(content.prefHeightProperty().multiply(0.75));

        Button exit = new Button();
        exit.setText("Back");
        exit.setOnAction(e -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(new SettingsEditor());
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(scrollPane, exit);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);

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
                    try {
                        Control.writeToFile();
                    } catch (IOException e) {
                        Driver.setMenu(new ErrorDisplay("An error occured while saving your controls\n"+e, this));
                        e.printStackTrace();
                    }
                });
                controlButton.setOnAction(_ -> {
                    EventHandler<KeyEvent> keyListener = new EventHandler<>() {
                        @Override
                        public void handle(KeyEvent k) {
                            control.keyProperty.set(k.getCode());
                            content.removeEventFilter(KeyEvent.KEY_PRESSED, this);
                            controlButton.setSelected(false);
                            k.consume();
                        }
                    };
                    if (controlButton.isSelected()) {
                        content.addEventFilter(KeyEvent.KEY_PRESSED, keyListener);
                    } else {
                        content.removeEventFilter(KeyEvent.KEY_PRESSED, keyListener);
                    }
                });
                tg.getToggles().add(controlButton);
                controls.add(controlButton, 1, i);

                // reset button
                Button resetButton = new Button("Reset");
                resetButton.setOnAction(_ -> {
                    control.keyProperty.set(control.defaultKey);
                });
                controls.add(resetButton, 2, i);
                i++;
            }

        }
    }

    @Override
    public Pane getContent() {
        return content;
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
