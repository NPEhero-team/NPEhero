package net.sowgro.npehero.main;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import net.sowgro.npehero.Driver;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ValidIndicator extends Region {


    private final Tooltip diffLabelTooltip = new Tooltip();

    public ValidIndicator() {
        this.setScaleX(0.7);
        this.setScaleY(0.7);
        this.prefWidthProperty().bind(super.heightProperty());
    }

    public void setValid() {
        this.setShape(null);
        this.setBackground(Background.EMPTY);

        Tooltip.uninstall(this, diffLabelTooltip);
    }

    public void setInvalid(String reason) {
        SVGPath diffLabelIcon = new SVGPath();
        diffLabelIcon.setContent(pathFromSvg(Driver.getResource("error.svg")));
        this.setShape(diffLabelIcon);
        this.setBackground(Background.fill(Color.RED));

        diffLabelTooltip.setText(reason);
        diffLabelTooltip.setShowDelay(Duration.ZERO);
        Tooltip.install(this, diffLabelTooltip);
    }

    private String pathFromSvg(URL url) {
        try {
            String fileContent = Files.readString(Paths.get(url.toURI()));
            String startKey = "<path d=\"";
            String endKey = "\"";
            int startIndex = fileContent.indexOf(startKey) + startKey.length();
            int endIndex = fileContent.substring(startIndex).indexOf(endKey) + startIndex;
            return fileContent.substring(startIndex, endIndex);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
