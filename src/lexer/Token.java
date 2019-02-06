package lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Token {

    TK_MINUS ("-", 3), 
    TK_PLUS ("\\+", 3), 
    TK_MUL ("\\*", 2), 
    TK_DIV ("/", 2), 
    TK_NOT ("!", 2), 
    TK_AND ("&", 4),  
    TK_OR ("\\|", 4),  
    TK_LESS ("<", 5),
    TK_LEG ("<=", 5),
    TK_GT (">", 5),
    TK_GEQ (">=", 5), 
    TK_EQ ("==", 5),
    TK_ASSIGN ("=", 6),
    TK_OPEN ("\\(", 1),
    TK_CLOSE ("\\)", 1), 
    TK_SEMI (";", -1), 
    TK_COMMA (",", 0), 
    TK_KEY_LET ("let", 0), 
    TK_KEY_CONST ("const", 0),
    TK_KEY_IF ("if", 0),
    TK_KEY_ELSE ("else", 0), 
    TK_KEY_RETURN ("return", 0),
    TK_FUNCTION ("function", 0),
    TK_VOID("void", 0),
    OPEN_BRACKET ("\\{", 1),
    CLOSE_BRACKET ("\\}", 1),

    STRING ("\"[^\"]+\"", 0),
    INTEGER ("\\d+", 0), 
    IDENTIFIER ("\\w+", 0),
	REAL ("(\\d*)\\.\\d+", 0);

    private final Pattern pattern;
    private int precedence;
    Token(String regex, int precedence) {
        pattern = Pattern.compile("^" + regex);
        this.precedence = precedence;
    }

    public int getPrecedence() {
    	return precedence;
    }
    int endOfMatch(String s) {
        Matcher m = pattern.matcher(s);

        if (m.find()) {
            return m.end();
        }
        return -1;
    }
}