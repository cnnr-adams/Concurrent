package interpreter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import interpreter.DynamicThreadPool.DeclarationHandler;
import lexer.Lexer.LexemaTokenPair;
import parser.Parsed.ParsedValue;

public class BaseFunctionHandler {
	BaseFunctions fns;
	static HashMap<String, Method> baseFunctions;

	public static void initialize() {
		baseFunctions = new HashMap<String, Method>();
		Class<BaseFunctions> fnClass = BaseFunctions.class;
		try {
			baseFunctions.put("=",
					fnClass.getMethod("assign", Scope.class, ParsedValue[].class));
			baseFunctions.put("+",
					fnClass.getMethod("add", Scope.class, ParsedValue[].class));
			baseFunctions.put("-",
					fnClass.getMethod("sub", Scope.class, ParsedValue[].class));
			baseFunctions.put("print",
					fnClass.getMethod("print", Scope.class, ParsedValue[].class));
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DeclarationHandler invokeFunction(LexemaTokenPair fn, Scope scope, ParsedValue[] args) {
		try {
			Method fne = baseFunctions.get(fn.lexema);
			return (DeclarationHandler)fne.invoke(null, scope, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
