package com.dopTask;

import com.model.Arc;
import com.model.Graph;
import com.model.Vertex;

import javax.swing.*;
import java.util.*;

public class EulerCycle {
    private Graph graph;
    private List<Vertex> vertexes;
    private Map<Vertex, Boolean> visited;
    private Map<Vertex, Boolean> vertexPower;
    private Thread thread;

    public EulerCycle(Graph graph){
        this.graph = graph;
        vertexes = graph.getVertices();
        vertexPower = new HashMap<>();
        markAllVertexesAsUnvisited();
        //thread = new Thread();
        if (graph.getArcs().get(0) != null) {
            eulerCycle();
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

    private void colorizeEdges(List<Arc> edges){
        for (Arc edge : edges){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                System.out.println("Exception in Euler Cycle graph algo");
            }
        }
    }

    public void callCheckEulerCycle(){
        try {
            thread.start();
        }
        catch (IllegalThreadStateException exception){
            System.out.println("new thread exception from Euler Cycle");
            thread.interrupt();
            thread = new Thread();
            thread.start();
        }
    }


    public void run() {
        if (graph.getArcs().get(0) != null) {
            eulerCycle();
        }
    }

    private void showAnswer(){
        boolean isEuler = isEuler();
        if (isEuler){
            JOptionPane.showMessageDialog(null, "Это эйлеров граф",
                    "Answer", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(null, "Это не эйлеров граф",
                    "Answer", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public boolean isEuler(){
        for (Vertex vertex : vertexes){
            if (!vertexPower.get(vertex)){
                return false;
            }
        }
        return true;
    }

    /*public void run(){
        for (int iterator = 0; iterator<graph.getVertices().size(); iterator++){
            visited.add(false);
        }


        boolean check = true
        MessageBox messageBox = new MessageBox(mainWindow, SWT.OK);
        messageBox.setText("Является ли эйлеровым");
        if(check){
            messageBox.setMessage("Граф эйлеров");
        }
        else
            messageBox.setMessage("Граф не эйлеровый.");

        messageBox.open();

    }*/
}
