/**
 * 
 */
package edu.opinion.forecast.model;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.jstatcom.engine.Engine;
import com.jstatcom.engine.EngineTypes;
import com.jstatcom.engine.PCall;
import com.jstatcom.util.FArg;


/**
 * @author ch
 *
 */
public abstract class GaussPCall extends PCall {	
	private static final Logger log = Logger.getLogger(GaussPCall.class);

    private static boolean isGRTE = true;
    static {
        Properties sysprops = System.getProperties();
        String grte = sysprops.getProperty("GRTE", "true");
        log.info(FArg.sprintf("%-20s%s", new FArg("GRTE:").add(grte)));
        isGRTE = Boolean.valueOf(grte).booleanValue();
    }

	/* (non-Javadoc)
	 * @see com.jstatcom.engine.PCall#engine()
	 */
	@Override
	public Engine engine() {
		String osName = System.getProperty("os.name").toUpperCase();
        if (isGRTE || osName.indexOf("WIN") < 0)
            return EngineTypes.GRTE.getEngine();

        return EngineTypes.GAUSS.getEngine();
	}


}
