import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class GraphPlotter extends JPanel {
    private JButton zoomInButton;
    private JButton zoomOutButton;

    private int screenMaxX;
    private int screenMaxY;

    private double xMin, yMin, xMax, yMax;
    private double xUnit, yRange;
    private double xRange;
    private double yUnit;

    private String expression; // User-entered function expression

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

        // Prompt the user to enter the function expression
        expression = JOptionPane.showInputDialog("Enter the function expression:");
        if (expression == null) {
            expression = ""; // Set empty string if user cancels input
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        coordinateSystem(g2d);
        plot(g2d);
    }

    private void drawPoint(Graphics2D g2d, Point p, Color c) {
        g2d.setColor(c);
        g2d.drawLine(p.x, p.y, p.x, p.y);
    }

    private void coordinateSystem(Graphics2D g2d) {
        int screenWidth = getWidth();
        int screenHeight = getHeight();

        g2d.clearRect(0, 0, screenWidth, screenHeight); // Clear the screen

        int gridSize = 50; // Adjust this value to change the grid spacing

        // Draw the grid lines
        g2d.setColor(Color.LIGHT_GRAY);
        for (int xScreen = 0; xScreen <= screenWidth; xScreen += gridSize) {
            g2d.drawLine(xScreen, 0, xScreen, screenHeight);
        }
        for (int yScreen = 0; yScreen <= screenHeight; yScreen += gridSize) {
            g2d.drawLine(0, yScreen, screenWidth, yScreen);
        }

        // Draw the x-axis
        g2d.setColor(Color.BLACK);
        g2d.drawLine(0, screenHeight / 2, screenWidth, screenHeight / 2);

        // Draw the y-axis
        g2d.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight);

        // Add labels for x-axis
        for (int xScreen = 0; xScreen < screenWidth; xScreen += gridSize) {
            double xValue = xMin + (xScreen * xUnit);
            g2d.drawString(String.format("%.2f", xValue), xScreen, screenHeight / 2 + 15);
        }

        // Add labels for y-axis
        for (int yScreen = 0; yScreen < screenHeight; yScreen += gridSize) {
            double yValue = yMax - ((double) yScreen / screenHeight) * yRange;
            g2d.drawString(String.format("%.2f", yValue), screenWidth / 2 + 5, yScreen + 15);
        }

        // Draw y = 0 line
        g2d.setColor(Color.RED);
        int yScreenZero = screenHeight / 2;
        g2d.drawLine(0, yScreenZero, screenWidth, yScreenZero);

        // Draw x = 0 line
        g2d.drawLine(screenWidth / 2, 0, screenWidth / 2, screenHeight);
    }

    private void plot(Graphics2D g2d) {
        double x = xMin;
        while (x <= xMax) {
            int xScreen = (int) ((x - xMin) / xUnit);
            int yScreen = (int) (screenMaxY - (f(x) - yMin) / yUnit);
            drawPoint(g2d, new Point(xScreen, yScreen), Color.BLUE);

            x += xUnit;
        }
    }

    private void createZoomInButton() {
        zoomInButton = new JButton("Zoom In");
        zoomInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomIn();
            }
        });
        add(zoomInButton);
    }

    private void createZoomOutButton() {
        zoomOutButton = new JButton("Zoom Out");
        zoomOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomOut();
            }
        });
        add(zoomOutButton);
    }

    private void zoomIn() {
        double zoomFactor = 2.0;
        xRange /= zoomFactor;
        yRange /= zoomFactor;

        xUnit = xRange / screenMaxX;
        yUnit = yRange / screenMaxY;

        repaint();
    }

    private void zoomOut() {
        double zoomFactor = 2.0;
        xRange *= zoomFactor;
        yRange *= zoomFactor;

        xUnit = xRange / screenMaxX;
        yUnit = yRange / screenMaxY;

        repaint();
    }

    private int screenMidX() {
        return screenMaxX / 2;
    }

    private int screenMidY() {
        return screenMaxY / 2;
    }

    private double f(double x) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");

            // Replace 'x' with the current x value and evaluate the expression
            engine.put("x", x);
            Object result = engine.eval(expression);
            return Double.parseDouble(result.toString());


        } catch (Exception e) {
            return 0.0; // Return 0 if the expression is invalid or the evaluation fails
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        GraphPlotter plotter = new GraphPlotter(800, 600);
        javax.swing.JFrame frame = new javax.swing.JFrame("Graph Plotter");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

        // Set the preferred size of the JPanel
        plotter.setPreferredSize(new java.awt.Dimension(800, 600));

        frame.setContentPane(plotter);
        frame.pack(); // Adjust the JFrame size based on the preferred size
        frame.setVisible(true);
    }

}
