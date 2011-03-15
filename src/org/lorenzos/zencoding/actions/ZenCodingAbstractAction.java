
package org.lorenzos.zencoding.actions;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import org.lorenzos.utils.OutputUtils;
import org.lorenzos.zencoding.zeneditor.ZenEditor;
import org.openide.cookies.EditorCookie;
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
		ArrayList<Integer> editorCookieDone = new ArrayList<Integer>();
		for (EditorCookie editorCookie : this.context) {
			if (editorCookieDone.contains(editorCookie.hashCode())) continue;
			editorCookieDone.add(editorCookie.hashCode());
			try {
				JSExecutor jsRunner = JSExecutor.getSingleton();
				ZenEditor editor = ZenEditor.create(editorCookie);
				jsRunner.runAction(editor, this.action);
				editor.restoreInitialScrollingPosition();
			} catch (Exception ex) {
				ex.printStackTrace(OutputUtils.getErrorStream());
			}
		}
	}
	
}
