

import org.junit.Test;

public class Tests {

	private static final String[] GENE_POSSIBILITIES = {"G", "A", "C", "T"};

	@Test
	public void testInputs() {
		for (int i = 0; i < 20; i++) {
			System.out.println(testFullyRandom(10));
		}

		for (int i = 0; i < 20; i++) {
			System.out.println(testRandomStrings(10));
		}

		for (int i = 0; i < 20; i++) {
			System.out.println(testReverse(10));
		}
	}

	@Test
	public void performanceTests() {
		long timeStart = System.currentTimeMillis();
		/*for (int i = 0; i < 100; i++) {
			testFullyRandom(10);
		}
		System.out.println((System.currentTimeMillis() - timeStart) / 100.0);

		timeStart = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			testRandomStrings(10);
		}
		System.out.println((System.currentTimeMillis() - timeStart) / 10.0);

		timeStart = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			testReverse(10);
		}
		System.out.println((System.currentTimeMillis() - timeStart) / 100.0);*/

		/*timeStart = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			testExclusive(7);
		}
		System.out.println((System.currentTimeMillis() - timeStart) / 10.0);*/

		timeStart = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			String input = buildRandomString(10, true);
			new SequenceMatcher(input, input).getOutput();
		}
		System.out.println((System.currentTimeMillis() - timeStart) / 100.0);
	}

	private String testExclusive(int upperSize) {
		String inputX, inputY;

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < upperSize; i++) {
			builder.append(GENE_POSSIBILITIES[(int) (Math.random() * 2)]);
		}

		inputX = builder.toString();

		builder = new StringBuilder();

		for (int i = 0; i < upperSize; i++) {
			builder.append(GENE_POSSIBILITIES[2 + (int) (Math.random() * 2)]);
		}

		inputY = builder.toString();

		return new SequenceMatcher(inputX, inputY).getOutput();

	}

	private String testReverse(int upperSize) {
		String inputX = buildRandomString(upperSize, true);
		String inputY = new StringBuilder(inputX).reverse().toString();
		return new SequenceMatcher(inputX, inputY).getOutput();
	}

	private String testFullyRandom(int upperSize) {
		return new SequenceMatcher(buildRandomString(upperSize, true), buildRandomString(upperSize, true)).getOutput();
	}

	private String testRandomStrings(int upperSize) {
		return new SequenceMatcher(buildRandomString(upperSize, false), buildRandomString(upperSize, false)).getOutput();
	}

	private String buildRandomString(int upperBound, boolean randomLength) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < ((randomLength) ? (int) (Math.random() * upperBound + 1) : upperBound); i++) {
			builder.append(GENE_POSSIBILITIES[(int) (Math.random() * 4)]);
		}
		return builder.toString();
	}

	@Test
	public void test_02() {
		SequenceMatcher s = new SequenceMatcher(buildRandomString(10, true), buildRandomString(10, true));
		System.out.println(s);
		System.out.println(s.getOutput());
	}
}
