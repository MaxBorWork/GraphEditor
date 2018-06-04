package com.model.parser;

import com.model.Arc;
import com.model.BaseOfVertex;
import com.model.Graph;
import com.model.Vertex;
import org.eclipse.swt.widgets.Canvas;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

public class SAXReader extends DefaultHandler {
    private Graph graph;
    private Canvas canvas;
    private Map<Long, Vertex> mapVertex = new HashMap();
    private BaseOfVertex base;
    private int id=0;

    public void setCanvas(Canvas canvas, BaseOfVertex base) {
        this. base =  base;
        this.canvas = canvas;
    }

    public Graph getGraph() {
        return graph;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("graph".equals(qName)) {
            graph = new Graph(attributes.getValue("name"), canvas, base);
            return;
        }
        if ("vertex".equals(qName)) {
            Vertex vertex = new Vertex(Integer.parseInt(attributes.getValue("x")), Integer.parseInt(attributes.getValue("y")), canvas, id, base);
            id++;
            vertex.setName(attributes.getValue("name"));
            mapVertex.put(Long.parseLong(attributes.getValue("ID")), vertex);
            graph.addVertex(vertex);
            return;
        }
        if ("arc".equals(qName)) {
            Vertex outgoing = mapVertex.get(Long.parseLong(attributes.getValue("outgoing")));
            Vertex ingoing = mapVertex.get(Long.parseLong(attributes.getValue("ingoing")));
            if (outgoing != null && ingoing != null) {
                Arc arc = new Arc(outgoing, ingoing, canvas, base);
                arc.setWeight(Integer.parseInt(attributes.getValue("weight")), base);
                arc.setOriented(Boolean.parseBoolean(attributes.getValue("isOriented")));
                graph.addArc(arc);
            }
            return;
        }
    }

}
