package interpreter;

import parser.Parsed.ParsedValue;
import interpreter.DynamicThreadPool.DeclarationHandler;

public class Interpreter {

	ParsedValue[] ast;
	public Interpreter(ParsedValue[] ast) {
		this.ast = ast;
		DynamicThreadPool.initialize();
		BaseFunctionHandler.initialize();
	}

	public void interpret() {
		Scope global = new Scope();
		new DeclarationHandler(true, new BodyDelegator(global, ast));
	}
}
