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
		this.body = body;
	}
	public BodyDelegator(Scope scope, ParsedValue[] body, ParsedValue[] scopeVars, DeclarationHandler[] toAssign) {
		this.scope = new Scope(scope);
		for(int i = 0; i < scopeVars.length; i++) {
			this.scope.addDeclaration(scopeVars[i].tk.lexema, toAssign[i]);
		}
		this.body = body;
	}

	@Override
	public DeclarationHandler call() throws Exception {
		for(ParsedValue val : body) {
			if(val.isFn) {
				if(val.fn.title.token.equals(Token.TK_KEY_RETURN)) {
					return new DeclarationHandler(true, new LineHandler(scope, val.fn.args[0]));
				} else if(val.fn.title.token.equals(Token.TK_WAIT)) {
					return new DeclarationHandler(new DeclarationHandler(true, new LineHandler(scope, val.fn.args[0])).getValue());
				} else if(val.fn.title.token.equals(Token.TK_KEY_COND)) {
					BaseFunctionHandler.invokeFunction(val.fn.title, scope, val.fn.args, val.fn.body);
				} else if(val.fn.title.token.equals(Token.TK_ASSIGN)) {
					BaseFunctionHandler.invokeFunction(val.fn.title, scope, val.fn.args, val.fn.body);
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
