import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
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

        coordinateSystem();
        plot();
    }

    private void drawPoint(Point p, Color c) {
        g.setColor(c);
        g.drawLine(p.x, p.y, p.x, p.y);
    }

    private void coordinateSystem() {
        g.drawLine(0, scrrenMidY(), screenMaxX, scrrenMidY());
        g.drawLine(scrrenMidX(), 0, scrrenMidX(), screenMaxY);

/*        // Add labels for x-axis
        for (int xScreen = 0; xScreen < screenMaxX; xScreen += 50) {
            double xValue = xMin + (xScreen * xUnit);
            g.drawString(String.format("%.2f", xValue), xScreen, scrrenMidY() + 15);
        }

        // Add labels for y-axis
        for (int yScreen = 0; yScreen < screenMaxY; yScreen += 50) {
            double yValue = yMax - ((double) yScreen / screenMaxY) * yRange;
            g.drawString(String.format("%.2f", yValue), scrrenMidX() + 5, yScreen + 15);
        }*/
    }

    private void plot() {
        double x = xMin;
        for (int xScreen = 0; xScreen < screenMaxX; xScreen++) {
            x = x + xUnit;
            int yScreen = scrrenMidY() - ((int) ((f(x) / yRange) * screenMaxY));
            drawPoint(new Point(xScreen, yScreen), Color.BLUE);
        }
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
