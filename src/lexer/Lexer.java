package lexer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Lexer {
    private StringBuilder input = new StringBuilder();
    private Token token;
    private String lexema;
    private boolean exhausted = false;
    private String errorMessage = "";
    private Set<Character> blankChars = new HashSet<Character>();

    public Lexer(String filePath) {
        try (Stream<String> st = Files.lines(Paths.get(filePath))) {
            st.forEach(input::append);
        } catch (IOException ex) {
            exhausted = true;
            errorMessage = "Could not read file: " + filePath;
            return;
        }

        blankChars.add('\r');
        blankChars.add('\n');
        blankChars.add((char) 8);
        blankChars.add((char) 9);
        blankChars.add((char) 11);
        blankChars.add((char) 12);
        blankChars.add((char) 32);

        moveAhead();
    }

    public List<LexemaTokenPair> lex() throws LexerException {
    	  System.out.println("Lexical Analysis");
          System.out.println("-----------------");
          List<LexemaTokenPair> lst = new ArrayList<LexemaTokenPair>();
          while (!isExausthed()) {
              lst.add(new LexemaTokenPair(currentLexema(), currentToken()));
              moveAhead();
          }

          if (isSuccessful()) {
        	  System.out.println("-----------------");
        	  return lst;
          } else {
              throw new LexerException(errorMessage());
          }
    }
    
    public void moveAhead() {
        if (exhausted) {
            return;
        }

        if (input.length() == 0) {
            exhausted = true;
            return;
        }

        ignoreWhiteSpaces();

        if (findNextToken()) {
            return;
        }

        exhausted = true;

        if (input.length() > 0) {
            errorMessage = "Unexpected symbol: '" + input.charAt(0) + "'";
        }
    }

    private void ignoreWhiteSpaces() {
        int charsToDelete = 0;

        while (blankChars.contains(input.charAt(charsToDelete))) {
            charsToDelete++;
        }

        if (charsToDelete > 0) {
            input.delete(0, charsToDelete);
        }
    }

    private boolean findNextToken() {
        for (Token t : Token.values()) {
            int end = t.endOfMatch(input.toString());
            if (end != -1) {
                token = t;
                lexema = input.substring(0, end);
                input.delete(0, end);
                return true;
            }
        }

        return false;
    }

    public Token currentToken() {
        return token;
    }

    public String currentLexema() {
        return lexema;
    }

    public boolean isSuccessful() {
        return errorMessage.isEmpty();
    }

    public String errorMessage() {
        return errorMessage;
    }

    public boolean isExausthed() {
        return exhausted;
    }
    
    static class LexerException extends Exception {
		private static final long serialVersionUID = -1452267466200782358L;
		public LexerException() {
    		
    	}
    	public LexerException(String message) {
    		super(message);
    	}
    }
    
    public static class LexemaTokenPair {
    	public String lexema;
    	public Token token;
    	public LexemaTokenPair(String lexema, Token token) {
    		this.lexema = lexema;
    		this.token = token;
    	}
    	public boolean equals(LexemaTokenPair pair) {
    		return pair.lexema.equals(lexema) && pair.token.equals(token);
    	}
    }
}