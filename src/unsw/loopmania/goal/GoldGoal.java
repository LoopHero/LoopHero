package unsw.loopmania.goal;
import unsw.loopmania.allies.Character;

public class GoldGoal extends Goal {

	public GoldGoal(Character character, int quantity) {
		super(character, quantity);
		setIsLeaf(true);
	}

	/**
	 * Update obtained Gold against GolgGoal
	 */
	public void update() {
		int q = getQuantity();
		setSatisfied(getCharacter().getGold().get() >= q);
	}

	/**
	 * Convert Goal into String for display
	 */
	@Override
	public String toString() {
		return getQuantity() + " Gold";
	}
}