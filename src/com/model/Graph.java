package com.model;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Graph {
    private Canvas canvas;
    private String name;
    private List<Arc> arcs = new ArrayList<>();
    private List<Vertex> vertices = new ArrayList<>();
    private Vertex selectVertex;
    private Arc selectArc;
    private BaseOfVertex base;

    public Graph(String name, Canvas canvas, BaseOfVertex base ) {
        this.name = name;
        this.canvas = canvas;
        this.base = base;
    }

	public void addVertex(int x, int y, int id,BaseOfVertex base) {
        if (findVertex(x, y) == null)
            vertices.add(new Vertex(x, y, canvas, id, base));
    }

    public void addVertex(Vertex vertex) {
        if (vertex != null)
            vertices.add(vertex);
      //  ArrayList<Integer> vv= new ArrayList<Integer>();
       // base.add(vv);
    }

    public void addArc(Vertex outgoing, Vertex ingoing) {
        if (outgoing != null && ingoing != null) {
            Arc arc = new Arc(outgoing, ingoing, canvas, base);
            arcs.add(arc);
        }
    }

    public void addArc(Arc arc) {
        if (arc != null)
            arcs.add(arc);
    }

    public Arc addArc(Vertex outgoing, int xIn, int yIn) {
        if (outgoing != null) {
            Arc arc = new Arc(outgoing, xIn, yIn, canvas, base);
            arcs.add(arc);
            return arc;
        }
        return null;
    }

    public void delete(Arc arc) {
        arcs.remove(arc);
        arc.delete();
    }

    public void delete(Vertex vertex) {
        vertices.remove(vertex);
        for (Arc arc : new ArrayList<>(vertex.getIngoing())) {
            arc.delete();
            arcs.remove(arc);

        }
        for (Arc arc : new ArrayList<>(vertex.getOutgoing())) {
            arc.delete();
            arcs.remove(arc);
        }
        vertex.delete();
    }

    public Vertex findVertex(int x, int y) {
        for (Vertex vertex : vertices) {
            int x1 = vertex.getX() - x;
            int y1 = vertex.getY() - y;
            int r = vertex.getRadius();
            if (r >= Math.sqrt(x1 * x1 + y1 * y1)) {
                return vertex;
            }
        }
        return null;
    }

    public void select(Vertex vertex) {
        if (selectVertex != null) {
            selectVertex.deselect();
        }
        if (selectArc != null)
            selectArc.deselect();
        selectVertex = vertex;
        if (vertex != null)
            vertex.select();
    }

    public Arc findArc(int x, int y) {
        for (Arc arc : arcs) {
            int x1 = arc.getOutgoing().getX();
            int y1 = arc.getOutgoing().getY();
            int x2 = arc.getIngoing().getX();
            int y2 = arc.getIngoing().getY();
            double len1 = Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
            double len2 = Math.sqrt((x2 - x) * (x2 - x) + (y2 - y) * (y2 - y));
            double len = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
            if (len - 0.5 < len1 + len2 && len1 + len2 < len + 0.5) {
                return arc;
            }
        }
        return null;
    }

    public void select(Arc arc) {
        if (selectArc != null) {
            selectArc.deselect();
        }
        if (selectVertex != null)
            selectVertex.deselect();
        selectArc = arc;
        if (arc != null)
            arc.select();
    }

    public Vertex getSelectVertex() {
        return selectVertex;
    }

    public Arc getSelectArc() {
        return selectArc;
    }

    public void deselectArc() {
        selectArc.deselect();
        selectArc = null;
    }

    public void deselectVertex() {
        if (selectVertex != null)
            selectVertex.deselect();
        selectVertex = null;
    }

    public void deleteSelected() {
        if (selectArc != null) {
            delete(selectArc);
        }
        if (selectVertex != null) {
            delete(selectVertex);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Arc> getArcs() {
        return arcs;
    }

    public Canvas getCanvas() {
        return canvas;
    }




	public int findCentre() {
		int index = 0;
			Vector<Integer> rad= new Vector<Integer>();
			for (int i=0; i<base.size(); i++) {
				rad.add(base.findEstr(i));
			}
			int min=0;
			for (int i=0; i<rad.size(); i++) {
				if(rad.get(i)<min) {min=rad.get(i);
				}
			}
			for (int i=0; i<rad.size(); i++) {
				if(rad.get(i)==min) {index=i;
				}
			}
			return index;
		
	}




	public int findDiameter() {
		Vector<Integer> rad= new Vector<Integer>();
		for (int i=0; i<base.size(); i++) {
			rad.add(base.findEstr(i));
		}
		int min=0;
		for (int i=0; i<rad.size(); i++) {
			if(rad.get(i)>min) {min=rad.get(i);}
		}
			return min;
		}




	public int findRadius() {
        System.out.println("radius");
	Vector<Integer> rad= new Vector<>();
	for (int i=0; i<base.size(); i++) {
		rad.add(base.findEstr(i));
	}
	int min=0;
	for (int i=0; i<rad.size(); i++) {
		if(rad.get(i)<min) {min=rad.get(i);}
	}
		return min;
	}




	public void findVertexForId(int index) {
		//Vertex vv = new Vertex(x, index, canvas, index, base);
    	System.out.println("ee");
		for (Vertex vertex : vertices) {
			if(vertex.id ==index) {    	System.out.println("ee");vertex.select1();}
			}   
      //  return vv;
	}


	public void select1( Vertex vertex) {
		vertex.select1();
		
	}
}
