import java.util.ArrayList;

public class Eval {

  private ArrayList<String> valid = new ArrayList<>();

  public Eval(ArrayList<Expression> expressions) {
    String value;
    Expression expression;
    int startIndex = 0, endIndex;
    boolean isRecursive, hasRecursive = true;
    for (int i = 0; i < expressions.size() - 1; i++) {
      expression = expressions.get(i);
      value = expression.getValue();
      isRecursive = expression.getRecursive();

      if (isRecursive) {
        valid.add("#");
      } else {
        while (hasRecursive) {
          endIndex = value.indexOf("*");
          if (endIndex == -1) {
            hasRecursive = false;
            continue;
          }

          value = value.substring(startIndex, endIndex - 1) + value.substring(endIndex);
          startIndex = endIndex - 1;

          value = value.replace("*", "#");
        }
        valid.add(value);
      }
    }
  }

  public boolean validate(String validate) {
    String arrChar, temp1, temp2;
    String[] arr, recurs;
    boolean isValid = false, infinite = false, added = false;
    int valIndex = 0, index = 0, startIndex = 0;
    int length = this.valid.size(), tempIndex = 0;

    while (true) {
      arr = valid.get(valIndex).split("\\+"); // Obtetngo ['a', '#b']

      if (arr.length == index) { // La cadena no es valida
        break;
      }

      arrChar = arr[index]; // Obtengo '#b'

      if (arrChar.contains("#")) { // Verifico que sea recursiva
        tempIndex = arrChar.lastIndexOf("#");
        if ((tempIndex + 1) == arrChar.length() && (valIndex + 1) == length) {
          infinite = true;
        }

        if (arr.length == 1) {
          index = 0;
          valIndex++;
          added = true;
        }
      }

      // Se verifica si hay opciones todavia
      recurs = arrChar.split("#");

      // Ahora se verifica si se encuentran en la expresion
      tempIndex = -1;
      for (int i = 0; i < recurs.length; i++) {
        tempIndex = validate.indexOf(recurs[i], startIndex);
        if (tempIndex == -1) {
          index++;
          added = true;
          break;
        } else {
          startIndex = tempIndex;
        }
      }

      if (length == (valIndex + 1) && !infinite && !added) {
        temp1 = recurs[recurs.length - 1];
        temp2 = validate.substring(validate.length() - temp1.length());

        if (!temp1.equals(temp2)) {
          index++;
          continue;
        }
      }

      added = false;
      if (tempIndex != -1) {
        index = 0;
        valIndex++;
      }
      // Es porque paso todas las pruebas
      if (length == valIndex) {
        if (infinite) {
          isValid = true;
        } else {
          temp1 = recurs[recurs.length - 1];
          temp2 = validate.substring(validate.length() - temp1.length());
          if (temp1.equals(temp2)) {
            isValid = true;
          }
        }
        break;
      }

    }

    return isValid;
  }

}
