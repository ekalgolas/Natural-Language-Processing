import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 * Test if Wordnet is configured properly
 *
 * @author Ekal.Golas
 */
public class TestWordnet {
	/**
	 * Change this according to your environment
	 */
	@BeforeClass
	public static void setup() {
		System.setProperty("wordnet.database.dir", "/usr/local/WordNet-3.0/dict/");
	}

	/**
	 * Get sysnsets for fly
	 */
	@Test
	public final void testFly() {
		NounSynset nounSynset;
		NounSynset[] hyponyms;
		final WordNetDatabase database = WordNetDatabase.getFileInstance();
		final Synset[] synsets = database.getSynsets("fly", SynsetType.NOUN);

		// Check if sysnsets retrieved and print them
		Assert.assertFalse("No Synsets found", synsets == null || synsets.length == 0);
		for (final Synset synset : synsets) {
			nounSynset = (NounSynset) synset;
			hyponyms = nounSynset.getHyponyms();
			System.out.println(nounSynset.getWordForms()[0] +
					": " + nounSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms");
		}
	}

	/**
	 * Get sysnsets for airplane
	 */
	@Test
	public void testAirplane() throws Exception {
		final String wordForm = "airplane";

		// Get the synsets containing the word form
		final WordNetDatabase database1 = WordNetDatabase.getFileInstance();
		final Synset[] synsets1 = database1.getSynsets(wordForm);

		// Check if sysnsets retrieved and print them
		Assert.assertFalse("No Synsets found", synsets1 == null || synsets1.length == 0);

		// Display the word forms and definitions for synsets retrieved
		System.out.println("The following synsets contain '" +
				wordForm + "' or a possible base form " +
				"of that text:");

		for (final Synset synset : synsets1) {
			System.out.println("");
			final String[] wordForms = synset.getWordForms();
			for (int j = 0; j < wordForms.length; j++) {
				System.out.print((j > 0 ? ", " : "") +
						wordForms[j]);
			}

			System.out.println(": " + synset.getDefinition());
		}
	}
}