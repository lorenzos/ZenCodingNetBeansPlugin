
package org.lorenzos.zencoding.actions;

import java.util.List;
import org.openide.cookies.EditorCookie;

public final class ZenCodingGoToPreviousEditPoint extends ZenCodingAbstractAction {

	public ZenCodingGoToPreviousEditPoint(List<EditorCookie> context) {
		super(context, "prev_edit_point");
	}

}
