package interpreter;

import java.util.concurrent.Callable;

import interpreter.DynamicThreadPool.DeclarationHandler;
import lexer.Lexer.LexemaTokenPair;
import lexer.Token;
import parser.Parsed.ParsedValue;

public class CondHandler implements Callable<DeclarationHandler> {
	Scope scope;
	ParsedValue[] body;
	public CondHandler(Scope scope, ParsedValue[] body) {
		this.scope = new Scope(scope);
		this.body = body;
	}

	@Override
	public DeclarationHandler call() throws Exception {
		for(ParsedValue val : body) {
				if(val.fn.title.token.equals(Token.TK_DEFAULT)) {
					return new DeclarationHandler(true, new LineHandler(scope, val.fn.args[0]));
				} else {
					DeclarationHandler currentCase = BaseFunctionHandler.invokeFunction(new LexemaTokenPair("case", Token.IDENTIFIER), scope, val.fn.args, null);
					if(currentCase != null) {
						return currentCase;
					}
				}
		}
		return new DeclarationHandler(new ParsedValue(new LexemaTokenPair("void", Token.TK_VOID)));
	}
}
