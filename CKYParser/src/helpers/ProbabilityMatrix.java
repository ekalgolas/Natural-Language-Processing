package helpers;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class to represent probability matrix
 *
 * @author Ekal.Golas
 */
public class ProbabilityMatrix {
	private final Map<Integer, Map<Integer, Map<String, Double>>> probs;

	/**
	 * Default Constructor
	 */
	public ProbabilityMatrix() {
		this.probs = new HashMap<>();
	}

	/**
	 * Get a specific probability in the matrix
	 *
	 * @param row
	 *            Specific row
	 * @param col
	 *            Specific column
	 * @param symbol
	 *            Specific symbol
	 * @return Probability for prob[row][col][symbol]
	 */
	public double getProbInMatrix(final int row, final int col, final String symbol) {
		return this.probs.getOrDefault(row, new HashMap<>()).getOrDefault(col, new HashMap<>()).getOrDefault(symbol, 0.0);
	}

	/**
	 * Set a specific probability in the matrix as prob[row][col][symbol] = prob
	 *
	 * @param row
	 *            Specific row
	 * @param col
	 *            Specific column
	 * @param symbol
	 *            Specific symbol
	 * @param prob
	 *            Probability to set
	 */
	public void setProbInMatrix(final int row, final int col, final String symbol, final double prob) {
		this.probs.putIfAbsent(row, new HashMap<>());
		this.probs.get(row).putIfAbsent(col, new HashMap<>());
		this.probs.get(row).get(col).put(symbol, prob);
	}

	/**
	 * Get all heads for a specific row and column in the probability matrix
	 *
	 * @param row
	 *            Specific row
	 * @param col
	 *            Specific column
	 * @return Set of heads
	 */
	public Set<String> getHeads(final int row, final int col) {
		return this.probs.getOrDefault(row, new HashMap<>()).getOrDefault(col, new HashMap<>()).keySet();
	}

	/**
	 * Getter
	 *
	 * @return Probability matrix
	 */
	public Map<Integer, Map<Integer, Map<String, Double>>> getProbs() {
		return this.probs;
	}
}
