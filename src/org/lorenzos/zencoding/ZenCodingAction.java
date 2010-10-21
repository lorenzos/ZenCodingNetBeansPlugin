
package org.lorenzos.zencoding;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.lorenzos.zencoding.zenexpander.ZenCodingExpander;
import org.lorenzos.editor.*;
import org.openide.cookies.EditorCookie;
import org.openide.util.Exceptions;

public final class ZenCodingAction implements ActionListener {

	private final EditorCookie context;

	public ZenCodingAction(EditorCookie context) {
		this.context = context;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		try {
			EditorUtilities e = EditorUtilities.create(context);
			String expandedZenCode = new ZenCodingExpander(e.getLine()).parse();
			e.replaceLine(EditorUtilities.stringIndent(expandedZenCode, e.getIndentation()));
		} catch (EditorUtilitiesException ex) {
			Exceptions.printStackTrace(ex);
		}
	}
}
