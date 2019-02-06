package interpreter;

import interpreter.DynamicThreadPool.DeclarationHandler;
import lexer.Lexer.LexemaTokenPair;
import lexer.Token;
import parser.Parsed.ParsedValue;

public class BaseFunctions {

	public static DeclarationHandler assign(Scope scope, ParsedValue[] args) {
		LexemaTokenPair varName = args[0].fn.args[0].tk;
		scope.addDeclaration(varName.lexema, getLine(scope, args[1]));
		return getVoid();
	}
	public static DeclarationHandler print(Scope scope, ParsedValue[] args) {
		ParsedValue p = getLine(scope, args[0]).getValue();
		if(p.tk.token.equals(Token.IDENTIFIER)) {
			DeclarationHandler d = scope.lookup(p.tk.lexema);
			ParsedValue pv = d.getValue();
			System.out.println(pv.tk.lexema);
		} else {
			System.out.println(p.tk.lexema);
		}
		return getVoid();
	}
	public static DeclarationHandler add(Scope scope, ParsedValue[] args) {
		ParsedValue ls = getLine(scope, args[0]).getValue();
		ParsedValue rs = getLine(scope, args[1]).getValue();
		if((ls.tk.token.equals(Token.REAL) || ls.tk.token.equals(Token.INTEGER)) && (rs.tk.token.equals(Token.REAL) || rs.tk.token.equals(Token.INTEGER))) {
			double sum = Integer.parseInt(ls.tk.lexema) + Integer.parseInt(rs.tk.lexema);
			return new DeclarationHandler(new ParsedValue(new LexemaTokenPair("" + sum, Token.REAL)));
		} else {
			return new DeclarationHandler(new ParsedValue(new LexemaTokenPair(ls.tk.lexema + rs.tk.lexema, Token.STRING)));
		}
	}
	
	private static DeclarationHandler getVoid() {
		return new DeclarationHandler(new ParsedValue(new LexemaTokenPair("void", Token.TK_VOID)));
	}
	
	private static DeclarationHandler getLine(Scope scope, ParsedValue val) {
		return new DeclarationHandler(true, new LineHandler(scope, val));
	}
}
