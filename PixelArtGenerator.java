import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class PixelArtGenerator {
	private int w = 2360;
	private int h = 1640;

	String fileName = "default";
	private static PixelArtGenerator INSTANCE;


	ArrayList<Color> colours = new ArrayList<Color>();
	
//	private Color a = new Color(120, 92, 50);
//	private Color b = new Color(161, 119, 47);
//	private Color c = new Color(193, 146, 39);
//	private Color d = new Color(205, 170, 94);

	private int ratio = 4;
	private int maxDither = 2;
	private int secondaryDither = 8;
	private int tertiaryDither = 8;
	private int quaternaryDither = 8;

	private int totalDitherLength = maxDither + secondaryDither + tertiaryDither;
	private int convw = w / ratio;
	private int convh = h / ratio;
	private int type = BufferedImage.TYPE_INT_ARGB;

	public static PixelArtGenerator getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new PixelArtGenerator();
        }
        
        return INSTANCE;
    }
	
	private PixelArtGenerator() {

		colours.add(new Color(139, 0, 1));
		colours.add(new Color(158, 23, 17));
		colours.add(new Color(177, 46, 33));
		colours.add(new Color(195, 70, 50));
	}

	public void generate() {
		BufferedImage image = new BufferedImage(w, h, type);
		File f = new File("./Photos/" + fileName + ".png");

		Graphics2D g2d = image.createGraphics();

		for (int x = 0; x < convw; x++) {
			for (int y = 0; y < convh; y++) {
				if (y < convh / 4) {
					g2d.setColor(setColor(x, y, convh / 4, 0, colours.get(0), colours.get(0), colours.get(1)));
				} else if (y < convh / 2) {
					g2d.setColor(setColor(x, y, convh / 2, convh / 4, colours.get(1), colours.get(0), colours.get(2)));
				} else if (y < convh / 4 * 3) {
					g2d.setColor(setColor(x, y, convh / 4 * 3, convh / 2, colours.get(2), colours.get(1), colours.get(3)));
				} else if (y >= convh / 4 * 3) {
					g2d.setColor(setColor(x, y, convh, convh / 4 * 3, colours.get(3), colours.get(2), colours.get(3)));
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

	private Color setColor(int x, int y, int height, int start, Color a, Color b, Color c) {
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
	
	protected void changeColour(Color r, int i) {
		colours.set(i, r);
	}
	
	protected Color returnColour(int i) {
		return colours.get(i);
	}
}
