
package org.lorenzos.zencoding.actions;

import java.util.List;
import org.openide.cookies.EditorCookie;

public final class ZenCodingMergeLines extends ZenCodingAbstractAction {

	public ZenCodingMergeLines(List<EditorCookie> context) {
		super(context, "merge_lines");
	}

}
