package Client.Lazada.Lazop.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Lazada Open Platform logger.
 * 
 * @author carver.gu
 * @since Feb 4, 2018
 */
public class LazopLogger {

	private static final Log log = LogFactory.getLog(LazopLogger.class);
	private static final String LOG_SPLIT = "^_^";

	private static String osName = System.getProperties().getProperty("os.name");
	private static boolean needEnableLogger = true;

	public static void setNeedEnableLogger(boolean needEnableLogger) {
		LazopLogger.needEnableLogger = needEnableLogger;
	}

	public static void write(String appKey, String sdkVersion, String apiName, String url, Map<String, String> params,
								   long latency, String errorMessage) {
		if (!needEnableLogger) {
			return;
		}

		StringBuilder sb = buildLogApi(appKey, sdkVersion, apiName, url, params, latency, errorMessage);
		log.error(sb.toString());
	}

	private static StringBuilder buildLogApi(String appKey, String sdkVersion, String apiName, String url, Map<String, String> params,
											 long latency, String errorMessage) {
		StringBuilder sb = new StringBuilder();
		sb.append(formatDateTime(new Date()));// timestamp
		sb.append(LOG_SPLIT);
		sb.append(appKey);// appkey
		sb.append(LOG_SPLIT);
		sb.append(sdkVersion);
		sb.append(LOG_SPLIT);
		sb.append(apiName);//
		sb.append(LOG_SPLIT);
		sb.append(LazopUtils.getIntranetIp());// internal ip address
		sb.append(LOG_SPLIT);
		sb.append(osName);
		sb.append(LOG_SPLIT);
		sb.append(latency);
		sb.append(LOG_SPLIT);
		sb.append(url);// gateway URL
		try {
			sb.append(LOG_SPLIT);
			sb.append(WebUtils.buildQuery(params, "utf-8"));// parameters
		} catch (IOException e) {
		}
		sb.append(LOG_SPLIT);
		sb.append(errorMessage); // error info
		return sb;
	}

	private static String formatDateTime(Date date) {
		return LazopUtils.formatDateTime(date, "yyyy-MM-dd HH:mm:ss.SSS");
	}
}
