package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import helpers.Rule;

/**
 * Class to parse grammar into lexical rules
 *
 * @author Ekal.Golas
 */
public class Parser {
	public static List<Rule> parse(final File file) throws FileNotFoundException, IOException {
		// Get a list for rules and a pattern to match rule definition substrings
		final List<Rule> rules = new ArrayList<>();
		final Pattern pattern = Pattern.compile("(.*\\[.+\\])");

		// Read the grammar file line by line
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				// Match and get rule for each line
				final Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					final String match = matcher.group().trim();
					String text = match.replaceAll("[\\[^\\]']", "");
					text = text.replace("->", "");
					text = text.replaceAll("\\s+", " ");

					final String[] split = text.split(" ");
					final Rule rule = new Rule();
					rule.setHead(split[0].toLowerCase());
					rule.setProb(Double.parseDouble(split[split.length - 1]));

					final int len = split.length - 2;
					final String[] symbols = new String[len];
					for (int i = 0; i < symbols.length; i++) {
						symbols[i] = split[1 + i].toLowerCase();
					}

					rule.setSymbols(symbols);
					rules.add(rule);
				}
			}
		}

		// Return the parsed rules
		return rules;
	}
}