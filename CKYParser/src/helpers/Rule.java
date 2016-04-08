package helpers;

/**
 * Class to represent a rule in the grammar
 *
 * @author Ekal.Golas
 */
public class Rule {
	private double		prob;
	private String[]	symbols;
	private String		head;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// For debugging purposes
		return this.head + " -> " + String.join(" ", this.symbols) + " # " + this.prob;
	}

	/**
	 * @return the prob
	 */
	public final double getProb() {
		return this.prob;
	}

	/**
	 * @param prob
	 *            the prob to set
	 */
	public final void setProb(final double prob) {
		this.prob = prob;
	}

	/**
	 * @return the symbols
	 */
	public final String[] getSymbols() {
		return this.symbols;
	}

	/**
	 * @param symbols
	 *            the symbols to set
	 */
	public final void setSymbols(final String[] symbols) {
		this.symbols = symbols;
	}

	/**
	 * @return the head
	 */
	public String getHead() {
		return this.head;
	}

	/**
	 * @param head
	 *            the head to set
	 */
	public void setHead(final String head) {
		this.head = head;
	}
}