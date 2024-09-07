package net.sowgro.npehero.editor;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
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
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class NotesEditor2 extends Page {
    private final DoubleBinding scaleBind;
    Difficulty diff;
    ScrollPane scroll = new ScrollPane();
    Pane[] lanes;
    MediaPlayer m;
    Polygon playhead;
    ListProperty<Block> selectedNotes = new SimpleListProperty<>(FXCollections.observableArrayList());
    ListProperty<Note> noteList;
    DiffEditor prev;
    DoubleProperty newEndTime = new SimpleDoubleProperty(0);
    CheckBox selectMultiple;

    private final HBox content = new HBox();

    public NotesEditor2(Difficulty diff, DiffEditor prev) {
        this.diff = diff;
        noteList = diff.notes.deepCopyList();
        m = new MediaPlayer(diff.level.song);
        m.volumeProperty().bind(Settings.musicVol);
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
        selectMultiple       = new CheckBox("Select multiple");
        Button selectAll     = new Button("Select All");
        Button clearSelect   = new Button("Clear");
        actionBox.getChildren().addAll(selectionLabel, selectMultiple, selectAll, clearSelect);

        Label playbackLabel     = new Label("Playback");
        ToggleButton play       = new ToggleButton("Play");
        Button reset            = new Button("Reset");
        ToggleButton scrollLock = new ToggleButton("Scroll Lock");
        Button setEnd         = new Button("End Here");
        actionBox.getChildren().addAll(playbackLabel, play, reset, scrollLock, setEnd);

        delNote.disableProperty().bind(selectedNotes.emptyProperty());
        moveNote.disableProperty().bind(selectedNotes.emptyProperty());
        clearSelect.disableProperty().bind(selectedNotes.emptyProperty());

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
//            System.out.println("dbg: "+e.getY());
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
        // TODO
//        playhead.setOnMouseDragged(e -> {
//            scroll.get
//            playhead.layoutYProperty().bind(secondToScreenPos());
//        });

        Block trailBlazer = new Block(Color.BLACK, false, null);
        trailBlazer.layoutYProperty().bind(playhead.translateYProperty().add(scroll.heightProperty().divide(2)));
        trailBlazer.setVisible(false);
        playheadLane.getChildren().add(trailBlazer);

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
        playheadLine.layoutYProperty().bind(playhead.translateYProperty());
//        playheadLine.layoutYProperty().bind(secondToScreenPos(m.getCurrentTime().toSeconds()));
//        m.currentTimeProperty().addListener((_, _, _) -> {
//            playheadLine.layoutYProperty().bind(secondToScreenPos(m.getCurrentTime().toSeconds()));
//        });

        Line endLine = new Line();
        endLine.setStartX(0);
        endLine.endXProperty().bind(scroll.widthProperty().subtract(80));
        endLine.setStartY(0);
        endLine.setEndY(0);
        endLine.setStroke(Color.RED);

        HBox main = new HBox();

        Pane contentOverlay = new Pane(playheadLine, endLine);
        contentOverlay.setPickOnBounds(false);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(scrollContent, contentOverlay);

//        var contentHeight = content.heightProperty().multiply(0.75);
        BorderPane scrollHolder = new BorderPane(scroll);
        scroll.setContent(stackPane);
//        scroll.prefHeightProperty().bind(content.heightProperty().multiply(0.75));
        final int SCROLLPORT_NATIVE_HEIGHT = 864;
        scroll.setMinHeight(SCROLLPORT_NATIVE_HEIGHT);
        scroll.setMinWidth(SCROLLPORT_NATIVE_HEIGHT * 0.72);
        scroll.setMaxHeight(SCROLLPORT_NATIVE_HEIGHT);
        scroll.setMaxWidth(SCROLLPORT_NATIVE_HEIGHT * 0.72);
        scaleBind = scrollHolder.heightProperty().divide(SCROLLPORT_NATIVE_HEIGHT);
        scroll.scaleXProperty().bind(scaleBind);
        scroll.scaleYProperty().bind(scaleBind);
//        scroll.prefWidthProperty().bind(scroll.heightProperty().multiply(0.72));
//        scroll.minWidthProperty().bind(scroll.heightProperty().multiply(0.72));
        scroll.getStyleClass().remove("scroll-pane");
        scroll.getStyleClass().add("box");
        scroll.setPadding(new Insets(5));
//        scaleBind.addListener((_, _, v) -> {
//            scroll.setStyle("-fx-font-size: " + (1/v.doubleValue()) * 10);
//        });

        scrollHolder.minHeightProperty().bind(main.prefHeightProperty());
        scrollHolder.minWidthProperty() .bind(main.prefHeightProperty().multiply(0.72));
        scrollHolder.maxHeightProperty().bind(main.prefHeightProperty());
        scrollHolder.maxWidthProperty() .bind(main.prefHeightProperty().multiply(0.72));

        Pane helpBox = new Pane();

        ScrollPane actionScroll = new ScrollPane(actionBox);
        actionScroll.getStyleClass().remove("scroll-pane");
        actionScroll.prefWidthProperty().bind(actionBox.widthProperty().add(20));

        main.getChildren().addAll(scrollHolder, actionScroll, helpBox);
        main.setSpacing(10);
        main.maxWidthProperty().bind(content.widthProperty().multiply(0.95));
        main.prefHeightProperty().bind(content.prefHeightProperty().multiply(0.80));

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
                diff.writeMetadata();
            } catch (IOException e) {
                e.printStackTrace();
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
            int ruler1 = (int) (screenPosToSecond(scrollContent.getHeight()) * 10);
            for (int i = lastRuler.get() + 1; i <= ruler1; i++) {
                Label l= new Label();
                if (i % 10 == 0) {
                    l.setText(toMinAndSec(i / 10) + " - ");
                }
                else {
                    l.setText("-");
                    l.getStyleClass().add("gray");
                }
                l.layoutYProperty().bind(secondToScreenPos(i / 10.0));
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
            if (k == Control.LANE0.getKey())           { e.consume(); WriteNote(0); }
            if (k == Control.LANE1.getKey())           { e.consume(); WriteNote(1); }
            if (k == Control.LANE2.getKey())           { e.consume(); WriteNote(2); }
            if (k == Control.LANE3.getKey())           { e.consume(); WriteNote(3); }
            if (k == Control.LANE4.getKey())           { e.consume(); WriteNote(4); }
            if (k == Control.NOTE_DOWN.getKey())       { e.consume(); MoveNoteDown(); }
            if (k == Control.NOTE_UP.getKey())         { e.consume(); MoveNoteUp(); }
            if (k == Control.DELETE_NOTE.getKey())     { e.consume(); delNote.fire(); }
            if (k == Control.CLEAR_SELECTION.getKey()) { e.consume(); clearSelect.fire(); }
            if (k == Control.SCROLL_LOCK.getKey())     { e.consume(); scrollLock.fire(); }
            if (k == Control.PLAY_PAUSE.getKey())      { e.consume(); play.fire(); }
            if (k == Control.SELECT_ALL.getKey())      { e.consume(); selectAll.fire(); }
            if (k == Control.SELECT_MULTIPLE.getKey()) { e.consume(); selectMultiple.fire(); }
        });
        content.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            KeyCode k = e.getCode();
            if (k == Control.SELECT_MULTIPLE.getKey()) { e.consume(); selectMultiple.fire(); }
        });

        m.currentTimeProperty().addListener((_ ,oldValue ,newValue) -> {
            var diffr = newValue.toSeconds() - oldValue.toSeconds();
            TranslateTransition anim = new TranslateTransition(Duration.seconds(0.09), playhead);
            anim.byYProperty().bind(secondToScreenPos(diffr));
            anim.setInterpolator(Interpolator.LINEAR);
            anim.play();
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
                scroll.vvalueProperty().bind(playhead.translateYProperty().subtract(scroll.heightProperty().divide(2)).divide(scrollContent.heightProperty().subtract(scroll.heightProperty())));
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
            selectedNotes.forEach(e -> {
                noteList.remove(e.note);
            });
            selectedNotes.clear();
        });

        clearSelect.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
//            selectedNotes.forEach(e -> e.setStroke(Color.TRANSPARENT));
            selectedNotes.clear();
        });

        selectAll.setOnAction(_ -> {
            Sound.playSfx(Sound.FORWARD);
            selectedNotes.clear();
            for (Pane lane : lanes) {
                lane.getChildren().forEach(e -> selectedNotes.add((Block) e));
            }
//            selectedNotes.forEach(e -> e.setStroke(Color.WHITE));
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
            double tmp = screenPosToSecond(playhead.getTranslateY());
            if (Math.round(tmp*10)/10 == Math.round(m.getTotalDuration().toSeconds() * 10)/10) {
                newEndTime.set(0);
            }
            else {
                newEndTime.set(tmp);
            }
        });

        selectedNotes.addListener((ListChangeListener<? super Block>) (arg) -> {
            selectionLabel.setText("Selection (" + selectedNotes.size() + ")");
            while (arg.next()) {
                arg.getAddedSubList().forEach(n -> n.setStroke(Color.WHITE));
                arg.getRemoved().forEach(n -> n.setStroke(Color.TRANSPARENT));
            }
        });
    }

    @Override
    public Pane getContent() {
        return content;
    }

    @Override
    public void onView() {
        var sb = getScrollBar(scroll);
        if (sb != null) {
            sb.prefWidthProperty().bind(scaleBind.multiply(1 / 17.0));
        } else {
            System.out.println("No scrollbar :(");
        }
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
        Block b = new Block(color, false, n);
//        var sizeBind = scroll.widthProperty().divide(8);
//        b.heightProperty().bind(sizeBind);
//        b.widthProperty().bind(sizeBind);
//        var arcBind = scroll.widthProperty().divide(30);
//        b.arcHeightProperty().bind(arcBind);
//        b.arcWidthProperty().bind(arcBind);
        var sizeBind = scroll.heightProperty().multiply(87/1080.0);
        b.heightProperty().bind(sizeBind);
        b.widthProperty().bind(sizeBind);
        var arcBind = scroll.heightProperty().multiply(20/1080.0);
        b.arcHeightProperty().bind(arcBind);
        b.arcWidthProperty().bind(arcBind);
        b.strokeWidthProperty().bind(scroll.heightProperty().multiply(5/1080.0));
        b.layoutYProperty().bind(secondToScreenPos(n.time.add(0)));
        b.setOnMouseClicked(_ -> {
            if (selectedNotes.contains(b)) {
                if (selectMultiple.isSelected() || selectedNotes.size() == 1) {
                    selectedNotes.remove(b);
                } else {
                    selectedNotes.clear();
                    selectedNotes.add(b);
                }
            }
            else {
                if (!selectMultiple.isSelected()) {
                    selectedNotes.clear();
                }
                selectedNotes.add(b);
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
        Note tmp = new Note(screenPosToSecond(playhead.getTranslateY()), col);
        noteList.add(tmp);
    }

    private void MoveNoteUp() {
        selectedNotes.forEach(n -> n.note.time.setValue(n.note.time.get() - 0.01));
    }

    private void MoveNoteDown() {
        selectedNotes.forEach(n -> n.note.time.setValue(n.note.time.get() + 0.01));
    }

    private Pane addHelp() {
        Label l1 = new Label("Use the following keys");
        HBox hb = new HBox(
                createTarget(diff.level.colors[0], Control.LANE0),
                createTarget(diff.level.colors[1], Control.LANE1),
                createTarget(diff.level.colors[2], Control.LANE2),
                createTarget(diff.level.colors[3], Control.LANE3),
                createTarget(diff.level.colors[4], Control.LANE4)
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
        HBox hb = new HBox(
                createTarget(Color.BLACK, Control.NOTE_UP),
                new Label("and"),
                createTarget(Color.BLACK, Control.NOTE_DOWN),
                new Label("keys")
        );
        hb.setSpacing(10);
        hb.setAlignment(Pos.CENTER_LEFT);
        Label l2 = new Label("to move the selected \nnote(s) up and down.");

        VBox ret = new VBox(l1, hb, l2);
        ret.setPadding(new Insets(10));
        ret.getStyleClass().add("box");
        ret.setLayoutY(120);
        return ret;
    }

    private Target createTarget(Color color, Control control) {
        Target target = new Target(color, control.targetString());
        target.rect.setWidth(40);
        target.rect.setHeight(40);
        target.rect.setArcHeight(5);
        target.rect.setArcWidth(5);
        target.rect.setStrokeWidth(3);
        return target;
    }

    private ScrollBar getScrollBar(ScrollPane s) {
        Set<Node> nodes = s.lookupAll(".scroll-bar");
        for (final Node node : nodes) {
            if (node instanceof ScrollBar sb) {
                return sb;
            }
        }
        return null;
    }
}
