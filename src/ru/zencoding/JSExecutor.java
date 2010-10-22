package ru.zencoding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

public class JSExecutor {
	private volatile static JSExecutor singleton;
	private Context cx;
	private Scriptable scope;
	private FileReader fReader;
	private boolean inited = false; 
	private String fileName = "zencoding.js";

	private JSExecutor() {
		inited = false;
		cx = Context.enter();
		scope = cx.initStandardObjects();
		FileReader input = getJSInput();
		if (input != null) {
			try {
				cx.evaluateReader(scope, getJSInput(), getFilename(), 1, null);
				inited = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static JSExecutor getSingleton() {
		if (singleton == null) {
			synchronized (JSExecutor.class) {
				if (singleton == null)
					singleton = new JSExecutor();
			}
		}
		return singleton;
	}
	
	public String getFilename() {
		return fileName;
	}
	
	public FileReader getJSInput() {
		if (fReader == null) {
			File f;
			try {
				f = new File( this.getClass().getResource("/" + getFilename()).toURI() );
				fReader = new FileReader(f);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return fReader;
	}
	
	public boolean isInited() {
		return inited;
	}
	
	/**
	 * Runs Zen Coding script on passed editor object
	 * @return 'True' if action was successfully executed
	 */
	public boolean runAction(IZenEditor editor, String actionName) {
		if (isInited()) {
			Object fnObj = scope.get("runZenCodingAction", scope);
			if (fnObj instanceof Function) {
				
				Object wrappedEditor = Context.javaToJS(editor, scope);
				
				Object fnArgs[] = {wrappedEditor, actionName};
				Function f = (Function) fnObj;
				Object result = f.call(cx, scope, scope, fnArgs);
				return Context.toBoolean(result);
			} else {
				System.err.println("Cannot get 'runZenCodingAction' function from JS");
			}
		}
		
		return false;
	}
}
