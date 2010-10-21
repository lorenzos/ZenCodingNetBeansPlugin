
package org.lorenzos.zencoding.zenexpander;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.lorenzos.editor.EditorUtilities;

public class ZenCodingElement {

	private ArrayList<ZenCodingElement> innerElements;

	private String name;
	private HashMap<String, String> attributes;
	private int multiplier = 1;

	public ZenCodingElement(String tagName) {
		this.name = tagName;
		this.innerElements = new ArrayList<ZenCodingElement>();
		this.attributes = new HashMap<String, String>();
	}

	public ArrayList<ZenCodingElement> getInnerElements() {
		return innerElements;
	}

	public String getName() {
		return name;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void addAttribute(String name, String value) {
		this.attributes.put(name, value);
	}

	public void addInnerElement(ZenCodingElement element) {
		this.innerElements.add(element);
	}

	public void setMultiplier(int multiplier) {
		this.multiplier = (multiplier > 0) ? multiplier : 1;
	}

	public String getHtml() {

		// Tag name
		String html = "<" + this.getName();

		// Attributes
		for (Map.Entry<String, String> attribute : this.getAttributes().entrySet()) {
			html += " " + attribute.getKey().trim() + "=\"" + attribute.getValue() + "\"";
		}

		// Tag closing
		html += ">";

		// If no closing element, just stop
		if (!this.isNoClosing()) {

			// Inner elements
			if (this.getInnerElements().size() > 0) html += "\n";
			for (ZenCodingElement element : this.getInnerElements()) {
				html += EditorUtilities.stringIndent(element.getHtml()) + "\n";
			}

			// End tag
			html += "</" + this.getName() + ">";

		}
		
		// Return
		if (this.multiplier > 1) {
			String multiHtml = "";
			for (int i = 0; i < this.multiplier; i++) 
				multiHtml += html.replaceAll("[$]", String.valueOf(i + 1)) + "\n";
			return multiHtml.substring(0, multiHtml.length() - 1);
		} else {
			return html;
		}

	}

	public boolean isNoClosing() {
		return (name.equals("br") || name.equals("img"));
	}

}
