package org.apache.jmeter.protocol.infinispan.config.gui;

import java.awt.BorderLayout;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.config.gui.AbstractConfigGui;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.protocol.infinispan.sampler.InfinispanSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanConfigGui extends AbstractConfigGui {

    /**
     *
     */
    private static final long serialVersionUID = -157541532129212938L;

    private static final Logger log = LoggerFactory.getLogger(InfinispanConfigGui.class);

    private JTextField serverList;

    // private JTextField protocolVersion;

    private JTextField key;

    private JTextField objectName;

    private JTextArea jsonContent;

    // private JCheckBox useSsl;

    private JRadioButton createBox;
    private JRadioButton readBox;
    private JRadioButton updateBox;
    private JRadioButton deleteBox;

    private boolean displayName = true;

    public InfinispanConfigGui(boolean displayName) {
        this.displayName = displayName;
        init();
    }

    /**
     * Implements JMeterGUIComponent.clearGui
     */
    @Override
    public void clearGui() {
        super.clearGui();

        serverList.setText("");
        key.setText("");
        objectName.setText("");
        jsonContent.setText("");
        readBox.setSelected(true);
        createBox.setSelected(false);
    }

    @Override
    public String getLabelResource() {
        return "infinispan_sample_title";
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);

        serverList.setText(element.getPropertyAsString(InfinispanSampler.SERVER_LIST));
        key.setText(element.getPropertyAsString(InfinispanSampler.KEY));
        objectName.setText(element.getPropertyAsString(InfinispanSampler.OBJECT_NAME));
        jsonContent.setText(element.getPropertyAsString(InfinispanSampler.JSON_CONTENT));

        String method = element.getPropertyAsString(InfinispanSampler.METHOD);
        if (method.equals("put")) {
            createBox.setSelected(true);
        } else {
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
        // Note: the element is a ConfigTestElement, so cannot use FTPSampler access
        // methods
        element.setProperty(InfinispanSampler.SERVER_LIST, serverList.getText());
        element.setProperty(InfinispanSampler.KEY, key.getText());
        element.setProperty(InfinispanSampler.OBJECT_NAME, objectName.getText());
        element.setProperty(InfinispanSampler.JSON_CONTENT, jsonContent.getText());

        // TODO: update this for multiple methods
        String method = null;

        if (createBox.isSelected()) {
            method = "put";
        } else if (readBox.isSelected()) {
            method = "get";
        }

        element.setProperty(InfinispanSampler.METHOD, method);
    }

    private JPanel createJsonContentPanel() {
        JLabel label = new JLabel(JMeterUtils.getResString("infininspan_json_content"));

        jsonContent = new JTextArea();
        label.setLabelFor(jsonContent);

        JPanel contentsPanel = new JPanel(new BorderLayout(5, 0));
        contentsPanel.add(label, BorderLayout.WEST);
        contentsPanel.add(jsonContent, BorderLayout.CENTER);
        return contentsPanel;
    }

    private JPanel createOptionsPanel() {

        ButtonGroup group = new ButtonGroup();

        readBox = new JRadioButton(JMeterUtils.getResString("infinispan_read"));
        group.add(readBox);
        readBox.setSelected(true);

        createBox = new JRadioButton(JMeterUtils.getResString("infinispan_create"));
        group.add(createBox);

        JPanel optionsPanel = new HorizontalPanel();
        optionsPanel.add(readBox);
        optionsPanel.add(createBox);
        return optionsPanel;
    }

    private void init() { // WARNING: called from ctor so must not be overridden (i.e. must be private or
                          // final)
        setLayout(new BorderLayout(0, 5));

        if (displayName) {
            setBorder(makeBorder());
            add(makeTitlePanel(), BorderLayout.NORTH);
        }

        // MAIN PANEL
        VerticalPanel mainPanel = new VerticalPanel();
        mainPanel.add(createJTextPanel("infinispan_server_list", serverList = new JTextField(40)));
        mainPanel.add(createJTextPanel("infinispan_key", key = new JTextField(10)));
        mainPanel.add(createJTextPanel("infinispan_object_name", objectName = new JTextField(40)));
        
        mainPanel.add(createJsonContentPanel());

        mainPanel.add(createOptionsPanel());

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createJTextPanel(String labelResString, JTextField field) {
        JLabel label = new JLabel(JMeterUtils.getResString(labelResString));
//        JLabel label = new JLabel(labelResString);

        label.setLabelFor(field);

        JPanel fieldPanel = new JPanel(new BorderLayout(5, 0));
        fieldPanel.add(label, BorderLayout.WEST);
        fieldPanel.add(field, BorderLayout.CENTER);
        return fieldPanel;
    }

}
