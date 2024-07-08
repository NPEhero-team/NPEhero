module net.sowgro.npehero {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires json.simple;
    requires java.desktop;


    opens net.sowgro.npehero to javafx.fxml;
    exports net.sowgro.npehero.gui;
    exports net.sowgro.npehero;
}