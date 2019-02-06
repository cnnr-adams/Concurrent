package interpreter;

import java.util.concurrent.Callable;

import interpreter.DynamicThreadPool.DeclarationHandler;
import lexer.Lexer.LexemaTokenPair;
import lexer.Token;
import parser.Parsed.ParsedValue;

public class BodyDelegator implements Callable<DeclarationHandler> {
	Scope scope;
	ParsedValue[] body;
	public BodyDelegator(Scope scope, ParsedValue[] body) {
		this.scope = new Scope(scope);
		this.scope = scope;
		this.body = body;
	}

	@Override
	public DeclarationHandler call() throws Exception {
		for(ParsedValue val : body) {
			if(val.isFn) {
				if(val.fn.title.token.equals(Token.TK_KEY_RETURN)) {
					return new DeclarationHandler(true, new LineHandler(scope, val));
				} else if(val.fn.isDec) {
					// add fn to scope
					scope.addDeclaration(val.fn.title.lexema, new DeclarationHandler(val));
				} else {
					new DeclarationHandler(true, new FunctionHandler(scope, val.fn));
				}
			} else {
				System.out.println("Useless Line of code");
			}
		}
		return new DeclarationHandler(new ParsedValue(new LexemaTokenPair("void", Token.TK_VOID)));
	}
}
