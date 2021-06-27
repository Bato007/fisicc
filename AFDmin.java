import java.util.*;

public class AFDmin {
	// AFD
	private HashMap<String, ArrayList<String>> finalAFD = new HashMap<String, ArrayList<String>>();
	// Estados
	private HashMap<String, ArrayList<Integer>> finalStates = new HashMap<String, ArrayList<Integer>>();


	public AFDmin(HashMap<String, ArrayList<String>> finalAFD, HashMap<String, ArrayList<Integer>> finalStates) {
		this.finalStates = finalStates;
		this.finalAFD = finalAFD;

		System.out.println(finalAFD);
		System.out.println(finalStates);
	}
}