package org.apache.jmeter.protocol.infinispan.sampler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanSampler extends AbstractSampler implements Interruptible {

    private static final long serialVersionUID = 240L;

    private static final Logger log = LoggerFactory.getLogger(InfinispanSampler.class);

    private static final Set<String> APPLIABLE_CONFIG_CLASSES = new HashSet<>(
            Arrays.asList("org.apache.jmeter.config.gui.LoginConfigGui",
                    "org.apache.jmeter.protocol.infinispan.config.gui.InfinispanConfigGui",
                    "org.apache.jmeter.config.gui.SimpleConfigGui"));

    public static final String SERVER_LIST = "InfinispanSampler.serverlist";
    public static final String KEY = "InfinispanSampler.key"; 
    public static final String OBJECT_NAME = "InfinispanSampler.objectname";
    public static final String JSON_CONTENT = "InfinispanSampler.jsoncontent";
    public static final String METHOD = "InfinispanSampler.method";

    @Override
    public SampleResult sample(Entry arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean interrupt() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean applies(ConfigTestElement configElement) {
        String guiClass = configElement.getProperty(TestElement.GUI_CLASS).getStringValue();
        return APPLIABLE_CONFIG_CLASSES.contains(guiClass);
    }
}