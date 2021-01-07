package org.apache.jmeter.protocol.infinispan.config.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Color;
import java.awt.Font;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.JLabeledChoice;
import org.apache.jorphan.reflect.ClassFinder;
import org.infinispan.commons.marshall.AbstractMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.jmeter.protocol.infinispan.sampler.InfinispanSampler;
import org.apache.jmeter.protocol.infinispan.util.InfinispanPluginUtils;

public class InfinispanConfigGui extends AbstractConfigGui implements ChangeListener {

    /**
     *
     */
    private static final long serialVersionUID = -157541532129212938L;

    private static final Logger log = LoggerFactory.getLogger(InfinispanConfigGui.class);

    private JTextField server;
    private JTextField port;
    private JTextField cacheName;
    private JLabeledChoice marshallerLabeledChoice;
    private JTextField key;
    // private JTextField objectName;
    // private JTextArea jsonContent;
    // private JCheckBox useSsl;

    private JRadioButton createBox;
    private JRadioButton readBox;
    private JRadioButton updateBox;
    private JRadioButton deleteBox;

    private boolean displayName = true;
    private final JLabel warningLabel;

    public InfinispanConfigGui() {
        this(true);
    }

    public InfinispanConfigGui(boolean displayName) {
        this.displayName = displayName;
        ImageIcon image = JMeterUtils.getImage("warning.png");
        warningLabel = new JLabel(InfinispanPluginUtils.getResString("infinispan_marshaller_warning"), image, SwingConstants.LEFT);
        init();
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    @Override
    public void clearGui() {
        super.clearGui();

        log.debug("Clearing infinispan Gui elements");

        server.setText("localhost");
        port.setText("11222");
        cacheName.setText("");
        marshallerLabeledChoice.setText("");
        key.setText("");
        // objectName.setText("");
        // jsonContent.setText("");

        createBox.setSelected(false);
        readBox.setSelected(true);
        updateBox.setSelected(false);
        deleteBox.setSelected(false);
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
    public void configure(TestElement element) {
        super.configure(element);

        log.debug("Configuring infinispan Gui elements");

        server.setText(element.getPropertyAsString(InfinispanSampler.SERVER));
        port.setText(element.getPropertyAsString(InfinispanSampler.PORT));
        cacheName.setText(element.getPropertyAsString(InfinispanSampler.CACHE_NAME));
 
        String className = element.getPropertyAsString(InfinispanSampler.MARSHALLER);
        if(!checkContainsClassName(marshallerLabeledChoice, className)) {
            marshallerLabeledChoice.addValue(className);
        }
        
        warningLabel.setVisible(!classOk(className));
        marshallerLabeledChoice.setText(className);
        // configureClassName();
 
        key.setText(element.getPropertyAsString(InfinispanSampler.KEY));
        // objectName.setText(element.getPropertyAsString(InfinispanSampler.OBJECT_NAME));
        // jsonContent.setText(element.getPropertyAsString(InfinispanSampler.JSON_CONTENT));

        String method = element.getPropertyAsString(InfinispanSampler.METHOD);
        if (method.equals("put")) {
            createBox.setSelected(true);
        } else if (method.equals("post")) {
            updateBox.setSelected(true);
        } else if (method.equals("delete")) {
            deleteBox.setSelected(true);
        } else {
            // We use "get" as the default one
            readBox.setSelected(true);
        }
    }

    @Override
    public TestElement createTestElement() {
        ConfigTestElement element = new ConfigTestElement();
        modifyTestElement(element);
        return element;
    }

    @Override
    public void modifyTestElement(TestElement element) {
        configureTestElement(element);

        element.setProperty(InfinispanSampler.SERVER, server.getText());
        element.setProperty(InfinispanSampler.PORT, port.getText());
        element.setProperty(InfinispanSampler.CACHE_NAME, cacheName.getText());
        element.setProperty(InfinispanSampler.MARSHALLER, marshallerLabeledChoice.getText());
        element.setProperty(InfinispanSampler.KEY, key.getText());
        // element.setProperty(InfinispanSampler.OBJECT_NAME, objectName.getText());
        // element.setProperty(InfinispanSampler.JSON_CONTENT, jsonContent.getText());

        String method = null;

        if (createBox.isSelected()) {
            method = "put";
        } else if (readBox.isSelected()) {
            method = "get";
        } else if (updateBox.isSelected()) {
            method = "post";
        } else if (deleteBox.isSelected()) {
            method = "delete";
        }

        element.setProperty(InfinispanSampler.METHOD, method);
    }

    // private JPanel createJsonContentPanel() {
    // JLabel label = new
    // JLabel(InfinispanPluginUtils.getResString("infinispan_json_content"));

    // jsonContent = new JTextArea();
    // label.setLabelFor(jsonContent);

    // JPanel contentsPanel = new JPanel(new BorderLayout(5, 0));
    // contentsPanel.add(label, BorderLayout.WEST);
    // contentsPanel.add(jsonContent, BorderLayout.CENTER);
    // return contentsPanel;
    // }

    private JPanel createMethodPanel() {
        ButtonGroup group = new ButtonGroup();

        readBox = new JRadioButton(InfinispanPluginUtils.getResString("infinispan_method_read"));
        group.add(readBox);
        readBox.setSelected(true);

        createBox = new JRadioButton(InfinispanPluginUtils.getResString("infinispan_method_create"));
        group.add(createBox);

        updateBox = new JRadioButton(InfinispanPluginUtils.getResString("infinispan_method_update"));
        group.add(updateBox);

        deleteBox = new JRadioButton(InfinispanPluginUtils.getResString("infinispan_method_delete"));
        group.add(deleteBox);

        JPanel optionsPanel = new HorizontalPanel();
        optionsPanel.add(createBox);
        optionsPanel.add(readBox);
        optionsPanel.add(updateBox);
        optionsPanel.add(deleteBox);
        return optionsPanel;
    }

    private JPanel createMarshallerPanel() {
        List<String> possibleClasses = new ArrayList<>();

        try {
            possibleClasses = ClassFinder.findClassesThatExtend(InfinispanPluginUtils.getPluginDependencyPaths(),
                    new Class[] { AbstractMarshaller.class });

        } catch (Exception e) {
            log.error("Exception getting interfaces.", e);
        }

        marshallerLabeledChoice = new JLabeledChoice(InfinispanPluginUtils.getResString("infinispan_marshaller_class"),
                possibleClasses.toArray(ArrayUtils.EMPTY_STRING_ARRAY), false, false);
        marshallerLabeledChoice.addChangeListener(this);

        warningLabel.setForeground(Color.RED);
        Font font = warningLabel.getFont();
        warningLabel.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
        if (possibleClasses.size() != 0) {
          warningLabel.setVisible(false);
        }

        // Bug in JLabeledChoice show no label. Fix in manually
        JLabel label = new JLabel(InfinispanPluginUtils.getResString("infinispan_marshaller_class"));
        label.setLabelFor(marshallerLabeledChoice);

        JPanel fieldPanel = new JPanel(new BorderLayout(5, 0));
        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(marshallerLabeledChoice, BorderLayout.CENTER);
        // End Workaround

        VerticalPanel panel = new VerticalPanel();
        // panel.add(marshallerLabeledChoice);
        panel.add(fieldPanel);
        panel.add(warningLabel);
        return panel;
    }

    private JPanel createJTextPanel(String labelResString, JTextField field) {
        JLabel label = new JLabel(InfinispanPluginUtils.getResString(labelResString));

        label.setLabelFor(field);

        JPanel fieldPanel = new JPanel(new BorderLayout(5, 0));
        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(field, BorderLayout.CENTER);
        return fieldPanel;
    }

    private void init() {
        setLayout(new BorderLayout(0, 5));

        if (displayName) {
            Container t = makeTitlePanel();
            setBorder(makeBorder());
            add(t, BorderLayout.NORTH);
        }

        // Main Panel
        VerticalPanel mainPanel = new VerticalPanel();
        JPanel serverPanel = new HorizontalPanel();
        serverPanel.add(createJTextPanel("infinispan_server", server = new JTextField(40)), BorderLayout.CENTER);
        serverPanel.add(createJTextPanel("infinispan_port", port = new JTextField(10)), BorderLayout.EAST);
        mainPanel.add(serverPanel);
        mainPanel.add(createJTextPanel("infinispan_cache_name", cacheName = new JTextField(10)));

        mainPanel.add(createMarshallerPanel());
        mainPanel.add(createJTextPanel("infinispan_key", key = new JTextField(10)));

        // mainPanel.add(createJTextPanel("infinispan_object_name", objectName = new JTextField(40)));
        // mainPanel.add(createJsonContentPanel());

        mainPanel.add(createMethodPanel());

        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void stateChanged(ChangeEvent evt) {
        if (evt.getSource() == marshallerLabeledChoice) {
            // configureMarshaller();
        }
    }

    // private void configureMarshaller() {
    //     String className = marshallerLabeledChoice.getText().trim();
    //     try {
    //         JavaSamplerClient client = (JavaSamplerClient) Class.forName(className, true,
    //                 Thread.currentThread().getContextClassLoader()).getDeclaredConstructor().newInstance();

    //         Arguments currArgs = new Arguments();
    //         argsPanel.modifyTestElement(currArgs);
    //         Map<String, String> currArgsMap = currArgs.getArgumentsAsMap();

    //         Arguments newArgs = new Arguments();
    //         Arguments testParams = null;
    //         try {
    //             testParams = client.getDefaultParameters();
    //         } catch (AbstractMethodError e) {
    //             log.warn("JavaSamplerClient doesn't implement "
    //                     + "getDefaultParameters.  Default parameters won't "
    //                     + "be shown.  Please update your client class: " + className);
    //         }

    //         if (testParams != null) {
    //             for (JMeterProperty jMeterProperty : testParams.getArguments()) {
    //                 Argument arg = (Argument) jMeterProperty.getObjectValue();
    //                 String name = arg.getName();
    //                 String value = arg.getValue();

    //                 // If a user has set parameters in one test, and then
    //                 // selects a different test which supports the same
    //                 // parameters, those parameters should have the same
    //                 // values that they did in the original test.
    //                 if (currArgsMap.containsKey(name)) {
    //                     String newVal = currArgsMap.get(name);
    //                     if (newVal != null && newVal.length() > 0) {
    //                         value = newVal;
    //                     }
    //                 }
    //                 newArgs.addArgument(name, value);
    //             }
    //         }

    //         argsPanel.configure(newArgs);
    //         warningLabel.setVisible(false);
    //     } catch (Exception e) {
    //         log.error("Error getting argument list for " + className, e);
    //         warningLabel.setVisible(true);
    //     }
    // }

    private static boolean checkContainsClassName(JLabeledChoice classnameChoice, String className) {
        Set<String> set = new HashSet<>(Arrays.asList(classnameChoice.getItems()));
        return set.contains(className);
    }

    private boolean classOk(String className) {
        try {
            AbstractMarshaller client = (AbstractMarshaller) Class.forName(className, true,
                    Thread.currentThread().getContextClassLoader()).getConstructor().newInstance();
            // Just to use client
            return client != null;
        } catch (Exception ex) {
            log.error("Error creating class:'"+className+"' in InfinispanSampler '"+getName()
                +"', check for a missing jar in your jmeter 'search_paths' and 'plugin_dependency_paths' properties",
                ex);
            return false;
        }
    }

}
