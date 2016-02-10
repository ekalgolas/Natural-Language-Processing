import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Class to compute bi-grams for a given text
 *
 * @author Ekal.Golas
 */
public class ComputeBigrams {
	/**
	 * Main function
	 *
	 * @param args
	 *            Command line arguments
	 */
	public static void main(final String[] args) {
		// Validate command line arguments
		final Options options = new Options();
		options.addOption("file", "filePath", true, "Absolute or relative file path to the corpus");
		options.addOption("s1", "sentence1", true, "Sentence 1");
		options.addOption("s2", "sentence2", true, "Sentence 2");

		final CommandLineParser commandLineParser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = commandLineParser.parse(options, args, false);
		} catch (final ParseException e1) {
			System.out.println("Invalid arguments provided");
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ComputeBigrams", options);
			return;
		}

		// Get file path
		String filePath = "";
		if (cmd.hasOption("file")) {
			filePath = cmd.getOptionValue("file");
		} else {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ComputeBigrams", options);
			return;
		}

		// Try to parse the input file
		ParserData parserData = null;
		try {
			final Parser parser = new Parser(filePath);
			parserData = parser.parse();
		} catch (final IOException e) {
			System.out.println("Error while parsing: " + e.getMessage());
			return;
		}

		// Get sentences
		final String[] sentences = getSentences(cmd);

		// Apply smoothing on the data and display results
		final ComputeBigrams bigrams = new ComputeBigrams();
		bigrams.compareAndDisplay(sentences[0], sentences[1], parserData);
	}

	/**
	 * Compares two sentences and prints the results with probability tables
	 *
	 * @param sentence1
	 *            Sentence 1
	 * @param sentence2
	 *            Sentence 2
	 * @param smoothing
	 *            Object of {@link Smoothing}
	 */
	private void compareAndDisplay(String sentence1, String sentence2, final ParserData parserData) {
		// Remove special characters, trim white spaces and convert to lower case
		final Smoothing smoothing = new Smoothing(parserData);
		sentence1 = sentence1.replaceAll("[^a-zA-Z0-9. ]", "").replaceAll("\\s+", " ").toLowerCase();
		sentence2 = sentence2.replaceAll("[^a-zA-Z0-9. ]", "").replaceAll("\\s+", " ").toLowerCase();

		// Compare sentences with each type of smoothing
		this.compareSentence(sentence1, sentence2, Models.NO_SMOOTHING, smoothing);
		this.compareSentence(sentence1, sentence2, Models.ADD_ONE_SMOOTHING, smoothing);
		this.compareSentence(sentence1, sentence2, Models.GOOD_TURING, smoothing);

		// Construct the table with the bigram probabilities for the sentences with no smoothing
		System.out.println(Models.NO_SMOOTHING);
		this.displayTable(sentence1, sentence2, smoothing, null);

		// Construct the table with the bigram probabilities for the sentences with add one smoothing
		System.out.println(Models.ADD_ONE_SMOOTHING);
		this.displayTable(sentence1, sentence2, smoothing, Models.ADD_ONE_SMOOTHING);

		// Construct the table with the bigram probabilities for the sentences with Good Turing discounting
		System.out.println(Models.GOOD_TURING);
		this.displayTable(sentence1, sentence2, smoothing, Models.GOOD_TURING);

		// Compute the total probabilities for each sentence S1 and S2.
		// To get total probability, use the formula for no smoothing
		System.out.println("TOTAL PROBABILITIES");
		this.displayTable(sentence1, sentence2, smoothing, Models.NO_SMOOTHING);
	}

	/**
	 * Display probability table for sentences
	 *
	 * @param sentence1
	 *            Sentence 1
	 * @param sentence2
	 *            Sentence 2
	 * @param smoothing
	 *            Object of class {@link Smoothing}
	 * @param smoothingType
	 *            Type of smoothing
	 */
	private void displayTable(final String sentence1, final String sentence2, final Smoothing smoothing, final Models smoothingType) {
		System.out.println("\nS1: " + sentence1 + "\n");
		smoothing.displayProbabilityTable(sentence1, smoothingType);
		System.out.println("\nS2: " + sentence2 + "\n");
		smoothing.displayProbabilityTable(sentence2, smoothingType);
		System.out.println();
	}

	/**
	 * Applies smoothing and compares two sentences
	 *
	 * @param sentence1
	 *            Sentence 1
	 * @param sentence2
	 *            Sentence 2
	 * @param operation
	 *            Type of smoothing to apply
	 * @param smoothing
	 *            Object of class {@link Smoothing}
	 */
	private void compareSentence(final String sentence1, final String sentence2, final Models operation, final Smoothing smoothing) {
		// Do smoothing for each sentence
		System.out.println(operation);
		final double comp1 = smoothing.doSmoothing(sentence1, operation);
		System.out.println("Sentence 1 Probability: " + comp1);

		final double comp2 = smoothing.doSmoothing(sentence2, operation);
		System.out.println("Sentence 2 Probability: " + comp2);

		// Compute result
		String output = "";
		if (comp1 > comp2) {
			output = "S1 is more probable";
		} else if (comp1 < comp2) {
			output = "S2 is more probable";
		} else {
			output = "S1 and S2 are equally probable";
		}

		System.out.println(output + "\n");
	}

	/**
	 * Get sentences to compare
	 *
	 * @param cmd
	 *            Command line arguments object
	 * @return Array of 2 sentences as strings
	 */
	private static String[] getSentences(final CommandLine cmd) {
		// Initialize sentences
		final String[] sentences = new String[2];
		sentences[0] = "The president has relinquished his control of the company's board.";
		sentences[1] = "The chief executive officer said the last year revenue was good.";

		// If user has provided any sentence, use those
		if (cmd.hasOption("s1")) {
			sentences[0] = cmd.getOptionValue("s1");
		}
		if (cmd.hasOption("s2")) {
			sentences[1] = cmd.getOptionValue("s2");
		}

		return sentences;
	}
}