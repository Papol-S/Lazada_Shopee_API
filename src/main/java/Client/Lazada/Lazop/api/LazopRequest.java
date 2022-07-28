package Client.Lazada.Lazop.api;

import Client.Lazada.Lazop.util.Constants;
import Client.Lazada.Lazop.util.FileItem;
import Client.Lazada.Lazop.util.LazopHashMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Lazada Open Platform API basic request.
 * 
 * @author carver.gu
 * @since Feb 4, 2018
 */
public class LazopRequest {

	/**
	 * User custom request query parameters.
	 */
	protected LazopHashMap apiParams;

	/**
	 * HTTP header parameters.
	 */
	protected LazopHashMap headerParams;

	/**
	 * HTTP file parameters.
	 */
	protected Map<String, FileItem> fileParams;

	private Long timestamp;
	private String apiName;
	private String httpMethod = Constants.METHOD_POST;
	
	public LazopRequest() {
		
	}
	
	/**
	 * create LazopRequest with apiName
	 * @param apiName
	 */
	public LazopRequest(String apiName) {
		this.apiName = apiName;
	}

	public void addApiParameter(String key, String value) {
		if (this.apiParams == null) {
			this.apiParams = new LazopHashMap();
		}
		this.apiParams.put(key, value);
	}

	public void addFileParameter(String key, FileItem file) {
		if (this.fileParams == null) {
			this.fileParams = new HashMap<String, FileItem>();
		}
		this.fileParams.put(key, file);
	}

	public void addHeaderParameter(String key, String value) {
		if (this.headerParams == null) {
			this.headerParams = new LazopHashMap();
		}
		this.headerParams.put(key, value);
	}

	public LazopHashMap getApiParams() {
		return apiParams;
	}

	public Map<String, FileItem> getFileParams() {
		return fileParams;
	}

	public Map<String, String> getHeaderParams() {
		return this.headerParams;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public void setHeaderParams(LazopHashMap headerParams) {
		this.headerParams = headerParams;
	}

}
