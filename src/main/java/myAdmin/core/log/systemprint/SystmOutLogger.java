
package myAdmin.core.log.systemprint;

import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystmOutLogger extends PrintStream {
	
	private Logger LOGGER = LoggerFactory.getLogger("System.out.println");
	
	public SystmOutLogger() {
		super(System.out);
	}
	
	public void println(boolean x) {
		log(Boolean.valueOf(x));
	}
	
	public void println(char x) {
		log(Character.valueOf(x));
	}
	
	public void println(char[] x) {
		log(x == null ? null : new String(x));
	}
	
	public void println(double x) {
		log(Double.valueOf(x));
	}
	
	public void println(float x) {
		log(Float.valueOf(x));
	}
	
	public void println(int x) {
		log(Integer.valueOf(x));
	}
	
	public void println(long x) {
		log(x);
	}
	
	public void println(Object x) {
		log(x);
	}
	
	public void println(String x) {
		log(x);
	}
	
	private void log(Object x) {
		LOGGER.info("{}", x);
	}
	
}
