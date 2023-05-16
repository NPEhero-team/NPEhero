package fallTest;

import javafx.animation.*;
import javafx.util.Duration;

public class NoteNode {
	private NoteField info;
	private Block vis;
	TranslateTransition anim = new TranslateTransition(Duration.millis(TIME));
	public NoteNode(NoteField newInfo, Block newVis) {
		vis=newVis;
		info = newInfo;
		anim.setNode(vis);
		anim.setByY();
		anim.setCycleCount(1);
		anim.setAutoReverse(false);
		anim.play();
	}
}
