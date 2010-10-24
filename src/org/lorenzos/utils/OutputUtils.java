
package org.lorenzos.utils;

import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;

public class OutputUtils {

	private static final String outputPanelName = "org.lorenzs Output";

	public static void writeOutputLine(String message) {
		OutputUtils.getOutputStream().println(message);
	}

	public static void writeErrorLine(String message) {
		OutputUtils.getErrorStream().println(message);
	}

	public static OutputWriter getOutputStream() {
		InputOutput p = IOProvider.getDefault().getIO(OutputUtils.outputPanelName, false);
		p.setErrSeparated(false);
		if (p.isClosed()) { p.setOutputVisible(true); p.select(); }
		return p.getOut();
	}

	public static OutputWriter getErrorStream() {
		InputOutput p = IOProvider.getDefault().getIO(OutputUtils.outputPanelName, false);
		p.setErrSeparated(false);
		if (p.isClosed()) { p.setErrVisible(true); p.select(); }
		return p.getErr();
	}

}
