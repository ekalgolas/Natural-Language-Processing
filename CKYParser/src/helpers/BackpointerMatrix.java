package helpers;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent backpointers
 *
 * @author Ekal.Golas
 */
public class BackpointerMatrix {
	private final Map<Integer, Map<Integer, Map<String, Pointers>>> backs;

	/**
	 * Default constructor
	 */
	public BackpointerMatrix() {
		this.backs = new HashMap<>();
	}

	/**
	 * Get a specific pointer in the matrix
	 *
	 * @param row
	 *            Specific row
	 * @param col
	 *            Specific column
	 * @param symbol
	 *            Specific symbol
	 * @return {@link Pointers} for backs[row][col][symbol]
	 */
	public Pointers getPointersInMatrix(final int row, final int col, final String symbol) {
		return this.backs.getOrDefault(row, new HashMap<>()).getOrDefault(col, new HashMap<>()).get(symbol);
	}

	/**
	 * Set a specific pointer in the matrix as backs[row][col][symbol] = Pointer(mid, head1, head2)
	 *
	 * @param row
	 *            Specific row
	 * @param col
	 *            Specific column
	 * @param symbol
	 *            Specific symbol
	 * @param mid
	 *            Mid pointer
	 * @param head1
	 *            Head 1 pointer
	 * @param head2
	 *            Head 2 pointer
	 */
	public void setPointersInMatrix(final int row, final int col, final String symbol, final String mid, final String head1, final String head2) {
		final Pointers pointers = new Pointers();
		pointers.setMid(mid);
		pointers.setHead1(head1);
		pointers.setHead2(head2);

		this.backs.putIfAbsent(row, new HashMap<>());
		this.backs.get(row).putIfAbsent(col, new HashMap<>());
		this.backs.get(row).get(col).put(symbol, pointers);
	}

	/**
	 * @return the Backpointer matrix
	 */
	public Map<Integer, Map<Integer, Map<String, Pointers>>> getBacks() {
		return this.backs;
	}
}