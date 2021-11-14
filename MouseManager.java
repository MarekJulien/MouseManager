import java.awt.*;
import javax.swing.*;

public class MouseManager {

	private static Robot r;

	private static int xPos = 0;
	private static int yPos = 0;
	private static int rValue = 0;
	private static int gValue = 0;
	private static int bValue = 0;

	private static boolean running = true;

	public static void main(String[] args) {

		// Create Instances
		try {
			r = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

		// Get SCeen Size
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();

		// Update Information Thread
		Thread infoUpdater = new Thread() {

			public void run() {

				try {

					while (running) {

						sleep(50);

						Point p = MouseInfo.getPointerInfo().getLocation();
						xPos = p.x;
						yPos = p.y;

						int rgb = r.createScreenCapture(new Rectangle((int) width, (int) height)).getRGB(xPos, yPos);
						bValue = rgb & 0xff;
						gValue = (rgb & 0xff00) >> 8;
						rValue = (rgb & 0xff0000) >> 16;

					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		};

		infoUpdater.start();

		// Create GUI
		JFrame.setDefaultLookAndFeelDecorated(false);
		JFrame frame = new JFrame();
		frame.setTitle("Mouse Information");
		frame.setSize(300, 100);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel posLabel = new JLabel("Positon:");
		posLabel.setBounds(25, 0, 300, 50);
		posLabel.setVisible(true);
		frame.add(posLabel);

		JLabel posDisplay = new JLabel("0, 0");
		posDisplay.setBounds(0, 0, 300, 50);
		posDisplay.setHorizontalAlignment(JLabel.CENTER);
		posDisplay.setVisible(true);
		frame.add(posDisplay);

		JLabel rgbLabel = new JLabel("RGB-Value:");
		rgbLabel.setBounds(25, 0, 300, 100);
		rgbLabel.setVisible(true);
		frame.add(rgbLabel);

		JLabel rgbDisplay = new JLabel("0, 0, 0");
		rgbDisplay.setBounds(0, 0, 300, 100);
		rgbDisplay.setHorizontalAlignment(JLabel.CENTER);
		rgbDisplay.setVisible(true);
		frame.add(rgbDisplay);

		ColorPanel cPanel = new ColorPanel();
		cPanel.setBounds(200, 15, 280, 55);
		cPanel.setVisible(true);
		frame.add(cPanel);

		frame.setVisible(true);

		// Update GUI Thread
		Thread guiUpdater = new Thread() {

			public void run() {

				try {

					while (running) {

						sleep(50);

						posDisplay.setText(xPos + ", " + yPos);
						rgbDisplay.setText(rValue + ", " + gValue + ", " + bValue);
						cPanel.refresh(new Color(rValue, gValue, bValue));

					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		};

		guiUpdater.start();

	}

}

class ColorPanel extends JPanel {

	private Color color = Color.WHITE;

	public void paint(Graphics g) {
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(10));
		g2d.drawRect(0, 0, 80, 40);
		
		g2d.setColor(color);
		g2d.fillRect(5, 5, 75, 35);
		
	}

	public void refresh(Color color) {

		this.color = color;
		repaint();

	}

}
