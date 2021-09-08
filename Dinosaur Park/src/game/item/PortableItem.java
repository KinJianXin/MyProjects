package game.item;

import edu.monash.fit2099.engine.Item;

/**
 * A class that represents any items that can be picked up and dropped.
 */
public class PortableItem extends Item {

	private int eatPoints;
	private int feedPoints;
	private String condition = "";

	/**
	 * Constructor.
	 *
	 * @param name the name of this item
	 * @param displayChar the character to represent this item in the UI
	 */
	public PortableItem(String name, char displayChar) {
		super(name, displayChar, true);
	}

	/**
	 * Get the hit points recovered to actor after eating this item.
	 *
	 * @return an integer representing the eatPoints of this item
	 */
	public int getEatPoints() {
		return eatPoints;
	}

	/**
	 * Set the eatPoints of this item with the specified value.
	 *
	 * @param eatPoints an integer representing the eatPoints to set for this item
	 */
	public void setEatPoints(int eatPoints) {
		this.eatPoints = eatPoints;
	}

	/**
	 * Get the hit points recovered to actor after being fed with this item.
	 *
	 * @return an integer representing the feedPoints of this item
	 */
	public int getFeedPoints() {
		return feedPoints;
	}

	/**
	 * Set the feedPoints of this item with the specified value.
	 *
	 * @param feedPoints an integer representing the feedPoints to set for this item
	 */
	public void setFeedPoints(int feedPoints) {
		this.feedPoints = feedPoints;
	}

	/**
	 * Get the condition of this item.
	 *
	 * @return a string representing the condition of this item
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * Set the condition of this item with the specified string.
	 *
	 * @param condition a string representing the condition to set for this item
	 */
	public void setCondition(String condition) {
		this.condition = condition;
	}
}
