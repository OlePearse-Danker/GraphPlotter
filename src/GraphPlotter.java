
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class GraphPlotter extends JPanel {
    private Graphics g;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private Zoom zoom;
    private double xMax, xMin, yMax, yMin;

    private double xRange;
    private double yRange;


    private int screenMaxX;
    private int screenMaxY;

    public GraphPlotter(int screenMaxX, int screenMaxY) {
        this.screenMaxX = screenMaxX;
        this.screenMaxY = screenMaxY;

        xMax = 1 * Math.PI;
        xMin = -xMax;
        yMax = 1.5;
        yMin = -yMax;

        xRange = xMax - xMin;
        yRange = yMax - yMin;

        zoom = new Zoom(xRange,  yRange );

        createZoomInButton();
        createZoomOutButton();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.g = g;

        int screenWidth = getWidth();
        int screenHeight = getHeight();

        screenMaxX = screenWidth;
        screenMaxY = screenHeight;

        double xUnit = zoom.getXRange() / screenMaxX;
        double yUnit = zoom.getYRange() / screenMaxY;

        coordinateSystem();
        plot(xUnit, yUnit);
    }

    private void drawPoint(Point p, Color c) {
        g.setColor(c);
        g.drawLine(p.x, p.y, p.x, p.y);
    }

    private void coordinateSystem() {
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        g.clearRect(0, 0, screenWidth, screenHeight); // Clear the screen

        int gridSize = 50;
        g.setColor(Color.LIGHT_GRAY);
        for (int xScreen = 0; xScreen <= screenWidth; xScreen += gridSize) {
            g.drawLine(xScreen, 0, xScreen, screenHeight);
        }
        for (int yScreen = 0; yScreen <= screenHeight; yScreen += gridSize) {
            g.drawLine(0, yScreen, screenWidth, yScreen);
        }

        g.setColor(Color.BLACK);
        g.drawLine(0, screenHeight / 2, screenWidth, screenHeight / 2);

        // Draw the y-axis
        g.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight);

        // Add labels for x-axis
        for (int xScreen = 0; xScreen < screenWidth; xScreen += gridSize) {
            double xValue = zoom.getXRange() / screenMaxX + (xScreen * zoom.getXRange() / screenMaxX);
            g.drawString(String.format("%.2f", xValue), xScreen, screenHeight / 2 + 15);
        }

        // Add labels for y-axis
        for (int yScreen = 0; yScreen < screenHeight; yScreen += gridSize) {
            double yValue = zoom.getYRange() - ((double) yScreen / screenHeight) * zoom.getYRange();
            g.drawString(String.format("%.2f", yValue), screenWidth / 2 + 5, yScreen + 15);
        }

        // Draw y = 0 line
        g.setColor(Color.RED);
        int yScreenZero = screenHeight / 2;
        g.drawLine(0, yScreenZero, screenWidth, yScreenZero);

        // Draw x = 0 line
        g.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight);
    }

    private void plot(double xUnit, double yUnit) {
        double x = xMin; //zoom.getXRange(); //xMin
        for (int xScreen = 0; xScreen < screenMaxX; xScreen++) {
            x = x + xUnit;
            int yScreen = scrrenMidY() - ((int) ((f(x) / zoom.getYRange()) * screenMaxY));
            drawPoint(new Point(xScreen, yScreen), Color.BLUE);
        }
    }

    private void createZoomInButton() {
        zoomInButton = new JButton("Zoom In");
        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the zoom-in method here
                zoom.zoomIn();
                repaint();
            }
        });
        add(zoomInButton);
    }

    private void createZoomOutButton() {
        zoomOutButton = new JButton("Zoom Out");
        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the zoom-out method here
                zoom.zoomOut();
                repaint();
            }
        });
        add(zoomOutButton);
    }

    private int scrrenMidX() {
        return screenMaxX / 2;
    }

    private int scrrenMidY() {
        return screenMaxY / 2;
    }

    private double f(double x) {
        return Math.sin(x);
        //return Math.exp(x);
    }
}