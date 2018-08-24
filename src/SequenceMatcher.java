import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SequenceMatcher {

	/*
	alignStrings(X, Y) {
		given a node map of size X.len + 1 * Y.len + 1
		buildMap(X, Y)
		path = []
		cost = 0
		path, cost = findPath()
		X2, Y2 = createStrings(X, Y, path)
		return X2, Y2, cost
	}
	 */

	private Node[][] nodeMap;

	public static final int GAP_COST = 1;
	public static final int MISMATCH_COST = 2;
	public static final int MATCH_COST = 0;

	private String X2, Y2 = "";
	private String X, Y = "";

	int cost;

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

		System.out.println(X2);
		System.out.println(Y2);
		System.out.println(cost);
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

		if (getNode(curX, curY).getxChar() == getNode(curX, curY).getyChar()) {
			getNode(oldX, oldY).setCost(curX, curY, MATCH_COST);
		} else {
			getNode(oldX, oldY).setCost(curX, curY, MISMATCH_COST);
		}
	}
	



	public Node getNode(int x, int y) {
		return nodeMap[x][y];
	}

	public List<Node> findPath() {
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

		return getNode(nodeMap.length - 1, nodeMap[0].length - 1).getShortestPath();
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

	public void createStrings(List<Node> path) {

		StringBuilder X2Builder = new StringBuilder();
		StringBuilder Y2Builder = new StringBuilder();
		int count = 1;
		X2Builder.append(X.charAt(0));
		Y2Builder.append(Y.charAt(0));

		for (int i = 1; i < path.size(); i++) {
			if (path.get(i-1).getX() < path.get(i).getX() && path.get(i-1).getY() == path.get(i).getY()) {
				Y2Builder.append("-");
				continue;
			}
			if (path.get(i-1).getX() == path.get(i).getX() && path.get(i-1).getY() < path.get(i).getY()) {
				X2Builder.append("-");
				continue;
			}

			X2Builder.append(X.charAt(count));
			Y2Builder.append(Y.charAt(count));
			count++;
		}
		X2 = X2Builder.toString();
		Y2 = Y2Builder.toString();

		if (X2.length() < Y2.length()) {
			X2 = rightPad(X2, Y2.length() - X2.length());
		} else if (Y2.length() < X2.length()) {
			Y2 = rightPad(Y2, X2.length() - Y2.length());
		}
	}

	private String rightPad(String str, int length) {
		StringBuilder stringBuilder = new StringBuilder(str);
		for (int i = 0; i < length; i++) {
			stringBuilder.append("-");
		}
		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		SequenceMatcher s = new SequenceMatcher("GGTAC", "ATACA");
		System.out.println(s);
	}
}
