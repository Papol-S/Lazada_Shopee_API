package Client.Lazada.Lazop.util;

/**
 * Constant values.
 * 
 * @author carver.gu
 * @since Feb 4, 2018
 */
public abstract class Constants {

	/** lazop common request parameters **/
	public static final String APP_KEY = "app_key";
	public static final String TIMESTAMP = "timestamp";
	public static final String SIGN = "sign";
	public static final String SIGN_METHOD = "sign_method";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String PARTNER_ID = "partner_id";
	public static final String DEBUG = "debug";
	public static final String NONCE = "nonce";

	/** lazop error response parameters **/
	public static final String RSP_TYPE = "type";
	public static final String RSP_CODE = "code";
	public static final String RSP_MSG = "message";
	public static final String RSP_REQUEST_ID = "request_id";

	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String CHARSET_UTF8 = "UTF-8";

	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";

    public static final String SIGN_METHOD_HMAC = "hmac";
	public static final String SIGN_METHOD_HMAC_MD5 = "HmacMD5";

	public static final String SIGN_METHOD_SHA256 = "sha256";
	public static final String SIGN_METHOD_HMAC_SHA256 = "HmacSHA256";


	public static final String SDK_VERSION = "lazop-sdk-java";
	public static final String ACCEPT_ENCODING = "Accept-Encoding";
	public static final String CONTENT_ENCODING = "Content-Encoding";
	public static final String CONTENT_ENCODING_GZIP = "gzip";
	public static final String MIME_TYPE_DEFAULT = "application/octet-stream";

	public static final int READ_BUFFER_SIZE = 1024 * 4;

	/** lazop log level **/
	public static final String LOG_LEVEL_DEBUG = "DEBUG";
	public static final String LOG_LEVEL_INFO = "INFO";
	public static final String LOG_LEVEL_ERROR = "ERROR";
}
