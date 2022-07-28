package Client.Lazada.Lazop.api;

import java.io.Serializable;

/**
 * Lazada Open Platform API basic response.
 * 
 * @author carver.gu
 * @since Feb 4, 2018
 */
public class LazopResponse implements Serializable {

	private static final long serialVersionUID = 5014379068811962022L;

	private String type;
	private String code;
	private String message;
	private String requestId;
	private String body;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isSuccess() {
		return code == null || "0".equals(code);
	}

}
