package interpreter;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import parser.Parsed.ParsedValue;

public class DynamicThreadPool {

	static ExecutorService threadService;
	public static void initialize() {
		threadService = Executors.newCachedThreadPool();
	}
	
	public static <T> Future<T> execute(Callable<T> c) {
		return threadService.submit(c);
	}
	
	public static void awaitTermination() {
		try {
			threadService.awaitTermination(30,TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static class DeclarationHandler {
		private Future<ParsedValue> threadedValue;
		private ParsedValue pureValue;
		public boolean hasPure = false;
		private Future<DeclarationHandler> recursive;
		private boolean isRecursive = false;
		public DeclarationHandler(Callable<ParsedValue> threadedValue) {
			this.threadedValue = execute(threadedValue);
		}
		public DeclarationHandler(boolean isRecursive, Callable<DeclarationHandler> threadedValue) {
			this.isRecursive = true;
			this.recursive = execute(threadedValue);
		}
		public DeclarationHandler(ParsedValue pureValue) {
			this.hasPure = true;
			this.pureValue = pureValue;
		}
		public ParsedValue getValue() {
			if(isRecursive) {
				try {
					return recursive.get().getValue();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			} else if(hasPure) {
				return pureValue;
			} else {
				try {
					pureValue = threadedValue.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				return pureValue;
			}
		}
	}
}
