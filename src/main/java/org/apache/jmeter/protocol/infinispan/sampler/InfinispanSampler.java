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
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

public class InfinispanSampler extends AbstractSampler implements Interruptible {

    private static final long serialVersionUID = 240L;

    private static final Logger log = LoggerFactory.getLogger(InfinispanSampler.class);

    private static final Set<String> APPLIABLE_CONFIG_CLASSES = new HashSet<>(
            Arrays.asList("org.apache.jmeter.config.gui.LoginConfigGui",
                    "org.apache.jmeter.protocol.infinispan.config.gui.InfinispanConfigGui",
                    "org.apache.jmeter.config.gui.SimpleConfigGui"));

    public static final String SERVER = "InfinispanSampler.server";
    public static final String PORT = "InfinispanSampler.port";
    public static final String CACHE_NAME = "InfinispanSampler.cachename";
    public static final String MARSHALLER = "InfinispanSampler.marshaller";
    public static final String KEY = "InfinispanSampler.key";
    public static final String STRING_VALUE = "InfinispanSampler.stringvalue";
    // public static final String OBJECT_NAME = "InfinispanSampler.objectname";
    // public static final String JSON_CONTENT = "InfinispanSampler.jsoncontent";
    public static final String METHOD = "InfinispanSampler.method";

    public InfinispanSampler(){
        super();
    }

    public String getUsername() {
        return getPropertyAsString(ConfigTestElement.USERNAME);
    }

    public String getPassword() {
        return getPropertyAsString(ConfigTestElement.PASSWORD);
    }

    public String getServer() {
        return getPropertyAsString(SERVER, "");
    }

    public String getPort() {
        return getPropertyAsString(PORT, "");
    }

    public int getPortAsInt() {
        return getPropertyAsInt(PORT, 0);
    }

    public String getCacheName() {
        return getPropertyAsString(CACHE_NAME, "");
    }

    public String getMarshallerString() {
        return getPropertyAsString(MARSHALLER, "");
    }

    public String getKey() {
        return getPropertyAsString(KEY, "");
    }

    public String getStringValue() {
        return getPropertyAsString(STRING_VALUE, "");
    }

    @Override
    public SampleResult sample(Entry arg0) {
        SampleResult res = new SampleResult();
  
        res.setSuccessful(false);


        ConfigurationBuilder clientBuilder = new ConfigurationBuilder(); 
        clientBuilder.addServer()
            .host(getServer()).port(getPortAsInt())
            .security().authentication().username(getUsername()).password(getPassword());
        
        RemoteCacheManager remoteCacheManager = new RemoteCacheManager(clientBuilder.build());

        return res;
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