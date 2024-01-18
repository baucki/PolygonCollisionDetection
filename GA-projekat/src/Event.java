import java.util.*;

public class Event {

    private Point point;
    private ArrayList<Segment> segments;
    private double value;
    private int type;

    public Event(Point point, Segment segments, int type) {
        this.point = point;
        this.segments = new ArrayList<>(Arrays.asList(segments));
        this.value = point.getCoordX();
        this.type = type;
    }

    public Event(Point point, ArrayList<Segment> segments, int type) {
        this.point = point;
        this.segments = segments;
        this.value = point.getCoordX();
        this.type = type;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return this.point;
    }

    public void addSegment(Segment segment) {
        this.segments.add(segment);
    }

    public ArrayList<Segment> getSegments() {
        return this.segments;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return this.value;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
