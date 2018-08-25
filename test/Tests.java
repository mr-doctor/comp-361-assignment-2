

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
		for (int i = 0; i < 100; i++) {
			testFullyRandom(10);
		}
		System.out.println((System.currentTimeMillis() - timeStart) / 100);

		timeStart = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			System.out.println("doing");
			testRandomStrings(9);
		}
		System.out.println((System.currentTimeMillis() - timeStart) / 100);

		timeStart = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			testReverse(10);
		}
		System.out.println((System.currentTimeMillis() - timeStart) / 100);
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
}
