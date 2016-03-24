package driver;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import helpers.Rule;
import parser.CKYParser;
import parser.Parser;

/**
 * Driver class
 *
 * @author Ekal.Golas
 */
public class Solution {
	/**
	 * Driver function
	 *
	 * @param args
	 *            Command line arguments
	 */
	public static void main(final String[] args) {
		// Validate command line arguments
		final long start = System.currentTimeMillis();
		final CommandLine cmd = validateArguments(args);

		// Get arguments
		final File file = new File(cmd.getOptionValue("pcfg"));
		final String[] words = cmd.getOptionValue("text").split(" ");

		// Parse the file
		List<Rule> rules = null;
		try {
			rules = Parser.parse(file);
		} catch (final IOException e) {
			System.out.println("Unable to parse the grammar: " + e.getMessage());
		}

		// Get parse tree
		final CKYParser parser = new CKYParser(rules, words);
		final String tree = parser.parse();

		// Print result
		final double probInMatrix = parser.getProb().getProbInMatrix(0, words.length, "s");
		if (probInMatrix == 0.0) {
			System.out.println("Sentence not recognized by the given grammar: " + cmd.getOptionValue("text"));
		} else {
			System.out.println("Parse tree for the sentence: " + cmd.getOptionValue("text"));
			System.out.println(tree);
			System.out.println("Sentence Probability: " + probInMatrix);
		}

		// Print total running time
		System.out.println("\nTotal running time: " + (System.currentTimeMillis() - start) + " milliseconds");
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
		options.addOption("pcfg", "grammar", true, "The file that contains a set of rules for Lexicalized PCFG");
		options.addOption("text", "testString", true, "The string to generate the parse tree for");

		// Parse arguments
		final CommandLineParser commandLineParser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = commandLineParser.parse(options, args, false);
		} catch (final ParseException e1) {
			System.out.println("Invalid arguments provided");
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("Tokenization", options);
			System.exit(1);
		}

		// Validate
		if (!cmd.hasOption("pcfg") || !cmd.hasOption("text")) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("CKY Parser", options);
			System.exit(2);
		}

		return cmd;
	}
}