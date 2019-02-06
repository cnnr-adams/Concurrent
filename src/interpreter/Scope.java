package interpreter;

import java.util.HashMap;

import interpreter.DynamicThreadPool.DeclarationHandler;
import lexer.Lexer.LexemaTokenPair;

public class Scope {
	private HashMap<String, DeclarationHandler> current = new HashMap<String, DeclarationHandler>();
	private Scope higherScope;
	private boolean root = false;
	public Scope() {
		root = true;
	}
	
	public Scope(Scope higherScope) {
		this.higherScope = higherScope;
	}
	
	public void addDeclaration(String key, DeclarationHandler value) {
		current.put(key, value);
	}
	
	public DeclarationHandler lookup(String key) {
		if(current.containsKey(key)) {
			return current.get(key);
		} else {
			if(root) {
				return null;
			} else {
				return higherScope.lookup(key);
			}
		}
	}
}
