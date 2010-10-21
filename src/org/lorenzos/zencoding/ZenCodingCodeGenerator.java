
package org.lorenzos.zencoding;

import java.util.Collections;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.lorenzos.zencoding.zenexpander.ZenCodingExpander;
import org.lorenzos.editor.*;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public class ZenCodingCodeGenerator implements CodeGenerator {

	private JTextComponent textComp;

	private ZenCodingCodeGenerator(Lookup context) {
		textComp = context.lookup(JTextComponent.class);
	}

	public static class Factory implements CodeGenerator.Factory {
		@Override
		public List<? extends CodeGenerator> create(Lookup context) {
			return Collections.singletonList(new ZenCodingCodeGenerator(context));
		}
	}

	@Override
	public String getDisplayName() {
		return "Expand Zen code";
	}

	@Override
	public void invoke() {
		try {
			EditorUtilities e = EditorUtilities.create(textComp);
			String expandedZenCode = new ZenCodingExpander(e.getLine()).parse();
			e.replaceLine(EditorUtilities.stringIndent(expandedZenCode, e.getIndentation()));
		} catch (EditorUtilitiesException ex) {
			Exceptions.printStackTrace(ex);
		}
	}
}
