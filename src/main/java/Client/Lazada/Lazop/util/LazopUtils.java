package Client.Lazada.Lazop.util;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetAddress;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * Lazada Open Platform utilities.
 * 
 * @author carver.gu
 * @since Feb 4, 2018
 */
public abstract class LazopUtils {

	private static String intranetIp;

	private LazopUtils() {
		
	}

	/**
	 * Sign the API request using particular method.
	 */
	public static String signApiRequest(RequestContext requestContext, String appSecret, String signMethod) throws IOException {
		return signApiRequest(requestContext.getApiName(),requestContext.getAllParams(), null, appSecret, signMethod);
	}

	/**
	 * Sign the API request with body.
	 */
	public static String signApiRequest(String apiName,Map<String, String> params, String body, String appSecret, String signMethod) throws IOException {
		// first: sort all text parameters
		String[] keys = params.keySet().toArray(new String[0]);
		Arrays.sort(keys);

		// second: connect all text parameters with key and value
		StringBuilder query = new StringBuilder();
		query.append(apiName);
		
		for (String key : keys) {
			String value = params.get(key);
			if (areNotEmpty(key, value)) {
				query.append(key).append(value);
			}
		}

		// third：put the body to the end
		if (body != null) {
			query.append(body);
		}

		// next : sign the whole request
		byte[] bytes = null;
		
		if(signMethod.equals(Constants.SIGN_METHOD_SHA256)) {
			bytes = encryptHMACSHA256(query.toString(), appSecret);
		} else {
			throw new IOException("Invalid Sign Method");
		}

		// finally : transfer sign result from binary to upper hex string
		return byte2hex(bytes);
	}

	
    private static byte[] encryptHMACSHA256(String data, String secret) throws IOException  {
    	byte[] bytes = null;
    	try {
	        SecretKey secretKey = new SecretKeySpec(secret.getBytes(Constants.CHARSET_UTF8), Constants.SIGN_METHOD_HMAC_SHA256);
	        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
	        mac.init(secretKey);
	        bytes = mac.doFinal(data.getBytes(Constants.CHARSET_UTF8));
    	} catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }
    
	/**
	 * Transfer binary array to HEX string.
	 */
	public static String byte2hex(byte[] bytes) {
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex.toUpperCase());
		}
		return sign.toString();
	}

	/**
	 * Clean the empty key or value items in map.
	 */
	public static <V> Map<String, V> cleanupMap(Map<String, V> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}

		Map<String, V> result = new HashMap<String, V>(map.size());
		Set<Entry<String, V>> entries = map.entrySet();

		for (Entry<String, V> entry : entries) {
			if (entry.getValue() != null) {
				result.put(entry.getKey(), entry.getValue());
			}
		}

		return result;
	}

	/**
	 * Get local IP address. Return 127.0.0.1 when any exception happen.
	 */
	public static String getIntranetIp() {
		if (intranetIp == null) {
			try {
				intranetIp = InetAddress.getLocalHost().getHostAddress();
			} catch (Exception e) {
				intranetIp = "127.0.0.1";
			}
		}
		return intranetIp;
	}

	/**
	 * Check whether the give string is null or blank.
	 * <ul>
	 * <li>SysUtils.isEmpty(null) = true</li>
	 * <li>SysUtils.isEmpty("") = true</li>
	 * <li>SysUtils.isEmpty(" ") = true</li>
	 * <li>SysUtils.isEmpty("abc") = false</li>
	 * </ul>
	 */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check whether the given string list are null or blank.
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	/**
	 * Format date use target pattern.
	 */
	public static String formatDateTime(Date date, String pattern) {
		DateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

}
