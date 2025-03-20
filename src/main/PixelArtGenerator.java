package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class PixelArtGenerator {
	private int length = 1050;
	private int height = 1050;

	String fileName = "default";
	private static PixelArtGenerator INSTANCE;

	ArrayList<Color> colors = new ArrayList<Color>();

	private int ratio = 1; // side length of a single "pixel" for image
	private int maxDither = 10;
	private int secondaryDither = 8;
	private int tertiaryDither = 8;
	private int quaternaryDither = 0;

	private int totalDitherLength = maxDither + secondaryDither + tertiaryDither;

	private final int type = BufferedImage.TYPE_INT_ARGB;

	protected static PixelArtGenerator getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PixelArtGenerator();
		}

		return INSTANCE;
	}

	private PixelArtGenerator() {

		colors.add(new Color(139, 0, 1));
		colors.add(new Color(158, 23, 17));
		colors.add(new Color(177, 46, 33));
		colors.add(new Color(195, 70, 50));
	}

	protected void generate() {
		BufferedImage image = new BufferedImage(length * ratio, height * ratio, type);
		int convw = length;
		int convh = height;
		File f = new File("./Photos/" + fileName + ".png");

		Graphics2D g2d = image.createGraphics();

		for (int x = 0; x < convw; x++) {
			for (int y = 0; y < convh; y++) {
				double bb = (double) (convh) / colors.size();
				int coordinate = (int) ((y / bb));
				int a = coordinate < numberOfColors() ? coordinate
						: numberOfColors() - 1;
				int b = coordinate - 1 < 0 ? 0 : coordinate - 1;
				int c = coordinate + 1 > colors.size() - 1 ? colors.size() - 1
						: coordinate + 1;
				g2d.setColor(setColor(x, y, convh / colors.size() * (a + 1), convh / colors.size() * a, colors.get(a),
						colors.get(b), colors.get(c)));

				g2d.fillRect(x * ratio, y * ratio, ratio, ratio);

			}
		}

		try {
			ImageIO.write(image, "png", f);
			Desktop.getDesktop().open(f);
		} catch (IOException ee) {
			ee.printStackTrace();
		}
	}

	private Color setColor(int x, int y, int height, int start, Color a, Color b, Color c) {
		if (y - maxDither < start && ((x % 2 == 1 && y % 2 == 0) || (x % 2 == 0 && y % 2 == 1))) {
			return b;
		} else if (y - maxDither - secondaryDither < start && y - maxDither >= start
				&& ((x % 4 == 2 && y % 4 == 1) || (x % 4 == 0 && y % 4 == 3) || (x % 2 == 1 && y % 2 == 0))) {
			return b;
		} else if (y - maxDither - secondaryDither - tertiaryDither < start && y - maxDither - secondaryDither >= start
				&& (x % 2 == 1 && y % 2 == 0)) {
			return b;
		}
		// else if (y - totalDitherLength >= height
		// && y - maxDither - secondaryDither - tertiaryDither < height
		// && ((x % 2 == 1 && x % 4 != 0 && y % 4 == 1) || (x % 2 == 1 && x % 4 != 3 &&
		// y % 4 == 3))) {
		// return b;
		// } else if (y + totalDitherLength >= height
		// && y + maxDither + secondaryDither + tertiaryDither < height
		// && ((x % 2 == 1 && x % 4 != 0 && y % 4 == 1) || (x % 2 == 1 && x % 4 != 3 &&
		// y % 4 == 3))) {
		// return c;
		// }
		else if (y + maxDither + secondaryDither + tertiaryDither >= height
				&& y + maxDither + secondaryDither < height && (x % 2 == 1 && y % 2 == 1)) {
			return c;
		} else if (y + maxDither + secondaryDither >= height && y + maxDither < height
				&& ((x % 4 == 2 && y % 4 == 2) || (x % 4 == 0 && y % 4 == 0) || (x % 2 == 1 && y % 2 == 1))) {
			return c;
		} else if (y + maxDither >= height && ((x % 2 == 1 && y % 2 == 1) || (x % 2 == 0 && y % 2 == 0))) {
			return c;
		}
		return a;
	}

	protected void changeColour(Color r, int i) {
		colors.set(i, r);
	}

	protected Color returnColour(int i) {
		return colors.get(i);
	}

	protected void setLength(int length) {
		this.length = length;
	}

	protected int getLength() {
		return length;
	}

	protected void setHeight(int height) {
		this.height = height;
	}

	protected int getHeight() {
		return height;
	}

	protected int numberOfColors() {
		return colors.size();
	}

	/**
	 * Adds or subtracts colors from the gradient
	 * 
	 * @param numberOfColors
	 */
	protected void resizeGradient(int numberOfColors) {

		if (numberOfColors < colors.size()) { // remove colors
			for (int i = colors.size(); i > numberOfColors; i--) {
				colors.remove(i - 1);
			}
		}
		if (numberOfColors > colors.size()) { // add new colors
			for (int i = colors.size(); i < numberOfColors; i++) {
				colors.add(new Color(0));
			}
		}
	}

	protected void resizeDither(String level, int value) {
		switch (level) {
			case "Primary":
				maxDither = value;
				break;
			case "Secondary":
				secondaryDither = value;
				break;
			case "Tertiary":
				tertiaryDither = value;
				break;
			case "Quaternary":
				quaternaryDither = value;
				break;
		}
		totalDitherLength = maxDither + secondaryDither + tertiaryDither + quaternaryDither;
		// System.out.println("Total: " + totalDitherLength);
	}

	/**
	 * 
	 * @param level
	 * @return dither length
	 */
	protected int returnDitherLength(String level) {
		switch (level) {
			case "Primary":
				return maxDither;
			case "Secondary":
				return secondaryDither;
			case "Tertiary":
				return tertiaryDither;
			case "Quaternary":
				return quaternaryDither;
		}
		return -1; // error

	}

	protected int returnTotalDitherLength() {
		return totalDitherLength;
	}

	protected void changeRatio(int ratio) {
		this.ratio = ratio;
	}

	protected int returnRatio() {
		return ratio;
	}

	protected void changeName(String fileName) {
		this.fileName = fileName;
	}

	protected String returnName() {
		return fileName;
	}

}
