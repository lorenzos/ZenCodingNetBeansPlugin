
package org.lorenzos.zencoding.actions;

import java.util.List;
import org.openide.cookies.EditorCookie;

public final class ZenCodingGoToNextEditPoint extends ZenCodingAbstractAction {

	public ZenCodingGoToNextEditPoint(List<EditorCookie> context) {
		super(context, "next_edit_point");
	}

}
