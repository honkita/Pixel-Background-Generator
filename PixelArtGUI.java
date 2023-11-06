import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.JButton;

public class PixelArtGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	JButton button = new JButton("Generate Image");
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	String fileName = "default";
	static final Color a = new Color(139, 0, 1);
	static final Color b = new Color(158, 23, 17);
	static final Color c = new Color(177, 46, 33);
	static final Color d = new Color(195, 70, 50);
//	static final Color a = new Color(120, 92, 50);
//	static final Color b = new Color(161, 119, 47);
//	static final Color c = new Color(193, 146, 39);
//	static final Color d = new Color(205, 170, 94);
	static final int w = 2360;
	static final int h = 1640;

	static final int ratio = 4;
	static final int maxDither = 2;
	static final int secondaryDither = 8;
	static final int tertiaryDither = 8;
	static final int quaternaryDither = 8;

	static final int totalDitherLength = maxDither + secondaryDither + tertiaryDither;
	static final int convw = w / ratio;
	static final int convh = h / ratio;
	static final int type = BufferedImage.TYPE_INT_ARGB;

	Color setColor(int x, int y, int height, int start, Color a, Color b, Color c) {
		if (y - maxDither < start && ((x % 2 == 1 && y % 2 == 0) || (x % 2 == 0 && y % 2 == 1))) {
			return b;
		} else if (y - maxDither - secondaryDither < start && y - maxDither >= start
				&& ((x % 4 == 2 && y % 4 == 3) || (x % 4 == 0 && y % 4 == 1) || (x % 2 == 1 && y % 2 == 0))) {
			return b;
		} else if (y - maxDither - secondaryDither - tertiaryDither < start && y - maxDither - secondaryDither >= start
				&& (x % 2 == 1 && y % 2 == 0)) {
			return b;
		} else if (y - maxDither - secondaryDither - tertiaryDither - quaternaryDither >= height
				&& y - maxDither - secondaryDither - tertiaryDither < height
				&& ((x % 2 == 1 && x % 4 != 0 && y % 4 == 1) || (x % 2 == 1 && x % 4 != 3 && y % 4 == 3))) {
			return c;
		} else if (y + maxDither + secondaryDither + tertiaryDither + quaternaryDither >= height
				&& y + maxDither + secondaryDither + tertiaryDither < height
				&& ((x % 2 == 1 && x % 4 != 0 && y % 4 == 0) || (x % 2 == 1 && x % 4 != 3 && y % 4 == 2))) {
			return c;
		} else if (y + maxDither + secondaryDither + tertiaryDither >= height
				&& y + maxDither + secondaryDither < height && (x % 2 == 1 && y % 2 == 0)) {
			return c;
		} else if (y + maxDither + secondaryDither >= height && y + maxDither < height
				&& ((x % 4 == 2 && y % 4 == 0) || (x % 4 == 0 && y % 4 == 2) || (x % 2 == 1 && y % 2 == 1))) {
			return c;

		} else if (y + maxDither >= height && ((x % 2 == 1 && y % 2 == 1) || (x % 2 == 0 && y % 2 == 0))) {
			return c;
		}
		return a;
	}

	PixelArtGUI() {
		setSize(screenSize.width / 2, screenSize.height / 2);
		setVisible(true);
		buttons();

	}

	private void buttons() {
		button.setBounds(50, 100, screenSize.width/5, screenSize.height/10);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage image = new BufferedImage(w, h, type);
				File f = new File(fileName + ".png");

				Graphics2D g2d = image.createGraphics();

				for (int x = 0; x < convw; x++) {
					for (int y = 0; y < convh; y++) {
						if (y < convh / 4) {
							g2d.setColor(setColor(x, y, convh / 4, 0, a, a, b));
						} else if (y < convh / 2) {
							g2d.setColor(setColor(x, y, convh / 2, convh / 4, b, a, c));
						} else if (y < convh / 4 * 3) {
							g2d.setColor(setColor(x, y, convh / 4 * 3, convh / 2, c, b, d));
						} else if (y >= convh / 4 * 3) {
							g2d.setColor(setColor(x, y, convh, convh / 4 * 3, d, c, d));
						}
						g2d.fillRect(x * ratio, y * ratio, ratio, ratio);

					}
				}

				try {
					ImageIO.write(image, "png", f);
					Desktop.getDesktop().open(f);
				} catch (IOException ee) {
					// TODO Auto-generated catch block
					ee.printStackTrace();
				}
			}
		});
		add(button);
	}

}
