
public class Zoom {
    private double xRange;
    private double yRange;
    private double zoomFactor = 2.0;

    public Zoom(double xRange, double yRange) {
        this.xRange = xRange;
        this.yRange = yRange;
    }

    public void zoomIn() {
        xRange /= zoomFactor;
        yRange /= zoomFactor;
    }

    public void zoomOut() {
        xRange *= zoomFactor;
        yRange *= zoomFactor;
    }

    public double getXRange() {
        return xRange;
    }

    public double getYRange() {
        return yRange;
    }
}