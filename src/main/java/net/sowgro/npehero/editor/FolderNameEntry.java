package net.sowgro.npehero.editor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.main.Page;

public class FolderNameEntry extends Page {
    private final HBox content = new HBox();

    public interface StringToVoidLambda {
        void run(String name);
    }

    public FolderNameEntry(String itemType, Page prev, StringToVoidLambda next) {

        Label newLevelLabel = new Label("Name of new "+itemType);
        TextField newLevelEntry = new TextField();

        Label folderName = new Label("Folder name");
        TextField folderNameEntry = new TextField();
        folderNameEntry.setEditable(false);
        folderNameEntry.setDisable(true);

        newLevelEntry.setOnKeyTyped(_ -> {
            folderNameEntry.setText(newLevelEntry.getText().toLowerCase().replaceAll("\\W+", "-"));
        });

        VBox newLevelBox = new VBox(newLevelLabel, newLevelEntry, folderName, folderNameEntry);
        newLevelBox.setSpacing(10);
        newLevelBox.getStyleClass().add("box");
        newLevelBox.setPadding(new Insets(10));
        newLevelBox.setPrefWidth(400);

        Button newLevelButton = new Button("Create");
        newLevelButton.setOnAction(_ -> next.run(newLevelEntry.getText()));

        Button cancel = new Button("Cancel");
        cancel.setOnAction(_ -> Driver.setMenu(prev));

        HBox buttonBox = new HBox(cancel, newLevelButton);
        buttonBox.setSpacing(10);

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(newLevelBox, buttonBox);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);
    }

    @Override
    public HBox getContent() {
        return content;
    }
}
