import java.util.ArrayList;

public class AFD {

	public AFD(ArrayList<Node> grafo) {
		for (Node node : grafo ) {
			ArrayList<Edge> edges = new ArrayList<Edge>(node.getEdges());

			System.out.print(node.getState());

			for (Edge edge : edges) {
				System.out.print(edge.getValue());
				System.out.print(edge.getFinish().getState());
			}

			System.out.println("");

		}
	}

}