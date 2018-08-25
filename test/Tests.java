import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class Tests {

	enum TestType {
		RANDOM,
		REVERSE,
		EXCLUSIVE,
		IDENTICAL
	}

	private static final String[] GENE_POSSIBILITIES = {"G", "A", "C", "T"};

	@Test
	public void displayInputs() {
		System.out.println(testRandomStrings(20, 5));
		System.out.println(testReverse(20, 5));
		System.out.println(testExclusive(20, 5));
		System.out.println(testIdentical(20));
	}

	@Test
	public void performanceTests() {
		int iterations = 10;

		testPerformance(TestType.RANDOM, iterations);
		testPerformance(TestType.REVERSE, iterations);
		testPerformance(TestType.EXCLUSIVE, iterations);
		testPerformance(TestType.IDENTICAL, iterations);
	}

	private void testPerformance(TestType type, int iterations) {
		for (int sizes = 10; sizes <= 1000; sizes *= 10) {
			long timeStart = System.currentTimeMillis();
			for (int i = 0; i < iterations; i++) {
				switch (type) {
					case RANDOM:
						testRandomStrings(sizes, 0);
					case REVERSE:
						testReverse(sizes, 0);
					case EXCLUSIVE:
						testExclusive(sizes, 0);
					case IDENTICAL:
						testIdentical(sizes);
				}
			}
			System.out.println(type.toString().toLowerCase() + " at " + sizes + ":			" + (System.currentTimeMillis() - timeStart) / ((float) iterations));
		}
		System.out.println();
	}

	private String testIdentical(int sizes) {
		String input = buildRandomString(sizes, 0);
		return new SequenceMatcher(input, input).getOutput();
	}

	private String testExclusive(int size, int deviation) {
		String inputX, inputY;

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size + + ThreadLocalRandom.current().nextInt(-deviation, deviation + 1); i++) {
			builder.append(GENE_POSSIBILITIES[(int) (Math.random() * 2)]);
		}

		inputX = builder.toString();

		builder = new StringBuilder();

		for (int i = 0; i < size + ThreadLocalRandom.current().nextInt(-deviation, deviation + 1); i++) {
			builder.append(GENE_POSSIBILITIES[2 + (int) (Math.random() * 2)]);
		}

		inputY = builder.toString();

		return new SequenceMatcher(inputX, inputY).getOutput();

	}

	private String testReverse(int size, int deviation) {
		String inputX = buildRandomString(size, deviation);
		String inputY = new StringBuilder(inputX).reverse().toString();
		return new SequenceMatcher(inputX, inputY).getOutput();
	}

	private String testRandomStrings(int size, int deviation) {
		return new SequenceMatcher(buildRandomString(size, deviation), buildRandomString(size, deviation)).getOutput();
	}

	private String buildRandomString(int size, int deviation) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size + ThreadLocalRandom.current().nextInt(-deviation, deviation + 1); i++) {
			builder.append(GENE_POSSIBILITIES[(int) (Math.random() * 4)]);
		}
		return builder.toString();
	}
}
