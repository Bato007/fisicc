public class Edge {
  protected Node finish;
  protected String value;

  public Edge(Node finish, String value) {
    this.finish = finish;
    this.value = value;
  }

  public void makeRecursive() {
    String temp = String.valueOf(value.charAt(0));
    if (!temp.equals(" ")) {
      this.value = " " + this.value;
    }
  }

  public Node getFinish() {
    return this.finish;
  }

  public String getValue() {
    return this.value;
  }
}
