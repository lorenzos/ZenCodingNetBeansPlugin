
package org.lorenzos.editor;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.openide.cookies.EditorCookie;

public class EditorUtilities {
	
	private JTextComponent textComp;
	private Document doc;
	
	private int caretPosition;
	private int lineStart;
	private int lineEnd;

	private EditorUtilities(JTextComponent textComp) throws EditorUtilitiesException {
		this.textComp = textComp;
		this.setup();
	}

	public static EditorUtilities create(EditorCookie context) throws EditorUtilitiesException {
		for (JEditorPane pane : context.getOpenedPanes()) return new EditorUtilities(pane);
		throw new EditorUtilitiesException();
	}

	public static EditorUtilities create(JTextComponent textComp) throws EditorUtilitiesException {
		return new EditorUtilities(textComp);
	}

	private void setup() throws EditorUtilitiesException {

		try {

			doc = textComp.getDocument();
			caretPosition = textComp.getCaret().getDot();
			lineStart = caretPosition;
			lineEnd = caretPosition;
			String cTemp;

			// Search for line start
			if (lineStart > 0) {
				cTemp = doc.getText(lineStart - 1, 1);
				while (!cTemp.equals("\n") && !cTemp.equals("\r")) {
					lineStart--;
					cTemp = doc.getText(lineStart - 1, 1);
				}
			}

			// Search for line end
			if (lineEnd < doc.getLength()) {
				cTemp = doc.getText(lineEnd, 1);
				while (!cTemp.equals("\n") && !cTemp.equals("\r")) {
					lineEnd++;
					cTemp = doc.getText(lineEnd, 1);
				}
			}

		} catch (BadLocationException ex) {
			throw new EditorUtilitiesException();
		}
		
	}

	public String getLine() {
		try {
			int offset = lineEnd - lineStart;
			return doc.getText(lineStart, offset);
		} catch (BadLocationException ex) {
			return "";
		}
	}

	public void replaceLine(String replacement) {
		try {
			int offset = lineEnd - lineStart;
			doc.remove(lineStart, offset);
			doc.insertString(lineStart, replacement, null);
			lineEnd = lineStart + replacement.length();
		} catch (BadLocationException ex) {
			return;
		}
	}

	public String getIndentation() {
		String ws = "";
		String line = this.getLine();
		int i = 0;
		while (Character.isWhitespace(line.charAt(i))) ws += line.charAt(i++);
		return ws;
	}

	public static String stringIndent(String source) {
		return EditorUtilities.stringIndent(source, "\t");
	}

	public static String stringIndent(String source, String indentation) {
		return indentation + source.replaceAll("\n", "\n" + indentation);
	}

}
