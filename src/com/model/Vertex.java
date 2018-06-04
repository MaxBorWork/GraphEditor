package com.model;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Vertex {
    private static AtomicLong atomicLong = new AtomicLong();
    private long ID;
    private Canvas canvas;
    private static int radius = 8;
    private String name = "";
    int id;
    private int x;
    private int y;
    private int distance = -1;
    private List<Arc> ingoing;
    private List<Arc> outgoing;
    private ArrayList<Integer> weight ;
    private ArrayList<Integer> ekstr ;
    private Color defaultColor;
    private Color   priviewColor;
    private Color green = new Color(null, 29, 216, 23);
    private Color black = new Color(null, 0, 0, 0);
    private Color red = new Color(null, 255, 13, 13);
    private Color yellow = new Color(null, 255, 255, 15);
    private Color blue = new Color(null, 12, 40, 240);
    private PaintListener paintListener;

    public Vertex(int x, int y, Canvas canvas, int id, BaseOfVertex base) {
    	this.id = id;
        ID = atomicLong.incrementAndGet();
        weight = new ArrayList<Integer>();
        for (int i=0; i<20; i++) {
        	weight.add(null);
        }
        base.add(weight);
        this.x = x;
        this.y = y;
        this.canvas = canvas;
        ingoing = new ArrayList<>();
        outgoing = new ArrayList<>();
        defaultColor = black;
        draw();
        canvas.redraw();
    }

    private void draw() {
        paintListener = new PaintListener() {
            @Override
            public void paintControl(PaintEvent paintEvent) {
                paintEvent.gc.setForeground(defaultColor);
                paintEvent.gc.setLineWidth(5);
                paintEvent.gc.drawOval(x - radius, y - radius, radius * 2, radius * 2);
                paintEvent.gc.drawText(name, x + radius, y + radius);
                if (distance != -1) {
                    paintEvent.gc.setForeground(new Color(null, 10,10,255));
                    paintEvent.gc.drawText(String.valueOf(distance), x + 2*radius, y - 2*radius);
                }
            }
        };
        canvas.addPaintListener(paintListener);
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
        canvas.redraw();
    }

    public void move(int x, int y) {
        defaultColor = green;
        this.x = x;
        this.y = y;
        canvas.redraw();
    }
    
    public int giveId() {
        return this.id;
    }
    public List<Integer> giveWeight() {
        return this.weight;
    }

    public void select() {
    	priviewColor= defaultColor;
        defaultColor = green;
        canvas.redraw();
    }
    
    public void chaneColorRed() {
    	priviewColor= red;
        defaultColor = red;
        canvas.redraw();
    }
    
    public void chaneColorYellow() {
    	priviewColor= yellow;
        defaultColor = yellow;
        canvas.redraw();
    }
    
    public void chaneColorBlue() {
    	priviewColor=blue;
        defaultColor = blue;
        canvas.redraw();
    }

    public void setDistance(int distance) {
        this.distance = distance;
        canvas.redraw();
    }
    
    public void deselect() {
        defaultColor = priviewColor;
        canvas.redraw();
    }

 

    public static int getRadius() {
        return radius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void delete() {
        ingoing.clear();
        outgoing.clear();
        canvas.removePaintListener(paintListener);
        canvas.redraw();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        canvas.redraw();
    }

    public List<Arc> getIngoing() {
        return ingoing;
    }

    public List<Arc> getOutgoing() {
        return outgoing;
    }

    public void addIngoing(Arc arc) {
        ingoing.add(arc);
    }

    public void addOutgoing(Arc arc) {
        outgoing.add(arc);
    }

    public void delIngoing(Arc arc) {
        ingoing.remove(arc);
    }

    public void delOutgoing(Arc arc) {
        outgoing.remove(arc);
    }

    public long getID() {
        return ID;
    }

	public void changeColor(Color ddd, int index) {
		// TODO Auto-generated method stub
		
	}

	public void select1() {
		Color one = new Color(null, 150, 150, 11);
    	System.out.println("ee");
		priviewColor= defaultColor;
        defaultColor = one;
        canvas.redraw();		
	}

    public List<Vertex> getIncVert() {
        List<Vertex> incVert = new ArrayList<>();
        for (Arc outArc : this.getOutgoing()) {
            incVert.add(outArc.getIngoing());
        }
        return incVert;
    }
}
