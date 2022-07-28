package Client.Lazada.Lazop.util;

import java.util.HashMap;
import java.util.Map;

/**
 * API request context wrapper.
 * 
 * @author carver.gu
 * @since Feb 4, 2018
 */
public class RequestContext {

	private String requestUrl;
	private String responseBody;
	private String apiName;
	private LazopHashMap commonParams;
	private LazopHashMap queryParams;

	public String getRequestUrl() {
		return this.requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getResponseBody() {
		return this.responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public LazopHashMap getQueryParams() {
		return this.queryParams;
	}

	public void setQueryParams(LazopHashMap queryParams) {
		this.queryParams = queryParams;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public LazopHashMap getCommonParams() {
		return commonParams;
	}

	public void setCommonParams(LazopHashMap commonParams) {
		this.commonParams = commonParams;
	}

	public Map<String, String> getAllParams() {
		Map<String, String> params = new HashMap<String, String>();
		if (commonParams != null && !commonParams.isEmpty()) {
			params.putAll(commonParams);
		}

		if (queryParams != null && !queryParams.isEmpty()) {
			params.putAll(queryParams);
		}
		return params;
	}

}
