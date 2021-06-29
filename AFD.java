import java.util.*;
import java.util.Map.Entry;
import java.io.FileWriter;   
import java.io.IOException;

public class AFD {
	// Tabla Pos, followPos
	private HashMap<Integer, ArrayList<Integer>> matrix = new HashMap<Integer, ArrayList<Integer>>();
	// AFD
	private HashMap<String, ArrayList<String>> finalADF = new HashMap<String, ArrayList<String>>();
	// Expression
	private ArrayList<Expression> expressions;
	private String[] alphabet;
	private HashMap<Integer, String> expressionPositions = new HashMap<Integer, String>();
	Eval eval;
	// Estados
	private HashMap<String, ArrayList<Integer>> finalStates = new HashMap<String, ArrayList<Integer>>();
	// Letra
	private ArrayList<String> letrasActuales = new ArrayList<String>();
	private int letter = 0;
	private int iteracion = 0;

	// Se crea la tabla segun el grafo
	public AFD(ArrayList<Expression> list, String[] alphabet, ArrayList<Node> grafo, String nombreTxt) {
		this.eval = new Eval(list);
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

		getIndexes();
		analizeMatrix();
		generarTxt(nombreTxt);
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
		letter = 1;
		while (true) {
			String nombreActual = String.valueOf((char)(letter + 64)); // x cantidad de conjuntos
			ArrayList<ArrayList<Integer>> allTemp = new ArrayList<ArrayList<Integer>>();
			if (letter == 1) { // Es el estado inicial
				finalStates.put(nombreActual, matrix.get(letter));
				allTemp.add(matrix.get(letter));
			}

			// Se sacan los caminos a probar del path inicial
			ArrayList<ArrayList<Integer>> temp3 = new ArrayList<ArrayList<Integer>>(finalStates.values());
			ArrayList<ArrayList<Integer>> allTemp2 = actualPath(temp3);
			for (ArrayList<Integer> temp2 : allTemp2) {
				allTemp.add(temp2);
			}

			// Se sacan el resto de paths a partir del path inicial
			recursivePath(allTemp);

			// Se eliminan posibles estados duplicados
			Set<ArrayList<Integer>> tempStates = new HashSet<>(allTemp);
			allTemp.clear();
			allTemp.addAll(tempStates);
			
			break;
		}
	}

	// Obtener todos los paths
	private void recursivePath(ArrayList<ArrayList<Integer>> actual) {
		int iteracionTemporal = 0;
		ArrayList<String> letrasIteradas = new ArrayList<String>();
		ArrayList<ArrayList<Integer>> tempRecursive = new ArrayList<ArrayList<Integer>>();
		
		// Obtenemos todas las letras hasta la actualidad
		while (iteracionTemporal < iteracion) {
			String letra = String.valueOf((char)(iteracionTemporal + 1 + 64));
			letrasIteradas.add(letra);
			iteracionTemporal+=1;
		}

		// Revisamos si la letra ya ha sido testeada
		for (String nombreConjunto : finalStates.keySet()) {	
			// EJ. A en la 1era iteracion, A, B, C en la segunda, etc
			if (!letrasIteradas.contains(nombreConjunto)) {
				tempRecursive.add(finalStates.get(nombreConjunto));
			}
		}
		
		// Se realiza recursividad hasta que ya no queden letras nuevas
		if (tempRecursive.size() != 0) {
			actual = actualPath(tempRecursive);
			recursivePath(actual);
		}
	}

	private ArrayList<ArrayList<Integer>> actualPath( ArrayList<ArrayList<Integer>> actual) {
		ArrayList<ArrayList<Integer>> allTemp = new ArrayList<ArrayList<Integer>>();
		Boolean yaExiste=false;
		// Revisamos letra por transicion para crear nuevos conjuntos
		for (ArrayList<Integer> estadosPorConjunto : actual) { // EJ. [1, 2, 3, 4];
			iteracion += 1;
			String nombreActual = String.valueOf((char)(iteracion + 64));
			// Evaluamos cada letra del abecedario EJ. a
			for (String letraAbc : alphabet) {
				ArrayList<Integer> temp = new ArrayList<Integer>(); // Arraylist temporal
				
				// Revisamos por cada posicion de la expresion si es la letra buscada  EJ. 1 = A, 3 = A
				for (Integer estado : estadosPorConjunto) { // EJ. 1, 2, 3, 4
					if (expressionPositions.get(estado).equals(letraAbc) || expressionPositions.get(estado).equals("#")) { // EJ. a = a
						// Se manda a llamar la lista del estado en la matriz EJ. 1=[1, 2, 3, 4] 3=[6]
						for (Integer estadoActual : matrix.get(estado)) {
							temp.add(estadoActual); // EJ. 1, 2, 3, 4, 6
						}
					}
				}

				if (evaluatePath(finalStates.get("A"), temp, letraAbc)) {
					// Evaluamos si la cadena aceptada ya existe en estados finales
					// Si no existe, asignamos la letra y el valor del array
					if (!finalStates.containsValue(temp)) {
						letter += 1;
						String nombreActualTemp = String.valueOf((char)(letter + 64)); // x cantidad de conjuntos	
						finalStates.put(nombreActualTemp, temp);
					}
				
					// Se busca la llave del path que corresponde
					for(Entry<String, ArrayList<Integer>> entry: finalStates.entrySet()) {
						if (entry.getValue().equals(temp)) { // Si es igual al temporal
							String caminoA = letraAbc + entry.getKey(); // Se obtiene la letra del alfabeto + llave EJ. aB 

							if (finalADF.containsKey(nombreActual)) { // Si existe
								finalADF.get(nombreActual).add(caminoA); // Se agrega
							} else {
								ArrayList<String> temp2 = new ArrayList<String>(); // Si no existe
								temp2.add(caminoA);
								finalADF.put(nombreActual, temp2); // Se crea
							}

							break;
						}
					}
				}
				allTemp.add(temp);
			}

			// Se eliminan posibles estados duplicados
			Set<ArrayList<Integer>> tempStates = new HashSet<>(allTemp);
			allTemp.clear();
			allTemp.addAll(tempStates);
		}

		return allTemp;
	}

	// Evaluar la cuerda con el camino obtenido
	private Boolean evaluatePath(ArrayList<Integer> startingPath, ArrayList<Integer> path, String actualLetter) {
		// System.out.println("Path " + path);
		String cadena = "";
		// Por cada expresion
		int posActual = 0;
		for (Expression expression : expressions) {
			// Si la expresion es recursiva, se ignora por completo pero se toma su posicion
			if (expression.getRecursive()) {
				for (char ch: expression.getValue().toCharArray()) {	
					String letraEnExpresion = Character.toString(ch);			
					if (!letraEnExpresion.equals("+")) {
						posActual++;
					}
				}
			} else if (expression.getValue().contains("+")) {
				// Si hay un + en la expresion, se toma solo una de la letras de a los lados EJ. a+bb
				// Se parte la expresion antes del signo y luego del signo
				int i = expression.getValue().indexOf("+");
				String ladoIzq = expression.getValue().substring(0, i); // EJ. a
				String ladoDer = expression.getValue().substring(i+1, expression.getValue().length()); //EJ. bb
				
				// Si la ultima letra es igual a la buscada actualmente
				String lastLetter = Character.toString(ladoIzq.charAt(ladoIzq.length()-1));
				if (lastLetter.equals(actualLetter)) {
					cadena += ladoIzq;
					posActual += ladoIzq.length();
				} else {
					cadena += ladoDer;
					posActual += ladoDer.length();
				}				
			} else if (!expression.getValue().equals("#")){
				// Es obligatorio en la cadena
				cadena += expression.getValue(); 
			}
		}

		if (eval.validate(cadena)) {
			return true;
		} else {
			return false;
		}
	} 

	// Generar txt
	private void generarTxt(String nombreTxt) {
		try {
			/*
			System.out.println(finalADF);
			System.out.println(matrix);
			System.out.println(finalStates);
			*/
			
			// Se crea el archivo
			FileWriter myWriter = new FileWriter(nombreTxt);
		
			String alfabeto = String.join(",", alphabet); // Se une el alfabeto con una ,
			String estados = "\n"+(finalADF.size()+1); // Se obtiene la cantidad de estados (tamano del final + 1 por el cero)

			myWriter.write(alfabeto); // Alfabeto
			myWriter.write(estados); // Cantidad de estados

			// El estado final es el que contenga el #
			String estadoFinal = "\n";
			for (String finales : finalStates.keySet()) {
				if (finalStates.get(finales).contains(matrix.size())) {
					int pos =  (int)finales.charAt(0)-64;
					estadoFinal += pos + ",";
				}
			}
			estadoFinal = estadoFinal.substring(0, estadoFinal.length() - 1); // Remuevo ultima coma
			myWriter.write(estadoFinal); // Estado final

			// Escribimos las transiciones
			for (int i=0; i<alphabet.length; i++) {			
				String transiciones = "\n0,";
				for (String finales : finalADF.keySet()) {
					// Se convierte de letra a num
					String letter = finalADF.get(finales).get(i); 
					int pos =  (int)letter.charAt(1)-64;
					transiciones += pos + ",";
				}
				transiciones = transiciones.substring(0, transiciones.length() - 1); // Remuevo ultima coma
				myWriter.write(transiciones);
			}

			myWriter.close(); // Se cierra
		} catch (IOException e) {
      		e.printStackTrace(); // Por si hay error
    	}
	}

	// GETTERS
	public HashMap<String, ArrayList<String>> getFinalAFD() {
		return finalADF;
	}

	public HashMap<String, ArrayList<Integer>> getFinalStates() {
		return finalStates;
	}

	public HashMap<Integer, ArrayList<Integer>> getMatrix() {
		return matrix;
	}
}
