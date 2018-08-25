import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SequenceMatcher {

	private Node[][] nodeMap;

	public static final int GAP_COST = 1;
	public static final int MISMATCH_COST = 2;
	public static final int MATCH_COST = -1;

	private String X2;
	private String Y2;
	private String X, Y;

	private int cost;

	public SequenceMatcher(String X, String Y) {
		this.X = X;
		this.Y = Y;

		nodeMap = new Node[X.length() + 1][Y.length() + 1];
		String Xprime = "-" + X;
		String Yprime = "-" + Y;

		for (int i = 0; i < nodeMap.length; i++) {
			for (int j = 0; j < nodeMap[i].length; j++) {
				Node n = new Node(Xprime.charAt(i), Yprime.charAt(j), i, j);
				nodeMap[i][j] = n;
			}
		}

		for (int i = 0; i < nodeMap.length; i++) {
			for (int j = 0; j < nodeMap[i].length; j++) {
				Node n = nodeMap[i][j];

				if (i != nodeMap.length - 1) {
					n.addChild(nodeMap[i + 1][j]);
				}

				if (j != nodeMap[i].length - 1) {
					n.addChild(nodeMap[i][j + 1]);
				}

				if (i != nodeMap.length - 1 && j != nodeMap[i].length - 1) {
					n.addChild(nodeMap[i + 1][j + 1]);
				}
			}
		}
		buildMap();

		List<Node> path = findPath();

		for (int i = 0; i < path.size() - 1; i++) {
			cost += path.get(i).getCost(path.get(i + 1).getX(), path.get(i + 1).getY());
		}

		createStrings(path);
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Node[] column : nodeMap) {
			for (Node row : column) {
				stringBuilder.append("[").append(row).append("]");
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	private void buildMap() {

		for (int x = 0; x < nodeMap.length; x++) {
			for (int y = 0; y < nodeMap[x].length; y++) {
				doCost(x, y, x + 1, y);
				doCost(x, y, x, y + 1);
				doCost(x, y, x + 1, y + 1);
			}
		}
	}

	private void doCost(int oldX, int oldY, int curX, int curY) {
		if (getNode(oldX, oldY).getCost(curX, curY) > -Integer.MAX_VALUE) {
			return;
		}

		if (oldX == curX || oldY == curY) {
			getNode(oldX, oldY).setCost(curX, curY, GAP_COST);
			return;
		}

		if (getNode(curX, curY).getXChar() == getNode(curX, curY).getYChar()) {
			getNode(oldX, oldY).setCost(curX, curY, MATCH_COST);
		} else {
			getNode(oldX, oldY).setCost(curX, curY, MISMATCH_COST);
		}
	}

	private Node getNode(int x, int y) {
		return nodeMap[x][y];
	}

	private List<Node> findPath() {
		Node root = getNode(0, 0);

		root.setDistance(0);
		
		List<Node> settled = new ArrayList<>();
		List<Node> unsettled = new ArrayList<>();

		unsettled.add(root);

		while (!unsettled.isEmpty()) {
			Node current = getShortest(unsettled);
			unsettled.remove(current);
			for (Map.Entry<Node, Integer> entry : current.getChildrenCost().entrySet()) {
				Node adjacent = entry.getKey();
				int weight = entry.getValue();
				if (!settled.contains(adjacent)) {
					calculateMinimumDistance(adjacent, weight, current);
					unsettled.add(adjacent);
				}
			}
			settled.add(current);
		}
		List<Node> path = getNode(nodeMap.length - 1, nodeMap[0].length - 1).getShortestPath();
		path.add(getNode(nodeMap.length - 1, nodeMap[0].length - 1));
		return path;
	}

	private static void calculateMinimumDistance(Node evaluationNode,
												 Integer edgeWeight, Node sourceNode) {
		Integer sourceDistance = sourceNode.getDistance();
		if (sourceDistance + edgeWeight < evaluationNode.getDistance()) {
			evaluationNode.setDistance(sourceDistance + edgeWeight);
			LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
			shortestPath.add(sourceNode);
			evaluationNode.setShortestPath(shortestPath);
		}
	}

	private Node getShortest(List<Node> unsettled) {
		Node lowestDistanceNode = null;
		int lowestDistance = Integer.MAX_VALUE;
		for (Node node: unsettled) {
			int nodeDistance = node.getDistance();
			if (nodeDistance < lowestDistance) {
				lowestDistance = nodeDistance;
				lowestDistanceNode = node;
			}
		}
		return lowestDistanceNode;
	}

	private void createStrings(List<Node> path) {

		StringBuilder X2Builder = new StringBuilder();
		StringBuilder Y2Builder = new StringBuilder();

		for (int i = 1; i < path.size(); i++) {
			if (path.get(i).getX() > path.get(i - 1).getX() && path.get(i).getY() == path.get(i - 1).getY()) {
				Y2Builder.append("-");
			} else {
				Y2Builder.append(path.get(i).getYChar());
			}
			if (path.get(i).getY() > path.get(i - 1).getY() && path.get(i).getX() == path.get(i - 1).getX()) {
				X2Builder.append("-");
			} else {
				X2Builder.append(path.get(i).getXChar());
			}
		}
		
		X2 = X2Builder.toString();
		Y2 = Y2Builder.toString();

		if (X2.length() < Y2.length()) {
			X2 = rightPad(X2, Y2.length() - X2.length());
		} else if (Y2.length() < X2.length()) {
			Y2 = rightPad(Y2, X2.length() - Y2.length());
		}
	}

	public String getX2() {
		return X2;
	}

	public String getY2() {
		return Y2;
	}

	private String rightPad(String str, int length) {
		StringBuilder stringBuilder = new StringBuilder(str);
		for (int i = 0; i < length; i++) {
			stringBuilder.append("-");
		}
		return stringBuilder.toString();
	}

	public int getCost() {
		return cost;
	}

	public String getMatchString() {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < X2.length(); i++) {
			if (X2.charAt(i) == Y2.charAt(i)) {
				stringBuilder.append("+");
			} else if (X2.charAt(i) == '-' || Y2.charAt(i) == '-') {
				stringBuilder.append("*");
			} else {
				stringBuilder.append("-");
			}
		}

		return stringBuilder.toString();
	}

	String getOutput() {

		String display = "Sequence Matcher {" + "\n" +
				indent(1) + "Input {" + "\n" +
				indent(2) + "X: " + X + "\n" +
				indent(2) + "Y: " + Y + "\n" +
				indent(1) + "}" + "\n" +
				indent(1) + "Output {" + "\n" +
				indent(2) + "X Out: " + X2 + "\n" +
				indent(2) + "Y Out: " + Y2 + "\n" +
				indent(2) + "Matches:" + getMatchString() + "\n" +
				indent(2) + "Total Cost: " + cost + "\n" +
				indent(1) + "}" + "\n" +
				"}\n";
		return display;
	}

	/**
	 * Returns as many tab characters as the parameter states
	 * @param depth an integer of the number of tab characters
	 */
	private static String indent(int depth) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			stringBuilder.append("	");
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		SequenceMatcher s = new SequenceMatcher("GGATACAG", "CCAGATAGAG");
		System.out.println(s);
	}
}
