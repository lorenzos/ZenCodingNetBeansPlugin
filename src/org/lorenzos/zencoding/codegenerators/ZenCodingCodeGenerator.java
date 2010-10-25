
package org.lorenzos.zencoding.codegenerators;

import java.util.Collections;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.lorenzos.utils.*;
import org.lorenzos.zencoding.zeneditor.ZenEditor;
import org.netbeans.spi.editor.codegen.CodeGenerator;
import org.openide.util.Lookup;
import ru.zencoding.JSExecutor;

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
			JSExecutor jsRunner = JSExecutor.getSingleton();
			ZenEditor editor = ZenEditor.create(textComp);
			jsRunner.runAction(editor, "expand_abbreviation");
		} catch (Exception ex) {
			ex.printStackTrace(OutputUtils.getErrorStream());
		}
	}
}
