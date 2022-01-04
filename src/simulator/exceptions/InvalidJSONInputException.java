package simulator.exceptions;

public class InvalidJSONInputException extends Exception {
	public InvalidJSONInputException() {
		super(); 
		}
	public InvalidJSONInputException(String message){
		super(message);
		}
	public InvalidJSONInputException(String message, Throwable cause){
	 super(message, cause);
	}
	public InvalidJSONInputException(Throwable cause){ 
		super(cause);
		}
	public InvalidJSONInputException(String message, Throwable cause,boolean enableSuppression, boolean writableStackTrace){
	 super(message, cause, enableSuppression, writableStackTrace);
	}
}
