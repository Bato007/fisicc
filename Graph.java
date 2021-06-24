import java.util.ArrayList;
import java.util.Arrays;

public class Graph {

  private String[] alphabet;
  private ArrayList<Expression> expressions;
  private ArrayList<Node> states = new ArrayList<>();
  private Integer numbStates = 0;

  // Se encarga de obtener las variables
  public Graph(ArrayList<Expression> list, String[] glos) {
    String item;
    boolean contains;
    this.expressions = list;
    this.alphabet = glos;

    // Se obtiene el numero de estados que se tendran
    states.add(new Node(numbStates));
    this.numbStates++;
    for (Expression expression : expressions) {
      String value = expression.getValue();
      System.out.println(value);
      for (int i = 0; i < value.length(); i++) {
        item = String.valueOf(value.charAt(i));
        contains = Arrays.stream(alphabet).anyMatch(item::equals);

        if (contains) {
          states.add(new Node(numbStates));
          this.numbStates++;
        }
      }
    }
    states.add(new Node(numbStates));
  }

  // Se encarga en construir la lista encadenada
  public void relate() {
    // Variables a utilizar
    int temp = 0;
    String item, value;
    Edge tempEdge;
    boolean containsAlp, nextAllow = false, first = true;

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

    for (Edge edge : states.get(0).getEdges()) {
      System.out.print(edge.value);
      System.out.println(" " + edge.finish.state);
    }
  }

  private String[] separate(String convert) {
    String[] separated = new String[convert.length()];
    for (int i = 0; i < convert.length(); i++) {
      separated[i] = String.valueOf(convert.charAt(i));
    }
    return separated;
  }
}
