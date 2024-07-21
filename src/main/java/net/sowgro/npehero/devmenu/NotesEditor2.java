package net.sowgro.npehero.devmenu;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.gameplay.Block;
import net.sowgro.npehero.gameplay.Target;
import net.sowgro.npehero.main.*;
import net.sowgro.npehero.main.Control;

import java.util.concurrent.atomic.AtomicInteger;

public class NotesEditor2 extends Page {
    Difficulty diff;
    ScrollPane scroll = new ScrollPane();
    Pane[] lanes;
    MediaPlayer m;
    Polygon playhead;
    ListProperty<Block> activeNotes = new SimpleListProperty<>(FXCollections.observableArrayList());
    ListProperty<Note> noteList;
    DiffEditor prev;

    public NotesEditor2(Difficulty diff, DiffEditor prev) {
        this.diff = diff;
        noteList = diff.notes.deepCopyList();
        m = new MediaPlayer(new Media(diff.level.song.toURI().toString()));
        this.prev = prev;
    }

    @Override
    public Pane getContent() {
        // Buttons
        VBox actionBox = new VBox();
        actionBox.setSpacing(10);

        Label noteLabel       = new Label("Notes");
        ToggleButton addNote  = new ToggleButton("Add");
        Button delNote        = new Button("Delete");
        ToggleButton moveNote = new ToggleButton("Move");
        actionBox.getChildren().addAll(noteLabel, addNote, delNote, moveNote);

        Label selectionLabel = new Label("Selection");
        Button selectAll     = new Button("Select All");
        Button clearSelect   = new Button("Clear");
        actionBox.getChildren().addAll(selectionLabel, selectAll, clearSelect);

        Label playbackLabel     = new Label("Playback");
        ToggleButton play       = new ToggleButton("Play");
        Button reset            = new Button("Reset");
        ToggleButton scrollLock = new ToggleButton("Scroll Lock");
        actionBox.getChildren().addAll(playbackLabel, play, reset, scrollLock);

        delNote.disableProperty().bind(activeNotes.emptyProperty());
        moveNote.disableProperty().bind(activeNotes.emptyProperty());
        clearSelect.disableProperty().bind(activeNotes.emptyProperty());

        ToggleGroup tg = new ToggleGroup();
        addNote.setToggleGroup(tg);
        moveNote.setToggleGroup(tg);

        // Lanes
        this.lanes = new Pane[5];
        for (int i = 0; i < lanes.length; i++) {
            lanes[i] = new Pane();
        }
        Block sizer = drawBlock(new Note(0, 0));
        for (Pane lane : lanes) {
            lane.prefWidthProperty().bind(sizer.widthProperty());
        }
        Pane rulerLane = new Pane();
        rulerLane.setManaged(false);
        Pane playheadLane = new Pane();
        playheadLane.setOnMouseClicked(e -> {
            m.seek(new Duration(screenPosToSecond(e.getY()) * 1000));
        });

        this.playhead = new Polygon();
        playhead.getPoints().addAll(
                0.0, -10.0,
                20.0, -10.0,
                30.0, 0.0,
                20.0, 10.0,
                0.0, 10.0
        );
        playhead.setFill(Color.WHITE);
        playheadLane.getChildren().add(playhead);

        HBox content = new HBox();
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);
        content.getChildren().addAll(playheadLane, rulerLane);
        content.getChildren().addAll(lanes);

        Line playheadLine = new Line();
        playheadLine.setStartX(0);
        playheadLine.endXProperty().bind(scroll.widthProperty().subtract(80));
        playheadLine.setStartY(0);
        playheadLine.setEndY(0);
        playheadLine.setStroke(Color.WHITE);
        playheadLine.layoutYProperty().bind(playhead.layoutYProperty());

        Pane contentOverlay = new Pane(playheadLine);
        contentOverlay.setPickOnBounds(false);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(content, contentOverlay);

        scroll.setContent(stackPane);
//        scroll.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.35));
        scroll.setMinWidth(400);
//        scroll.prefHeightProperty().bind(super.prefHeightProperty().multiply(0.75));
        scroll.getStyleClass().remove("scroll-pane");
        scroll.getStyleClass().add("box");
        scroll.setPadding(new Insets(5));

        Pane helpBox = new Pane();

        HBox main = new HBox();
        main.getChildren().addAll(scroll, actionBox, helpBox);
        main.setSpacing(10);

        Button exit = new Button();
        exit.setText("Cancel");
        exit.setOnAction(_ -> {
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(prev);
        });

        Button save = new Button();
        save.setText("Done");
        save.setOnAction(_ -> {
            diff.notes.list = noteList;
            diff.notes.writeFile();
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(new DiffEditor(diff, prev.prev));
        });

        HBox buttons = new HBox(save, exit);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);

        // Draw notes
        noteList.forEach(n -> lanes[n.lane].getChildren().add(drawBlock(n)));
        noteList.addListener((ListChangeListener<? super Note>) _ -> {
            // TODO
            for (Pane lane : lanes) {
                lane.getChildren().clear();
            }
            noteList.forEach(n -> lanes[n.lane].getChildren().add(drawBlock(n)));
        });

        // Draw and update ruler
        AtomicInteger lastRuler = new AtomicInteger(-1);
        content.heightProperty().addListener(_ -> {
            int ruler1 = (int) screenPosToSecond(content.getHeight());
            for (int i = lastRuler.get() + 1; i <= ruler1; i++) {
                Label l = new Label(toMinAndSec(i)+" -");
                l.layoutYProperty().bind(secondToScreenPos(i));
                l.setTextFill(Color.WHITE);
                rulerLane.getChildren().add(l);
            }
            lastRuler.set(ruler1);
        });

        VBox centerBox = new VBox();
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setSpacing(10);
        centerBox.getChildren().addAll(main, buttons);
        centerBox.setMinWidth(400);

        HBox rootBox = new HBox();
//        rootBox.prefWidthProperty().bind(super.prefWidthProperty());
//        rootBox.prefHeightProperty().bind(super.prefHeightProperty());
        rootBox.getChildren().add(centerBox);
        rootBox.setAlignment(Pos.CENTER);

        // write notes on key press
        rootBox.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            KeyCode k = e.getCode();
            if (k == Control.LANE0.getKey())           { WriteNote(0); }
            if (k == Control.LANE1.getKey())           { WriteNote(1); }
            if (k == Control.LANE2.getKey())           { WriteNote(2); }
            if (k == Control.LANE3.getKey())           { WriteNote(3); }
            if (k == Control.LANE4.getKey())           { WriteNote(4); }
            if (k == Control.NOTE_DOWN.getKey())       { MoveNoteDown(); }
            if (k == Control.NOTE_UP.getKey())         { MoveNoteUp(); }
            if (k == Control.DELETE_NOTE.getKey())     { delNote.fire(); }
            if (k == Control.CLEAR_SELECTION.getKey()) { clearSelect.fire(); }
            if (k == Control.SCROLL_LOCK.getKey())     { scrollLock.fire(); }
            if (k == Control.PLAY_PAUSE.getKey())      { play.fire(); }
            if (k == Control.SELECT_ALL.getKey())      { selectAll.fire(); }
            e.consume();
        });

        m.currentTimeProperty().addListener(_ -> {
            // TODO
            playhead.layoutYProperty().bind(secondToScreenPos(m.getCurrentTime().toSeconds()));
        });

        play.setOnAction(_ -> {
            if (play.isSelected()) {
                m.play();
            }
            else {
                m.pause();
            }

        });

        scrollLock.setOnAction(_ -> {
            if (scrollLock.isSelected()) {
                // vvalue takes in a value between 0 and 1 NOT a pixel value
                scroll.vvalueProperty().bind(playhead.layoutYProperty().subtract(scroll.heightProperty().divide(2)).divide(content.heightProperty().subtract(scroll.heightProperty())));
            }
            else {
                scroll.vvalueProperty().unbind();
            }


        });

        reset.setOnAction(_ -> {
            m.seek(new Duration(0));
        });

        delNote.setOnAction(_ -> {
            activeNotes.forEach(e -> {
                noteList.remove(e.note);
            });
            activeNotes.clear();
        });

        clearSelect.setOnAction(_ -> {
            activeNotes.forEach(e -> e.setFill(e.color));
            activeNotes.clear();
        });

        selectAll.setOnAction(_ -> {
            activeNotes.clear();
            for (Pane lane : lanes) {
                lane.getChildren().forEach(e -> activeNotes.add((Block) e));
            }
            activeNotes.forEach(e -> e.setFill(Color.WHITE));
        });

        Pane addHelp = addHelp();
        Pane moveHelp = moveHelp();
        addNote.setOnAction(_ -> {
            if (addNote.isSelected()) {
                helpBox.getChildren().clear();
                helpBox.getChildren().add(addHelp);
            }
            else {
                helpBox.getChildren().clear();
            }
        });

        moveNote.setOnAction(_ -> {
            if (moveNote.isSelected()) {
                helpBox.getChildren().clear();
                helpBox.getChildren().add(moveHelp);
            }
            else {
                helpBox.getChildren().clear();
            }
        });

        moveNote.disabledProperty().addListener(_ -> {
            if (moveNote.isDisabled() && moveNote.isSelected()) {
                moveNote.setSelected(false);
                helpBox.getChildren().clear();
            }
        });

        return rootBox;
    }

    @Override
    public void onView() {
        Sound.stopSong();
    }

    @Override
    public void onLeave() {
        m.stop();
        Sound.playSong(Sound.MENU_SONG);
    }

    private Block drawBlock(Note n) {
        Color color = diff.level.colors[n.lane];
        Block b = new Block(color,20, 20, 5, false, n);
        b.heightProperty().bind(scroll.widthProperty().divide(8));
        b.widthProperty().bind(scroll.widthProperty().divide(8));
        b.arcHeightProperty().bind(scroll.widthProperty().divide(25));
        b.arcWidthProperty().bind(scroll.widthProperty().divide(25));
        b.strokeWidthProperty().bind(scroll.widthProperty().divide(120));
        b.layoutYProperty().bind(secondToScreenPos(n.time.add(0)));
        b.setOnMouseClicked(_ -> {
            if (activeNotes.contains(b)) {
                activeNotes.remove(b);
                b.setFill(b.color);
            }
            else {
                activeNotes.add(b);
                b.setFill(Color.WHITE);
            }
        });
        return b;
    }

    private String toMinAndSec(int t) {
        int min = t / 60;
        int sec = t % 60;

        String min2 = min  + "";
        if (min2.length() == 1) {
            min2 = "0" + min2;
        }
        String sec2 = sec  + "";
        if (sec2.length() == 1) {
            sec2 = "0" + sec2;
        }
        return min2 + ":" + sec2;
    }

    private double screenPosToSecond(double screenYPos) {
        return screenYPos / (scroll.getHeight() * 0.9);
    }

    private DoubleBinding secondToScreenPos(DoubleBinding second) {
        return scroll.heightProperty().multiply(second).multiply(0.9);
    }

    private DoubleBinding secondToScreenPos(double second) {
        return scroll.heightProperty().multiply(second).multiply(0.9);
    }

    private void WriteNote(int col) {
        Note tmp = new Note(screenPosToSecond(playhead.getLayoutY()), col);
        noteList.add(tmp);
    }

    private void MoveNoteUp() {
        activeNotes.forEach(n -> n.note.time.setValue(n.note.time.get() + 0.01));
    }

    private void MoveNoteDown() {
        activeNotes.forEach(n -> n.note.time.setValue(n.note.time.get() - 0.01));
    }

    private Pane addHelp() {
        Label l1 = new Label("Use the following keys");
        HBox hb = new HBox(
                new Target(diff.level.colors[0], 40, 40, 10, Control.LANE0.targetString()),
                new Target(diff.level.colors[1], 40, 40, 10, Control.LANE1.targetString()),
                new Target(diff.level.colors[2], 40, 40, 10, Control.LANE2.targetString()),
                new Target(diff.level.colors[3], 40, 40, 10, Control.LANE3.targetString()),
                new Target(diff.level.colors[4], 40, 40, 10, Control.LANE4.targetString())
        );
        hb.setSpacing(10);
        hb.setAlignment(Pos.CENTER_LEFT);
        Label l2 = new Label("to write a new note at \nthe play head's position.");

        VBox ret = new VBox(l1, hb, l2);
        ret.setPadding(new Insets(10));
        ret.getStyleClass().add("box");
        return ret;
    }

    private Pane moveHelp() {
        Label l1 = new Label("Use the");
        HBox hb = new HBox(new Target(Color.BLACK, 40, 40, 10, Control.NOTE_UP.targetString()), new Label("and"), new Target(Color.BLACK, 40, 40, 10, Control.NOTE_DOWN.targetString()), new Label("keys"));
        hb.setSpacing(10);
        hb.setAlignment(Pos.CENTER_LEFT);
        Label l2 = new Label("to move the selected \nnote(s) up and down.");

        VBox ret = new VBox(l1, hb, l2);
        ret.setPadding(new Insets(10));
        ret.getStyleClass().add("box");
        ret.setLayoutY(120);
        return ret;
    }
}
