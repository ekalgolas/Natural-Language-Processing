package helpers;

/**
 * Class to represent pointers that are stored in backpointer matrix
 *
 * @author Ekal.Golas
 */
public class Pointers {
	private String	mid;
	private String	head1;
	private String	head2;

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// For debugging purposes
		return "Mid: " + this.mid + ", head1: " + this.head1 + ", head2: " + this.head2;
	}

	/**
	 * @return the mid
	 */
	public final String getMid() {
		return this.mid;
	}

	/**
	 * @param mid
	 *            the mid to set
	 */
	public final void setMid(final String mid) {
		this.mid = mid;
	}

	/**
	 * @return the head1
	 */
	public final String getHead1() {
		return this.head1;
	}

	/**
	 * @param head1
	 *            the head1 to set
	 */
	public final void setHead1(final String head1) {
		this.head1 = head1;
	}

	/**
	 * @return the head2
	 */
	public final String getHead2() {
		return this.head2;
	}

	/**
	 * @param head2
	 *            the head2 to set
	 */
	public final void setHead2(final String head2) {
		this.head2 = head2;
	}
}