import java.util.*;
import java.io.*; 

public class RegexpFunctions {
	private String[] alfabeto;
	private ArrayList<Expression> splitExpression = new ArrayList<Expression>();
	
	// Se lee el txt .rgx
	public void readTxt() {
		try {
			Scanner r = new Scanner(new File("expresion.rgx")); 
			while (r.hasNextLine()) {
				String line = r.nextLine(); // Mientras hayan lineas por leer se meten a la variable line
				// line = converseExpression(line);

				if (line.contains(",")) { // Si la linea contiene una coma
					// Es el alfabeto
					alfabeto = line.split(","); 
				} else {
					separateExpression(line); // Es la expresion
					orderExpression(line); // Se ordena la expresion
					break;
				}
			}
			r.close();	
		} catch (Exception e) { // Se muestra la razon de error por la que no se encuentra el doc
			e.printStackTrace();
		}
	}

	/**
	 * Se analiza la expresion completa
	 * EJ. expression = ((a+b)*a)b -> (a+b) (a) b
	 * */
	private void separateExpression(String expression) {
		int actualOpenParenthesis;
		int actualCloseParenthesis;
		int childrenPos = 0;
		List<String> split_expression = new ArrayList<String>(Arrays.asList(expression.split(""))); //EJ. (,(,a,+,b,),*,a,),b

		// Se obtienen los parentesis mas adentrados de la operacion
		for (int i=split_expression.size()-1; i>=0; i--) {
			if (split_expression.get(i).equals("(")) { // Se encuentra el ultimo abierto
				actualOpenParenthesis = i;

				for (int j=i; j<split_expression.size(); j++) {
					if (split_expression.get(j).equals(")")) { // Se encuentra el primero cerrado
						actualCloseParenthesis = j;

						//Se toma en cuenta si tiene un operador * luego del parentesis
						try {
							if (split_expression.get(j+1).equals("*")) {
								actualCloseParenthesis = j+1;
							}
						} catch (Exception e){}

						// Se obtiene la expresion dentro de los parentesis
						String completeParenthesis = "";


						//Se toma en cuenta si tiene un operador + antes del parentesis
						try {
							if (split_expression.get(i-1).equals("+")) {
								completeParenthesis += "+";
							}
						} catch (Exception e){}

						for (int k=actualOpenParenthesis; k<=actualCloseParenthesis; k++) {
							completeParenthesis += split_expression.get(k);
						}


						// Creo un objeto tipo Expression
						//System.out.println("Entra con " + completeParenthesis + " y child " + childrenPos);
						if (!completeParenthesis.equals("()") && !completeParenthesis.equals("(++++)")) {
							try {
								// Si hay un * luego del parentesis
								if (split_expression.get(j+1).equals("*") || split_expression.get(j+1).equals(")")) {
									// Significa que la expresion tiene al menos un elemento adentro
									splitExpression.add(new Expression(completeParenthesis, childrenPos));
									childrenPos = 0;
								} else {
									// De lo contrario es 0
									splitExpression.add(new Expression(completeParenthesis, 0));
								}
							} catch (Exception e) {
								// De lo contrario es 0
								splitExpression.add(new Expression(completeParenthesis, 0));
								childrenPos = 0;
							}
						}

						childrenPos += 1;

						// Se elimina de manera inversa
						for (int k=actualCloseParenthesis; actualOpenParenthesis<=k; k--) {
							split_expression.remove(k);
						}

						break;
					}
				}


			}
		}

		// Se termina agregando el final de la expresion
		for (String expresionFija : split_expression) {
			if (!expresionFija.equals("") && !expresionFija.equals("+") &&
				!expresionFija.equals("(") && !expresionFija.equals(")")) {
				splitExpression.add(new Expression(expresionFija, 0));
			}
		}
	}

	// Ordenar la expresion
	private void orderExpression(String expression) {
		ArrayList<Integer> actualOrder = new ArrayList<Integer>(); 
		int startExpression;
		int finalExpression;

		// Se pasa elemento por elemento
		for (int i=0; i<splitExpression.size(); i++) {
			String expresionActual = splitExpression.get(i).getValue();
			String firstChar = String.valueOf(splitExpression.get(i).getValue().charAt(0));

			if (firstChar.equals("+")) {
				expresionActual = expresionActual.substring(1);
			} else {
				firstChar = "";
			}


			if (splitExpression.get(i).getRecursive()) {		
				// Se busca el comienzo y final de la expresion buscada si es recursiva
				startExpression = expression.indexOf(firstChar+"("+expresionActual+")*");
				finalExpression = startExpression+3+expresionActual.length()+firstChar.length();	
			} else {	
				// Se busca el comienzo y final de la expresion buscada sin recursividad
				startExpression = expression.indexOf(firstChar+"("+expresionActual+")");
				finalExpression = startExpression+2+expresionActual.length()+firstChar.length();	
			}

			if (startExpression == -1) {
				// Se busca el comienzo y final de la expresion buscada si no ha sido encontrada
				startExpression = expression.indexOf(firstChar+expresionActual+")*");
				finalExpression = startExpression+2+expresionActual.length()+firstChar.length();

				if (startExpression == -1) {
					// Se busca el comienzo y final de la expresion buscada si no ha sido encontrada
					startExpression = expression.indexOf(firstChar+expresionActual);
					finalExpression = startExpression+expresionActual.length()+firstChar.length();
				}
			}

			// Se agrega a actual order el index del comienzo de la expresion
			actualOrder.add(startExpression);

			// Se elimina del string la expresion encontrada actualmente
			String tempExpression = expression.substring(0, startExpression);
			for (int j=0; j<finalExpression-startExpression; j++) {
				tempExpression += ","; // Intercambiando sus letras por comas
			}
			// Se agrega el resto del string no afectado
			tempExpression += expression.substring(finalExpression, expression.length());
			expression = tempExpression; // La expresion actual es la intercambiada con comas
		}

		// Se crea un array ordenado de los indices encontrados
		ArrayList<Integer> orderedList = new ArrayList<>(actualOrder);
		ArrayList<Expression> tempExpressionArray = new ArrayList<>(splitExpression);
		Collections.sort(orderedList);

		// Se ordenan las expresiones del array dependiendo su orden
		for (int i=0; i<actualOrder.size(); i++) {
			int actualSearch = actualOrder.get(i); // Se obtiene el primer indice obtenido

			for (int j=0; j<orderedList.size(); j++) {
				if (orderedList.get(j) == actualSearch) { // Se obtiene el verdadero orden de la exp.
					tempExpressionArray.set(j, splitExpression.get(i)); // Intercambiamos valor por objeto

					break;
				}
			}
		}
		// Copiamos el array temporal al de expresiones
		Collections.copy(splitExpression, tempExpressionArray);
	}

	// Get del alfabeto
	public String[] getAlfabeto() {
		return alfabeto;
	}

	// Get de la expresion
	public ArrayList<Expression> getExpresion() {
		
    	for (int i=0; i<splitExpression.size(); i++) {
    		System.out.println(splitExpression.get(i).getValue() + " " +
    		splitExpression.get(i).getChildenPos() + " " +
    		splitExpression.get(i).getRecursive());
    	}
    	
    	return splitExpression;
	}
}