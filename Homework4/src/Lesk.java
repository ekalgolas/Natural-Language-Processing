import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.smu.tspell.wordnet.Synset;

/**
 * Implements the lesk word overlap disambiguation algorithm to make sense of words in a sentence
 *
 * @author Ekal.Golas
 */
public class Lesk {
	/**
	 * Driver function
	 *
	 * @param args
	 *            Command line arguments
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(final String[] args) throws ClassNotFoundException, FileNotFoundException, IOException {
		// Validate command line arguments
		final long start = System.currentTimeMillis();
		final CommandLine cmd = validateArguments(args);

		// Get stopwords
		final File file = new File(cmd.getOptionValue("stop"));

		// Get sentence if provided, else take default sentence
		final String sentence = cmd.hasOption("text") ? cmd.getOptionValue("text") : "Time flies like an arrow.";

		// Parse the sentence and update the synsets accordingly for each word
		final Parser parser = new Parser(file);
		final String[] split = sentence.split(" ");
		parser.updateSynsets(split);

		// Calculate the synset with maximum overlap and point synsets to it
		disambiguate(parser.getSynsets(), parser.getDefaults(), parser.getSignatures());

		// Display results after disambiguation
		displayResults(parser.getSynsets(), split);
		System.out.println("Total time taken: " + (System.currentTimeMillis() - start) + " milliseconds");
	}

	/**
	 * Displays result for sense made for each word
	 *
	 * @param synsets
	 *            List of synset arrays after disambiguaton
	 * @param words
	 *            Words in the sentence
	 */
	private static void displayResults(final List<Synset[]> synsets, final String[] words) {
		for (int i = 0; i < words.length; i++) {
			System.out.println("Word: " + words[i]);

			// If it was a stopword, ignore
			if (synsets.get(i).length == 0) {
				System.out.println("No sense made for the word\n");
				continue;
			}

			// Else display the definition, type of word and its forms
			final Synset synset = synsets.get(i)[0];
			System.out.println("Definition: " + synset.getDefinition());
			System.out.println("Type: " + synset.toString().split("@")[0].toUpperCase());
			System.out.println("Forms: " + String.join(" ", synset.getWordForms()));
			System.out.println();
		}
	}

	/**
	 * Disambiguates senses based on the overlap of one sense with others
	 *
	 * @param synsets
	 *            List of synset arrays
	 * @param defaults
	 *            Default synsets for each synset array
	 * @param signatures
	 *            Map of all stems for the synsets
	 */
	private static void disambiguate(final List<Synset[]> synsets, final List<Synset> defaults, final Map<Synset, Set<String>> signatures) {
		for (int i = 0; i < synsets.size(); i++) {
			final Synset[] synset_list = synsets.get(i);
			if (synset_list.length == 0) {
				continue;
			}

			// Get a set of all words found except the ones in this synset
			final Set<String> strings = new HashSet<>();
			for (int j = 0; j < synsets.size(); j++) {
				// Skip the current synset
				if (j == i) {
					continue;
				}

				for (final Synset synset : synsets.get(j)) {
					strings.addAll(signatures.get(synset));
				}
			}

			// Get overlap counts for words in each synset with the rest of the words
			final Map<Synset, Integer> counts = new HashMap<>();
			for (final Synset synset : synset_list) {
				// Set intersection
				final Set<String> overlap = new HashSet<>();
				overlap.addAll(signatures.get(synset));
				overlap.retainAll(strings);

				counts.put(synset, overlap.size());
			}

			// Get the Synset with maximum overlap
			int max = Integer.MIN_VALUE;
			Synset maxSynset = null;
			for (final Synset synset : synset_list) {
				if (max < counts.get(synset)) {
					max = counts.get(synset);
					maxSynset = synset;
				}
			}

			// If maximum is 0, chose the default synset
			if (counts.get(maxSynset) == 0) {
				maxSynset = defaults.get(i);
			}

			synsets.set(i, new Synset[] { maxSynset });
		}
	}

	/**
	 * Validates and gets the command line arguments provided
	 *
	 * @param args
	 *            Command-line arguments
	 * @return Validates arguments
	 */
	private static CommandLine validateArguments(final String[] args) {
		// Get options
		final Options options = new Options();
		options.addOption("stop", "stopWords", true, "Absolute or relative path to the Stop Words file");
		options.addOption("text", "sentence", true, "Text or sentence to make sense of");

		// Parse arguments
		final CommandLineParser commandLineParser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = commandLineParser.parse(options, args, false);
		} catch (final ParseException e1) {
			System.out.println("Invalid arguments provided");
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Lesk", options);
			System.exit(1);
		}

		// Validate
		if (!cmd.hasOption("stop")) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Lesk", options);
			System.exit(2);
		}

		return cmd;
	}
}