import java.util.ArrayList;
import java.util.Scanner;

public class Regexp {
  public static void main(String[] args) {
    // Argumentos que pasan de compilacion
    String path = args[0];
    String flag = args[1];
    String output = "", expression, message;

    Scanner input = new Scanner(System.in);
    String[] alphabet;
    ArrayList<Expression> list;
    Graph grafo;
    RegexpFunctions rgxFunctions;

    AFD afd;
    AFDmin afdMin;
    Gld gld;
    Eval eval;
    // Objeto para analizar el rgx
    if (flag.compareTo("-eval") != 0) {
      output = args[2];
    }

    // Leyendo el txt
    rgxFunctions = new RegexpFunctions();
    rgxFunctions.readTxt(path);
    list = rgxFunctions.getExpresion();
    alphabet = rgxFunctions.getAlfabeto();

    // Generando relaciones
    grafo = new Graph(list, alphabet);
    grafo.relate();

    switch (flag) {
      case "-afd":
        afd = new AFD(list, alphabet, grafo.getRelations(), output);
        break;
      case "-min":
        afd = new AFD(list, alphabet, grafo.getRelations(), output);
        afdMin = new AFDmin(afd.getFinalAFD(), alphabet, afd.getFinalStates(), afd.getMatrix(), output);
        break;
      case "-gld":
        gld = new Gld(list, grafo.getRelations(), alphabet);
        gld.makeText(output);
        break;
      case "-eval":
        eval = new Eval(list);
        while (true) {
          System.out.print("Ingrese la cadena a probar o 'exit' para salir: ");
          expression = input.nextLine();
          if (expression.compareTo("exit") == 0) {
            break;
          }
          message = (eval.validate(expression)) ? "Cadena valida\n" : "Cadena Invalida\n";
          System.out.println(message);
        }
        break;
      default:
        System.out.println("Operacion Invalida");
    }

  }
}