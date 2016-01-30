import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent the data parsed by the parser
 *
 * @author Ekal.Golas
 */
public class ParserData {
	public Map<Integer, Integer>	frequencyMap;
	public Map<String, Integer>		bigramMap;
	public Map<String, Integer>		unigramMap;
	public int						totalWords;

	/**
	 * Default constructor
	 */
	public ParserData() {
		this.bigramMap = new HashMap<>();
		this.unigramMap = new HashMap<>();
		this.frequencyMap = new HashMap<>();
	}
}