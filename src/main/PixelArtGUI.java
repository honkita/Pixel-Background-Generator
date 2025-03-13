/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class PixelArtGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private final JButton generateButton = new JButton("Generate Image");
	private final JButton openFolderButton = new JButton("Open Folder");
	private final JButton exitButton = new JButton("Exit");
	private final JButton selectPresetButton = new JButton("Select Preset");
	private final JButton selectNewColourButton = new JButton("Select New Colour");
	private final JButton addlayerButton = new JButton("+");
	private final JButton removeLayerButton = new JButton("-");
	private final JButton addDitherLength = new JButton("+");
	private final JButton removeDitherLength = new JButton("-");
	private final JButton addRatioLength = new JButton("+");
	private final JButton removeRatioLength = new JButton("-");
	private final JButton changeNameButton = new JButton("Confirm");

	private final JComboBox<Integer> layerColour = new JComboBox<Integer>(new Integer[] { 1 });
	private final JComboBox<String> presets = new JComboBox<String>();
	private final JComboBox<String> ditherDropdown = new JComboBox<String>(
			new String[] { "Primary", "Secondary", "Tertiary" });

	private final JFrame colourPicker = new JFrame();

	private final JLabel presetsTitle = new JLabel("Presets");
	private final JLabel layerColourTitle = new JLabel("Layer Colour");
	private final JLabel lengthTitle = new JLabel("Number of Pixels for Length");
	private final JLabel heightTitle = new JLabel("Number of Pixels for Height");
	private final JLabel ditherTitle = new JLabel("Dither Length");
	private final JLabel ratioTitle = new JLabel("Ratio Length");
	private final JLabel ratioValue = new JLabel("1");
	private final JLabel nameTitle = new JLabel("Name of Image");
	private final JLabel namePNG = new JLabel(".png");

	private final int minVal = 100;
	private final int maxVal = 3000;
	private final JSlider lengthSlider = new JSlider(minVal, maxVal);
	private final JSlider heightSlider = new JSlider(minVal, maxVal);

	private final JTextField lengthField = new JTextField();
	private final JTextField heightField = new JTextField();
	private final JTextField ditherLength = new JTextField();
	private final JTextField newName = new JTextField();

	private final JPanel buttonPanel = new JPanel();
	private final JPanel presetPanel = new JPanel();
	private final JPanel colourPanel = new JPanel();
	private final JPanel pickedColour = new JPanel();
	private final JPanel currentPreset = new JPanel();
	private final JPanel currentGradient = new JPanel();
	private final JPanel lengthPanel = new JPanel();
	private final JPanel heightPanel = new JPanel();
	private final JPanel ditherPanel = new JPanel();
	private final JPanel ratioPanel = new JPanel();
	private final JPanel namePanel = new JPanel();

	private final ArrayList<JPanel> presetColours = new ArrayList<JPanel>();
	private final ArrayList<JPanel> gradiantColours = new ArrayList<JPanel>();

	private final jsonReader s;
	// private final painter p;

	Dimension size;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Rectangle r;
	int height;
	int width;
	PixelArtGenerator pixelArtGenerator;
	// private final double ratio = (double) screenSize.width / screenSize.height;
	private HashMap<String, Color[]> colors;

	PixelArtGUI() {
		setTitle("Elite's Pixel Gradient Maker");
		for (int i = 0; i < 5; i++) {
			presetColours.add(new JPanel());

		}
		setSize(screenSize.width / 2, screenSize.width / 8 * 3);
		size = getBounds().getSize();
		setLayout(null);
		setLocationRelativeTo(null);
		pixelArtGenerator = PixelArtGenerator.getInstance();
		s = new jsonReader();
		// p = new painter();
		colors = s.returnCombos();
		ditherLength.setText(Integer
				.toString(pixelArtGenerator.returnDitherLength(String.valueOf(ditherDropdown.getSelectedItem()))));

		ArrayList<String> presetNames = new ArrayList<String>(colors.keySet());
		for (String presetName : presetNames) {
			presets.addItem(presetName);
		}
		resizeGradientColours();
		buttonActions();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		// addComponentListener(new ComponentAdapter() {
		// public void componentResized(ComponentEvent e) {
		// // Ensures that the minimum size is possible
		// if (getBounds().getSize().height <= screenSize.height / 2) {
		// setSize(getWidth(), screenSize.height / 2);
		// }
		// if (getBounds().getSize().width <= screenSize.width / 2) {
		// setSize(screenSize.width / 2, getHeight());
		// }
		//
		// // Uniform scaling!!!!!!
		// if (getBounds().getSize().width >= screenSize.width / 2) {
		// setSize(getWidth(), (int) (getBounds().getSize().width / ratio));
		// } else if (getBounds().getSize().height >= screenSize.height / 2) {
		// setSize((int) (ratio * getBounds().getSize().height), getHeight());
		// }
		// size = getBounds().getSize();
		//
		// repaint();
		// }
		// });
		lengthField.setText(String.valueOf((lengthSlider.getMaximum() + lengthSlider.getMinimum()) / 2));
		heightField.setText(String.valueOf((heightSlider.getMaximum() + heightSlider.getMinimum()) / 2));
		repaint();
	}

	private Integer[] generator(int max) {
		Integer[] result = new Integer[max];

		for (int i = 0; i < max; i++) {
			result[i] = (int) (i + 1);

		}
		return result;
	}

	/*
	 * Function to change the number of colors that are present in the gradient
	 */
	private void resizeGradientColours() {
		gradiantColours.clear();
		layerColour.removeAllItems();
		Integer[] genned = generator(pixelArtGenerator.numberOfColors());

		for (int i = 0; i < pixelArtGenerator.numberOfColors(); i++) {
			layerColour.addItem(genned[i]);
			gradiantColours.add(new JPanel());

		}
	}

	/**
	 * Code for all the buttons
	 */
	private void buttonActions() {
		generateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// check height and width for min value
				if (pixelArtGenerator.getHeight() < minVal)
					pixelArtGenerator.setHeight(minVal);
				if (pixelArtGenerator.getLength() < minVal)
					pixelArtGenerator.setLength(minVal);
				pixelArtGenerator.generate();
			}
		});

		openFolderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Desktop desktop = Desktop.getDesktop();
				File dirToOpen = null;
				try {
					dirToOpen = new File("./Photos");
					desktop.open(dirToOpen);
				} catch (IllegalArgumentException iae) {
					System.out.println("File Not Found");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		selectPresetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color[] x = colors.get(presets.getSelectedItem());
				pixelArtGenerator.resizeGradient(x.length);
				for (int i = 0; i < x.length; i++) {
					// System.out.println(x[i].toString());
					if (x[i] != null)
						pixelArtGenerator.changeColour(x[i], i);
				}
				currentPreset.removeAll();
				resizeGradientColours();
				repaint();

			}
		});

		selectNewColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(colourPicker, getTitle(),
						pixelArtGenerator.returnColour((int) (layerColour.getSelectedItem()) - 1));
				if (newColor != null)
					pixelArtGenerator.changeColour(newColor, (int) (layerColour.getSelectedItem()) - 1);
				repaint();
			}
		});

		layerColour.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				repaint();
			}
		});

		addlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pixelArtGenerator.resizeGradient(pixelArtGenerator.numberOfColors() + 1);
				resizeGradientColours();
				repaint();
			}
		});
		removeLayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pixelArtGenerator.resizeGradient(pixelArtGenerator.numberOfColors() - 1);
				resizeGradientColours();
				repaint();
			}
		});

		addRatioLength.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pixelArtGenerator.changeRatio(pixelArtGenerator.returnRatio() + 1);
				ratioValue.setText(Integer.toString(pixelArtGenerator.returnRatio()));
				repaint();
			}
		});

		removeRatioLength.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pixelArtGenerator.changeRatio(pixelArtGenerator.returnRatio() - 1);
				ratioValue.setText(Integer.toString(pixelArtGenerator.returnRatio()));
				repaint();
			}
		});

		/**
		 * 
		 */
		addDitherLength.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pixelArtGenerator.resizeDither(String.valueOf(ditherDropdown.getSelectedItem()),
						pixelArtGenerator.returnDitherLength(String.valueOf(ditherDropdown.getSelectedItem())) + 1);
				ditherLength.setText(Integer.toString(
						pixelArtGenerator.returnDitherLength(String.valueOf(ditherDropdown.getSelectedItem()))));
				repaint();
			}
		});
		removeDitherLength.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pixelArtGenerator.resizeDither(String.valueOf(ditherDropdown.getSelectedItem()),
						pixelArtGenerator.returnDitherLength(String.valueOf(ditherDropdown.getSelectedItem())) - 1);
				ditherLength.setText(Integer.toString(
						pixelArtGenerator.returnDitherLength(String.valueOf(ditherDropdown.getSelectedItem()))));
				repaint();
			}
		});

		ditherDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ditherLength.setText(Integer.toString(
						pixelArtGenerator.returnDitherLength(String.valueOf(ditherDropdown.getSelectedItem()))));
				repaint();
			}
		});
		/**
		 * Modifies the length of the image
		 */
		lengthSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				int i = (int) (((JSlider) ce.getSource()).getValue());
				lengthField.setText(String.valueOf(i));
				pixelArtGenerator.setLength(i);
			}
		});
		/**
		 * Modifies the height of the image
		 */
		heightSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				int i = (int) (((JSlider) ce.getSource()).getValue());
				heightField.setText(String.valueOf(i));
				pixelArtGenerator.setHeight(i);
			}
		});

		changeNameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pixelArtGenerator.changeName(newName.getText());
				repaint();
			}
		});
	}

	/**
	 * Painting function Graphics g
	 */
	public void paint(Graphics g) {
		super.paint(g);
		final int border = (int) (size.width / 32.0);
		// final int quarter = size.width / 4;
		final int quarterBoxTop = (int) (size.width / 24.0 * 5);
		final int quarterBoxTopSmall = (int) (size.width / 64.0 * 5);

		final int topHeight = size.height / 8 * 3;
		final int mainTextHeight = screenSize.width / 256 * 3;
		final int subTextHeight = (int) Math.round(screenSize.width / 256 * 2);
		final int sliderFontHeight = screenSize.width / 256;
		final Font calibriTitle = new Font("Calibri", Font.PLAIN, mainTextHeight);
		final Font calibriSubTitle = new Font("Calibri", Font.PLAIN, subTextHeight);
		final Font sliderFont = new Font("Calibri", Font.PLAIN, sliderFontHeight);

		buttonPanel.setBounds(border, border, quarterBoxTop, topHeight);
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		buttonPanel.setLayout(null);

		generateButton.setBounds(0, 0, buttonPanel.getWidth(), buttonPanel.getHeight() / 8);
		openFolderButton.setBounds(0, buttonPanel.getHeight() / 8, buttonPanel.getWidth(), buttonPanel.getHeight() / 8);
		exitButton.setBounds(0, buttonPanel.getHeight() / 4, buttonPanel.getWidth(), buttonPanel.getHeight() / 8);

		presetPanel.setBounds(quarterBoxTop + border / 2 * 3, border, quarterBoxTop, topHeight);
		presetPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		presetPanel.setLayout(null);

		presetsTitle.setBounds(0, 0, presetPanel.getWidth(), presetPanel.getHeight() / 8);
		presetsTitle.setFont(calibriTitle);
		presetsTitle.setHorizontalAlignment(SwingConstants.CENTER);
		presetsTitle.setVerticalAlignment(SwingConstants.CENTER);

		presets.setBounds(0, presetPanel.getHeight() / 4, presetPanel.getWidth(), presetPanel.getHeight() / 8);
		presets.doLayout();
		presets.setVisible(true);

		selectPresetButton.setBounds(0, presetPanel.getHeight() / 8 * 7, presetPanel.getWidth(),
				colourPanel.getHeight() / 8);

		presets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				repaint();
			}
		});

		currentPreset.setBounds(quarterBoxTop * 2 + border * 2, border, quarterBoxTopSmall, topHeight);
		currentPreset.setBorder(BorderFactory.createLineBorder(Color.black));

		Color[] q = colors.get(presets.getSelectedItem());
		currentPreset.removeAll();
		currentPreset.setLayout(new GridLayout(q.length, 0));

		for (int i = 0; i < q.length; i++) {
			presetColours.get(i).setBackground(q[i]);
			currentPreset.add(presetColours.get(i));
		}

		colourPanel.setBounds((int) (quarterBoxTop * 2 + quarterBoxTopSmall + border * 5 / 2.0), border, quarterBoxTop,
				topHeight);
		colourPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		colourPanel.setLayout(null);

		layerColourTitle.setBounds(0, 0, colourPanel.getWidth(), colourPanel.getHeight() / 8);
		layerColourTitle.setFont(calibriTitle);
		layerColourTitle.setHorizontalAlignment(SwingConstants.CENTER);
		layerColourTitle.setVerticalAlignment(SwingConstants.CENTER);

		layerColour.setBounds(0, colourPanel.getHeight() / 4, colourPanel.getWidth(), colourPanel.getHeight() / 8);
		layerColour.doLayout();
		layerColour.setVisible(true);
		selectNewColourButton.setBounds(0, colourPanel.getHeight() / 8 * 7, colourPanel.getWidth(),
				colourPanel.getHeight() / 8);

		pickedColour.setBounds(colourPanel.getWidth() / 4, colourPanel.getHeight() / 16 * 7, colourPanel.getWidth() / 2,
				colourPanel.getWidth() / 2);
		pickedColour.setBackground(pixelArtGenerator.returnColour((int) (layerColour.getSelectedItem()) - 1));

		addlayerButton.setBounds(quarterBoxTop * 3 + quarterBoxTopSmall + border * 3, border, quarterBoxTopSmall,
				topHeight / 2);
		removeLayerButton.setBounds(quarterBoxTop * 3 + quarterBoxTopSmall + border * 3,
				(int) (topHeight / 2.0 + border), quarterBoxTopSmall, topHeight / 2);
		removeLayerButton.setEnabled(pixelArtGenerator.numberOfColors() > 1);

		currentGradient.setBounds((int) (size.width - border - quarterBoxTopSmall), border, quarterBoxTopSmall,
				topHeight);
		currentGradient.setBorder(BorderFactory.createLineBorder(Color.black));
		currentGradient.removeAll();
		currentGradient.setLayout(new GridLayout(pixelArtGenerator.numberOfColors(), 0));

		for (int i = 0; i < pixelArtGenerator.numberOfColors(); i++) {

			gradiantColours.get(i).setBackground(pixelArtGenerator.returnColour(i));
			currentGradient.add(gradiantColours.get(i));
		}

		presetPanel.add(presetsTitle);
		presetPanel.add(presets);
		presetPanel.add(selectPresetButton);
		add(presetPanel);

		colourPanel.add(layerColourTitle);
		colourPanel.add(selectNewColourButton);
		colourPanel.add(layerColour);
		colourPanel.add(pickedColour);
		add(colourPanel);

		buttonPanel.add(openFolderButton);
		buttonPanel.add(generateButton);
		buttonPanel.add(exitButton);
		add(buttonPanel);

		add(currentPreset);
		add(currentGradient);

		add(addlayerButton);
		add(removeLayerButton);

		int bottomSliderWidth = size.width - 2 * border;

		lengthPanel.setBounds(border, (int) (topHeight + 3.0 / 2 * border), bottomSliderWidth, size.height / 8);
		lengthPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		lengthPanel.setLayout(null);

		lengthTitle.setBounds(border / 2, border / 2, lengthPanel.getWidth(), subTextHeight * 2);
		lengthTitle.setFont(calibriSubTitle);
		lengthTitle.setVerticalAlignment(SwingConstants.CENTER);

		lengthSlider.setBounds(0, lengthPanel.getHeight() / 8 * 3, lengthPanel.getWidth() - 4 * border,
				lengthPanel.getHeight() / 8 * 5);
		lengthSlider.setMajorTickSpacing(100);
		lengthSlider.setMinorTickSpacing(50);
		lengthSlider.setPaintTicks(true);
		lengthSlider.setPaintLabels(true);
		lengthSlider.setFont(sliderFont);

		lengthField.setBounds(lengthPanel.getWidth() - 4 * border, lengthPanel.getHeight() / 8 * 3, 3 * border,
				lengthPanel.getHeight() / 8 * 5);

		dimensionSliders(lengthField, lengthSlider);

		lengthPanel.add(lengthTitle);
		lengthPanel.add(lengthSlider);
		lengthPanel.add(lengthField);
		add(lengthPanel);

		heightTitle.setBounds(border / 2, border / 2, lengthPanel.getWidth(), subTextHeight * 2);
		heightTitle.setFont(calibriSubTitle);
		heightTitle.setVerticalAlignment(SwingConstants.CENTER);
		heightPanel.setBounds(border, (int) (topHeight + 2 * border + size.height / 8), bottomSliderWidth,
				size.height / 8);
		heightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		heightPanel.setLayout(null);

		heightSlider.setBounds(0, lengthPanel.getHeight() / 8 * 3, heightPanel.getWidth() - 4 * border,
				heightPanel.getHeight() / 8 * 5);
		heightSlider.setMajorTickSpacing(100);
		heightSlider.setMinorTickSpacing(50);
		heightSlider.setPaintTicks(true);
		heightSlider.setPaintLabels(true);
		heightSlider.setFont(sliderFont);

		heightField.setBounds(heightPanel.getWidth() - 4 * border, heightPanel.getHeight() / 8 * 3, 3 * border,
				heightPanel.getHeight() / 8 * 5);

		dimensionSliders(heightField, heightSlider);

		heightPanel.add(heightTitle);
		heightPanel.add(heightSlider);
		heightPanel.add(heightField);
		add(heightPanel);

		ditherTitle.setBounds(0, 0, ditherPanel.getWidth(), ditherPanel.getHeight() / 4);
		ditherTitle.setFont(calibriSubTitle);
		ditherTitle.setHorizontalAlignment(SwingConstants.CENTER);
		ditherTitle.setVerticalAlignment(SwingConstants.CENTER);

		ditherPanel.setBounds(border, (int) (topHeight + 5.0 / 2 * border + size.height / 4),
				(int) Math.round(size.getWidth() / 4 - border / 4.0 * 3),
				size.height - border - (int) (topHeight + 7.0 / 2 * border + size.height / 4));
		ditherPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		ditherPanel.setLayout(null);

		ditherPanel.add(ditherTitle);
		ditherPanel.add(ditherDropdown);

		ditherDropdown.setBounds(0, ditherPanel.getHeight() / 4, ditherPanel.getWidth(), ditherPanel.getHeight() / 4);
		ditherDropdown.doLayout();
		ditherDropdown.setVisible(true);

		removeDitherLength.setBounds(0, ditherPanel.getHeight() / 2, ditherPanel.getWidth() / 3,
				ditherPanel.getHeight() / 4);

		removeDitherLength
				.setEnabled(pixelArtGenerator.returnDitherLength(String.valueOf(ditherDropdown.getSelectedItem())) > 0);

		addDitherLength.setBounds(ditherPanel.getWidth() / 3 * 2, ditherPanel.getHeight() / 2,
				ditherPanel.getWidth() / 3, ditherPanel.getHeight() / 4);

		ditherLength.setBounds(ditherPanel.getWidth() / 3, ditherPanel.getHeight() / 2, ditherPanel.getWidth() / 3,
				ditherPanel.getHeight() / 4);

		ditherLength.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				String value = ditherLength.getText();
				int l = value.length();
				if (l == 0) {
					if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
						int i = ke.getKeyChar() - '0';
						int q = pixelArtGenerator.returnTotalDitherLength() + i;

						if (q <= pixelArtGenerator.getHeight()) {
							ditherLength.setEditable(true);
						} else {
							ditherLength.setEditable(false);
						}
					} else {
						ditherLength.setEditable(false);
					}
				} else {
					char pp = value.toCharArray()[0];
					if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9' && pp != '0') {

						int i = Integer.parseInt(ditherLength.getText() + ke.getKeyChar());
						int q = pixelArtGenerator.returnTotalDitherLength() - Integer.parseInt(ditherLength.getText())
								+ i;

						if (q <= pixelArtGenerator.getHeight()) {
							ditherLength.setEditable(true);
						} else {
							ditherLength.setEditable(false);
						}
					} else if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
						ditherLength.setEditable(true);
					} else {
						ditherLength.setEditable(false);
					}
				}

			}

			public void keyReleased(KeyEvent ke) {
				try {
					int convert = Integer.parseInt(ditherLength.getText());
					pixelArtGenerator.resizeDither(String.valueOf(ditherDropdown.getSelectedItem()), convert);
				} catch (NumberFormatException nfe) {

				}
			}
		});

		ditherDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});

		ditherPanel.add(removeDitherLength);
		ditherPanel.add(addDitherLength);
		ditherPanel.add(ditherLength);
		add(ditherPanel);

		ratioPanel.setBounds(border / 2 * 3 + (int) Math.round(size.getWidth() / 4 - border / 4.0 * 3),
				(int) (topHeight + 5.0 / 2 * border + size.height / 4),
				(int) Math.round(size.getWidth() / 4 - border / 4.0 * 3),
				size.height - border - (int) (topHeight + 7.0 / 2 * border + size.height / 4));

		ratioPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		ratioPanel.setLayout(null);

		ratioTitle.setBounds(0, 0, ratioPanel.getWidth(), ratioPanel.getHeight() / 4);
		ratioTitle.setFont(calibriSubTitle);
		ratioTitle.setHorizontalAlignment(SwingConstants.CENTER);
		ratioTitle.setVerticalAlignment(SwingConstants.CENTER);

		ratioValue.setBounds(0, ratioPanel.getHeight() / 4, ratioPanel.getWidth(), ratioPanel.getHeight() / 4);
		ratioValue.setFont(calibriSubTitle);
		ratioValue.setHorizontalAlignment(SwingConstants.CENTER);
		ratioValue.setVerticalAlignment(SwingConstants.CENTER);

		removeRatioLength.setBounds(0, ratioPanel.getHeight() / 2, ratioPanel.getWidth() / 2,
				ratioPanel.getHeight() / 3);

		removeRatioLength
				.setEnabled(pixelArtGenerator.returnRatio() > 0);

		addRatioLength.setBounds(ratioPanel.getWidth() / 2, ratioPanel.getHeight() / 2,
				ratioPanel.getWidth() / 2, ratioPanel.getHeight() / 3);

		ratioPanel.add(ratioTitle);
		ratioPanel.add(ratioValue);
		ratioPanel.add(removeRatioLength);
		ratioPanel.add(addRatioLength);

		add(ratioPanel);

		namePanel.setBounds(border * 2 + (int) Math.round(size.getWidth() / 4 - border / 4.0 * 3) * 2,
				(int) (topHeight + 5.0 / 2 * border + size.height / 4),
				(int) Math.round(size.getWidth() / 4 - border / 4.0 * 3) * 2,
				size.height - border - (int) (topHeight + 7.0 / 2 * border + size.height / 4));

		namePanel.setBorder(BorderFactory.createLineBorder(Color.black));
		namePanel.setLayout(null);

		nameTitle.setBounds(0, 0, namePanel.getWidth(), namePanel.getHeight() / 4);
		nameTitle.setFont(calibriSubTitle);
		nameTitle.setHorizontalAlignment(SwingConstants.CENTER);
		nameTitle.setVerticalAlignment(SwingConstants.CENTER);

		newName.setBounds(0, namePanel.getHeight() / 4, namePanel.getWidth() / 8 * 7,
				namePanel.getHeight() / 4);
		newName.setText(pixelArtGenerator.returnName());

		namePNG.setBounds(namePanel.getWidth() / 8 * 7, namePanel.getHeight() / 4, namePanel.getWidth() / 8,
				namePanel.getHeight() / 4);
		namePNG.setFont(calibriSubTitle);
		namePNG.setHorizontalAlignment(SwingConstants.CENTER);
		namePNG.setVerticalAlignment(SwingConstants.CENTER);

		changeNameButton.setBounds(0, namePanel.getHeight() / 2, namePanel.getWidth(),
				namePanel.getHeight() / 3);

		newName.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.getKeyChar() != KeyEvent.VK_SPACE) {
					newName.setEditable(true);
				} else {
					newName.setEditable(false);
				}

			}

			public void keyReleased(KeyEvent ke) {
				changeNameButton
						.setEnabled(!pixelArtGenerator.returnName().equals(newName.getText()));
			}
		});

		changeNameButton
				.setEnabled(!pixelArtGenerator.returnName().equals(newName.getText()));

		namePanel.add(nameTitle);
		namePanel.add(newName);
		namePanel.add(namePNG);
		namePanel.add(changeNameButton);

		add(namePanel);

	}

	/**
	 * Adds the actions for the sliders for length and width
	 * 
	 * @param textField Corresponding JTextField
	 * @param slider    Corresponding JSlider
	 */
	private void dimensionSliders(JTextField textField, JSlider slider) {
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				String value = textField.getText();
				int l = value.length();
				if (l == 0 && ke.getKeyChar() >= '1' && ke.getKeyChar() <= '9' ||
						l > 0 && l <= 4 && ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9'
								&& inputChecker(value, ke.getKeyChar(), minVal, maxVal)
						||
						ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					textField.setEditable(true);
				} else {
					textField.setEditable(false);
				}
			}

			public void keyReleased(KeyEvent ke) {
				try {
					int convert = Integer.parseInt(textField.getText());
					if (convert >= 100) {
						slider.setValue(convert);

					}
				} catch (NumberFormatException nfe) {

				}
			}
		});
	}

	/**
	 * Checks if the input is valid
	 * If the string with the new character is of max langth, it must be between the
	 * min and max inclusive
	 * If the string with the new character is less than the max length, must be a
	 * number
	 * 
	 * @param s       Original string
	 * @param newChar New entered character
	 * @param min     Min value
	 * @param max     Max value
	 * @return
	 */
	private boolean inputChecker(String s, char newChar, int min, int max) {
		String combined = s + newChar;
		int length = combined.length();
		int c = Integer.valueOf(combined);
		if (c <= max && (length == 4 && c >= min || length < 4)) {
			return true;
		}
		return false;
	}

}