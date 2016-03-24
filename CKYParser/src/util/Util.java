package util;
/**
 * @author Ekal.Golas
 */
public class Util {
	/**
	 * Compares two arrays
	 *
	 * @param a
	 *            Array a
	 * @param b
	 *            Array b
	 * @return True if arrays have same values, false otherwise
	 */
	public static <T> boolean compareArray(final T[] a, final T[] b) {
		// If arrays are null, they are equal
		if (a == null && b == null) {
			return true;
		}

		// If one of the arrays are null, they cannot be same
		if (a == null || b == null) {
			return false;
		}

		// If lengths are not same, arrays are not same
		if (a.length != b.length) {
			return false;
		}

		// Else, compare each element of the array
		for (int i = 0; i < b.length; i++) {
			if (!a[i].equals(b[i])) {
				return false;
			}
		}

		// If all matched, return true
		return true;
	}
}