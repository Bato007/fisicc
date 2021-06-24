import java.util.ArrayList;

public class Node {

  protected int state;
  protected boolean isEnd = false;
  protected ArrayList<Edge> edges = new ArrayList<>();

  public Node(int state) {
    this.state = state;
  }

  public void addEdge(Edge edge) {
    this.edges.add(edge);
  }

  public void addRecursiveLast() {
    int index = edges.size() - 1;
    edges.get(index).makeRecursive();
  }

  public int getState() {
    return this.state;
  }

  public ArrayList<Edge> getEdges() {
    return this.edges;
  }

  public boolean isEnd() {
    return this.isEnd;
  }

  public void makeEnd() {
    this.isEnd = true;
  }

}