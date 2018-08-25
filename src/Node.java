public class Node {

	private final char xChar, yChar;
	private final int x, y;
	private int cost;
	private Node parent;

	Node(char xChar, char yChar, int x, int y) {
		this.x = x;
		this.y = y;
		this.xChar = xChar;
		this.yChar = yChar;
	}

	@Override
	public String toString() {
		return xChar + "" + yChar + "";
	}

	int getX() {
		return x;
	}

	int getY() {
		return y;
	}

	void setCost(int cost) {
		this.cost = cost;
	}

	void setParent(Node node) {
		this.parent = node;
	}

	int getCost() {
		return this.cost;
	}

	Node getParent() {
		return parent;
	}
}
