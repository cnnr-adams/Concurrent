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
					fnClass.getMethod("assign", Scope.class, ParsedValue[].class, ParsedValue[].class));
			baseFunctions.put("+",
					fnClass.getMethod("add", Scope.class, ParsedValue[].class, ParsedValue[].class));
			baseFunctions.put("%",
					fnClass.getMethod("modulus", Scope.class, ParsedValue[].class, ParsedValue[].class));
			baseFunctions.put("-",
					fnClass.getMethod("sub", Scope.class, ParsedValue[].class, ParsedValue[].class));
			baseFunctions.put("==",
					fnClass.getMethod("equality", Scope.class, ParsedValue[].class, ParsedValue[].class));
			baseFunctions.put("case",
					fnClass.getMethod("caseStatement", Scope.class, ParsedValue[].class, ParsedValue[].class));
			baseFunctions.put("print",
					fnClass.getMethod("print", Scope.class, ParsedValue[].class, ParsedValue[].class));
			baseFunctions.put("wait",
					fnClass.getMethod("wait", Scope.class, ParsedValue[].class, ParsedValue[].class));
			baseFunctions.put("cond",
					fnClass.getMethod("condStatement", Scope.class, ParsedValue[].class, ParsedValue[].class));
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static DeclarationHandler invokeFunction(LexemaTokenPair fn, Scope scope, ParsedValue[] args, ParsedValue[] body) {
		try {
			Method fne = baseFunctions.get(fn.lexema);
			return (DeclarationHandler)fne.invoke(null, scope, args, body);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			System.out.println("Method to invoke not found: " + fn.lexema);
			DynamicThreadPool.terminate();
			e.printStackTrace();
			return null;
		}
	}
}
