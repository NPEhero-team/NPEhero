package net.sowgro.npehero.editor;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import net.sowgro.npehero.Driver;
import net.sowgro.npehero.gameplay.Block;
import net.sowgro.npehero.gameplay.Target;
import net.sowgro.npehero.levelapi.Difficulty;
import net.sowgro.npehero.levelapi.Note;
import net.sowgro.npehero.main.*;
import net.sowgro.npehero.main.Control;

import java.io.IOException;
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
    DoubleProperty newEndTime = new SimpleDoubleProperty(0);

    private HBox content = new HBox();

    public NotesEditor2(Difficulty diff, DiffEditor prev) {
        this.diff = diff;
        noteList = diff.notes.deepCopyList();
        m = new MediaPlayer(diff.level.song);
        this.prev = prev;

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
        Button setEnd         = new Button("End Here");
        actionBox.getChildren().addAll(playbackLabel, play, reset, scrollLock, setEnd);

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
//        rulerLane.setManaged(false);

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
//        playhead.setOnMouseDragged(e -> {
//            scroll.get
//            playhead.layoutYProperty().bind(secondToScreenPos());
//        });

        HBox scrollContent = new HBox();
        scrollContent.setAlignment(Pos.CENTER);
        scrollContent.setSpacing(10);
        scrollContent.getChildren().addAll(playheadLane, rulerLane);
        scrollContent.getChildren().addAll(lanes);

        Line playheadLine = new Line();
        playheadLine.setStartX(0);
        playheadLine.endXProperty().bind(scroll.widthProperty().subtract(80));
        playheadLine.setStartY(0);
        playheadLine.setEndY(0);
        playheadLine.setStroke(Color.WHITE);
        playheadLine.layoutYProperty().bind(playhead.layoutYProperty());

        Line endLine = new Line();
        endLine.setStartX(0);
        endLine.endXProperty().bind(scroll.widthProperty().subtract(80));
        endLine.setStartY(0);
        endLine.setEndY(0);
        endLine.setStroke(Color.RED);

        Pane contentOverlay = new Pane(playheadLine, endLine);
        contentOverlay.setPickOnBounds(false);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(scrollContent, contentOverlay);

        scroll.setContent(stackPane);
//        scroll.prefWidthProperty().bind(super.prefWidthProperty().multiply(0.35));
//        scroll.setMinWidth(400);
        scroll.prefHeightProperty().bind(content.heightProperty().multiply(0.75));
        scroll.prefWidthProperty().bind(scroll.prefHeightProperty().multiply(0.70));
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
        save.setText("Save");
        save.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            diff.notes.list = noteList;
            try {
                diff.notes.writeFile();
            } catch (IOException e) {
                // TODO
                throw new RuntimeException(e);
            }
            diff.endTime = newEndTime.get();
            diff.bpm = 0.0;
            try {
                diff.readMetadata();
            } catch (IOException e) {
                // TODO
            }
            Sound.playSfx(Sound.BACKWARD);
            Driver.setMenu(new DiffEditor(diff, prev.prev));
        });

        HBox buttons = new HBox(save, exit);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);

        Runnable updateEndLine = () -> {
//            System.out.println("LISTENER CALLED");
            if (newEndTime.get() != 0) {
                endLine.layoutYProperty().bind(secondToScreenPos(newEndTime.get()));
            }
            else {
                endLine.layoutYProperty().bind(secondToScreenPos(m.getTotalDuration().toSeconds()));
            }
        };

        newEndTime.addListener((_, _, _) -> updateEndLine.run());
        newEndTime.set(diff.endTime);
        updateEndLine.run();

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
        scrollContent.heightProperty().addListener(_ -> {
            int ruler1 = (int) screenPosToSecond(scrollContent.getHeight());
            for (int i = lastRuler.get() + 1; i <= ruler1; i++) {
                Label l = new Label(toMinAndSec(i)+" -");
                l.layoutYProperty().bind(secondToScreenPos(i));
                l.setTextFill(Color.WHITE);
                rulerLane.getChildren().add(l);
            }
            lastRuler.set(ruler1);
        });

        VBox centerBox = new VBox();
        centerBox.getChildren().addAll(main, buttons);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        content.getChildren().add(centerBox);
        content.setAlignment(Pos.CENTER);

        // write notes on key press
        content.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
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
            Sound.playSfx(Sound.FORWARD);
            if (play.isSelected()) {
                m.play();
            }
            else {
                m.pause();
            }

        });

        scrollLock.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            if (scrollLock.isSelected()) {
                // vvalue takes in a value between 0 and 1 NOT a pixel value
                scroll.vvalueProperty().bind(playhead.layoutYProperty().subtract(scroll.heightProperty().divide(2)).divide(scrollContent.heightProperty().subtract(scroll.heightProperty())));
            }
            else {
                scroll.vvalueProperty().unbind();
            }


        });

        reset.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            m.seek(new Duration(0));
        });

        delNote.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            activeNotes.forEach(e -> {
                noteList.remove(e.note);
            });
            activeNotes.clear();
        });

        clearSelect.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            activeNotes.forEach(e -> e.setFill(e.color));
            activeNotes.clear();
        });

        selectAll.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            activeNotes.clear();
            for (Pane lane : lanes) {
                lane.getChildren().forEach(e -> activeNotes.add((Block) e));
            }
            activeNotes.forEach(e -> e.setFill(Color.WHITE));
        });

        Pane addHelp = addHelp();
        Pane moveHelp = moveHelp();
        addNote.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            if (addNote.isSelected()) {
                helpBox.getChildren().clear();
                helpBox.getChildren().add(addHelp);
            }
            else {
                helpBox.getChildren().clear();
            }
        });

        moveNote.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
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

        setEnd.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            double tmp = screenPosToSecond(playhead.getLayoutY());
            if (Math.round(tmp*10)/10 == Math.round(m.getTotalDuration().toSeconds() * 10)/10) {
                newEndTime.set(0);
            }
            else {
                newEndTime.set(tmp);
            }
        });

        activeNotes.addListener((_, _, _) -> {
            if (activeNotes.isEmpty()) {
                selectionLabel.setText("Selection");
            }
            else {
                selectionLabel.setText("Selection (" + activeNotes.size() + ")");
            }
        });
    }

    @Override
    public Pane getContent() {
        return content;
    }

    @Override
    public void onView() {
        Sound.stopSong();
        m.play();
        m.pause();
        m.seek(Duration.ZERO);
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
        activeNotes.forEach(n -> n.note.time.setValue(n.note.time.get() - 0.01));
    }

    private void MoveNoteDown() {
        activeNotes.forEach(n -> n.note.time.setValue(n.note.time.get() + 0.01));
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
