import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class Node {


	private int distance = Integer.MAX_VALUE;
	private final char xChar, yChar;
	private final int x, y;
	private List<Node> children;
	private Map<Node, Integer> childrenCost = new HashMap<>();
	private List<Node> shortestPath = new LinkedList<>();

	public Node(char xChar, char yChar, int x, int y) {
		this.x = x;
		this.y = y;
		this.xChar = xChar;
		this.yChar = yChar;
		this.children = new ArrayList<>();
	}

	public char getXChar() {
		return xChar;
	}

	public char getYChar() {
		return yChar;
	}

	@Override
	public String toString() {
		return xChar + "" + yChar + "";
	}

	public void addChild(Node node) {
		this.children.add(node);
		this.childrenCost.put(node, -Integer.MAX_VALUE);
	}

	public List<Node> getChildren() {
		return children;
	}

	public int getCost(int curX, int curY) {
		for (Node n : children) {
			if (n.getX() == curX && n.getY() == curY) {
				return childrenCost.get(n);
			}
		}
		return -1;
	}

	public int getCost(Node n) {
		return childrenCost.get(n);
	}

	public void setCost(int curX, int curY, int i) {
		for (Node n : children) {
			if (n.getX() == curX && n.getY() == curY) {
				childrenCost.put(n, i);
			}
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Map<Node, Integer> getChildrenCost() {
		return childrenCost;
	}

	public List<Node> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(List<Node> shortestPath) {
		this.shortestPath = shortestPath;
	}
}
