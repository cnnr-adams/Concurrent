package interpreter;

import java.util.concurrent.Callable;

import interpreter.DynamicThreadPool.DeclarationHandler;
import parser.Parsed.ParsedValue;

public class LineHandler implements Callable<DeclarationHandler> {
	Scope scope;
	ParsedValue val;
	public LineHandler(Scope scope, ParsedValue val) {
		this.scope = new Scope(scope);
		this.val = val;
	}
	@Override
	public DeclarationHandler call() throws Exception {
		if(val.isFn) {
			return new DeclarationHandler(true, new FunctionHandler(scope, val.fn));
		} else {
			return new DeclarationHandler(val);
		}
	}

}
