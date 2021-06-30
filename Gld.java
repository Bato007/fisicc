import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.IOException;

public class GLD {

  private ArrayList<Relation> relations = new ArrayList<>();
  private ArrayList<String> notTerminal = new ArrayList<>();
  private String[] arbitrary = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" };
  private String[] alphabet;
  private int arbIndex = 0;

  public GLD(ArrayList<Expression> expressions, ArrayList<Node> nodes, String[] alphabet) {
    String value, caracter;
    Expression expression;
    Relation tempRelation;
    int nodeIndex = 0, tempIndex;
    boolean isRecursive, isEnd, contains;
    this.alphabet = alphabet;

    for (int i = 0; i < expressions.size() - 1; i++) {
      expression = expressions.get(i);
      value = expression.getValue();
      isRecursive = expression.getRecursive();

      for (int j = 0; j < value.length(); j++) {
        caracter = String.valueOf(value.charAt(j));
        contains = Arrays.stream(alphabet).anyMatch(caracter::equals); // Verificando en el alfabeto
        if (contains) { // Si es una a o b
          tempIndex = ((arbIndex - 1) < 0) ? arbIndex : arbIndex - 1;
          isEnd = nodes.get(nodeIndex).isEnd();
          tempRelation = new Relation(arbitrary[tempIndex], arbitrary[arbIndex], caracter, isRecursive, isEnd);
          relations.add(tempRelation);
          arbIndex = (isRecursive) ? arbIndex : arbIndex + 1;
          nodeIndex++; // Se le sube al nodo porque es estado

        } else if (caracter.equals("*")) { // Si es un + o *
          relations.get(relations.size() - 1).makeRecursive();
          arbIndex--;
        }
      }
    }
  }

  public void makeText(String txtName) {
    String noTerminal = "", initial, productionRules = "";

    for (Relation relation : relations) {
      if (!notTerminal.contains(relation.getBefore())) {
        notTerminal.add(relation.getBefore());
      }
    }

    for (Relation relation : relations) {
      if (!notTerminal.contains(relation.getState()) && !relation.isEnd()) {
        notTerminal.add(relation.getBefore());
      }
    }

    for (int i = 0; i < notTerminal.size(); i++) { // Obteniendo no terminales
      noTerminal += notTerminal.get(i);
      if (!(i == notTerminal.size() - 1)) {
        noTerminal += ",";
      } else {
        noTerminal += "\n";
      }
    }

    String alfabeto = String.join(",", this.alphabet) + "\n"; // Se une el alfabeto con una ,
    initial = arbitrary[0] + "\n"; //

    for (Relation relation : relations) {
      productionRules += relation.getBefore() + "->" + relation.getTransition();

      if (relation.isEnd()) {
        productionRules += "\n";
      } else {
        productionRules += relation.getState() + "\n";
      }
    }

    try {
      // Se crea el archivo
      FileWriter myWriter = new FileWriter(txtName);

      myWriter.write(noTerminal);
      myWriter.write(alfabeto);
      myWriter.write(initial);
      myWriter.write(productionRules);

      myWriter.close(); // Se cierra
    } catch (IOException e) {
      e.printStackTrace(); // Por si hay error
    }
  }

}
