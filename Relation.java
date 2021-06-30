public class Relation {

  private String before, state, transition;
  private boolean recursive, end;

  public Relation(String before, String state, String transition, boolean recursive, boolean end) {
    this.before = before;
    this.state = state;
    this.transition = transition;
    this.recursive = recursive;
    this.end = end;
  }

  public void makeRecursive() {
    this.recursive = true;
  }

  public void makeEnd() {
    this.end = true;
  }

  public String getBefore() {
    return this.before;
  }

  public String getState() {
    return this.state;
  }

  public String getTransition() {
    return this.transition;
  }

  public boolean isEnd() {
    return this.end;
  }

  public boolean isRecursive() {
    return this.recursive;
  }

}
