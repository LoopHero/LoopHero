package unsw.loopmania.goal;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;
import unsw.loopmania.allies.Character;
import java.io.Serializable;
/**
 * Create a goal class to monitor how close Player is towards winning
 */
public abstract class Goal implements Serializable{

	private Character character;
	private transient BooleanProperty satisfied;
	private boolean isLeaf;
	private int quantity;

	public Goal(Character character, int quantity) {
		this.character = character;
		this.quantity = quantity;
		this.satisfied = new SimpleBooleanProperty(false);
	}

	/**
	 * Check if goal is satisfied
	 * @return
	 */
	public boolean isSatisfied() {
		update();
		return satisfied.get();
	}

	/**
	 * Set satisfied to be bool
	 * @param bool
	 */
	public void setSatisfied(boolean bool) {
		satisfied.set(bool);
	}

	/**
	 * Set current class to be leaf or not
	 * @param bool
	 */
	public void setIsLeaf(boolean bool) {
		this.isLeaf = bool;
	}

	/**
	 * Assert current class is leaf or not
	 * @return
	 */
	public boolean isLeaf() {
		return isLeaf;
	}

	/**
	 * returns BooleanProperty satisfied to Observer
	 * @return
	 */
	public BooleanProperty getSatisfiedProperty() {
		return satisfied;
	}

	/**
	 * Return Character object
	 */
	public Character getCharacter() {
		return character;
	}

	/**
	 * Returns quantity requirement of current goal
	 * @return
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Add subgoal under current goal
	 * @param goal
	 */
	public void addSubgoal(Goal goal) {
		System.out.println("Unsupported Operation for goal leaf!");
	}

	/**
	 * Remove subgoal under current goal
	 * @param goal
	 */
	public void removeSubgoal(Goal goal) {
		System.out.println("Unsupported Operation for goal leaf!");
	}

	/**
	 * Return all subgoals under current goal
	 * @return
	 */
	public List<Goal> getSubgoals() {
		System.out.println("Unsupported Operation for goal leaf!");
		return null;
	}

	public abstract void update();
	public abstract String toString();
}
