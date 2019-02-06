package parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import lexer.Lexer.LexemaTokenPair;
import lexer.Token;
import parser.Parsed.ParsedFunction;
import parser.Parsed.ParsedValue;

public class Parser {
	Stack<LexemaTokenPair> input;

	public Parser(List<LexemaTokenPair> lexed) {
		System.out.println("Parser");
		System.out.println("-----------------");
		input = new Stack<LexemaTokenPair>();
		for (int i = lexed.size() - 1; i >= 0; i--) {
			input.push(lexed.get(i));
		}
	}
	public Parser(Stack<LexemaTokenPair> lexed) {
		System.out.println("Inner Parser");
		System.out.println("-----------------");
		input = lexed;
	}
	private boolean openCurly = false;
	public ArrayList<ParsedValue> parse() {
		Stack<ParsedValue> output = new Stack<ParsedValue>();
		Stack<LexemaTokenPair> toPreProcessor = new Stack<LexemaTokenPair>();
		while (!input.empty()) {
			LexemaTokenPair tk = input.pop();
			if(tk.token.equals(Token.OPEN_BRACKET))
				openCurly = true;
			if ((tk.token.equals(Token.TK_SEMI) && !openCurly) || tk.token.equals(Token.CLOSE_BRACKET)) {
				openCurly = false;
				Stack<ParsedValue> toProcess = reverseParsedValues(preProcessLine(reverse(toPreProcessor)));
				ParsedValue lineOutput = new ParseLine(toProcess).parseLine();
				output.push(lineOutput);
				toPreProcessor = new Stack<LexemaTokenPair>();
			} else {
				toPreProcessor.push(tk);
			}
		}
		System.out.println("-----------------");
		ArrayList<ParsedValue> toArr = new ArrayList<ParsedValue>();
		  while(!output.empty()) {
			  toArr.add(output.pop());
		  }
		 Collections.reverse(toArr);
		return toArr;
	}

	public static Stack<LexemaTokenPair> reverse(Stack<LexemaTokenPair> st) {
		Stack<LexemaTokenPair> reverse = new Stack<LexemaTokenPair>();
		while (!st.isEmpty()) {
			LexemaTokenPair value = st.pop();
			reverse.push(value);
		}
		return reverse;
	}
	public static Stack<ParsedValue> reverseParsedValues(Stack<ParsedValue> st) {
		Stack<ParsedValue> reverse = new Stack<ParsedValue>();
		while (!st.isEmpty()) {
			ParsedValue value = st.pop();
			reverse.push(value);
		}
		return reverse;
	}

	public void printStack(Stack<LexemaTokenPair> toPreProcessor) {
		while (!toPreProcessor.empty()) {
			System.out.println(toPreProcessor.pop().lexema);
		}
	}
	
	public void printParsedValueStack(Stack<ParsedValue> toPrint)  {
		while(!toPrint.empty()) {
			printParsedValueValues(toPrint.pop());
		}
	}
	private void printParsedValueValues(ParsedValue v) {
		if(!v.isFn) {
			if(v.tk == null) {
				System.out.println("Anon fn");
			} else {
				System.out.println("LEXEMA: " + v.tk.lexema);
			}
		} else {
			printParsedFunctionValues(v.fn);
		}
	}
	
	private void printParsedFunctionValues(ParsedFunction v) {
		System.out.println(v.title.lexema + ": ");
		for(ParsedValue c : v.args) {
			printParsedValueValues(c);
		}
	}

	public Stack<ParsedValue> preProcessLine(Stack<LexemaTokenPair> input) {
		Stack<ParsedValue> output = new Stack<ParsedValue>();
		while (!input.empty()) {
			LexemaTokenPair pair = input.pop();
			System.out.println("PREPROCESSOR ROOT: " + pair.lexema);
			if (pair.token.equals(Token.TK_KEY_LET) || pair.token.equals(Token.TK_KEY_CONST)) {
				letHook(pair, input, output);
			} else if (pair.token.equals(Token.TK_FUNCTION)) {
				functionHook(pair, input, output);
			} else if (pair.token.equals(Token.IDENTIFIER)) {
				identifierHook(pair, input, output);
			} else if (pair.token.equals(Token.TK_KEY_RETURN)) {
				returnHook(pair, input, output);
			} else {
				// for now, since not dealing with if/else/loops/whatever else
				// needs special care, assume token is standalone or infix.
				output.push(new ParsedValue(pair));
			}
		}
		return output;
	}
	
	private void letHook(LexemaTokenPair pair, Stack<LexemaTokenPair> input, Stack<ParsedValue> output) {
		System.out.println("--- ENTERING LET/CONST HOOK ---");
		LexemaTokenPair nextVal = input.pop();
		System.out.println("LET: " + nextVal.lexema);
		ParsedValue p = new ParsedValue(new ParsedFunction(pair, new ParsedValue(nextVal)));
		output.push(p);
	}
	
	private void functionHook(LexemaTokenPair pair, Stack<LexemaTokenPair> input, Stack<ParsedValue> output) {
		System.out.println("--- ENTERING FUNCTION DECLARATION HOOK ---");
		LexemaTokenPair name = input.pop();
		System.out.println("FUNCTION DECLARATION: " + name.lexema);
		// open bracket
		input.pop();
		ArrayList<ParsedValue> vals = new ArrayList<ParsedValue>();
		LexemaTokenPair popped = input.pop();
		while (!popped.lexema.equals(")")) {

			if (popped.lexema.equals(",")) {
				popped = input.pop();
				continue;
			}
			// identifier may need to become a declaration, that is let
			// ..., but not now
			vals.add(new ParsedValue(popped));
			popped = input.pop();
		}
		// open curly
		input.pop();
		Parser p = new Parser(input);
		ArrayList<ParsedValue> res = p.parse();
		ParsedValue pp = new ParsedValue(new ParsedFunction(name, vals, res));
		output.push(pp);
	}
	
	private void identifierHook(LexemaTokenPair pair, Stack<LexemaTokenPair> input, Stack<ParsedValue> output) {
		// is function call
		if (!input.empty() && input.peek().lexema.equals("(")) {
			input.pop();
			LexemaTokenPair popped = input.pop();
			Stack<LexemaTokenPair> toPreProcessor = new Stack<LexemaTokenPair>();
			ArrayList<ParsedValue> toFn = new ArrayList<ParsedValue>();
			int r = 1;
			while (r != 0) {
				// send each arg to parseline()
				if (popped.lexema.equals(",")) {
					ParseLine l = new ParseLine(preProcessLine(reverse(toPreProcessor)));
					toPreProcessor = new Stack<LexemaTokenPair>();
					ParsedValue result = l.parseLine();
					toFn.add(result);
					popped = input.pop();
				} else {
					toPreProcessor.push(popped);
					popped = input.pop();
				}
				if(popped.lexema.equals(")")) {
					r--;
				} else if(popped.lexema.equals("(")) {
					r++;
				}
			}
			if(toPreProcessor.size() > 0) {
				ParseLine l = new ParseLine(preProcessLine(reverse(toPreProcessor)));
				toPreProcessor = new Stack<LexemaTokenPair>();
				ParsedValue result = l.parseLine();
				toFn.add(result);
			}
			ParsedValue p = new ParsedValue(new ParsedFunction(pair, toFn));
			output.push(p);
		} else {
			output.push(new ParsedValue(pair));
		}
	}
	
	private void returnHook(LexemaTokenPair pair, Stack<LexemaTokenPair> input, Stack<ParsedValue> output) {
		Stack<ParsedValue> toProcess = reverseParsedValues(preProcessLine(input));
		ParsedValue lineOutput = new ParseLine(toProcess).parseLine();
		output.push(new ParsedValue(new ParsedFunction(pair, lineOutput)));
	}
}
