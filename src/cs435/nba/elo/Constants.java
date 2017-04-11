package cs435.nba.elo;

/**
 * Represents constants that will be needed across all classes.
 * 
 * @author nate
 *
 */
public class Constants {

	/**
	 * Represents an invalid ID for all things that have IDs. Mainly used where
	 * default constructors are required.
	 */
	public static final String INVALID_ID = "xINVALIDx";

	/**
	 * An empty string to use
	 */
	public static final String EMPTY_STRING = "";

	/**
	 * Represents an invalid year, month or day. We shouldn't ever have 0 so it
	 * should be safe. Used in the default constructor
	 */
	public static final int INVALID_DATE = 0;

	/**
	 * Represents an invalid stat. Stats can never be less than 0.
	 */
	public static final int INVALID_STAT = -1;

	/**
	 * The starting Elo value
	 */
	public static final int START_ELO = 1200;

	/**
	 * The minimum K-Factor to test
	 */
	public static final int MIN_K_FACTOR = 1;

	/**
	 * The max K-Factor to test
	 */
	public static final int MAX_K_FACTOR = 40;

	/**
	 * The K-Factor step size
	 */
	public static final int K_FACTOR_STEP = 1;
}