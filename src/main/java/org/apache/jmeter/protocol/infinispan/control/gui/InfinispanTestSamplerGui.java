package org.apache.jmeter.protocol.infinispan.control.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;

import org.apache.jmeter.config.gui.LoginConfigGui;

import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.protocol.infinispan.config.gui.InfinispanConfigGui;

import org.apache.jmeter.protocol.infinispan.sampler.InfinispanSampler;
import org.apache.jmeter.protocol.infinispan.util.InfinispanPluginUtils;

public class InfinispanTestSamplerGui extends AbstractSamplerGui {

    /**
     *
     */
    private static final long serialVersionUID = -5864590079303632120L;

    private LoginConfigGui loginPanel;

    private InfinispanConfigGui infinispanDefaultPanel;

    public InfinispanTestSamplerGui() {
        init();
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        loginPanel.configure(element);
        infinispanDefaultPanel.configure(element);
    }


    @Override
    public String getLabelResource() {
        return getClass().getCanonicalName();
    }

    @Override
    public String getStaticLabel() {
        return InfinispanPluginUtils.getResString("infinispan_panel_name");
    }

    @Override
    public String getName() {
        return super.getName() == null ? this.getStaticLabel() : super.getName();
    }

    @Override
    public TestElement createTestElement() {
        InfinispanSampler element = new InfinispanSampler();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        element.clear();
        infinispanDefaultPanel.modifyTestElement(element);
        loginPanel.modifyTestElement(element);
        super.configureTestElement(element);
    }
    
    /**
     * Implements JMeterGUIComponent.clearGui
     */
    @Override
    public void clearGui() {
        super.clearGui();

        infinispanDefaultPanel.clearGui();
        loginPanel.clearGui();
    }

    private void init() { // WARNING: called from ctor so must not be overridden (i.e. must be private or final)
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());

        add(makeTitlePanel(), BorderLayout.NORTH);

        VerticalPanel mainPanel = new VerticalPanel();

        infinispanDefaultPanel = new InfinispanConfigGui(false);
        mainPanel.add(infinispanDefaultPanel);

        loginPanel = new LoginConfigGui(false);
        loginPanel.setBorder(BorderFactory.createTitledBorder(JMeterUtils.getResString("login_config"))); // $NON-NLS-1$
        mainPanel.add(loginPanel);

        add(mainPanel, BorderLayout.CENTER);
    }

}
