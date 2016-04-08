package parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import helpers.BackpointerMatrix;
import helpers.Pointers;
import helpers.ProbabilityMatrix;
import helpers.Rule;
import util.Util;

/**
 * @author Ekal.Golas
 */
public class CKYParser {
	private final BackpointerMatrix	backs;
	private final List<Rule>		grammar;
	private final ProbabilityMatrix	probs;
	private final String[]			words;

	/**
	 * Parameterized constructor
	 *
	 * @param grammar
	 *            Grammar definition of rules
	 * @param words
	 *            Words to get the parse tree for
	 */
	public CKYParser(final List<Rule> grammar, final String[] words) {
		this.grammar = grammar;
		this.words = words;
		this.probs = new ProbabilityMatrix();
		this.backs = new BackpointerMatrix();
	}

	/**
	 * Get probability table
	 *
	 * @return Probability table
	 */
	public ProbabilityMatrix getProb() {
		return this.probs;
	}

	/**
	 * Run CKY parser
	 *
	 * @return Parse tree for the words
	 */
	public String parse() {
		// Update probability matrix for each word in a bottom up fashion
		int col = 0;
		for (final String word : this.words) {
			col++;

			// Set probability when rules directly produce the word
			for (final Rule rule : this.getRules(word.toLowerCase())) {
				// If we can directly produce, set probability of head in the rule and backpointers point to null
				this.probs.setProbInMatrix(col - 1, col, rule.getHead(), rule.getProb());
				this.backs.setPointersInMatrix(col - 1, col, rule.getHead(), null, null, null);

				// Add unary rules
				this.addUnaryRules(col - 1, col);
			}

			// Create a new tree if two existing sub-trees join and a rule for them exists
			for (int row = col - 2; row >= -1; row--) {
				for (int mid = row + 1; mid <= col; mid++) {
					for (final String head1 : this.getPositiveRuleHeads(row, mid)) {
						for (final String head2 : this.getPositiveRuleHeads(mid, col)) {
							for (final Rule rule : this.getRules(head1, head2)) {
								// Get overall probability as rule multiplied by sub-tree probability
								double prob = rule.getProb();
								prob *= this.probs.getProbInMatrix(row, mid, head1);
								prob *= this.probs.getProbInMatrix(mid, col, head2);

								// Set probability to the higher value and update backpointer
								if (prob > this.probs.getProbInMatrix(row, col, rule.getHead())) {
									this.probs.setProbInMatrix(row, col, rule.getHead(), prob);
									this.backs.setPointersInMatrix(row, col, rule.getHead(), String.valueOf(mid), head1, head2);
								}
							}
						}
					}
				}

				// Add unary rules
				this.addUnaryRules(row, col);
			}
		}

		// Return tree once backpointers are updated
		return this.getTree(0, this.words.length, "s");
	}

	/**
	 * Process a chain of unary rules
	 *
	 * @param row
	 *            Row number in matrix
	 * @param col
	 *            Column number in matrix
	 */
	private void addUnaryRules(final int row, final int col) {
		// Get two queues, one for rules we have seen and another for all heads in this row, col
		final Queue<Rule> visitedRules = new LinkedList<>();
		final Queue<String> queue = new LinkedList<>();
		queue.addAll(this.probs.getHeads(row, col));

		// Do till we process all the heads
		while (!queue.isEmpty()) {
			final String head = queue.poll();

			// Queue unseen rules that led to this symbol
			for (final Rule rule : this.getRules(head)) {
				if (!visitedRules.contains(rule)) {
					visitedRules.add(rule);
					queue.add(rule.getHead());

					// Combine chain rules by property of transivity
					final double prob = rule.getProb() * this.probs.getProbInMatrix(row, col, head);
					if (prob > this.probs.getProbInMatrix(row, col, rule.getHead())) {
						this.probs.setProbInMatrix(row, col, rule.getHead(), prob);
						this.backs.setPointersInMatrix(row, col, rule.getHead(), null, head, null);
					}
				}
			}
		}
	}

	/**
	 * Get heads of all rules that have a positive probability
	 *
	 * @param row
	 *            Row of probability table
	 * @param col
	 *            Column for probability table
	 * @return List of heads
	 */
	private List<String> getPositiveRuleHeads(final int row, final int col) {
		final List<String> heads = new ArrayList<>();
		for (final String head : this.probs.getHeads(row, col)) {
			if (this.probs.getProbInMatrix(row, col, head) > 0.0) {
				heads.add(head);
			}
		}

		return heads;
	}

	/**
	 * Get all rules that produce the given symbols
	 *
	 * @param symbols
	 *            Given symbols
	 * @return List of {@link Rule}
	 */
	private List<Rule> getRules(final String... symbols) {
		final List<Rule> rules = new ArrayList<>();
		for (final Rule rule : this.grammar) {
			if (Util.compareArray(rule.getSymbols(), symbols)) {
				rules.add(rule);
			}
		}

		return rules;
	}

	/**
	 * Get the parse tree from the backpointers table
	 *
	 * @param row
	 *            Row for the backpointers table
	 * @param col
	 *            Column for the backpointers table
	 * @param symbol
	 *            Symbol in that row and column
	 * @return Parse tree as a string
	 */
	private String getTree(final int row, final int col, final String symbol) {
		final Pointers pointers = this.backs.getPointersInMatrix(row, col, symbol);
		// If it is a terminal, display the symbol and the word
		if (pointers == null || pointers.getMid() == null && pointers.getHead1() == null && pointers.getHead2() == null) {
			return "(" + symbol.toUpperCase() + " " + this.words[row] + ")";
		} else if (pointers.getMid() == null && pointers.getHead2() == null) {
			// Else, if it is a case of chain of unary rules, recurse through the chain and get the tree
			final String tree = this.getTree(row, col, pointers.getHead1());
			return "(" + symbol.toUpperCase() + " " + tree + ")";
		} else {
			// Else, recurse through all the non terminals and get trees for each
			final String tree1 = this.getTree(row, Integer.parseInt(pointers.getMid()), pointers.getHead1());
			final String tree2 = this.getTree(Integer.parseInt(pointers.getMid()), col, pointers.getHead2());
			return "(" + symbol.toUpperCase() + " " + tree1 + " " + tree2 + ")";
		}
	}
}