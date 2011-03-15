
package org.lorenzos.zencoding.actions;

import java.util.List;
import org.openide.cookies.EditorCookie;

public final class ZenCodingRemoveTag extends ZenCodingAbstractAction {

	public ZenCodingRemoveTag(List<EditorCookie> context) {
		super(context, "remove_tag");
	}
	
}
