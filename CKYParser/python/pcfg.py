from nltk import induce_pcfg
from nltk import treetransforms
from nltk.corpus import treebank
from nltk.grammar import Nonterminal
from nltk.parse import pchart

productions = []
for tree in treebank.parsed_sents():
	# perform optional tree transformations, e.g.:
	tree.collapse_unary(collapsePOS=False)  # Remove branches A-B-C into A-B+C
	tree.chomsky_normal_form(horzMarkov=2)  # Remove A->(B,C,D) into A->B,C+D->D

	productions += tree.productions()

# Print the grammar
S = Nonterminal('S')
grammar = induce_pcfg(S, productions)
print grammar