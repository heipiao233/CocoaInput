package jp.axer.cocoainput.util;

import com.sun.jna.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class NativeLogger {
    private static Logger NATIVE_LOGGER = LogManager.getLogger("CocoaInput:Native");
	
	public static Callback info = new Callback() {
        public void invoke(String msg) {
            NATIVE_LOGGER.info(msg);
        }
    };
    public static Callback error = new Callback() {
        public void invoke(String msg) {
            NATIVE_LOGGER.error(msg);
        }
    };

    public static Callback debug = new Callback() {
        public void invoke(String msg) {
            NATIVE_LOGGER.debug(msg);
        }
    };
}
