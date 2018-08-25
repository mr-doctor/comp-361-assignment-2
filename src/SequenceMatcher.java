public class SequenceMatcher {

	private Node[][] nodeMap;

	private static final int GAP_COST = 1;
	private static final int MISMATCH_COST = 2;
	private static final int MATCH_COST = -1;

	private String X2;
	private String Y2;
	private String X, Y;

	private int cost;

	SequenceMatcher(String X, String Y) {
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

		buildMap();
		findPath();
		createStrings();
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
			nodeMap[x][0].setCost(x * GAP_COST);
			if (x != 0) nodeMap[x][0].setParent(nodeMap[x - 1][0]);
		}
		for (int y = 0; y < nodeMap[0].length; y++) {
			nodeMap[0][y].setCost(y * GAP_COST);
			if (y != 0) nodeMap[0][y].setParent(nodeMap[0][y - 1]);

		}
	}

	private void findPath() {

		int leftCost;
		int upCost;
		int diagonalCost;
		for (int x = 1; x < nodeMap.length; x++) {
			for (int y = 1; y < nodeMap[x].length; y++) {

				leftCost = nodeMap[x - 1][y].getCost() + GAP_COST;
				upCost = nodeMap[x][y - 1].getCost() + GAP_COST;
				diagonalCost = (X.charAt(x - 1) == Y.charAt(y - 1) ?
						MATCH_COST : MISMATCH_COST) + nodeMap[x - 1][y - 1].getCost();

				if (diagonalCost <= upCost && diagonalCost <= leftCost) {
					nodeMap[x][y].setCost(diagonalCost);
					nodeMap[x][y].setParent(nodeMap[x - 1][y - 1]);

				} else if (upCost <= leftCost) {
					nodeMap[x][y].setCost(upCost);
					nodeMap[x][y].setParent(nodeMap[x][y - 1]);

				} else {
					nodeMap[x][y].setCost(leftCost);
					nodeMap[x][y].setParent(nodeMap[x - 1][y]);

				}
			}
		}
	}

	private void createStrings() {
		Node node = nodeMap[nodeMap.length - 1][nodeMap[0].length - 1];

		StringBuilder xBuilder = new StringBuilder(), yBuilder = new StringBuilder();
		int finalCost = node.getCost();
		int xPos, yPos;

		while (node.getParent() != null) {
			xPos = node.getX();
			yPos = node.getY();

			xBuilder.append(xPos != node.getParent().getX() ? X.charAt(xPos - 1) : '-');
			yBuilder.append(yPos != node.getParent().getY() ? Y.charAt(yPos - 1) : '-');

			node = node.getParent();
		}

		X2 = xBuilder.toString();
		Y2 = yBuilder.toString();
		cost = finalCost;
	}

	private String getMatchString() {
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

		return "Sequence Matcher {" + "\n" +
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
		if (args.length != 2) {
			throw new IllegalArgumentException("Must have two input strings");
		}

		if (args[0].length() == 0 || args[1].length() == 0) {
			throw new IllegalArgumentException("Inputs must have elements");
		}

		System.out.println(new SequenceMatcher(args[0], args[1]).getOutput());
	}
}
