package log.charter.gui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ParamsPane extends JDialog {

	protected static interface ValueSetter {
		void setValue(String val);
	}

	protected static interface ValueValidator {
		String validateValue(String val);
	}

	protected static final ValueValidator dirValidator = val -> {
		final File f = new File(val);
		if (!f.exists()) {
			return "directory doesn't exist";
		}
		if (!f.isDirectory()) {
			return "given path is not folder";
		}
		return null;
	};

	private static final long serialVersionUID = -3193534671039163160L;

	private static final int OPTIONS_LSPACE = 10;
	private static final int OPTIONS_USPACE = 10;
	private static final int OPTIONS_LABEL_WIDTH = 200;
	private static final int OPTIONS_HEIGHT = 25;
	private static final int OPTIONS_MAX_INPUT_WIDTH = 500;

	protected static ValueValidator createIntValidator(final int minVal, final int maxVal, final boolean acceptEmpty) {
		return val -> {
			if (((val == null) || val.isEmpty()) && acceptEmpty) {
				return null;
			}
			try {
				final int i = Integer.parseInt(val);
				if (i < minVal) {
					return "value must be greater than or equal " + minVal;
				}
				if (i > maxVal) {
					return "value must be less than or equal " + maxVal;
				}
				return null;
			} catch (final Exception e) {
				return "number expected";
			}
		};
	}

	public ParamsPane(final CharterFrame frame, final String title, final int rows) {
		super(frame, title, true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(true);
		setLocation(Config.windowPosX + 100, Config.windowPosY + 100);
		pack();
		final Insets insets = getInsets();
		setSize(700 + insets.left + insets.right, insets.top + insets.bottom + (OPTIONS_USPACE * 2) + (rows
				* OPTIONS_HEIGHT));
		setLayout(null);
	}

	protected void add(final JComponent component, final int x, final int y, final int w,
			final int h) {
		component.setBounds(x, y, w, h);
		final Dimension size = new Dimension(w, h);
		component.setMinimumSize(size);
		component.setPreferredSize(size);
		component.setMaximumSize(size);

		add(component);
	}

	protected void addButtons(final int row, final ActionListener onSave) {
		final JButton saveButton = new JButton("Save");
		saveButton.addActionListener(onSave);
		add(saveButton, 200, OPTIONS_USPACE + (row * OPTIONS_HEIGHT), 100, 25);
		final JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(e -> {
			dispose();
		});
		add(cancelButton, 325, OPTIONS_USPACE + (row * OPTIONS_HEIGHT), 100, 25);
	}

	protected void addConfigValue(final int id, final String name, final Object val,
			final int inputLength, final ValueValidator validator, final ValueSetter setter) {
		final int y = OPTIONS_USPACE + (id * OPTIONS_HEIGHT);
		final JLabel label = new JLabel(name, SwingConstants.LEFT);
		add(label, OPTIONS_LSPACE, y, OPTIONS_LABEL_WIDTH, OPTIONS_HEIGHT);

		final int fieldX = OPTIONS_LSPACE + OPTIONS_LABEL_WIDTH + 3;
		final JTextField field = new JTextField(val == null ? "" : val.toString(), inputLength);
		field.getDocument().addDocumentListener(new DocumentListener() {
			JLabel error = null;

			@Override
			public void changedUpdate(final DocumentEvent e) {
				if (validator == null) {
					setter.setValue(field.getText());
				} else {
					if (error != null) {
						remove(error);
					}

					error = null;
					final String val = field.getText();
					final String validation = validator.validateValue(val);
					if (validation == null) {
						setter.setValue(val);
					} else {
						error = new JLabel(validation);
						add(error, fieldX + field.getWidth(), y, OPTIONS_LABEL_WIDTH, OPTIONS_HEIGHT);
					}
					repaint();
				}
			}

			@Override
			public void insertUpdate(final DocumentEvent e) {
				changedUpdate(e);
			}

			@Override
			public void removeUpdate(final DocumentEvent e) {
				changedUpdate(e);
			}

		});
		add(field, fieldX, y, inputLength > OPTIONS_MAX_INPUT_WIDTH ? OPTIONS_MAX_INPUT_WIDTH : inputLength,
				OPTIONS_HEIGHT);
	}
}