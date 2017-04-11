package cs435.nba.elo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

/**
 * Represents a team in a single game. Implements {@link WritableComparable} so
 * it can be used in Hadoop's map and reduce.
 * 
 * TODO Need to override the toString method of this to make printing to files
 * easier
 * 
 * @author nate
 *
 */
public class TeamGameWritable implements WritableComparable<TeamGameWritable> {

	/**
	 * The ID of the team
	 */
	private String teamId;

	/**
	 * Total points scored by the team
	 */
	private double points;

	/**
	 * Total minutes played by the team
	 */
	private double minPlayed;

	/**
	 * Total rebounds by the team
	 */
	private double rebounds;

	/**
	 * Total assists by the team
	 */
	private double assists;

	/**
	 * Total steals by the team
	 */
	private double steals;

	/**
	 * Total blocks by the team
	 */
	private double blocks;

	/**
	 * Total turnovers by the team
	 */
	private double turnovers;

	/**
	 * A {@link MapWritable} for all the players on this team for this game. Has
	 * keys of {@link Text} and values of {@link PlayerGameWritable}
	 */
	private MapWritable players;

	/**
	 * Default constructor, required for Hadoop
	 */
	public TeamGameWritable() {
		this(Constants.INVALID_ID, Constants.INVALID_STAT, Constants.INVALID_STAT, Constants.INVALID_STAT,
				Constants.INVALID_STAT, Constants.INVALID_STAT, Constants.INVALID_STAT, Constants.INVALID_STAT);
	}

	/**
	 * Constructor for the case of setting up players. We don't know overall
	 * values, we just know teamID
	 * 
	 * @param teamId
	 *            The ID of this team
	 */
	public TeamGameWritable(String teamId) {
		this(teamId, Constants.INVALID_STAT, Constants.INVALID_STAT, Constants.INVALID_STAT, Constants.INVALID_STAT,
				Constants.INVALID_STAT, Constants.INVALID_STAT, Constants.INVALID_STAT);
	}

	/**
	 * Constructor that initializes all values
	 * 
	 * @param teamId
	 *            The ID of this team
	 * @param points
	 *            The points scored by the team
	 * @param minPlayed
	 *            Total minutes played by the team
	 * @param rebounds
	 *            Total rebounds by the team
	 * @param assists
	 *            Total assists by the team
	 * @param steals
	 *            Total steals by the team
	 * @param blocks
	 *            Total blocks by the team
	 * @param turnovers
	 *            Total turnovers by the team
	 */
	public TeamGameWritable(String teamId, double points, double minPlayed, double rebounds, double assists,
			double steals, double blocks, double turnovers) {

		this.teamId = teamId;
		this.points = points;
		this.minPlayed = minPlayed;
		this.rebounds = rebounds;
		this.assists = assists;
		this.steals = steals;
		this.blocks = blocks;
		this.turnovers = turnovers;
		this.players = new MapWritable();
	}

	/**
	 * @return {@link TeamGameWritable#teamId}
	 */
	public String getTeamId() {
		return teamId;
	}

	/**
	 * @return {@link TeamGameWritable#points}
	 */
	public double getPoints() {
		return points;
	}

	/**
	 * @return {@link TeamGameWritable#minPlayed}
	 */
	public double getMinPlayed() {
		return minPlayed;
	}

	/**
	 * @return {@link TeamGameWritable#rebounds}
	 */
	public double getRebounds() {
		return rebounds;
	}

	/**
	 * @return {@link TeamGameWritable#assists}
	 */
	public double getAssists() {
		return assists;
	}

	/**
	 * @return {@link TeamGameWritable#steals}
	 */
	public double getSteals() {
		return steals;
	}

	/**
	 * @return {@link TeamGameWritable#blocks}
	 */
	public double getBlocks() {
		return blocks;
	}

	/**
	 * @return {@link TeamGameWritable#turnovers}
	 */
	public double getTurnovers() {
		return turnovers;
	}

	/**
	 * @return {@link MapWritable} with a key of {@link Text} of the playerID
	 *         and a value of {@link PlayerGameWritable}
	 */
	public MapWritable getPlayers() {
		return players;
	}

	/**
	 * @param playerId
	 *            The playerId to retrieve
	 * @return The {@link PlayerGameWritable} represented by the given playerId
	 * @throws PlayerNotFoundException
	 *             If the player was not found
	 */
	public PlayerGameWritable getPlayer(String playerId) throws PlayerNotFoundException {

		if (playerId == null) {
			throw new PlayerNotFoundException("Cannot find a null player");
		}

		return getPlayer(new Text(playerId));
	}

	/**
	 * @param playerId
	 *            The playerId to retrieve
	 * @return The {@link PlayerGameWritable} represented by the given playerId
	 * @throws PlayerNotFoundException
	 *             If the player was not found
	 */
	public PlayerGameWritable getPlayer(Text playerId) throws PlayerNotFoundException {

		if (playerId == null) {
			throw new PlayerNotFoundException("Cannot find a null player");
		}

		if (players.containsKey(playerId)) {

			return (PlayerGameWritable) players.get(playerId);

		} else {

			throw new PlayerNotFoundException("Player with ID: " + playerId.toString() + " was not found");
		}
	}

	/**
	 * @param playerId
	 *            The playerId to check for
	 * @return true if the {@link TeamGameWritable#players} contains this
	 *         playerId, false otherwise
	 */
	public boolean hasPlayerId(String playerId) {

		if (playerId == null) {
			return false;
		}

		return hasPlayerId(new Text(playerId));
	}

	/**
	 * @param playerId
	 *            The playerId to check for
	 * @return true if the {@link TeamGameWritable#players} contains this
	 *         playerId, false otherwise
	 */
	public boolean hasPlayerId(Text playerId) {

		if (playerId == null) {
			return false;
		}

		return players.containsKey(playerId);
	}

	/**
	 * Adds a player to {@link TeamGameWritable#players}. If the given player
	 * already exists as a key, it will replace it.
	 * 
	 * @param player
	 *            The {@link PlayerGameWritable} to add
	 */
	public void addPlayer(PlayerGameWritable player) {

		if (player == null) {
			return;
		}

		Text playerId = new Text(player.getPlayerId());
		if (players.containsKey(playerId)) {

			players.remove(playerId);
		}

		players.put(playerId, player);
	}

	/**
	 * Reads all the member variables from HDFS
	 * 
	 * @param in
	 *            The {@link DataInput}
	 */
	@Override
	public void readFields(DataInput in) throws IOException {

		teamId = WritableUtils.readString(in);
		points = Double.parseDouble(WritableUtils.readString(in));
		minPlayed = Double.parseDouble(WritableUtils.readString(in));
		rebounds = Double.parseDouble(WritableUtils.readString(in));
		assists = Double.parseDouble(WritableUtils.readString(in));
		steals = Double.parseDouble(WritableUtils.readString(in));
		blocks = Double.parseDouble(WritableUtils.readString(in));
		turnovers = Double.parseDouble(WritableUtils.readString(in));
		players.readFields(in);
	}

	/**
	 * Writes all the member variables to HDFS
	 * 
	 * @param out
	 *            {@link DataOutput}
	 */
	@Override
	public void write(DataOutput out) throws IOException {

		WritableUtils.writeString(out, teamId);
		WritableUtils.writeString(out, Double.toString(points));
		WritableUtils.writeString(out, Double.toString(minPlayed));
		WritableUtils.writeString(out, Double.toString(rebounds));
		WritableUtils.writeString(out, Double.toString(assists));
		WritableUtils.writeString(out, Double.toString(steals));
		WritableUtils.writeString(out, Double.toString(blocks));
		WritableUtils.writeString(out, Double.toString(turnovers));
		players.write(out);
	}

	/**
	 * We don't really care how these are sorted (at least I don't think so). So
	 * I am just going to return the comparison of the teamID
	 * 
	 * @param other
	 *            The other {@link TeamGameWritable} object to compare this one
	 *            to
	 * @return Negative, positive or 0 if this object is less than, greater than
	 *         or equal to the given object
	 */
	@Override
	public int compareTo(TeamGameWritable other) {

		if (other == null) {
			return -1;
		}

		return teamId.compareTo(other.getTeamId());
	}

	/**
	 * This function compares equality. The only thing we care about when
	 * comparing teams is if their ID's are equal. If this object's ID is equal
	 * to {@link Constants#INVALID_ID} it should always return false
	 * 
	 * @param o
	 *            The object to compare this one to
	 * @return true if:
	 *         <ul>
	 *         <li>The given Object is {@link TeamGameWritable} and has a
	 *         {@link TeamGameWritable#teamId} equal to this object's</li>
	 *         <li>The given object is {@link String} equal to this
	 *         {@link TeamGameWritable#teamId}</li>
	 *         </ul>
	 *         false otherwise
	 */
	@Override
	public boolean equals(Object o) {

		if (teamId.equals(Constants.INVALID_ID)) {
			return false;
		}

		if (o == null) {
			return false;
		}

		if (o instanceof TeamGameWritable) {

			TeamGameWritable other = (TeamGameWritable) o;
			return teamId.equals(other.getTeamId());

		} else if (o instanceof String) {

			String otherTeamId = (String) o;
			return teamId.equals(otherTeamId);

		} else {

			// o not the same class
			return false;
		}
	}

	/**
	 * It is required in Hadoop to return consistent hashcodes across instances
	 * of JVMs. Luckily for us {@link String#hashCode} already does this. This
	 * is also so when adding to HashSets or HashMaps, we properly add or don't
	 * add.
	 * 
	 * @return The hashCode of {@link TeamGameWritable#teamId}
	 */
	@Override
	public int hashCode() {
		return teamId.hashCode();
	}
}
