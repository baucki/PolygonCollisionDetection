public class Point {

    private double coordX;
    private double coordY;

    public Point (double x, double y) {
        this.coordX = x;
        this.coordY = y;
    }

    public double getCoordX() {
        return this.coordX;
    }

    public void setCoordX(double x) {
        this.coordX = x;
    }

    public double getCoordY() {
        return this.coordY;
    }

    public void setCoordY(double y) {
        this.coordY = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "coordX=" + coordX +
                ", coordY=" + coordY +
                '}';
    }
}
