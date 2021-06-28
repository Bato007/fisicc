import java.util.*;
import java.util.Map.Entry;
import java.io.FileWriter;   
import java.io.IOException;

public class AFDmin {
	private String[] alphabet;
	// Tabla Pos, followPos
	private HashMap<Integer, ArrayList<Integer>> matrix = new HashMap<Integer, ArrayList<Integer>>();
	// Tabla estado -> a, b
	private HashMap<String, ArrayList<Integer>> matrixAFDmin = new HashMap<String, ArrayList<Integer>>();
	// AFD
	private HashMap<String, ArrayList<String>> finalAFD = new HashMap<String, ArrayList<String>>();
	// Estados
	private HashMap<String, ArrayList<Integer>> finalStatesAFD = new HashMap<String, ArrayList<Integer>>();
	// Particiones generadas EJ. 1: {[A, B]}, 2: {[C, D]} Donde el ultimo siempre seran los finales
	private HashMap<Integer, ArrayList<String>> particiones = new HashMap<Integer, ArrayList<String>>();


	public AFDmin(HashMap<String, ArrayList<String>> finalAFD2, String[] alphabet,
		HashMap<String, ArrayList<Integer>> finalStatesAFD2, HashMap<Integer, ArrayList<Integer>> matrix) {
		/*
		this.finalStatesAFD = finalStatesAFD2;
		this.finalAFD = finalAFD2;
		this.matrix = matrix;
		*/
		this.alphabet = alphabet;

		////EN LO QUE FUNCIONA LO DE VERIFICAR CADENAS////
		ArrayList<String> paths = new ArrayList<>(Arrays.asList("aB", "bC"));
		this.finalAFD.put("A", paths);
		this.finalAFD.put("B", paths);
		paths = new ArrayList<>(Arrays.asList("aB", "bB"));
		this.finalAFD.put("C", paths);

		// A: 1, 2, 3, 4 B: 1, 2, 3, 4, 6 C: 1, 2, 3, 4, 5
		ArrayList<Integer> finalStateTemp = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
		this.finalStatesAFD.put("A", finalStateTemp);
		finalStateTemp = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 6));
		this.finalStatesAFD.put("B", finalStateTemp);
		finalStateTemp = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		this.finalStatesAFD.put("C", finalStateTemp);
		//////////////////////////////////////////////////

		ArrayList<String> finalStates = new ArrayList<String>();
		ArrayList<String> noFinalStates = new ArrayList<String>();
		// Separo finales y no finales
		for (String posibleFinal : finalStatesAFD.keySet()) {
			// El numero del estado final es el tamano de la matriz + 1
			// if (finalStatesAFD.get(posibleFinal).contains(matrix.size())) {
			if (finalStatesAFD.get(posibleFinal).contains(6)) {
				finalStates.add(posibleFinal);
			} else {
				noFinalStates.add(posibleFinal);
			}
		}

		particiones.put(1, noFinalStates); // Estados no-finales
		particiones.put(2, finalStates); // Estados finales

		generateMatrix(); // Generamos matriz
		recursivePartition(0); // Generamos particiones
		generateMatrix(); // Generamos matriz
		generarTxt("pruebaMin.txt");
	}

	// Se genera la matriz de numeros EJ. A=[2, 1], B=[2, 1], C=[2, 2]
	private void generateMatrix() {
		// Obtenemos los conjuntos del AFD
		for (String conjuntoAFD : finalAFD.keySet()) {
			// Array temporal donde la posicion 0 -> a y 1 -> b
			ArrayList<Integer> temp = new ArrayList<Integer>();

			// Por cada conjunto revisamos hacia donde va a y b
			for (String elemento : finalAFD.get(conjuntoAFD)) {
				String goesTo = elemento.substring(1); // Obtenemos hacia donde va

				for (Integer name : particiones.keySet()) { // Revisamos cual de las particiones lo contiene
					if (particiones.get(name).contains(goesTo)) {
						temp.add(name); // Agregamos al temp el nombre de la particion
					}
				}
			}

			matrixAFDmin.put(conjuntoAFD, temp);
		}
	}

	// Genera todas las posibles particiones
	private void recursivePartition(int cantidadNuevos) {
		HashMap<Integer, ArrayList<String>> copiaParticiones = new HashMap<Integer, ArrayList<String>>();
		ArrayList<String> copiaFinales = new ArrayList<String>();

		for (String particionPosible : matrixAFDmin.keySet()) { // EJ. A=[2, 1]
			boolean existe = false;

			for (Integer particionExistente : copiaParticiones.keySet()) { // EJ 1 = [A, C]
				for (String posibleExistente : copiaParticiones.get(particionExistente)) { // EJ. A, C
					// Se compara si ya existe el valor dentro de alguna particion
					// EJ. A = [2, 1] y C = [2, 1]; pero son distintas letras
					// Se toma en cuenta que no-finales y finales no se pueden unir
					if (matrixAFDmin.get(posibleExistente).equals(matrixAFDmin.get(particionPosible)) &&
						particiones.get(particiones.size()).contains(posibleExistente)) {
						copiaParticiones.get(particionExistente).add(particionPosible);
						existe = true;
						break;
					}
				}
			}
	
			// Si no existe, se debe de crear, se verifica si es el final
			if (!existe && !(particiones.get(particiones.size()).contains(particionPosible))) {
				// Se crea un nuevo valor en el diccionario particiones
				ArrayList<String> temporal = new ArrayList<String>();
				temporal.add(particionPosible);
				cantidadNuevos += 1;
				copiaParticiones.put(cantidadNuevos, temporal);
			}	
			// Si es el final, se agrega a la copia y se deja de ultimo
			if (particiones.get(particiones.size()).contains(particionPosible)) {
				copiaFinales.add(particionPosible);
			}
		}

		copiaParticiones.put(cantidadNuevos+1, copiaFinales);

		// Se crean particiones de manera recursiva hasta que sean distintas al pasado
		if (!copiaParticiones.equals(particiones)) {
			particiones = copiaParticiones;
			recursivePartition(0);
		}
	}

	// Genera el txt
	private void generarTxt(String nombreTxt) {
		try {
			// Se crea el archivo
			FileWriter myWriter = new FileWriter(nombreTxt);
		
			String alfabeto = String.join(",", alphabet); // Se une el alfabeto con una ,
			String estados = "\n"+(particiones.size()+1); // Se obtiene la cantidad de estados (tamano de las particiones + 1 por el cero)

			myWriter.write(alfabeto); // Alfabeto
			myWriter.write(estados); // Cantidad de estados

			// El estado final es el final de las particiones
			String estadoFinal = "\n";
			for (String finales : particiones.get(particiones.size())) {
				int pos =  (int)finales.charAt(0)-64;
				estadoFinal += pos + ",";
			}
			estadoFinal = estadoFinal.substring(0, estadoFinal.length() - 1); // Remuevo ultima coma
			myWriter.write(estadoFinal); // Estado final

			// Escribimos las transiciones
			for (int i=0; i<alphabet.length; i++) {			
				String transiciones = "\n0,";
				for (Integer numParticion : particiones.keySet()) {
					for (String letra : particiones.get(numParticion)) {
						// Se convierte de letra a num
						int pos = matrixAFDmin.get(letra).get(i); // Matriz de 0 es a, 1 es b...
						transiciones += pos + ",";	
					}
				}
				transiciones = transiciones.substring(0, transiciones.length() - 1); // Remuevo ultima coma
				myWriter.write(transiciones);
			}

			myWriter.close(); // Se cierra
		} catch (IOException e) {
      		e.printStackTrace(); // Por si hay error
    	}
	}

}
