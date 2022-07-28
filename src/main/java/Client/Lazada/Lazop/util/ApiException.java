package Client.Lazada.Lazop.util;

/**
 * Lazada Open Platform common exception.
 * 
 * @author carver.gu
 * @since Feb 4, 2018
 */
public class ApiException extends Exception {

	private static final long serialVersionUID = -238091758285157331L;

	private String errorCode;
	private String errorMessage;

    public ApiException() {
		super();
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public ApiException(String errorCode, String errorMessage) {
		super(errorCode + ":" + errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

}
