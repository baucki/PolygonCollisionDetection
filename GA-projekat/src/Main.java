import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Main extends JFrame implements Runnable{

    private JPanel panel;

    boolean running = true;

    int pointPCounter;
    int pointQCounter;
    int flag;

    int px[];
    int py[];

    int qx[];
    int qy[];

    BentleyOttmann sweepLine;

    ArrayList<Segment> data;
    public Main() {

        this.setTitle("GA_projekat");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        px = new int[999];
        py = new int[999];

        qx = new int[999];
        qy = new int[999];

        flag = 0;
        pointPCounter = 0;
        pointQCounter = 0;

        data = new ArrayList<>();

        panel = new JPanel();

        Thread thread = new Thread(this);


        panel.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {

                if ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
                    Graphics2D g = (Graphics2D) panel.getGraphics();
                    if (flag == 0) {

                        px[pointPCounter] = e.getX();
                        py[pointPCounter] = e.getY();
                        pointPCounter++;

                        System.out.println("PolygonP point : " + (e.getPoint()));

                        if (pointPCounter > 1) {
                            g.drawLine(px[pointPCounter-2], py[pointPCounter-2], px[pointPCounter-1], py[pointPCounter-1]);
                            data.add(new Segment(new Point(px[pointPCounter-2], py[pointPCounter-2]), new Point(px[pointPCounter-1], py[pointPCounter-1])));
                        }
                    }
                    else if (flag == 1){

                        qx[pointQCounter] = e.getX();
                        qy[pointQCounter] = e.getY();
                        pointQCounter++;

                        System.out.println("PolygonQ point : " + (e.getPoint()));

                        if (pointQCounter > 1) {
                            g.drawLine(qx[pointQCounter-2], qy[pointQCounter-2], qx[pointQCounter-1], qy[pointQCounter-1]);
                            data.add(new Segment(new Point(qx[pointQCounter-2], qy[pointQCounter-2]), new Point(qx[pointQCounter-1], qy[pointQCounter-1])));
                        }
                    }
                }
                if ((e.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
                    Graphics g = panel.getGraphics();
                    if (flag == 0) {
                        g.drawLine(px[pointPCounter-1], py[pointPCounter-1], px[0], py[0]);
                        data.add(new Segment(new Point(px[pointPCounter-1], py[pointPCounter-1]), new Point(px[0], py[0])));
                        flag++;
                    } else if (flag == 1) {
                        g.drawLine(qx[pointQCounter - 1], qy[pointQCounter - 1], qx[0], qy[0]);
                        data.add(new Segment(new Point(qx[pointQCounter - 1], qy[pointQCounter - 1]), new Point(qx[0], qy[0])));
                        flag++;
                        System.out.println(data.size());
                        for (Segment s : data) {
                            System.out.println(s.first() + " " + s.second());
                        }
                        sweepLine = new BentleyOttmann(data);
                        thread.start();
                    }
                }
            }
        });
        this.add(panel);
        this.setSize(screenSize.width/3*2, screenSize.height/3*2);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paintComponents(g);
        for(int i = 0; i < pointPCounter; i++) {
            if (i == 0)
                g.drawLine(px[pointPCounter-1], py[pointPCounter-1], px[0],py[0]);
            else
                g.drawLine(px[i-1], py[i-1], px[i],py[i]);

        }

        for(int i = 0; i < pointQCounter; i++) {
            if (i == 0)
                g.drawLine(qx[pointQCounter-1], qy[pointQCounter-1], qx[0],qy[0]);
            else
                g.drawLine(qx[i-1], qy[i-1], qx[i],qy[i]);

        }
    }


    @Override
    public void run() {
        while (true) {
            if (running) {
                try {
                    Thread.sleep(150);
                    repaint();
                    for(int i = 0; i < pointPCounter; i++) {
                        px[i] += 3;
                    }
                    for (int i = 0; i < pointPCounter; i++) {
                        double x1 = data.get(i).first().getCoordX();
                        double x2 = data.get(i).second().getCoordX();
                        data.get(i).first().setCoordX(x1+3.0);
                        data.get(i).second().setCoordX(x2+3.0);
                    }

                    sweepLine = new BentleyOttmann(data);
                    sweepLine.findIntersections();
                    if (!(sweepLine.getIntersections().isEmpty())) {
                        ArrayList<Point> intersections = sweepLine.getIntersections();
                        for(Point p: intersections) {
                            Graphics gr = panel.getGraphics();
                            gr.drawOval((int)p.getCoordX() - 12,(int)p.getCoordY() - 35,10,10);
                            System.out.println("Intersection at: " + p.getCoordX() + " " + p.getCoordY());
                        }
                        running = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
