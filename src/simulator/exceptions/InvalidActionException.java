package simulator.exceptions;

public class InvalidActionException extends Exception {
	public InvalidActionException() {
		super(); 
		}
	public InvalidActionException(String message){
		super(message);
		}
	public InvalidActionException(String message, Throwable cause){
	 super(message, cause);
	}
	public InvalidActionException(Throwable cause){ 
		super(cause);
		}
	public InvalidActionException(String message, Throwable cause,boolean enableSuppression, boolean writableStackTrace){
	 super(message, cause, enableSuppression, writableStackTrace);
	}
}
