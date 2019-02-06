package parser;
import java.util.Stack;

import lexer.Lexer.LexemaTokenPair;
import parser.Parsed.ParsedFunction;
import parser.Parsed.ParsedValue;

public class ParseLine {
	
	Stack<ParsedValue> input;
	public ParseLine(Stack<ParsedValue> input) {
		this.input = input;
	}
	public ParsedValue parseLine() {
		Stack<LexemaTokenPair> operators = new Stack<LexemaTokenPair>();
		Stack<ParsedValue> operands = new Stack<ParsedValue>();
		while(input.size() > 0) {
			ParsedValue inToken = input.pop();
			int precedence = inToken.isFn ? 0 : inToken.tk.token.getPrecedence();
			if(!inToken.isFn && inToken.tk.lexema.equals("(")) {
				operators.push(inToken.tk);
			} else if(!inToken.isFn && inToken.tk.lexema.equals(")")) {
				String searchToken = inToken.tk.lexema.equals(")") ? "(" : "{";
				while(!operators.empty() && operators.peek().lexema != searchToken) {
					poppush(operators, operands);
				}
				operators.pop();
			} else if(precedence == -1) {
				continue;
			} else if(precedence == 0) {
				operands.push(inToken);
			} else {
				while(!operators.empty()) {
					if(operators.peek().token.getPrecedence() <= precedence) {
						poppush(operators, operands);
					} else {
						break;
					}
				}
				operators.push(inToken.tk);
			}
		}
		while(!operators.empty()) {
			poppush(operators, operands);
		}
		if(operands.size() > 1) {
			System.out.println("OPERANDS SIZE FOR ONE EXPRESSION GREATER THAN 1");
		}
		return operands.pop();
	}
	
	private void poppush(Stack<LexemaTokenPair> operators, Stack<ParsedValue> operands) {
		ParsedValue op1 = operands.pop();
		ParsedValue op2 = operands.pop();
		LexemaTokenPair operator = operators.pop();
		operands.push(new ParsedValue(new ParsedFunction(operator, op2, op1)));
		// add to operands stack
	}
	
//	private void getDependencies(ArrayList<ParsedValue> current, ParsedValue op) {
//		if(op.isFn) {
//			if(op.fn.isDec) {
//				return;
//			} else if (op.fn.isLambda) {
//				for(ParsedValue p : op.fn.args) {
//					getDependencies(current, p);
//				}
//			} else {
//				for(ParsedValue p : op.fn.args) {
//					getDependencies(current, p);
//				}
//			}
//		} else if(op.tk.token.equals(Token.IDENTIFIER)) {
//			current.add(op);
//		}
//	}
}
