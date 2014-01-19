package UserInterface;

import UserInterface.abstractInterface.ActionFactory;
import UserInterface.abstractInterface.OptionDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class OptionDialogSwing extends JFrame implements OptionDialog {

    private ActionFactory<ActionListener> factory;
    private JRadioButton personalizeButton;
    private JRadioButton[] defaultOptionButtons;
    private JTextField[] optionTextFields;

    public OptionDialogSwing(ActionFactory factory) throws HeadlessException {
        super("Options");
        this.factory = factory;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.createComponents();
        this.pack();
        this.setScreenLocation();
    }

    private void setScreenLocation() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        this.setLocation(
                (int) ((tk.getScreenSize().getWidth() - this.getWidth()) / 2),
                (int) ((tk.getScreenSize().getHeight() - this.getHeight()) / 2));
    }

    private void createComponents() {
        this.add(new Label("Select dificulty"), BorderLayout.NORTH);
        this.add(createToolbar(), BorderLayout.SOUTH);
        this.add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createToolbar() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.add(createToolBarButton("Start Game", "StartGame"));
        panel.add(createToolBarButton("Cancel", "Exit"));
        return panel;
    }

    private JButton createToolBarButton(String label, String action) {
        JButton button = new JButton(label);
        button.addActionListener(this.factory.createAction(action));
        return button;
    }

    private JPanel createMainPanel() {
        JPanel masterPanel = new JPanel();
        masterPanel.setLayout(new GridLayout(1, 2));
        masterPanel.add(createDefaultOptionsPanel(), 0);
        masterPanel.add(createPersonalizePanel(), 1);
        return masterPanel;
    }

    private JPanel createDefaultOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        this.defaultOptionButtons = createDefaultOptionButtons();
        for (JRadioButton jRadioButton : defaultOptionButtons)
            panel.add(jRadioButton);
        return panel;
    }

    private JRadioButton[] createDefaultOptionButtons() {
        return new JRadioButton[]{
            createOptionButton("Easy level (16 mines, 9x9 squares)", 0, 9, 9, 16),
            createOptionButton("Intermediate level (40 mines, 16x16 squares)", 1, 16, 16, 40),
            createOptionButton("Advanced level (99 mines, 16x30 squares)", 2, 16, 30, 99)
        };
    }

    private JRadioButton createOptionButton(String label, final int index, final int rows, final int columns, final int mines) {
        JRadioButton button = new JRadioButton(label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                personalizeButton.setSelected(false);
                setParameters(index, rows, columns, mines, false);
            }
        });
        return button;
    }

    private JPanel createPersonalizePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        this.optionTextFields = createTextFields();
        createPersonalizeButton();
        panel.add(personalizeButton);
        panel.add(createTextFieldPanel(this.optionTextFields[0], "High (9-24): "));
        panel.add(createTextFieldPanel(this.optionTextFields[1], "Width (9-30): "));
        panel.add(createTextFieldPanel(this.optionTextFields[2], "Mines (10-668): "));
        return panel;
    }

    private JTextField[] createTextFields() {
        return new JTextField[]{createTextField(10), createTextField(10), createTextField(10)};
    }

    private JTextField createTextField(int length) {
        final JTextField field = new JTextField(length);
        field.setEnabled(false);
        return field;
    }

    private void createPersonalizeButton() {
        personalizeButton = new JRadioButton("Personalize");
        personalizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setParameters(-1, 9, 9, 10, true);
            }
        });
    }

    private JPanel createTextFieldPanel(JTextField textField, String label) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.add(new Label(label));
        panel.add(textField);
        return panel;
    }

    private void setParameters(final int index, final int rows, final int columns, final int mines, boolean optionFieldsMode) {
        enableOptionFields(optionFieldsMode);
        for (int i = 0; i < this.defaultOptionButtons.length; i++)
            if (i != index) this.defaultOptionButtons[i].setSelected(false);
        this.optionTextFields[0].setText(Integer.toString(rows));
        this.optionTextFields[1].setText(Integer.toString(columns));
        this.optionTextFields[2].setText(Integer.toString(mines));
    }

    private void enableOptionFields(boolean mode) {
        for (JTextField jTextField : this.optionTextFields)
            jTextField.setEnabled(mode);
    }

    @Override
    public void showDialog() {
        this.defaultOptionButtons[0].doClick();
        this.setVisible(true);
    }

    @Override
    public void hidDialog() {
        this.setVisible(false);
    }

    @Override
    public int getRowsAmount() {
        if(this.optionTextFields[0].getText().equals("")) return 0;
        return Integer.parseInt(this.optionTextFields[0].getText());
    }

    @Override
    public int getColumnAmount() {
        if(this.optionTextFields[1].getText().equals("")) return 0;
        return Integer.parseInt(this.optionTextFields[1].getText());
    }

    @Override
    public int getMinesAmount() {
        if(this.optionTextFields[2].getText().equals("")) return 0;
        return Integer.parseInt(this.optionTextFields[2].getText());
    }

}
