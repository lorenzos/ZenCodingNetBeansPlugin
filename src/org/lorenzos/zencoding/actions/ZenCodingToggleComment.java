
package org.lorenzos.zencoding.actions;

import java.util.List;
import org.lorenzos.utils.OutputUtils;
import org.openide.cookies.EditorCookie;

public final class ZenCodingToggleComment extends ZenCodingAbstractAction {

	public ZenCodingToggleComment(List<EditorCookie> context) {
		super(context, "toggle_comment");
	}
	
}
