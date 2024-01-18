public class Segment {

    private Point p1;
    private Point p2;
    double value;


    public Segment(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.calculateValue(this.first().getCoordX());
    }

    public Point first() {
        if (p1.getCoordX() <= p2.getCoordX())
            return p1;
        return p2;
    }

    public Point second() {
        if (p1.getCoordX() <= p2.getCoordX())
            return p2;
        return p1;
    }

    public void calculateValue(double value) {
        double x1 = this.first().getCoordX();
        double x2 = this.second().getCoordX();
        double y1 = this.first().getCoordY();
        double y2 = this.second().getCoordY();
        this.value = y1 + (((y2 - y1) / (x2 - x1)) * (value - x1));
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
