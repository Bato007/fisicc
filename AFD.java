import java.util.*;
import java.util.Map.Entry;

public class AFD {
	// Tabla Pos, followPos
	HashMap<Integer, ArrayList<Integer>> matrix = new HashMap<Integer, ArrayList<Integer>>();
	// AFD
	HashMap<String, ArrayList<String>> finalADF = new HashMap<String, ArrayList<String>>();
	// Expression
	ArrayList<Expression> expressions;
	String[] alphabet;
	HashMap<Integer, String> expressionPositions = new HashMap<Integer, String>();
	// Estados
	HashMap<String, ArrayList<Integer>> finalStates = new HashMap<String, ArrayList<Integer>>();

	// Se crea la tabla segun el grafo
	public AFD(ArrayList<Expression> list, String[] alphabet, ArrayList<Node> grafo) {
		this.expressions = list;
		this.alphabet = alphabet;

		for (Node node : grafo ) {
			int pos = node.getState(); // Estado
			ArrayList<Integer> followPos = new ArrayList<Integer>();
			ArrayList<Edge> edges = new ArrayList<Edge>(node.getEdges());

			for (Edge edge : edges) {
				followPos.add(edge.getFinish().getState()); // Transiciones
			}

			matrix.put(pos, followPos); // Lo agregamos a la tablita
		}

		System.out.println(matrix);
		getIndexes();
		System.out.println(expressionPositions);
		analizeMatrix();
		System.out.println(finalStates);
	}

	// Colocamos los indices de la expresion
	private void getIndexes() {
		int pos = 1;
		// Se crea el diccionario de las posiciones de la expresion
		// Ej (a+b)*(a+bb) -> {1=a, 2=b, 3=a, 4=b, 5=b, 6=#}
		for (Expression exp : expressions) {
			String expression = exp.getValue();
			String[] splitExpression = expression.split(""); //EJ a+b -> a,+,b

			for (String letter : splitExpression) { // Por cada letra de la expresion
				for (String alphabetLetter : alphabet) { // Por cada letra del alfabeto
					if (letter.equals(alphabetLetter)) { // Si la letra pertenece al alfabeto
						expressionPositions.put(pos, alphabetLetter); // Se agrega con una pos
						pos += 1;
					}	
				}
			}
			expressionPositions.put(pos, "#"); // Final
		}
	}

	// Creamos los conjuntos
	private void analizeMatrix() {

		// Se crean los conjuntos
		int letter = 1;
		while (true) {
			String nombreActual = String.valueOf((char)(letter + 64)); // x cantidad de conjuntos
			ArrayList<ArrayList<Integer>> allTemp = new ArrayList<ArrayList<Integer>>();
			if (letter == 1) { // Es el estado inicial
				finalStates.put(nombreActual, matrix.get(letter));
				allTemp.add(matrix.get(letter));
			}

			// Revisamos letra por transicion para crear nuevos conjuntos
			for (ArrayList<Integer> estadosPorConjunto : finalStates.values()) { // EJ. [1, 2, 3, 4];
				// Evaluamos cada letra del abecedario EJ. a
				for (String letraAbc : alphabet) {
					ArrayList<Integer> temp = new ArrayList<Integer>(); // Arraylist temporal
					// Revisamos por cada posicion de la expresion si es la letra buscada  EJ. 1 = A, 3 = A
					for (Integer estado : estadosPorConjunto) { // EJ. 1, 2, 3, 4
						if (expressionPositions.get(estado).equals(letraAbc)) { // EJ. a = a
							// Se manda a llamar la lista del estado en la matriz EJ. 1=[1, 2, 3, 4] 3=[6]
							for (Integer estadoActual : matrix.get(estado)) {
								temp.add(estadoActual); // EJ. 1, 2, 3, 4, 6
							}
						}
					}

					// Se eliminan posibles estados duplicados
					Set<Integer> tempStates = new HashSet<>(temp);
					temp.clear();
					temp.addAll(tempStates);
					allTemp.add(temp);
				}
			}


			for (ArrayList<Integer> temp : allTemp) {
				for (String letraActual : alphabet) {
					// Evaluamos la cadena con la cadena inicial, la temporal y la letra actual
					if (evaluatePath(finalStates.get("A"), temp, letraActual)) {
						// Evaluamos si la cadena aceptada ya existe en estados finales
						// Si no existe, asignamos la letra y el valor del array
						if (!finalStates.containsValue(temp)) {
							System.out.println(temp);
							letter += 1;
							nombreActual = String.valueOf((char)(letter + 64)); // x cantidad de conjuntos	
							finalStates.put(nombreActual, temp);
						} 

						for(Entry<String, ArrayList<Integer>> entry: finalStates.entrySet()) {
							if (entry.getValue().equals(temp)) {
								String nombreAnterior = String.valueOf((char)(letter + 63));
								String caminoA = letraActual + entry.getKey();

								if (finalADF.containsKey(nombreAnterior)) {
									finalADF.get(nombreAnterior).add(caminoA);
								} else {
									ArrayList<String> temp2 = new ArrayList<String>();
									temp2.add(caminoA);
									finalADF.put(nombreAnterior, temp2);
								}
							}
						}

					}
				}
			}

			System.out.println(finalADF);
			break;
		}
	}

	// Evaluar la cuerda con el camino obtenido
	private Boolean evaluatePath(ArrayList<Integer> startingPath, ArrayList<Integer> path, String actualLetter) {
		ArrayList<Integer> temp2 = new ArrayList<Integer>(path);

		// El path inicial siempre será tomado en cuenta
		for (Integer alreadyEvaluated : startingPath ) {
			temp2.remove(alreadyEvaluated);
		}

		String cadena = "";
		// Evaluamos lo restante del path
		for (int i=0; i<temp2.size(); i++) {
			cadena += expressionPositions.get(temp2.get(i)); // Se obtiene la letra del estado
		}

		if (cadena.contains("#") || cadena.length() == 1 ) {
			// La cadena temporal es aceptada si llega al final
			// La cadena temporal es aceptada si solo tiene longitud 1
			try {
				if (cadena.substring(0, cadena.length() - 1).equals(actualLetter)) {
					return true;
	 			}
			} catch (Exception e) {}

			try {
				if (cadena.substring(0, cadena.length()).equals(actualLetter)) {
					return true;
	 			}
			} catch (Exception e) {}

			// La cadena termina con la letra del alfabeto una antes de ella
			// La cadena termina con la letra del alfabeto
			return true;
		} else {
			// Si la cadena no fue aceptada anteriormente, es false
			return false;
		}
	} 
}