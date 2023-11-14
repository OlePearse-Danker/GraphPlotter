import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class GraphPlotter extends JPanel{
    private Graphics g;
    private JButton zoomInButton;
    private JButton zoomOutButton;

    private int screenMaxX;
    private int screenMaxY;

    private double xMin, yMin, xMax, yMax;
    private double xUnit, yRange;
    private double xRange; // Added variable declaration
    private double yUnit; // Added variable declaration

    public GraphPlotter(int screenMaxX, int screenMaxY) {
        this.screenMaxX = screenMaxX;
        this.screenMaxY = screenMaxY;

        xMax = 1 * Math.PI;
        xMin = -xMax;
        yMax = 1.5;
        yMin = -yMax;

        xRange = (xMax - xMin); // Initialize xRange
        yRange = (yMax - yMin);

        xUnit = xRange / screenMaxX;
        yUnit = yRange / screenMaxY;

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

        xUnit = xRange / screenMaxX;
        yUnit = yRange / screenMaxY;

        coordinateSystem();
        plot();
    }

    private void drawPoint(Point p, Color c) {
        g.setColor(c);
        g.drawLine(p.x, p.y, p.x, p.y);
    }

    private void coordinateSystem() {
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        g.clearRect(0, 0, screenWidth, screenHeight); // Clear the screen

        int gridSize = 50; // Adjust this value to change the grid spacing

        // Draw the grid lines
        g.setColor(Color.LIGHT_GRAY);
        for (int xScreen = 0; xScreen <= screenWidth; xScreen += gridSize) {
            g.drawLine(xScreen, 0, xScreen, screenHeight);
        }
        for (int yScreen = 0; yScreen <= screenHeight; yScreen += gridSize) {
            g.drawLine(0, yScreen, screenWidth, yScreen);
        }

        // Draw the x-axis
        g.setColor(Color.BLACK);
        g.drawLine(0, screenHeight / 2, screenWidth, screenHeight / 2);

        // Draw the y-axis
        g.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight);

        // Add labels for x-axis
        for (int xScreen = 0; xScreen < screenWidth; xScreen += gridSize) {
            double xValue = xMin + (xScreen * xUnit);
            g.drawString(String.format("%.2f", xValue), xScreen, screenHeight / 2 + 15);
        }

        // Add labels for y-axis
        for (int yScreen = 0; yScreen < screenHeight; yScreen += gridSize) {
            double yValue = yMax - ((double) yScreen / screenHeight) * yRange;
            g.drawString(String.format("%.2f", yValue), screenWidth / 2 + 5, yScreen + 15);
        }

        // Draw y = 0 line
        g.setColor(Color.RED);
        int yScreenZero = screenHeight / 2;
        g.drawLine(0, yScreenZero, screenWidth, yScreenZero);

        // Draw x = 0 line
        g.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight);
    }






    private void plot() {
        double x = xMin;
        for (int xScreen = 0; xScreen < screenMaxX; xScreen++) {
            x = x + xUnit;
            int yScreen = scrrenMidY() - ((int) ((f(x) / yRange) * screenMaxY));
            drawPoint(new Point(xScreen, yScreen), Color.BLUE);
        }

        // Zeichne die Linie für y = 0
//        g.setColor(Color.RED);
//        int yScreenZero = scrrenMidY() - ((int) ((f(0.0) / yRange) * screenMaxY));
//        g.drawLine(0, yScreenZero, screenMaxX, yScreenZero);

    }
    private void createZoomInButton() {
        zoomInButton = new JButton("Zoom In");
        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the zoom-in method here
                zoomIn(); // Call the zoom-in method
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
                zoomOut(); // Call the zoom-out method
            }
        });
        add(zoomOutButton);
    }

    private void zoomIn() {
        // Decrease the x-axis and y-axis ranges for zooming in
        double zoomFactor = 2.0;
        xRange /= zoomFactor;
        yRange /= zoomFactor;

        // Recalculate the xUnit and yUnit values based on the updated ranges
        xUnit = xRange / screenMaxX;
        yUnit = yRange / screenMaxY;

        // Repaint the panel to reflect the new zoom level
        repaint();
    }

    private void zoomOut() {
        // Increase the x-axis and y-axis ranges for zooming out
        double zoomFactor = 2.0;
        xRange *= zoomFactor;
        yRange *= zoomFactor;

        // Recalculate the xUnit and yUnit values based on the updated ranges
        xUnit = xRange / screenMaxX;
        yUnit = yRange / screenMaxY;

        // Repaint the panel to reflect the new zoom level
        repaint();
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
