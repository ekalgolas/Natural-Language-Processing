import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.WordNetException;

/**
 * Class to parse the data and form the synsets
 *
 * @author Ekal.Golas
 */
public class Parser {
	private final List<Synset[]>			synsets;
	private final List<Synset>				defaults;
	private final Map<Synset, Set<String>>	signatures;
	private final Set<String>				stopwords;

	/**
	 * Constructor
	 *
	 * @param file
	 *            Stop words file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Parser(final File file) throws FileNotFoundException, IOException {
		this.synsets = new ArrayList<>();
		this.defaults = new ArrayList<>();
		this.signatures = new HashMap<>();
		this.stopwords = this.getStopWords(file);
	}

	/**
	 * @return the synsets
	 */
	public final List<Synset[]> getSynsets() {
		return this.synsets;
	}

	/**
	 * @return the defaults
	 */
	public final List<Synset> getDefaults() {
		return this.defaults;
	}

	/**
	 * @return the signatures
	 */
	public final Map<Synset, Set<String>> getSignatures() {
		return this.signatures;
	}

	/**
	 * Parses the words and updates the synset data
	 *
	 * @param words
	 *            Words in a sentece
	 * @throws WordNetException
	 */
	public void updateSynsets(final String[] words) throws WordNetException {
		for (String stem : words) {
			// Ignore stopwrods
			if (this.stopwords.contains(stem)) {
				this.synsets.add(new Synset[0]);
				this.defaults.add(null);
				continue;
			}

			// Get synsets from wordnet and add to synsets list. Since we chose the first in order in case of tie, default is the first synset
			stem = this.transformText(stem);
			final WordNetDatabase database = WordNetDatabase.getFileInstance();
			final Synset[] syn = database.getSynsets(stem);
			this.synsets.add(syn);
			this.defaults.add(syn[0]);

			// Add all words in definition and usages in signatures except stopwords
			for (final Synset synset : syn) {
				final Set<String> strings = new HashSet<>();
				for (final String string : synset.getDefinition().split(" ")) {
					strings.add(this.transformText(string));
				}

				for (final String example : synset.getUsageExamples()) {
					for (final String string : example.split(" ")) {
						strings.add(this.transformText(string));
					}
				}

				strings.removeAll(this.stopwords);
				this.signatures.put(synset, strings);
			}
		}
	}

	/**
	 * Get a set of stopwords from a file
	 *
	 * @param file
	 *            Stop words file
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Set<String> getStopWords(final File file) throws FileNotFoundException, IOException {
		final Set<String> stopwords = new HashSet<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			for (String line; (line = reader.readLine()) != null;) {
				stopwords.add(line.trim());
			}
		}

		return stopwords;
	}

	/**
	 * Removes punctuation
	 *
	 * @param text
	 *            Text to transform
	 * @return Transformed text
	 */
	private String transformText(String text) {
		// Remove the special characters
		text = text.replaceAll("[\"\\.]", "");

		// Trim and set text to lower case
		text = text.trim().toLowerCase();
		return text;
	}
}