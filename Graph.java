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

    stateCount.put(0, 0);
    // Se obtiene el numero de estados que se tendran
    for (int j = 0; j < expressions.size(); j++) {
      Expression expression = expressions.get(j);
      String value = expression.getValue();
      for (int i = 0; i < value.length(); i++) {
        item = String.valueOf(value.charAt(i));
        contains = Arrays.stream(alphabet).anyMatch(item::equals);

        if (contains) {
          this.numbStates++;
          states.add(new Node(numbStates));
          count++;
        }
      }
      stateCount.put(j + 1, count);
    }

    this.numbStates++;
    states.add(new Node(numbStates));
  }

  // Se encarga en construir la lista encadenada
  public void relate() {
    // Variables a utilizar
    int startIn = 0, tempState = 0;
    String caracter, value, aux;
    Node tempNode;
    Edge tempEdge;
    boolean accepted, isFirst = true, canAdd = false;

    // Se para el resto de estanos menos el final
    expressions.add(new Expression("#", 0));

    for (int i = 0; i < numbStates - 1; i++) { // --------------------- NODES
      tempNode = states.get(i);
      tempState = 0;
      System.out.println(i);

      // Las expresiones del array y en donde se hace toda la magia
      for (int j = startIn; j < expressions.size(); j++) { // ------- EXPRESION
        System.out.println(stateCount.get(j));
        Expression expression = expressions.get(j);
        String data = expression.getValue();
        boolean isRecursive = expression.getRecursive();
        String[] noOrs = data.split("\\+");

        // ['ab', 'b']
        for (int k = 0; k < noOrs.length; k++) { // --------------- SEPARADO
          aux = noOrs[k];

          // 'ab'
          for (int h = 0; h < aux.length(); h++) { // ------------ CARACTERES
            caracter = String.valueOf(aux.charAt(h)); // 'a'
            accepted = Arrays.stream(alphabet).anyMatch(caracter::equals);

            // Verificando si es un estado
            if (accepted) {
              tempState++;
            }

            // Deja agregar
            if (isFirst) {

              // Si es el estado final se le coloca de uan
              if (caracter.equals("#")) {
                tempEdge = new Edge(states.get(numbStates - 1), caracter);
                tempNode.makeEnd();
                tempNode.addEdge(tempEdge);
                break;
              }

              canAdd = true;
            }

            // Verificando si se agrega o no
            if (canAdd) {
              tempEdge = new Edge(states.get(tempState - 1), caracter);
              tempNode.addEdge(tempEdge);
              canAdd = false;
            }

          } // --------------------------------------- CARACTERES

          isFirst = true;

        } // ---------------------------------------------SEPARADO

      } // ------------------------------------------------- EXPRESION

    } // ---------------------------------------------------------- NODES

    for (Node node : states) {
      System.out.println("-----------" + node.getState());
      for (Edge edge : node.getEdges()) {
        System.out.print(edge.value);
        System.out.println(" -> " + edge.finish.state + "");
      }
    }
  }

  public ArrayList<Node> getRelations() {
    ArrayList<Node> temp = new ArrayList<>();
    Node aux1 = new Node(1);
    Node aux2 = new Node(2);
    Node aux3 = new Node(3);
    Node aux4 = new Node(4);
    Node aux5 = new Node(5);
    Node aux6 = new Node(6);

    aux1.addEdge(new Edge(aux1, "a"));
    aux1.addEdge(new Edge(aux2, "b"));
    aux1.addEdge(new Edge(aux3, "a"));
    aux1.addEdge(new Edge(aux4, "b"));

    aux2.addEdge(new Edge(aux1, "a"));
    aux2.addEdge(new Edge(aux2, "b"));
    aux2.addEdge(new Edge(aux3, "a"));
    aux2.addEdge(new Edge(aux4, "b"));

    aux3.addEdge(new Edge(aux6, "#"));
    aux3.makeEnd();

    aux4.addEdge(new Edge(aux5, "b"));
    aux5.addEdge(new Edge(aux6, "#"));
    aux5.makeEnd();

    temp.add(aux1);
    temp.add(aux2);
    temp.add(aux3);
    temp.add(aux4);
    temp.add(aux5);
    temp.add(aux6);

    return temp;
  }

}
