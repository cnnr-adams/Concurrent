package interpreter;

import interpreter.DynamicThreadPool.DeclarationHandler;
import lexer.Lexer.LexemaTokenPair;
import lexer.Token;
import parser.Parsed.ParsedValue;

public class BaseFunctions {

	public static DeclarationHandler assign(Scope scope, ParsedValue[] args, ParsedValue[] body) {
		LexemaTokenPair varName = args[0].fn.args[0].tk;
		scope.addDeclaration(varName.lexema, getLine(scope, args[1]));
		return getVoid();
	}
	public static DeclarationHandler print(Scope scope, ParsedValue[] args, ParsedValue[] body) {
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
	public static DeclarationHandler add(Scope scope, ParsedValue[] args, ParsedValue[] body) {
		ParsedValue ls = getLine(scope, args[0]).getValue();
		ParsedValue rs = getLine(scope, args[1]).getValue();
		if((ls.tk.token.equals(Token.REAL) || ls.tk.token.equals(Token.INTEGER)) && (rs.tk.token.equals(Token.REAL) || rs.tk.token.equals(Token.INTEGER))) {
			double sum = Double.parseDouble(ls.tk.lexema) + Double.parseDouble(rs.tk.lexema);
			return new DeclarationHandler(new ParsedValue(new LexemaTokenPair("" + sum, Token.REAL)));
		} else {
			return new DeclarationHandler(new ParsedValue(new LexemaTokenPair(ls.tk.lexema + rs.tk.lexema, Token.STRING)));
		}
	}
	public static DeclarationHandler sub(Scope scope, ParsedValue[] args, ParsedValue[] body) {
		ParsedValue ls = getLine(scope, args[0]).getValue();
		ParsedValue rs = getLine(scope, args[1]).getValue();
		if((ls.tk.token.equals(Token.REAL) || ls.tk.token.equals(Token.INTEGER)) && (rs.tk.token.equals(Token.REAL) || rs.tk.token.equals(Token.INTEGER))) {
			int res = Integer.parseInt(ls.tk.lexema) - Integer.parseInt(rs.tk.lexema);
			return new DeclarationHandler(new ParsedValue(new LexemaTokenPair("" + res, Token.INTEGER)));
		} else {
			System.out.println("cant sub strings");
			return getVoid();
		}
	}
	
	public static  DeclarationHandler wait(Scope scope, ParsedValue[] args, ParsedValue[] body) {
		ParsedValue parsed = getLine(scope, args[0]).getValue();
		return new DeclarationHandler(parsed);
	}
	
	public static DeclarationHandler caseStatement(Scope scope, ParsedValue[] args, ParsedValue[] body) {
		DeclarationHandler d = getLine(scope, args[0]);
		if(d.getValue().tk.lexema.equals("true")) {
			return new DeclarationHandler(true, new LineHandler(scope, args[1]));
		} else {
			return null;
		}
	}
	
	public static DeclarationHandler condStatement(Scope scope, ParsedValue[] args, ParsedValue[] body) {
		return new DeclarationHandler(true, new CondHandler(scope, body));
	}
	
	public static DeclarationHandler equality(Scope scope, ParsedValue[] args, ParsedValue[] body) {
		ParsedValue ls = getLine(scope, args[0]).getValue();
		ParsedValue rs = getLine(scope, args[1]).getValue();
		if(ls.tk.lexema.equals(rs.tk.lexema)) {
			return new DeclarationHandler(new ParsedValue(new LexemaTokenPair("true", Token.BOOL)));
		} else {
			return new DeclarationHandler(new ParsedValue(new LexemaTokenPair("false", Token.BOOL)));
		}
	}
	
	public static DeclarationHandler modulus(Scope scope, ParsedValue[] args, ParsedValue[] body) {
		ParsedValue ls = getLine(scope, args[0]).getValue();
		ParsedValue rs = getLine(scope, args[1]).getValue();
		int ans = Integer.parseInt(ls.tk.lexema) % Integer.parseInt(rs.tk.lexema);
		return new DeclarationHandler(new ParsedValue(new LexemaTokenPair("" + ans, Token.INTEGER)));
	}
	
	private static DeclarationHandler getVoid() {
		return new DeclarationHandler(new ParsedValue(new LexemaTokenPair("void", Token.TK_VOID)));
	}
	
	private static DeclarationHandler getLine(Scope scope, ParsedValue val) {
		return new DeclarationHandler(true, new LineHandler(scope, val));
	}
}
