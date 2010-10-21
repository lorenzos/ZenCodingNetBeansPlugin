
package org.lorenzos.zencoding.zenexpander;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZenCodingExpander {

	private String zenCode;
	
	public ZenCodingExpander(String zenCode) {
		this.zenCode = zenCode.trim();
	}

	public String getZenCode() {
		return zenCode;
	}
	
	public String parse() {
		
		// Get child list
		ZenCodingElement root = new ZenCodingElement("root");
		ZenCodingElement parent = root;
		String childs[] = this.zenCode.split("[>]");
		for (String child : childs) {

			// Get adjacent list
			String adjacents[] = child.split("[+]");
			ZenCodingElement newElement = null;
			for (String adjacent : adjacents) {
				newElement = createElement(adjacent);
				parent.addInnerElement(newElement);
			}

			// Setup parent element wiwth last element
			if (newElement != null) parent = newElement;

		}

		// Return HTML
		String html = "";
		for (ZenCodingElement element : root.getInnerElements()) html += element.getHtml() + "\n";
		return html.endsWith("\n") ? html.substring(0, html.length() - 1) : html;

	}

	private ZenCodingElement createElement(String code) {

		// Element name, create it
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9$_\\-]+");
		Matcher matcher = pattern.matcher(code);
		if (!matcher.find()) return new ZenCodingElement(code);
		ZenCodingElement e = new ZenCodingElement(matcher.group());

		// Element ID
		pattern = Pattern.compile("^[^\\[]+#([a-zA-Z0-9$_\\-]+)");
		matcher = pattern.matcher(code);
		if (matcher.find()) e.addAttribute("id", matcher.group(1));

		// Element classes
		String classes = "";
		pattern = Pattern.compile("^[^\\[]+\\.([a-zA-Z0-9$_\\-]+)");
		matcher = pattern.matcher(code);
		while (matcher.find()) classes += matcher.group(1) + " ";
		if (!classes.equals("")) e.addAttribute("class", classes.trim());

		// Multiplier
		pattern = Pattern.compile("\\*[0-9]+$");
		matcher = pattern.matcher(code);
		if (matcher.find()) e.setMultiplier(Integer.parseInt(matcher.group().substring(1)));

		// Other attributes
		pattern = Pattern.compile("\\[[\\p{Graph} ]+\\]");
		matcher = pattern.matcher(code);
		if (matcher.find()) {
			String attributesList = matcher.group();
			attributesList = attributesList.substring(1, attributesList.length() - 1).trim();

			// Allow spaces and = in "" (use a token)
			Pattern patternValues = Pattern.compile("\"[\\p{Graph} ]+\"");
			Matcher matcherValues = patternValues.matcher(attributesList);
			int offset = 0;
			while (matcherValues.find()) {
				String match = matcherValues.group();
				attributesList =
					attributesList.substring(0, matcherValues.start() + offset * 9) +
					match.replaceAll("[ ]", "__________") +
					attributesList.substring(matcherValues.end() + offset * 9);
				offset += match.replaceAll("[^ ]", "").length(); }
			patternValues = Pattern.compile("\"[\\p{Graph} ]+\"");
			matcherValues = patternValues.matcher(attributesList);
			offset = 0;
			while (matcherValues.find()) {
				String match = matcherValues.group();
				attributesList =
					attributesList.substring(0, matcherValues.start() + offset * 9) +
					match.replaceAll("[=]", "##########") +
					attributesList.substring(matcherValues.end() + offset * 9);
				offset += match.replaceAll("[^=]", "").length();
			}

			// Create attributes
			String attributes[] = attributesList.split("[ ]+");
			for (String attribute : attributes) {
				String nameValue[] = attribute.split("[=]");
				if (nameValue.length == 0) continue;
				if (nameValue.length == 1) e.addAttribute(nameValue[0], "");
				if (nameValue.length >= 2) {
					if (nameValue[1].startsWith("\"")) nameValue[1] = nameValue[1].substring(1);
					if (nameValue[1].endsWith  ("\"")) nameValue[1] = nameValue[1].substring(0, nameValue[1].length() - 1);
					e.addAttribute(nameValue[0], nameValue[1].replaceAll("__________", " ").replaceAll("##########", "="));
				}
			}

		}

		return e;
		
	}

}
