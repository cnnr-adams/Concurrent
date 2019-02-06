package parser;

import java.util.ArrayList;

import lexer.Lexer.LexemaTokenPair;

public class Parsed {
	public static class ParsedFunction {
		public LexemaTokenPair title;
		public ParsedValue[] args;
		public boolean isDec = false;
		public boolean isLambda = false;
		public ParsedValue[] body;
		public ParsedFunction(LexemaTokenPair title, ParsedValue... args) {
			this.title = title;
			this.args = args;
		}
		public ParsedFunction(LexemaTokenPair title, ArrayList<ParsedValue> args) {
			this.title = title;
			this.args = args.stream().toArray(ParsedValue[]::new);
		}
		public ParsedFunction(LexemaTokenPair title, ArrayList<ParsedValue> args, ArrayList<ParsedValue> body) {
			this.title = title;
			this.args = args.stream().toArray(ParsedValue[]::new);
			this.body = body.stream().toArray(ParsedValue[]::new);
			this.isDec = true;
		}
		// anon fn
		public ParsedFunction(ArrayList<ParsedValue> body) {
			this.args = new ParsedValue[0];
			this.body = body.stream().toArray(ParsedValue[]::new);
			isLambda = true;
		}
		// single line lambda
		public ParsedFunction(ArrayList<ParsedValue> args, ParsedValue body) {
			this.args = args.stream().toArray(ParsedValue[]::new);
			this.body = new ParsedValue[] {body};
			isLambda = true;
		}
	}
	public static class ParsedValue {
		public ParsedFunction fn;
		public LexemaTokenPair tk;
		public boolean isFn = false;
		public ParsedValue(ParsedFunction fn) {
			this.fn = fn;
			isFn = true;
		}
		public ParsedValue(LexemaTokenPair tk) {
			this.tk = tk;
			isFn = false;
		}
//		public static ParsedValue deepCopy(ParsedValue p) {
//			if(p.isFn) {
//			//	return new ParsedValue(ParsedFunction.deepCopy(p.fn));
//			} else {
//				//return new ParsedValue(new LexemaTokenPair(p.p.tk);
//			}
//		}
	}
}
