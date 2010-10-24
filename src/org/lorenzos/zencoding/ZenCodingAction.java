
package org.lorenzos.zencoding;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.lorenzos.utils.*;
import org.openide.cookies.EditorCookie;
import ru.zencoding.JSExecutor;

public final class ZenCodingAction implements ActionListener {

	private final EditorCookie context;

	public ZenCodingAction(EditorCookie context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		try {
			JSExecutor jsRunner = JSExecutor.getSingleton();
			ZenEditor editor = ZenEditor.create(context);
			jsRunner.runAction(editor, "expand_abbreviation");
		} catch (Exception ex) {
			ex.printStackTrace(OutputUtils.getErrorStream());
		}
	}

}
