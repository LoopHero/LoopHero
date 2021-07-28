package unsw.loopmania.goal;
import unsw.loopmania.allies.Character;

public class ExperienceGoal extends Goal {

	public ExperienceGoal(Character character, int quantity) {
		super(character, quantity);
		setIsLeaf(true);
	}

	/**
	 * Update obtained Experiment against GolgGoal
	 */
	public void update() {
		int q = getQuantity();
		setSatisfied(getCharacter().getExp().get() >= q);
	}

	/**
	 * Convert Goal into String for display
	 */
	@Override
	public String toString() {
		return getQuantity() + " Exp";
	}
}