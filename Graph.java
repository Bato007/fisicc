import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Graph {

  private String[] alphabet;
  private ArrayList<Expression> expressions;
  private HashMap<Integer, Integer> stateCount = new HashMap<>();
  private ArrayList<Node> states = new ArrayList<>();
  private Integer numbStates = 0;

  // Se encarga de obtener las variables
  public Graph(ArrayList<Expression> list, String[] glos) {
    String item;
    boolean contains;
    int count = 0;
    this.expressions = list;
    this.alphabet = glos;

    // Se obtiene el numero de estados que se tendran
    states.add(new Node(numbStates));
    this.numbStates++;
    for (int j = 0; j < expressions.size(); j++) {
      Expression expression = expressions.get(j);
      String value = expression.getValue();
      for (int i = 0; i < value.length(); i++) {
        item = String.valueOf(value.charAt(i));
        contains = Arrays.stream(alphabet).anyMatch(item::equals);

        if (contains) {
          states.add(new Node(numbStates));
          this.numbStates++;
          count++;
        }
      }
      stateCount.put(j, count);
    }
    states.add(new Node(numbStates));
    this.numbStates++;
  }

  // Se encarga en construir la lista encadenada
  public void relate() {
    // Variables a utilizar
    int temp = 0, startIn = 0, tempState = 0, index, tempAux;
    String item, value, aux;
    Edge tempEdge;
    boolean containsAlp, nextAllow = false, first = true;

    // -------------------------------------------------------------------
    // Se obtienen las del inicio
    for (int i = 0; i < expressions.size(); i++) {
      Expression expression = expressions.get(i);
      String data = expression.getValue();
      boolean isRecursive = expression.getRecursive();

      // Se separa para ver cuantos ors hay ['aa', 'b']
      String[] noOrs = data.split("\\+");
      for (String x : noOrs) {
        for (int j = 0; j < x.length(); j++) { // 'aa'
          item = String.valueOf(x.charAt(j)); // 'a'
          containsAlp = Arrays.stream(alphabet).anyMatch(item::equals);

          // Verificando si es un estado
          if (containsAlp) {
            temp++;
          }

          // Verificando si se agrega o no
          if (first) {
            if (isRecursive) { // Aqui se coloca lambda con su valor
              value = " " + item;
            } else {
              value = item;
            }

            tempEdge = new Edge(states.get(temp), value);
            states.get(0).addEdge(tempEdge);
            first = false;
          }

          // El anterior era un * agregando el estado
          if (nextAllow) {
            tempEdge = new Edge(states.get(temp), item);
            states.get(0).addEdge(tempEdge);
            nextAllow = false;
          }

          // El item es * puede pasar al siguiente estado o quedarse
          if (item.equals("*")) {
            states.get(0).addRecursiveLast();
            nextAllow = true;
          }

        }
        nextAllow = false;
        first = true;
      }

      // Tengo que verificar si es recursivo y si no ya no pasa
      if (!isRecursive) {
        break;
      }
    }

    // Se para el resto de estanos menos el final
    nextAllow = false;
    first = true;
    expressions.add(new Expression("#", 0));

    // ------------------------------------------------------------------
    for (int i = 1; i < numbStates - 1; i++) { // Los estados
      index = startIn - 1;
      index = (index < 0) ? 0 : index;
      tempState = stateCount.get(index) * startIn;
      if (tempState < 0) {
        tempState = 0;
      }
      tempAux = 0;

      // Las expresiones del array y en donde se hace toda la magia
      for (int j = startIn; j < expressions.size(); j++) {
        Expression expression = expressions.get(j);
        String data = expression.getValue();
        boolean isRecursive = expression.getRecursive();
        String[] noOrs = data.split("\\+");

        for (int k = 0; k < noOrs.length; k++) { // ['ab', 'b']
          aux = noOrs[k];

          for (int h = 0; h < aux.length(); h++) { // 'ab'
            item = String.valueOf(aux.charAt(h)); // 'a'
            containsAlp = Arrays.stream(alphabet).anyMatch(item::equals);

            // Verificando si es un estado
            if (containsAlp) {
              tempState++;
            }

            if (!isRecursive && (tempState < states.get(i).getState())) {
              continue;
            }

            // Verificando si se agrega o no
            if (first) {
              tempEdge = new Edge(states.get(tempState), item);
              states.get(i).addEdge(tempEdge);
              first = false;
            }

            // El anterior era un * agregando el estado
            if (nextAllow) {
              tempEdge = new Edge(states.get(tempState), item);
              states.get(0).addEdge(tempEdge);
              nextAllow = false;
            }

            // El item es * puede pasar al siguiente estado o quedarse
            if (item.equals("*")) {
              System.out.println(i);
              states.get(i).addRecursiveLast();
              nextAllow = true;
            }

            if (item.equals("#")) {
              tempEdge = new Edge(states.get(numbStates - 1), item);
              states.get(i).addEdge(tempEdge);
              states.get(i).makeEnd();
            }

          }
          nextAllow = false;
          first = true;

        }

        if (stateCount.get(startIn) == states.get(i).state) {
          startIn++;
        }

        if (noOrs[tempAux].indexOf("*") != -1) {
          System.out.println(i);
          continue;
        }

        if (!isRecursive) {
          break;
        }
      }
    }

    for (Node node : states) {
      System.out.println(node.getState());
      for (Edge edge : node.getEdges()) {
        System.out.print(edge.value);
        System.out.print(" |" + edge.finish.state + "|");
      }
      System.out.println();
    }
  }

  public ArrayList<Node> getRelations() {
    ArrayList<Node> temp = new ArrayList<>();
    Node aux0 = new Node(0);
    Node aux1 = new Node(1);
    Node aux2 = new Node(2);
    Node aux3 = new Node(3);
    Node aux4 = new Node(4);
    Node aux5 = new Node(5);
    Node aux6 = new Node(6);

    aux0.addEdge(new Edge(aux1, " a"));
    aux0.addEdge(new Edge(aux2, " b"));
    aux0.addEdge(new Edge(aux3, " a"));
    aux0.addEdge(new Edge(aux4, "b"));

    aux1.addEdge(new Edge(aux1, "a"));
    aux1.addEdge(new Edge(aux2, "b"));
    aux1.addEdge(new Edge(aux3, "a"));
    aux1.addEdge(new Edge(aux4, "b"));

    aux2.addEdge(new Edge(aux1, "a"));
    aux2.addEdge(new Edge(aux2, "b"));
    aux2.addEdge(new Edge(aux3, "a"));
    aux2.addEdge(new Edge(aux4, "b"));

    aux3.addEdge(new Edge(aux3, "a"));
    aux3.addEdge(new Edge(aux6, "#"));
    aux3.makeEnd();

    aux4.addEdge(new Edge(aux5, "b"));
    aux5.addEdge(new Edge(aux6, "#"));
    aux5.makeEnd();

    temp.add(aux0);
    temp.add(aux1);
    temp.add(aux2);
    temp.add(aux3);
    temp.add(aux4);
    temp.add(aux5);
    temp.add(aux6);

    return temp;
  }

}
