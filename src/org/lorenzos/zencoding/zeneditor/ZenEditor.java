
package org.lorenzos.zencoding.zeneditor;

import java.awt.Rectangle;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.lorenzos.utils.EditorUtilities;
import org.lorenzos.utils.OutputUtils;
import org.openide.cookies.EditorCookie;
import org.openide.util.Exceptions;
import ru.zencoding.IZenEditor;
import ru.zencoding.SelectionData;

public class ZenEditor implements IZenEditor {

	private JTextComponent textComp;
	private Document doc;
	private String contentType;

	private Rectangle initialScrollingPosition;

	private int caretPosition;
	private int lineStart;
	private int lineEnd;

	private final static String caretPlaceholder = "{%::zen-caret::%}";

	private ZenEditor(JTextComponent textComp) throws ZenEditorException {
		this.textComp = textComp;
		this.initialScrollingPosition = textComp.getVisibleRect();
		this.setup();
	}

	public static ZenEditor create(EditorCookie context) throws ZenEditorException {
		for (JEditorPane pane : context.getOpenedPanes()) {
			ZenEditor n = new ZenEditor(pane);
			n.contentType = pane.getContentType();
			return n; }
		throw new ZenEditorException();
	}

	public static ZenEditor create(JTextComponent textComp) throws ZenEditorException {
		return new ZenEditor(textComp);
	}
	
	@Override
	public SelectionData getSelectionRange() {
		return new SelectionData(
			this.textComp.getSelectionStart(),
			this.textComp.getSelectionEnd()
		);
	}

	@Override
	public void createSelection(int start, int end) {
		this.textComp.setSelectionStart(start);
		this.textComp.setSelectionEnd(end);
	}

	@Override
	public SelectionData getCurrentLineRange() {
		return new SelectionData(
			this.lineStart,
			this.lineEnd
		);
	}

	@Override
	public int getCaretPos() {
		return this.caretPosition;
	}

	@Override
	public void setCaretPos(int pos) {
		this.textComp.setCaretPosition(pos);
	}

	@Override
	public String getCurrentLine() {
		return this.getLine();
	}

	@Override
	public void replaceContent(String value) {
		this.replaceContent(value, 0, this.doc.getLength());
	}

	@Override
	public void replaceContent(String value, int start) {
		this.replaceContent(value, start, this.doc.getLength());
	}

	@Override
	public void replaceContent(String value, int start, int end) {
		try {
			value = EditorUtilities.stringIndent(value, this.getIndentation()).trim();
			int placeholderPosition = value.length();
			if (value.contains(ZenEditor.caretPlaceholder)) {
				placeholderPosition = value.indexOf(ZenEditor.caretPlaceholder);
				value = value.replace(ZenEditor.caretPlaceholder, ""); }
			doc.remove(start, end - start);
			doc.insertString(start, value, null);
			this.setCaretPos(start + placeholderPosition);
		} catch (BadLocationException ex) {
			ex.printStackTrace(OutputUtils.getErrorStream());
		}
	}

	@Override
	public String getContent() {
		try {
			return this.doc.getText(0, this.doc.getLength());
		} catch (BadLocationException ex) {
			ex.printStackTrace(OutputUtils.getErrorStream());
			return "";
		}
	}

	@Override
	public String getSyntax() {
		if (this.contentType != null) if (this.contentType.equals("text/x-css")) return "css";
		return "html";
	}

	@Override
	public String getProfileName() {
		if (this.contentType != null) if (this.contentType.equals("text/x-css")) return "css";
		return "xhtml";
	}

	@Override
	public String prompt(String title) {
		return JOptionPane.showInputDialog(null,
			"Enter Zen Coding abbreviation:",
			"Wrap with Abbreviation",
			JOptionPane.QUESTION_MESSAGE);
	}

	@Override
	public String getSelection() {
		return this.textComp.getSelectedText();
	}

	@Override
	public String getFilePath() {
		return "local";
	}

	private void setup() throws ZenEditorException {

		try {

			doc = textComp.getDocument();
			caretPosition = textComp.getCaretPosition();
			lineStart = caretPosition;
			lineEnd = caretPosition;
			String cTemp;

			// Search for line start
			if (lineStart > 0) {
				cTemp = doc.getText(lineStart - 1, 1);
				while (!cTemp.equals("\n") && !cTemp.equals("\r") && (lineStart > 0)) {
					lineStart--;
					if (lineStart > 0) cTemp = doc.getText(lineStart - 1, 1);
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
			ex.printStackTrace(OutputUtils.getErrorStream());
			throw new ZenEditorException();
		}

	}

	public String getLine() {
		try {
			int offset = lineEnd - lineStart;
			return doc.getText(lineStart, offset);
		} catch (BadLocationException ex) {
			ex.printStackTrace(OutputUtils.getErrorStream());
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

	public void restoreInitialScrollingPosition() {
		textComp.scrollRectToVisible(initialScrollingPosition);
	}

}
