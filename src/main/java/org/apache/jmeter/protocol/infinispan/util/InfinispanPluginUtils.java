package org.apache.jmeter.protocol.infinispan.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.jmeter.util.JMeterUtils;

//import org.apache.jmeter.util.JMeterUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanPluginUtils {

    private static final Logger log = LoggerFactory.getLogger(InfinispanPluginUtils.class);

    private static final String PACKAGE_MESSAGE = "org.apache.jmeter.protocol.infinispan.resources.messages";

//    private static Locale locale = JMeterUtils.getLocale();
    private static Locale locale = new Locale("en","US");

    private static volatile ResourceBundle resources;

    static {
        log.info("Setting Infinispan Locale to {}", locale);
        resources = ResourceBundle.getBundle(PACKAGE_MESSAGE, locale);
    }

    public static final String RES_KEY_PFX = "[res_key=";



    public static String getResString(String key) {
        return getResStringDefault(key, RES_KEY_PFX + key + "]"); // $NON-NLS-1$
    }

    private static String getResStringDefault(String key, String defaultValue) {
        if (key == null) {
            return null;
        }
        // Resource keys cannot contain spaces, and are forced to lower case
        String resKey = key.replace(' ', '_'); 
        resKey = resKey.toLowerCase(java.util.Locale.ENGLISH);
        String resString = null;
        try {
            ResourceBundle bundle = resources;
            if (bundle.containsKey(resKey)) {
                resString = bundle.getString(resKey);
            } else {
                if (defaultValue == null) {
                    log.warn("ERROR! Resource string not found: [{}]", resKey);
                } else {
                    log.debug("Resource string not found: [{}], using default value {}", resKey, defaultValue);
                }
                resString = defaultValue;
            }
        } catch (MissingResourceException mre) { 
            if (defaultValue == null) {
                log.warn("ERROR! Resource string not found: [{}]", resKey);
            } else {
                log.debug("Resource string not found: [{}], using default value {}", resKey, defaultValue);
            }
            resString = defaultValue;
        }
        return resString;
    }

    /**
     * Generate a list of paths to search.
     * The output array always starts with
     * JMETER_HOME/lib/ext
     * and is followed by any paths obtained from the "search_paths" JMeter property.
     *
     * @return array of path strings
     */
    public static String[] getPluginDependencyPaths() {
        String p = JMeterUtils.getPropDefault("plugin_dependency_paths", null);
        String[] result = new String[1];

        if (p != null) {
            String[] paths = p.split(";"); // $NON-NLS-1$
            result = new String[paths.length + 1];
            System.arraycopy(paths, 0, result, 1, paths.length);
        }
        result[0] = JMeterUtils.getJMeterHome() + "/lib";
        return result;
    }
    

}
