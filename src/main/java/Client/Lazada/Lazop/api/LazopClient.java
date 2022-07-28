package Client.Lazada.Lazop.api;

import Client.Lazada.Lazop.util.*;
import Client.Lazada.Lazop.util.json.JSONValidatingReader;
import Client.Lazada.Lazop.util.json.JSONReader;

import java.io.IOException;
import java.net.Proxy;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Lazada Open Platform API Client.
 * 
 * @author carver.gu
 * @since Feb 4, 2018
 */
public class LazopClient implements ILazopClient {

	protected String serverUrl;
	protected String appKey;
	protected String appSecret;

	protected String signMethod = Constants.SIGN_METHOD_SHA256;
	protected int connectTimeout = 15000; // default connection timeout
	protected int readTimeout = 30000; // default read timeout
	protected boolean useGzipEncoding = true; // use gzip encoding or not
	protected Proxy proxy;
	protected String sdkVersion = "lazop-sdk-java-20181207";
	protected String logLevel = Constants.LOG_LEVEL_ERROR;

	public LazopClient(String serverUrl, String appKey, String appSecret) {
		this.appKey = appKey;
		this.appSecret = appSecret;
		this.serverUrl = serverUrl;
	}

	public LazopClient(String serverUrl, String appKey, String appSecret, int connectTimeout, int readTimeout) {
		this(serverUrl, appKey, appSecret);
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}

	public LazopResponse execute(LazopRequest request) throws ApiException {
		return execute(request, null);
	}

	public LazopResponse execute(LazopRequest request, String accessToken) throws ApiException {
		return doExecute(request, accessToken);
	}

	private LazopResponse doExecute(LazopRequest request, String accessToken) throws ApiException {
		long start = System.currentTimeMillis();

		RequestContext requestContext = new RequestContext();
		LazopHashMap bizParams = new LazopHashMap(request.getApiParams() != null ? request.getApiParams() : new HashMap<String, String>());
		requestContext.setQueryParams(bizParams);
		requestContext.setApiName(request.getApiName());

		// add common parameters
		LazopHashMap commonParams = new LazopHashMap();
		commonParams.put(Constants.APP_KEY, appKey);
		Long timestamp = request.getTimestamp();
		if (timestamp == null) {
			timestamp = System.currentTimeMillis();
		}

		commonParams.put(Constants.TIMESTAMP, new Date(timestamp));
		commonParams.put(Constants.SIGN_METHOD, signMethod);
		commonParams.put(Constants.ACCESS_TOKEN, accessToken);
		commonParams.put(Constants.PARTNER_ID, sdkVersion);

		if(isDebugEnabled()) {
			commonParams.put(Constants.DEBUG, true);
		}

		requestContext.setCommonParams(commonParams);

		try {
			// compute request signature
			commonParams.put(Constants.SIGN, LazopUtils.signApiRequest(requestContext, appSecret, signMethod));
			String rpcUrl = WebUtils.buildRestUrl(this.serverUrl, request.getApiName());
			String urlQuery = WebUtils.buildQuery(requestContext.getCommonParams(), Constants.CHARSET_UTF8);
			String fullUrl = WebUtils.buildRequestUrl(rpcUrl, urlQuery);

			String rsp = null;
			if (this.useGzipEncoding) {
				request.addHeaderParameter(Constants.ACCEPT_ENCODING, Constants.CONTENT_ENCODING_GZIP);
			}

			// use file upload request if there are file parameters
			if (request.getFileParams() != null) {
				rsp = WebUtils.doPost(fullUrl, bizParams, request.getFileParams(), request.getHeaderParams(), Constants.CHARSET_UTF8, connectTimeout, readTimeout);
			} else {
				if(request.getHttpMethod().equals(Constants.METHOD_POST)){
					rsp = WebUtils.doPost(fullUrl, bizParams, request.getHeaderParams(), Constants.CHARSET_UTF8, connectTimeout, readTimeout, proxy);
				} else {
					rsp = WebUtils.doGet(fullUrl, bizParams, request.getHeaderParams(), connectTimeout, readTimeout, Constants.CHARSET_UTF8, proxy);
				}
			}

			requestContext.setResponseBody(rsp);
		} catch (IOException e) {
			LazopLogger.write(appKey, sdkVersion, request.getApiName(), serverUrl, requestContext.getAllParams(), System.currentTimeMillis() - start, e.toString());
			throw new ApiException(e);
		} catch (Exception e) {
			LazopLogger.write(appKey, sdkVersion, request.getApiName(), serverUrl, requestContext.getAllParams(), System.currentTimeMillis() - start, e.toString());
			throw new ApiException(e);
		}

		LazopResponse response = parseResponse(requestContext.getResponseBody());
		if (!response.isSuccess()) {
			LazopLogger.write(appKey, sdkVersion, request.getApiName(), serverUrl, requestContext.getAllParams(), System.currentTimeMillis() - start, response.getBody());
		} else {
			if(isDebugEnabled() || isInfoEnabled()) {
				LazopLogger.write(appKey, sdkVersion, request.getApiName(), serverUrl, requestContext.getAllParams(), System.currentTimeMillis() - start, "");
			}
		}

		return response;
	}

	private LazopResponse parseResponse(String jsonRsp) {
		JSONReader reader = new JSONValidatingReader();
		Map<?, ?> root = (Map<?, ?>) reader.read(jsonRsp);
		LazopResponse response = new LazopResponse();
		response.setType((String) root.get(Constants.RSP_TYPE));
		response.setCode((String) root.get(Constants.RSP_CODE));
		response.setMessage((String) root.get(Constants.RSP_MSG));
		response.setRequestId((String) root.get(Constants.RSP_REQUEST_ID));
		response.setBody(jsonRsp);
		return response;
	}

	/**
	 * Enable request logging when error happen.
	 */
	public void setNeedEnableLogger(boolean needEnableLogger) {
		LazopLogger.setNeedEnableLogger(needEnableLogger);
	}

	/**
	 * Set whether ignore SSL certificate verification.
	 */
	public void setIgnoreSSLCheck(boolean ignore) {
		WebUtils.setIgnoreSSLCheck(ignore);
	}

	/**
	 * Set whether need gzip encoding or not.
	 */
	public void setUseGzipEncoding(boolean useGzipEncoding) {
		this.useGzipEncoding = useGzipEncoding;
	}

	/**
	 * Set socket connect timeout time, default is 15 seconds.
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * Set socket read timeout time, default is 30 seconds.
	 */
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	/**
	 * set new sign method to client
	 * @param signMethod
	 */
	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

	/**
	 * Set request proxy.
	 */
    public void setProxy(Proxy proxy){
    	this.proxy = proxy;
    }

    public void setLogLevel(String logLevel) {
    	this.logLevel = logLevel;
	}

    public boolean isDebugEnabled() {
		return logLevel.equals(Constants.LOG_LEVEL_DEBUG);
	}

	public boolean isInfoEnabled() {
    	return logLevel.equals(Constants.LOG_LEVEL_INFO);
	}

	public boolean isErrorEnabled() {
    	return logLevel.equals(Constants.LOG_LEVEL_ERROR);
	}
}
