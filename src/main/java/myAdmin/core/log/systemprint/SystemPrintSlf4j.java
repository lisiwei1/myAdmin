package myAdmin.core.log.systemprint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemPrintSlf4j {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(SystemPrintSlf4j.class);
	
	public static void init() {
		LOGGER.info("init system print log...");
		System.setOut(new SystmOutLogger());
		System.setErr(new SystemErrLogger());
		LOGGER.info("init system print log finish");
		
	}
	
}
