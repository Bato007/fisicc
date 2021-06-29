/**
 * Objetos de tipo Expression
 * Value > String (ej. a+b)
 * Recursive > Boolean (contiene *)
 * ChildrenPos > Int con la cantidad de elementos dentro de la recursividad
 **/

public class Expression {
	private String value;
	private Boolean recursive;
	private int actualPos;
	private int childrenPos;

	/**
	 * Constructor que recibe la expression completa
	 */
	public Expression(String expression, int childrenPos) {
		// Revisando si la expresion completa es recursiva
		try {
			if (expression.substring(expression.length()-2).equals(")*")) {
				this.recursive = true;
				expression = expression.substring(0, expression.length() - 1); // Se remueve el * final
			} else {
				this.recursive = false;
			}
		} catch (Exception e) {
			this.recursive = false;
		}
		
		this.childrenPos = childrenPos; // Se guarda la cantidad de expresiones dentro de la recursividad
		// Se remueven parentesis
		expression = expression.replace("(", "");
		expression = expression.replace(")", "");
		this.value = expression; // Se guarda el valor de la expresion
	}

	// Get del atributo value
	public String getValue() {
		return value;
	}

	// Get del atributo recursive
	public Boolean getRecursive() {
		return recursive;
	}

	// Get del atributo childenPos 
	public int getChildenPos() {
		return childrenPos;
	}
}