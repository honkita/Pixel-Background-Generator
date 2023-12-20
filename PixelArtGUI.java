import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;

public class PixelArtGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private final Integer[] choices = { 1, 2, 3, 4 };

	private final JButton generateButton = new JButton("Generate Image");
	private final JButton openFolderButton = new JButton("Open Folder");
	private final JButton exitButton = new JButton("Exit");
	private final JButton selectNewColourButton = new JButton("Select New Colour");
	private final JComboBox<Integer> layerColour = new JComboBox<>(choices);
	private final JFrame colourPicker = new JFrame();
	private final JLabel layerColourTitle = new JLabel("Change Layer Colour");
	private final JPanel buttonPanel = new JPanel();
	private final JPanel colourPanel = new JPanel();
	private final JPanel pickedColour = new JPanel();
	private final JPanel currentGradient = new JPanel();
	private final ArrayList<JPanel> gradiantColours = new ArrayList<JPanel>();

	Dimension size;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Rectangle r;
	int height;
	int width;
	PixelArtGenerator p;
	private final double ratio = (double) screenSize.width / screenSize.height;

	PixelArtGUI() {
		for (int i = 0; i < 4; i++) {
			gradiantColours.add(new JPanel());
		}
		setSize(screenSize.width / 2, screenSize.height / 2);
		size = getBounds().getSize();
		setLayout(null);
		setLocationRelativeTo(null);
		p = PixelArtGenerator.getInstance();
		buttonActions();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				// Ensures that the minimum size is possible
				if (getBounds().getSize().height <= screenSize.height / 2) {
					setSize(getWidth(), screenSize.height / 2);
				}
				if (getBounds().getSize().width <= screenSize.width / 2) {
					setSize(screenSize.width / 2, getHeight());
				}

				// Uniform scaling!!!!!!
				if (getBounds().getSize().width >= screenSize.width / 2) {
					setSize(getWidth(), (int) (getBounds().getSize().width / ratio));
				} else if (getBounds().getSize().height >= screenSize.height / 2) {
					setSize((int) (ratio * getBounds().getSize().height), getHeight());
				}
				size = getBounds().getSize();

				repaint();
			}
		});
		repaint();
	}

	private void buttonActions() {
		// TODO Auto-generated method stub
		generateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				p.generate();
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

		selectNewColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(colourPicker, getTitle(),
						p.returnColour((int) (layerColour.getSelectedItem()) - 1));
				if (newColor != null)
					p.changeColour(newColor, (int) (layerColour.getSelectedItem()) - 1);
				repaint();
			}
		});

		layerColour.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				repaint();
			}
		});
	}

	/**
	 * Painting function Graphics g
	 */
	public void paint(Graphics g) {
		super.paint(g);
		buttonPanel.setBounds(size.width / 10, size.height / 10, size.width / 5, size.height / 2);
		buttonPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		buttonPanel.setLayout(null);
		generateButton.setBounds(0, 0, buttonPanel.getWidth(), buttonPanel.getHeight() / 8);
		openFolderButton.setBounds(0, buttonPanel.getHeight() / 8, buttonPanel.getWidth(), buttonPanel.getHeight() / 8);
		exitButton.setBounds(0, buttonPanel.getHeight() / 4, buttonPanel.getWidth(), buttonPanel.getHeight() / 8);

		colourPanel.setBounds(size.width / 5 * 3, size.height / 10, size.width / 4, size.height / 2);
		colourPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		colourPanel.setLayout(null);

		layerColourTitle.setBounds(0, 0, colourPanel.getWidth(), colourPanel.getHeight() / 8);
		layerColourTitle.setFont(new Font("Calibri", Font.PLAIN, colourPanel.getHeight() / 16));
		layerColourTitle.setHorizontalAlignment(SwingConstants.CENTER);
		layerColourTitle.setVerticalAlignment(SwingConstants.CENTER);

		layerColour.setBounds(0, colourPanel.getHeight() / 4, colourPanel.getWidth(), colourPanel.getHeight() / 8);
		layerColour.doLayout();
		layerColour.setVisible(true);
		selectNewColourButton.setBounds(0, colourPanel.getHeight() / 8 * 7, colourPanel.getWidth(),
				colourPanel.getHeight() / 8);

		pickedColour.setBounds(colourPanel.getWidth() / 4, colourPanel.getHeight() / 16 * 7, colourPanel.getWidth() / 2,
				colourPanel.getWidth() / 2);
		pickedColour.setBackground(p.returnColour((int) (layerColour.getSelectedItem()) - 1));

		currentGradient.setBounds(size.width / 16 * 15, size.height / 10, size.width / 16, size.height / 2);
		currentGradient.setBorder(BorderFactory.createLineBorder(Color.black));
		currentGradient.setLayout(null);

		for (int i = 0; i < 4; i++) {
			gradiantColours.get(i).setBounds(0, (2 * i + 1) * currentGradient.getHeight() / 8,
					currentGradient.getHeight() / 8, currentGradient.getHeight() / 8);
			gradiantColours.get(i).setBackground(p.returnColour(i));
			currentGradient.add(gradiantColours.get(i));
		}

		colourPanel.add(layerColourTitle);
		colourPanel.add(selectNewColourButton);
		colourPanel.add(layerColour);
		colourPanel.add(pickedColour);
		add(colourPanel);

		buttonPanel.add(openFolderButton);
		buttonPanel.add(generateButton);
		buttonPanel.add(exitButton);
		add(buttonPanel);

		add(currentGradient);
	}

}
