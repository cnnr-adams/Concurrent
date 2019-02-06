package interpreter;

import java.util.concurrent.Callable;

import interpreter.DynamicThreadPool.DeclarationHandler;
import parser.Parsed.ParsedFunction;

public class FunctionHandler implements Callable<DeclarationHandler> {
	Scope scope;
	ParsedFunction fn;
	public FunctionHandler(Scope scope, ParsedFunction fn) {
		this.scope = new Scope(scope);
		this.scope = scope;
		this.fn = fn;
	}
	@Override
	public DeclarationHandler call() throws Exception {
		// find fn in scope and call its body
		DeclarationHandler d = scope.lookup(fn.title.lexema);
		// not in scope, try global?
		if(d == null) {
			return BaseFunctionHandler.invokeFunction(fn.title, scope, fn.args);
		} else {
			// create body run task
			return new DeclarationHandler(true, new BodyDelegator(scope, d.getValue().fn.body));
		}
	}

}
