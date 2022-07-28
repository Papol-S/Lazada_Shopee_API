package Client.Lazada.Lazop.util;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * Web utilities.
 * 
 * @author carver.gu
 * @since Feb 4, 2018
 */
public abstract class WebUtils {

	private static final String DEFAULT_CHARSET = Constants.CHARSET_UTF8;
	private static boolean ignoreSSLCheck = true; // ignore SSL check when establish connection
	private static boolean ignoreHostCheck = true; // ignore HOST check when establish connection

	public static class TrustAllTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}
	}

	private WebUtils() {
	}

	public static void setIgnoreSSLCheck(boolean ignoreSSLCheck) {
		WebUtils.ignoreSSLCheck = ignoreSSLCheck;
	}

	public static void setIgnoreHostCheck(boolean ignoreHostCheck) {
		WebUtils.ignoreHostCheck = ignoreHostCheck;
	}

	/**
	 * Do HTTP GET request with query parameters.
	 */
	public static String doGet(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws IOException {
		return doGet(url, params, null, connectTimeout, readTimeout, DEFAULT_CHARSET, null);
	}

	/**
	 * Do HTTP GET request with query and header parameters.
	 */
	public static String doGet(String url, Map<String, String> queryParams, Map<String, String> headerParams, int connectTimeout, int readTimeout, String charset, Proxy proxy) throws IOException {
		HttpURLConnection conn = null;
		String rsp = null;

		try {
			String ctype = "application/x-www-form-urlencoded;charset=" + charset;
			String query = buildQuery(queryParams, charset);
			conn = getConnection(buildGetUrl(url, query), Constants.METHOD_GET, ctype, headerParams, proxy);
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			rsp = getResponseAsString(conn);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	/**
	 * Do HTTP POST request with query parameters.
	 */
	public static String doPost(String url, Map<String, String> params, int connectTimeout, int readTimeout) throws IOException {
		return doPost(url, params, null, DEFAULT_CHARSET, connectTimeout, readTimeout, null);
	}

	/**
	 * Do HTTP POST request with body payload and header parameters.
	 */
	public static String doPost(String url, String body, Map<String, String> headers, String charset, int connectTimeout, int readTimeout) throws IOException {
        String ctype = "text/plain;charset=" + charset;
        byte[] content = body.getBytes(charset);
        return _doPost(url, ctype, content, headers, connectTimeout, readTimeout, null);
    }

	/**
	 * Do HTTP POST request with query and header parameters.
	 */
	public static String doPost(String url, Map<String, String> queryParams, Map<String, String> headerParams, String charset, int connectTimeout, int readTimeout, Proxy proxy) throws IOException {
		String ctype = "application/x-www-form-urlencoded;charset=" + charset;
		String query = buildQuery(queryParams, charset);
		byte[] content = {};
		if (query != null) {
			content = query.getBytes(charset);
		}
		return _doPost(url, ctype, content, headerParams, connectTimeout, readTimeout, proxy);
	}

	private static String _doPost(String url, String ctype, byte[] content, Map<String, String> headers, int connectTimeout, int readTimeout, Proxy proxy) throws IOException {
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;

		try {
			conn = getConnection(new URL(url), Constants.METHOD_POST, ctype, headers, proxy);
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			out = conn.getOutputStream();
			out.write(content);
			rsp = getResponseAsString(conn);
		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	/**
	 * Do HTTP POST request with query and file parameters.
	 */
	public static String doPost(String url, Map<String, String> queryParams, Map<String, FileItem> fileParams, int connectTimeout, int readTimeout) throws IOException {
		return doPost(url, queryParams, fileParams, null, DEFAULT_CHARSET, connectTimeout, readTimeout);
	}

	/**
	 * Do HTTP POST request with query, file and header parameters.
	 */
	public static String doPost(String url, Map<String, String> queryParams, Map<String, FileItem> fileParams, Map<String, String> headerParams,
			String charset, int connectTimeout, int readTimeout) throws IOException {
		if (fileParams == null || fileParams.isEmpty()) {
			return doPost(url, queryParams, headerParams, charset, connectTimeout, readTimeout, null);
		} else {
			return _doPostWithFile(url, queryParams, fileParams, headerParams, charset, connectTimeout, readTimeout);
		}
	}

	private static String _doPostWithFile(String url, Map<String, String> queryParams, Map<String, FileItem> fileParams,
			Map<String, String> headerParams, String charset, int connectTimeout, int readTimeout) throws IOException {
		String boundary = String.valueOf(System.nanoTime());
		HttpURLConnection conn = null;
		OutputStream out = null;
		String rsp = null;
		try {
			String ctype = "multipart/form-data;charset=" + charset + ";boundary=" + boundary;
			conn = getConnection(new URL(url), Constants.METHOD_POST, ctype, headerParams, null);
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);
			out = conn.getOutputStream();

			// add upload request start line
			byte[] entryBoundaryBytes = ("\r\n--" + boundary + "\r\n").getBytes(charset);

			Set<Entry<String, String>> textEntrySet = queryParams.entrySet();
			for (Entry<String, String> textEntry : textEntrySet) {
				byte[] textBytes = getTextEntry(textEntry.getKey(), textEntry.getValue(), charset);
				out.write(entryBoundaryBytes);
				out.write(textBytes);
			}

			Set<Entry<String, FileItem>> fileEntrySet = fileParams.entrySet();
			for (Entry<String, FileItem> fileEntry : fileEntrySet) {
				FileItem fileItem = fileEntry.getValue();
				if (!fileItem.isValid()) {
					throw new IOException("FileItem is invalid");
				}
				byte[] fileBytes = getFileEntry(fileEntry.getKey(), fileItem.getFileName(), fileItem.getMimeType(), charset);
				out.write(entryBoundaryBytes);
				out.write(fileBytes);
				fileItem.write(out);
			}

			// add upload request end line
			byte[] endBoundaryBytes = ("\r\n--" + boundary + "--\r\n").getBytes(charset);
			out.write(endBoundaryBytes);
			rsp = getResponseAsString(conn);
		} finally {
			if (out != null) {
				out.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}

		return rsp;
	}

	private static byte[] getTextEntry(String fieldName, String fieldValue, String charset) throws IOException {
		StringBuilder entry = new StringBuilder();
		entry.append("Content-Disposition:form-data;name=\"");
		entry.append(fieldName);
		entry.append("\"\r\nContent-Type:text/plain\r\n\r\n");
		entry.append(fieldValue);
		return entry.toString().getBytes(charset);
	}

	private static byte[] getFileEntry(String fieldName, String fileName, String mimeType, String charset) throws IOException {
		StringBuilder entry = new StringBuilder();
		entry.append("Content-Disposition:form-data;name=\"");
		entry.append(fieldName);
		entry.append("\";filename=\"");
		entry.append(fileName);
		entry.append("\"\r\nContent-Type:");
		entry.append(mimeType);
		entry.append("\r\n\r\n");
		return entry.toString().getBytes(charset);
	}

	private static HttpURLConnection getConnection(URL url, String method, String ctype, Map<String, String> headers, Proxy proxy) throws IOException {
		HttpURLConnection conn = null;
		if (proxy == null) {
			conn = (HttpURLConnection) url.openConnection();
		} else {
			conn = (HttpURLConnection) url.openConnection(proxy);
		}
		if (conn instanceof HttpsURLConnection) {
			HttpsURLConnection connHttps = (HttpsURLConnection) conn;
			if (ignoreSSLCheck) {
				try {
					SSLContext ctx = SSLContext.getInstance("TLS");
					ctx.init(null, new TrustManager[] { new TrustAllTrustManager() }, new SecureRandom());
					connHttps.setSSLSocketFactory(ctx.getSocketFactory());
					connHttps.setHostnameVerifier(new HostnameVerifier() {
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					});
				} catch (Exception e) {
					throw new IOException(e.toString());
				}
			} else {
				if (ignoreHostCheck) {
					connHttps.setHostnameVerifier(new HostnameVerifier() {
						public boolean verify(String hostname, SSLSession session) {
							return true;
						}
					});
				}
			}
			conn = connHttps;
		}

		conn.setRequestMethod(method);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Host", url.getHost());
		conn.setRequestProperty("Accept", "text/xml,text/javascript");
		conn.setRequestProperty("User-Agent", Constants.SDK_VERSION);
		conn.setRequestProperty("Content-Type", ctype);
		if (headers != null) {
			for (Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		return conn;
	}

	private static URL buildGetUrl(String url, String query) throws IOException {
		if (LazopUtils.isEmpty(query)) {
			return new URL(url);
		}

		return new URL(buildRequestUrl(url, query));
	}

	public static String buildRequestUrl(String url, String... queries) {
		if (queries == null || queries.length == 0) {
			return url;
		}

		StringBuilder newUrl = new StringBuilder(url);
		boolean hasQuery = url.contains("?");
		boolean hasPrepend = url.endsWith("?") || url.endsWith("&");

		for (String query : queries) {
			if (!LazopUtils.isEmpty(query)) {
				if (!hasPrepend) {
					if (hasQuery) {
						newUrl.append("&");
					} else {
						newUrl.append("?");
						hasQuery = true;
					}
				}
				newUrl.append(query);
				hasPrepend = false;
			}
		}
		return newUrl.toString();
	}

	public static String buildRestUrl(String url, String apiName) {
		if (apiName == null || apiName.length() == 0) {
			return url;
		}

		boolean hasPrepend = url.endsWith("/");
		if (hasPrepend) {
			return url + apiName.substring(1);
		} else {
			return url + apiName;
		}
	}

	public static String buildQuery(Map<String, String> params, String charset) throws IOException {
		if (params == null || params.isEmpty()) {
			return null;
		}

		StringBuilder query = new StringBuilder();
		Set<Entry<String, String>> entries = params.entrySet();
		boolean hasParam = false;

		for (Entry<String, String> entry : entries) {
			String name = entry.getKey();
			String value = entry.getValue();
			// ignore blank parameter
			if (LazopUtils.areNotEmpty(name, value)) {
				if (hasParam) {
					query.append("&");
				} else {
					hasParam = true;
				}

				query.append(name).append("=").append(URLEncoder.encode(value, charset));
			}
		}

		return query.toString();
	}

	protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
		String charset = getResponseCharset(conn.getContentType());
		if (conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
			String contentEncoding = conn.getContentEncoding();
			if (Constants.CONTENT_ENCODING_GZIP.equalsIgnoreCase(contentEncoding)) {
				return getStreamAsString(new GZIPInputStream(conn.getInputStream()), charset);
			} else {
				return getStreamAsString(conn.getInputStream(), charset);
			}
		} else {
			// OAuth bad request always return 400 status
			if (conn.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
				InputStream error = conn.getErrorStream();
				if (error != null) {
					return getStreamAsString(error, charset);
				}
			}
			// Client Error 4xx and Server Error 5xx
			throw new IOException(conn.getResponseCode() + " " + conn.getResponseMessage());
		}
	}

	public static String getStreamAsString(InputStream stream, String charset) throws IOException {
		try {
			Reader reader = new InputStreamReader(stream, charset);
			StringBuilder response = new StringBuilder();

			final char[] buff = new char[1024];
			int read = 0;
			while ((read = reader.read(buff)) > 0) {
				response.append(buff, 0, read);
			}

			return response.toString();
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
	}

	public static String getResponseCharset(String ctype) {
		String charset = DEFAULT_CHARSET;

		if (!LazopUtils.isEmpty(ctype)) {
			String[] params = ctype.split(";");
			for (String param : params) {
				param = param.trim();
				if (param.startsWith("charset")) {
					String[] pair = param.split("=", 2);
					if (pair.length == 2) {
						if (!LazopUtils.isEmpty(pair[1])) {
							charset = pair[1].trim();
						}
					}
					break;
				}
			}
		}

		return charset;
	}

	/**
	 * Split parameter's key value pairs from query.
	 */
	public static Map<String, String> splitUrlQuery(String query) {
		Map<String, String> result = new HashMap<String, String>();

		String[] pairs = query.split("&");
		if (pairs != null && pairs.length > 0) {
			for (String pair : pairs) {
				String[] param = pair.split("=", 2);
				if (param != null && param.length == 2) {
					result.put(param[0], param[1]);
				}
			}
		}

		return result;
	}

}
