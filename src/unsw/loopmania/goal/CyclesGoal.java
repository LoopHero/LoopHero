package unsw.loopmania.goal;
import unsw.loopmania.allies.Character;

public class CyclesGoal extends Goal {

	public CyclesGoal(Character character, int quantity) {
		super(character, quantity);
		setIsLeaf(true);
	}

	/**
	 * Update completed cycles/loops against GolgGoal
	 */
	public void update() {
		int q = getQuantity();
		setSatisfied(getCharacter().getLoop().get() >= (float) q);
	}

	/**
	 * Convert Goal into String for display
	 */
	@Override
	public String toString() {
		return getQuantity() + " Cycles";
	}
}