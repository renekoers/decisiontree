package main.java.nl.sogyo.javaopdrachten.decisiontree;

// Imports
import java.util.*;
import java.io.*;

// Class
public class Decisiontree{
  private File file = new File("D:/Opdrachten/java-opdrachten/src/main/resources/intermediate/decision-tree-data.txt");
  private Scanner scnrInput = new Scanner(System.in);
  private Node currentNode;
  private String userDecision;

  // Main method
  public static void main(String[] args) {
    Decisiontree decisiontree = new Decisiontree();
    decisiontree.filterTree();
    decisiontree.treeDecisionProcess();
  }

  // Reads every line of the file and splits each line into 2D Arraylist
  private void filterTree(){
    ArrayList<ArrayList<String>> edgeStringArrays = new ArrayList<>();
    HashMap<String, Node> nodeObjects = new HashMap<>();
    ArrayList<Edge> edgeObjects = new ArrayList<>();

      try {
        Scanner filescan = new Scanner(file);

        while(filescan.hasNextLine()){

        /* Read through file and add each line to the edge or node list as a
        String Array*/
        String line = filescan.nextLine();
        String[] lineList = line.split(", ");
          if (lineList.length > 2){
            edgeObjects.add(new Edge(lineList[2]));
            edgeStringArrays.add(new ArrayList<>(Arrays.asList(lineList)));
          } else {
            nodeObjects.put(lineList[0], new Node(lineList[0], lineList[1]));
          }
        }
      }
      catch (FileNotFoundException e) {
        e.printStackTrace();
    }

    // Make the decisiontree
    giveEdgesNodeObjects(edgeStringArrays, nodeObjects, edgeObjects);
    addEdgesToNodes(nodeObjects, edgeObjects);
    addNodesToEdges(nodeObjects, edgeObjects);
    getStartNode(nodeObjects, edgeObjects);
  }

  /* If the origin of two edges is the same, make Edge objects, put them into a
  list and add it to a hashmap with their origin key.*/
  private void giveEdgesNodeObjects(ArrayList<ArrayList<String>> edgeStringArrays, HashMap<String, Node> nodeObjects, ArrayList<Edge> edgeObjects){
    for (int i = 0; i < edgeObjects.size(); i++){
      edgeObjects.get(i).originNode = nodeObjects.get(edgeStringArrays.get(i).get(0));
      edgeObjects.get(i).targetNode = nodeObjects.get(edgeStringArrays.get(i).get(1));
    }
  }

  // Adds edges to all nodes
  private void addEdgesToNodes(HashMap<String, Node> nodeObjects, ArrayList<Edge> edgeObjects){
    for (Node node : nodeObjects.values()){
      ArrayList<Edge> nodeEdgeList = new ArrayList<>();
      for (Edge edge : edgeObjects){
        if (edge.originNode.nodeKey.equals(node.nodeKey)){
          nodeEdgeList.add(edge);
        }
      }

      node.edges = nodeEdgeList;
    }
  }

  // Adds nodes to all edges
  private void addNodesToEdges(HashMap<String, Node> nodeObjects, ArrayList<Edge> edgeObjects){
    for (Edge edge : edgeObjects){
      for (Node node : nodeObjects.values()){
        if (edge.targetNode.nodeKey.equals(node.nodeKey)){
          edge.targetNode = node;
        }
        if (edge.originNode.nodeKey.equals(node.nodeKey)){
          edge.originNode = node;
        }
      }
    }
  }

  // Finds the first node to start from
  private void getStartNode(HashMap<String, Node> nodeObjects, ArrayList<Edge> edgeObjects){

    for (Node node : nodeObjects.values()){
      boolean check = false;
      for (Edge edge : edgeObjects){
        if (edge.targetNode.equals(node)){
          check = true;
          break;
        }
      }
      if (!check){
        currentNode = node;
      }
    }
  }

    // Prints out the question the user needs to answer and asks for input.
    private void treeDecisionProcess(){
      boolean answerGot = false;
      System.out.println("Dit is een bomen decisiontree! Typ in \"Ja\" of \"Nee\" om antwoord te geven op de vragen.");
      while(!answerGot){
        System.out.println(currentNode.nodeText);
        userDecision = userInput();
        getNextNode();
        answerGot = checkIfNoEdges();
      }

      System.out.println("Dit blad is van een: " + currentNode.nodeText);
      System.out.println("Wil je een nieuw blad uitzoeken? (Ja / Nee)");
      userAgain();
    }

  // Get user input
  private String userInput(){
    String input = scnrInput.nextLine();
    input = input.substring(0, 1).toUpperCase() + input.substring(1);
    if(input.equals("Ja") || input.equals("Nee")){
      return input;
    } else {
      System.out.println("Typ alleen \"Ja\" of \"Nee\" in. ");
      userInput();
    }
    return input;
  }

  // Get next node
  private void getNextNode(){
    for (Edge edge : currentNode.edges){
      if (userDecision.equals(edge.decision)){
        currentNode = edge.targetNode;
      }
    }
  }

  // Checks if user is on an answer node
  private boolean checkIfNoEdges(){
    boolean check;
    check = currentNode.edges.isEmpty();
    return check;
  }

  // Asks the user if they would like to try again
  private void userAgain(){
    String input = scnrInput.nextLine();
    input = input.substring(0, 1).toUpperCase() + input.substring(1);
    if(input.equals("Ja")){
      filterTree();
      treeDecisionProcess();
    } else if (input.equals("Nee")){
      System.out.println("Tot de volgende keer!");  // Exits program
    } else {
      System.out.println("Typ alleen \"Ja\" of \"Nee\" in. ");
      userAgain();
    }
  }
}


  // Node Class
  class Node{
    String nodeKey;
    String nodeText;
    ArrayList<Edge> edges;

    Node(String nodeKey, String nodeText){
      this.nodeKey = nodeKey;
      this.nodeText = nodeText;
      this.edges = new ArrayList<>();
    }
}


  // Edge Class
  class Edge{
    Node targetNode;
    Node originNode;
    String decision;

    Edge(String decision){
      this.targetNode = new Node(null, null);
      this.originNode = new Node(null, null);
      this.decision = decision;
    }
}
