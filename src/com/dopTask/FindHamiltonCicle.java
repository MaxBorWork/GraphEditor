package com.dopTask;

import com.model.Arc;
import com.model.Graph;
import com.model.Vertex;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindHamiltonCicle {
    private Graph graph;
    private List<Vertex> vertexes;
    private Map<Vertex, Boolean> visited;
    private Map<Vertex, Boolean> vertexPower;
    private Thread thread;

    public FindHamiltonCicle(Graph graph){
        this.graph = graph;
        vertexes = graph.getVertices();
        vertexPower = new HashMap<>();
        markAllVertexesAsUnvisited();
        //thread = new Thread();
        if (graph.getArcs().get(0) != null) {
            eulerCycle();
            showAnswer();
        }
    }

    private void markAllVertexesAsUnvisited(){
        visited = new HashMap<>();
        for (Vertex vertex: vertexes)
            visited.put(vertex, false);
    }

    private void eulerCycle(){
        for (Vertex vertex: vertexes){
            if (!visited.get(vertex)){
                visited.put(vertex, true);
                List<Arc> outputEdges = outputEdges(vertex);
                List<Arc> inputEdges = inputEdges(vertex);
                boolean equalVertexPower = inputEdges.size() == outputEdges.size();
                vertexPower.put(vertex, equalVertexPower);
            }
        }
    }

    private List<Arc> outputEdges(Vertex vertex){
        List<Arc> outputEdges = new ArrayList<>();
        List<Arc> arcs = graph.getArcs();
        for (Arc arc : arcs) {
            boolean isOutputEdge = arc.getOutgoing().equals(vertex);
            if (isOutputEdge) {
                outputEdges.add(arc);
            }
        }
        return outputEdges;
    }

    private List<Arc> inputEdges (Vertex vertex){
        List<Arc> inputEdges = new ArrayList<>();
        List<Arc> arcs = graph.getArcs();
        for (Arc arc : arcs) {
            boolean isInputEdge = arc.getIngoing().equals(vertex);
            if (isInputEdge) {
                inputEdges.add(arc);
            }
        }
        return inputEdges;
    }

    private void showAnswer(){
        boolean isEuler = isEuler();
        if (isEuler){
            for (Vertex vertex : vertexes){
                vertex.chaneColorBlue();
            }
            JOptionPane.showMessageDialog(null, "Гамильтонов цикл найден!",
                    "Answer", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null, "Гамильтонова цикла нет.",
                    "Answer", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean isEuler(){
        for (Vertex vertex : vertexes){
            if (!vertexPower.get(vertex)){
                return false;
            }
        }
        return true;
    }

    /*private void colorizeVertexes(List<Vertex> vertexes){
        workingSpace.setLineColor(color);
        for (EdgeView edge : edges){
            workingSpace.drawEdge(edge);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                System.out.println("Exception in Euler Cycle graph algo");
            }
        }
    }*/
}
