public class Regexp {
  public static void main(String[] args) {
    // Argumentos que pasan de compilacion
    /*
    String path = args[0];
    String flag = args[1];
    String output = "";
    */
    // Objeto para analizar el rgx
    RegexpFunctions rgxFunctions = new RegexpFunctions();

    /*
    if (flag.compareTo("-eval") != 0) {
      output = args[2];
    }
    */

    rgxFunctions.readTxt();
    rgxFunctions.getExpresion();

    /*
    // Ahora se instancia
    try {
      String response = "";
      switch (flag) {
        case "-afd":
          Gld afd = new Gld(path);
          response = afd.operate(output);
          break;
        case "-gld":
          Gld gld = new Gld(path);
          response = gld.operate(output);
          break;
        case "-min":
          Gld min = new Gld(path);
          response = min.operate(output);
          break;
        default:
          Gld eval = new Gld(path);
          response = eval.operate(output);
          break;
      }

      System.out.println(response);
    } catch (Exception error) {
      System.out.println("Error inesperado");
    }
    */
  }
}