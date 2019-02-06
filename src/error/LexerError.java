package error;

public class LexerError {

	public static class NoEqualityException extends Exception {
		private static final long serialVersionUID = -5503627896242968225L;
		public NoEqualityException() {}
		public NoEqualityException(String message) {
			super(message);
		}
	}
	
	public static class NoArgumentSectionException extends Exception {
		private static final long serialVersionUID = -2638423477920174420L;
		public NoArgumentSectionException() {};
		public NoArgumentSectionException(String message) {
			super(message);
		}
	}
}
