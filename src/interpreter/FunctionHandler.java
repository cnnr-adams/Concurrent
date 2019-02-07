package interpreter;

import java.util.concurrent.Callable;

import interpreter.DynamicThreadPool.DeclarationHandler;
import parser.Parsed.ParsedFunction;
import parser.Parsed.ParsedValue;

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
			return BaseFunctionHandler.invokeFunction(fn.title, scope, fn.args, fn.body);
		} else {
			ParsedValue fnBody = d.getValue();
			DeclarationHandler[] decs = new DeclarationHandler[fn.args.length];
			for(int i = 0; i < fn.args.length; i++) {
				decs[i] = new DeclarationHandler(true, new LineHandler(scope, fn.args[i]));
			}
			// create body run task
			return new DeclarationHandler(true, new BodyDelegator(scope, fnBody.fn.body, fnBody.fn.args, decs));
		}
	}

}
