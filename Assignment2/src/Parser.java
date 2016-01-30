import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Class to parse the corpus file
 *
 * @author Ekal.Golas
 */
public class Parser {
	private final BufferedReader	reader;

	/**
	 * Constructor that takes in a file path to initialize the parser
	 *
	 * @param file
	 *            File path
	 * @throws FileNotFoundException
	 */
	public Parser(final String file) throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(file));
	}

	/**
	 * Parses and tokenizes the input read by the reader of Parser
	 *
	 * @return parsed data as object of {@link ParserData}
	 * @throws IOException
	 */
	public ParserData parse() throws IOException {
		// Read input and transform it
		final ParserData parserData = new ParserData();
		final String corpus = this.readInput();

		// Tokenize the input
		this.tokenize(parserData, corpus);

		// Map each bigram to it`s number of occurrences
		this.countBigrams(parserData);

		// Return the parsed data
		return parserData;
	}

	/**
	 * Tokenizes the input mapping bigrams and unigrams
	 *
	 * @param parserData
	 *            Object of {@link ParserData}
	 * @param corpus
	 *            Input data
	 */
	private void tokenize(final ParserData parserData, final String corpus) {
		// Get the first token
		final StringTokenizer tokenizer = new StringTokenizer(corpus);
		String token1 = "";
		if (tokenizer.hasMoreTokens()) {
			token1 = tokenizer.nextToken();

			// Add this token to unigram map
			if (parserData.unigramMap.putIfAbsent(token1, 1) != null) {
				parserData.unigramMap.put(token1, parserData.unigramMap.get(token1) + 1);
			}
		}

		// Go through all the tokens in the corpus
		while (tokenizer.hasMoreTokens()) {
			final String token2 = tokenizer.nextToken();
			final String word = token1 + " " + token2;

			// Add to bigram map
			if (parserData.bigramMap.putIfAbsent(word, 1) != null) {
				parserData.bigramMap.put(word, parserData.bigramMap.get(word) + 1);
			}

			// Count total words
			parserData.totalWords++;

			// Add this token to unigram map
			if (parserData.unigramMap.putIfAbsent(token2, 1) != null) {
				parserData.unigramMap.put(token2, parserData.unigramMap.get(token2) + 1);
			}

			// The second token becomes the first token for the next bigram
			token1 = token2;
		}
	}

	/**
	 * Reads input from reader and transforms it for tokenizing
	 *
	 * @return String of lines containing transformed input
	 * @throws IOException
	 */
	private String readInput() throws IOException {
		final StringBuilder lines = new StringBuilder();

		// Read input and join all lines
		String line = this.reader.readLine();
		while (line != null) {
			lines.append(line);
			line = this.reader.readLine();
		}

		// Remove special characters, trim white spaces and convert to lower case
		String corpus = lines.toString().replaceAll("[^a-zA-Z0-9. ]", "");
		corpus = corpus.replaceAll("\\s+", " ").toLowerCase();
		return corpus;
	}

	/**
	 * Counts the occurrences of each bigram
	 *
	 * @param parserData
	 *            Parser data object contains bigram map
	 */
	private void countBigrams(final ParserData parserData) {
		for (final String key : parserData.bigramMap.keySet()) {
			final int val = parserData.bigramMap.get(key);
			if (parserData.frequencyMap.putIfAbsent(val, 1) != null) {
				final int count = parserData.frequencyMap.get(val);
				parserData.frequencyMap.put(val, count + 1);
			}
		}
	}
}