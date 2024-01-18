import java.util.*;

public class BentleyOttmann {

    private Queue<Event> Q;
    private NavigableSet<Segment> T;
    private ArrayList<Point> X;

    public BentleyOttmann(ArrayList<Segment> inputData) {
        this.Q = new PriorityQueue<>(new EventComparator());
        this.T = new TreeSet<>(new SegmentComparator());
        this.X = new ArrayList<>();
        for(Segment segment : inputData) {
            this.Q.add(new Event(segment.first(), segment, 0));
            this.Q.add(new Event(segment.second(), segment, 1));
        }
    }

    public void findIntersections() {
        while(!this.Q.isEmpty()) {
            Event e = this.Q.poll();
            double L = e.getValue();
            switch (e.getType()) {
                case 0:
                    for (Segment s : e.getSegments()) {
                        this.recalculate(L);
                        this.T.add(s);
                        if (this.T.lower(s) != null) {
                            Segment r = this.T.lower(s);
                            this.reportIntersection(r, s, L);
                        }
                        if (this.T.higher(s) != null) {
                            Segment t = this.T.higher(s);
                            this.reportIntersection(t, s, L);
                        }
                        if (this.T.lower(s) != null && this.T.higher(s) != null) {
                            Segment r = this.T.lower(s);
                            Segment t = this.T.higher(s);
                            this.removeFuture(r, t);
                        }
                    }
                    break;
                case 1:
                    for (Segment s : e.getSegments()) {
                        if (this.T.lower(s) != null && this.T.higher(s) != null) {
                            Segment r = this.T.lower(s);
                            Segment t = this.T.higher(s);
                            this.reportIntersection(r, t, L);
                        }
                        this.T.remove(s);
                    }
                    break;
                case 2:
                    Segment s1 = e.getSegments().get(0);
                    Segment s2 = e.getSegments().get(1);
                    this.swap(s1, s2);
                    if (s1.getValue() < s2.getValue()) {
                        if (this.T.higher(s1) != null) {
                            Segment t = this.T.higher(s1);
                            this.reportIntersection(t, s1, L);
                            this.removeFuture(t, s2);
                        }
                        if (this.T.lower(s2) != null) {
                            Segment r = this.T.lower(s2);
                            this.reportIntersection(r, s2, L);
                            this.removeFuture(r, s1);

                        }
                    } else {
                        if (this.T.higher(s2) != null) {
                            Segment t = this.T.higher(s2);
                            this.reportIntersection(t, s2, L);
                            this.removeFuture(t, s1);
                        }
                        if (this.T.lower(s1) != null) {
                            Segment r = this.T.lower(s1);
                            this.reportIntersection(r, s1, L);
                            this.removeFuture(r, s2);
                        }
                    }

                    this.X.add(e.getPoint());
                    break;
            }
        }
    }

    private boolean reportIntersection(Segment s1, Segment s2, double L) {
        double x1 = s1.first().getCoordX();
        double y1 = s1.first().getCoordY();
        double x2 = s1.second().getCoordX();
        double y2 = s1.second().getCoordY();
        double x3 = s2.first().getCoordX();
        double y3 = s2.first().getCoordY();
        double x4 = s2.second().getCoordX();
        double y4 = s2.second().getCoordY();

        if ((x1 == x3 && y1 == y3) || (x1 == x4 && y1 == y4) || (x2 == x3 && y2 == y3) || (x2 == x4 && y2 == y4)) return false;

        double r = (x2 - x1) * (y4 - y3) - (y2 - y1) * (x4 - x3);
        if (r != 0) {
            double t = ((x3 - x1) * (y4 - y3) - (y3 - y1) * (x4 - x3)) / r;
            double u = ((x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1)) / r;
            if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
                double pointOfIntersectionX = x1 + t * (x2 - x1);
                double pointOfIntersectionY = y1 + t * (y2 - y1);
                if (pointOfIntersectionX > L) {
                    this.Q.add(new Event(new Point(pointOfIntersectionX, pointOfIntersectionY), new ArrayList<>(Arrays.asList(s1, s2)), 2));
                    return true;
                }
            }
        }
        return false;

    }

    private boolean removeFuture(Segment s1, Segment s2) {
        for (Event e: this.Q) {
            if (e.getType() == 2) {
                if ((e.getSegments().get(0) == s1 && e.getSegments().get(1) == s2) || (e.getSegments().get(0) == s2 && e.getSegments().get(1) == s1)) {
                    this.Q.remove(e);
                    return true;
                }
            }
        }
        return false;
    }

    private void swap(Segment s1, Segment s2) {
        this.T.remove(s1);
        this.T.remove(s2);
        double value = s1.getValue();
        s1.setValue(s2.getValue());
        s2.setValue(value);
        this.T.add(s1);
        this.T.add(s2);
    }

    private void recalculate(double L) {
        Iterator<Segment> iterator = this.T.iterator();
        while(iterator.hasNext()) {
            iterator.next().calculateValue(L);
        }
    }

    public ArrayList<Point> getIntersections() {
        return this.X;
    }

    private class EventComparator implements Comparator<Event> {
        @Override
        public int compare(Event e1, Event e2) {
            if (e1.getValue() > e2.getValue())
                return 1;
            if (e1.getValue() < e2.getValue())
                return -1;
            return 0;
        }
    }
    private class SegmentComparator implements Comparator<Segment> {
        @Override
        public int compare(Segment s1, Segment s2) {
            if (s1.getValue() < s2.getValue())
                return 1;
            if (s1.getValue() > s2.getValue())
                return -1;
            return 0;
        }
    }

}
