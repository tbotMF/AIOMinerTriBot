package scripts.sbf.graphics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import scripts.sbf.concurrency.Threadsafe;

/**
 * Used for GUI specific option value retrieval or input.
 * 
 * @author modulusfrank12
 */
@Threadsafe
public class UserSelections {

	private static Map<String, String> optionMap = new HashMap<String, String>();

	private static Map<String, List<String>> tableOptionMap = new HashMap<String, List<String>>();

	/**
	 * No invocation is allowed for this class.
	 */
	private UserSelections() {
	}

	/**
	 * Holds the instance of UserSelections.
	 * 
	 * @author modulusfrank12
	 * 
	 */
	private static class InstanceHolder {
		private static final UserSelections INSTANCE = new UserSelections();
	}

	/**
	 * Gets the instance for UserSelections from InstanceHolder and returns it.
	 * 
	 * @return instance of the singleton, UserSelections
	 */
	public static UserSelections getInstance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * Maps each GUI option to its identifying key string.
	 * 
	 * @param k
	 *            - the identifying string for the option.
	 * @param v
	 *            - the corresponding value for the key k.
	 */
	public void put(String k, String v) {
		optionMap.put(k, v);
	}

	/**
	 * Maps a list of GUI options to its identifying key string.
	 * 
	 * @param k
	 *            - the identifying string for the option.
	 * @param v
	 *            - the corresponding values for the key k.
	 */
	public void put(String k, List<String> v) {
		tableOptionMap.put(k, v);
	}

	/**
	 * The primary retrieval method for procuring a value associated with a GUI
	 * option.
	 * 
	 * @param k  - the identifying string key.
	 * @return - the value of the GUI option.
	 */
	public String get(String k) {
		return optionMap.get(k);
	}
	/**
	 * The primary retrieval method for procuring values associated with a GUI option.
	  * @param k  - the identifying string key.
	 * @return - the values of the GUI option.
	 */
	public List<String> getContents(String k) {
		return tableOptionMap.get(k);
	}

}
