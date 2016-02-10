import java.text.DecimalFormat;
import java.util.StringTokenizer;

/**
 * Class that provides various methods of smoothing for the models implemented
 *
 * @author Ekal.Golas
 */
public class Smoothing {
	private final ParserData	parserData;

	/**
	 * Constructor
	 *
	 * @param parserData
	 *            Data parsed on which smoothing is applied
	 */
	public Smoothing(final ParserData parserData) {
		this.parserData = parserData;
	}

	/**
	 * Apply desired smoothing and get sentence probability
	 *
	 * @param sentence
	 *            Sentence to compute
	 * @param smoothingType
	 *            Type of smoothing
	 * @return Probability as a decimal
	 */
	public double doSmoothing(final String sentence, final Models smoothingType) {
		// Initialize variables
		String token1 = "";
		double sentenceProb = 1.0;

		// Get the first token
		final StringTokenizer tokenizer = new StringTokenizer(sentence);
		if (tokenizer.hasMoreTokens()) {
			token1 = tokenizer.nextToken();
		}

		// Go through all the rest of tokens
		while (tokenizer.hasMoreTokens()) {
			final String token2 = tokenizer.nextToken();
			final String bigramToken = token1 + " " + token2;
			final double conditionalProb = this.getConditionalProbability(smoothingType, token1, bigramToken);
			if (conditionalProb > 0) {
				sentenceProb *= conditionalProb;
			}

			// Set the second token as the first token for the next bigram
			token1 = token2;
		}

		return sentenceProb;
	}

	/**
	 * Get conditional probability
	 *
	 * @param smoothingType
	 *            Type of smoothing to apply
	 * @param unigramToken
	 *            Single word token
	 * @param bigramToken
	 *            double word token
	 * @return Conditional probability as decimal
	 */
	private double getConditionalProbability(final Models smoothingType, final String unigramToken, final String bigramToken) {
		final double bigramCount = this.parserData.bigramMap.getOrDefault(bigramToken, 0);
		final double unigramCount = this.parserData.unigramMap.getOrDefault(unigramToken, 0);
		double conditionalProb = 0;

		if (smoothingType == Models.NO_SMOOTHING) {
			// For no smoothing, just divide bigram count by unigram count
			if (bigramCount > 0) {
				conditionalProb = bigramCount / unigramCount;
			}
		} else if (smoothingType == Models.ADD_ONE_SMOOTHING) {
			// For add one smoothing - Add one to bigram count and divide by unigram count added by total number of unigrams
			conditionalProb = (bigramCount + 1) / (unigramCount + this.parserData.unigramMap.size());
		} else if (smoothingType == Models.GOOD_TURING) {
			// c* is N(c + 1)/Nc, or N1/N if there is no occurrence
			final double c = this.parserData.bigramMap.getOrDefault(bigramToken, 0);
			final double n = this.parserData.frequencyMap.getOrDefault((int) (c + 1), 1);
			final double cStar = (c + 1) * n / (double) this.parserData.frequencyMap.getOrDefault((int) c, 1);

			// Compute probability with c* computed above
			conditionalProb = cStar / this.parserData.totalWords;
		}

		return conditionalProb;
	}

	/**
	 * Display probability table for a sentence
	 *
	 * @param sentence
	 *            Sentence to display table for
	 * @param smoothingType
	 *            Type of smoothing to be applied
	 */
	public void displayProbabilityTable(final String sentence, final Models smoothingType) {
		// Add header to the table
		final OutputFormatter formatter = new OutputFormatter();
		formatter.addRow((" " + sentence).split(" "));

		// For each token, get bigrams and compute probability
		final String[] tokenz = sentence.split(" ");
		for (final String element : tokenz) {
			final String[] strings = new String[tokenz.length + 1];
			strings[0] = element;
			for (int i = 0; i < tokenz.length; i++) {
				final String bigram = element + " " + tokenz[i];

				// If no smoothing, just get bigram occurrence
				if (smoothingType == null) {
					strings[i + 1] = this.parserData.bigramMap.getOrDefault(bigram, 0).toString();
				} else {
					// To get total probability, use the formula for no smoothing
					final DecimalFormat decimalFormat = new DecimalFormat("#.####");
					strings[i + 1] = decimalFormat.format(this.getConditionalProbability(smoothingType, tokenz[i], bigram));
				}
			}

			formatter.addRow(strings);
		}

		// Print the table
		System.out.println(formatter + "\n");
	}
}