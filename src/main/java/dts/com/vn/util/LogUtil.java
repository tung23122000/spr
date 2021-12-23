package dts.com.vn.util;

import com.google.gson.Gson;
import dts.com.vn.enumeration.LogConstants;
import org.slf4j.Logger;

public class LogUtil {

	public static void writeLog(Logger loggerName, String action, Object data) {
		String jsonString = new Gson().toJson(data);
		switch (action.toUpperCase()) {
			case LogConstants.REQUEST:
				loggerName.info("==========> " + LogConstants.LOG_REQUEST + "{}", jsonString);
				break;

			case LogConstants.RESPONSE:
				loggerName.info("==========> " + LogConstants.LOG_RESPONSE + "{}", jsonString);
				break;

			case LogConstants.ERROR:
				loggerName.error("==========> " + LogConstants.LOG_RESPONSE_FAIL + "{}", jsonString);
				break;

			default:
				loggerName.info("==========> " + LogConstants.LOG_OTHER + "{}", action + " " + jsonString);
				break;
		}
	}

}
