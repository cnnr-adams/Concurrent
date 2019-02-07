package main;

import lexer.Lexer;
import parser.Parsed.ParsedFunction;
import parser.Parser;
import interpreter.Interpreter;

public class Main {
	  public static void main(String[] args)throws Exception 
	  { 
	  Lexer l = new Lexer("find-primes.con");
	  Parser p = new Parser(l.lex());
	  ParsedFunction s = new ParsedFunction(p.parse());
	  Interpreter i = new Interpreter(s.body);
	  long cur = System.currentTimeMillis();
	  i.interpret();
	  System.out.println("Time Taken: " + (System.currentTimeMillis() - cur));
	  }
}
