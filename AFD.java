import java.util.*;

public class AFD {
	// Tabla Pos, followPos
	HashMap<Integer, ArrayList<Integer>> matrix = new HashMap<Integer, ArrayList<Integer>>();
	// AFD
	HashMap<String, ArrayList<Integer>> finalADF = new HashMap<String, ArrayList<Integer>>();
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
		int pos = 0;
		// Se crea el diccionario de las posiciones de la expresion
		// Ej (a+b)*(a+bb) -> {0=a, 1=b, 2=a, 3=b, 4=b, 5=#}
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
			String nombreActual = String.valueOf((char)(letter + 64));

			if (letter == 1) { // Es el estado inicial
				finalStates.put(nombreActual, matrix.get(letter));
			}

			// Revisamos letra por transicion para crear nuevos conjuntos
			for (ArrayList<Integer> estadosPorConjunto : finalStates.values()) { // EJ. [1, 2, 3, 4]
				// Evaluamos cada letra del abecedario
				for (Integer letraAbc : expressionPositions.keySet()) {
					ArrayList<Integer> temp = new ArrayList<Integer>(); // Temporal
					// Revisamos cada transicion dentro de los estados
					for (int i=0; i<estadosPorConjunto.size(); i++) { 
						// Revisamos por cada posicion de la expresion si es la letra buscada			
						if (letraAbc == estadosPorConjunto.get(i)) {
							// Se manda a llamar de la matriz y se agrega al array temporal
							// temp.add(expressionPositions.get(posExpression));;
							System.out.println(matrix.get(letraAbc));
						}
					}



					// Se eliminan posibles estados duplicados
					Set<Integer> tempStates = new HashSet<>(temp);
					temp.clear();
					temp.addAll(tempStates);
				}
					/*
					// Se compara si el temporal es identico a algun conjunto anterior
					for (ArrayList<Integer> estadosPorConjunto : finalStates.values()) {
						if (estadosPorConjunto.equals(temp)) { // Si ya existe

						}
					}
					*/
			}

			break;
		}

	}
}