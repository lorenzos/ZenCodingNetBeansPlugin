
package org.lorenzos.zencoding.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import org.lorenzos.utils.OutputUtils;
import org.lorenzos.zencoding.zeneditor.ZenEditor;
import org.openide.cookies.EditorCookie;
import org.openide.text.NbDocument;
import ru.zencoding.JSExecutor;

public abstract class ZenCodingAbstractAction implements ActionListener {

	protected List<EditorCookie> context;
	protected String action;

	public ZenCodingAbstractAction(List<EditorCookie> context, String action) {
		this.context = context;
		this.action = action;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {

		// For each EditorCookie
		ArrayList<Integer> editorCookieDone = new ArrayList<Integer>();
		for (EditorCookie editorCookie : this.context) {
			if (editorCookieDone.contains(editorCookie.hashCode())) continue;
			editorCookieDone.add(editorCookie.hashCode()); // Store

			// Do action
			try {

				// Create JS executor and setup a Zen editor
				final JSExecutor jsRunner = JSExecutor.getSingleton();
				final ZenEditor editor = ZenEditor.create(editorCookie);

				// Create a runnable that run the Zen action
				final String zenAction = this.action;
				Runnable runZenAction = new Runnable() {
					@Override public void run() {
						jsRunner.runAction(editor, zenAction);
					}
				};

				// Run it in an atomic Undo/Redo
				NbDocument.runAtomic(editor.getDocument(), runZenAction);

				// Restore scrolling position
				editor.restoreInitialScrollingPosition();

			} catch (Exception ex) {
				ex.printStackTrace(OutputUtils.getErrorStream());
			}

		}
	}
	
}
