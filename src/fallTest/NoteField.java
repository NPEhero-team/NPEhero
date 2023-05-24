/*Name:	Guitar Hero Project
 *Description: Contains the information for a single note on the field
 */
package fallTest;

import javafx.animation.TranslateTransition;

public class NoteField
{
    private Block note;
    private TranslateTransition anim;

    public NoteField(Block newNote, TranslateTransition newAnim) {
        note = newNote;
        anim = newAnim;
    }

    public Block getNote() {
        return note;
    }

    public TranslateTransition getAnim() {
        return anim;
    }
}
