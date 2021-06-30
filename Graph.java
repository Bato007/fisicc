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
    int startIn = 0, tempState = 0, passed;
    String caracter, aux, tempCaracter;
    Node tempNode;
    Edge tempEdge;
    boolean accepted, isFirst = true, canAdd = false, canContinue = false;

    // Se para el resto de estanos menos el final
    expressions.add(new Expression("#", 0));

    try {
      for (int i = 0; i < numbStates - 1; i++) { // --------------------- NODES
        tempNode = states.get(i);
        tempState = stateCount.get(startIn);
        passed = 0;

        // Las expresiones del array y en donde se hace toda la magia
        for (int j = startIn; j < expressions.size(); j++) { // ------- EXPRESION
          Expression expression = expressions.get(j);
          String data = expression.getValue();
          String[] noOrs = data.split("\\+");
          boolean isRecursive = expression.getRecursive();

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

              // Se verifica si no puede ir al anterior
              if (!isRecursive) {
                if (tempState < tempNode.getState()) {
                  continue;
                } else if (passed < 1) { // Cuando son locales
                  if (noOrs[k].length() == 1 || (isFirst && (h == (aux.length() - 1)))) {
                    canContinue = true;
                    break;
                  } else {
                    // Se mira si el siguiente es * o una letra
                    tempCaracter = String.valueOf(aux.charAt(h + 1));
                    if (!tempCaracter.equals("*")) {
                      tempEdge = new Edge(states.get(tempState), tempCaracter);
                      tempNode.addEdge(tempEdge);
                      break;
                    }
                  }
                } else if (passed >= 1) { // Si no es local
                  if (noOrs[k].length() == 1) {
                    canContinue = false;
                  }
                }
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

                isFirst = false;
                canAdd = true;
              } else {
                if (caracter.equals("*")) {
                  canAdd = true;
                  continue;
                } else {
                  canAdd = false;
                }
              }

              // Verificando si se agrega o no
              if (canAdd) {
                tempEdge = new Edge(states.get(tempState - 1), caracter);
                tempNode.addEdge(tempEdge);
                canAdd = false;
              }

            } // --------------------------------------- CARACTERES

            isFirst = true;
            if (canContinue) {
              tempState = stateCount.get(startIn + 1);
              break;
            }

          } // ---------------------------------------------SEPARADO

          passed++;
          if (!canContinue && !isRecursive) {
            break;
          }
          canContinue = false;

        } // ------------------------------------------------- EXPRESION

        if (tempNode.getState() == stateCount.get(startIn + 1))
          startIn++;

      } // ---------------------------------------------------------- NODES
    } catch (Exception e) {
      // TODO: handle exception
    }

  }

  public ArrayList<Node> getRelations() {
    return states;
  }

}
