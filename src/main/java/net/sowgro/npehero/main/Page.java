package net.sowgro.npehero.main;

import javafx.scene.layout.Pane;

public abstract class Page {
    abstract public Pane getContent(); // must be implemented
    public void onView() {} // can optionally be implemented
    public void onLeave() {}
}
