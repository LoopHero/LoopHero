package unsw.loopmania.goal;

import java.util.ArrayList;
import java.util.List;
import unsw.loopmania.allies.Character;

public class AndGoal extends Goal {

	private List<Goal> subgoals = new ArrayList<Goal>();

	public AndGoal(Character character, int quantity) {
		super(character, quantity);
		setIsLeaf(false);
	}

	/**
	 * Add subgoal under current goal
	 * @param goal
	 */
	@Override
	public void addSubgoal(Goal goal) {
		subgoals.add(goal);
	}

	/**
	 * Updates all subgoal
	 */
	public void update() {
		subgoals.forEach(Goal::update);
		setSatisfied(subgoals.stream().allMatch(Goal::isSatisfied));
	}

	/**
	 * Convert Goal into String for display
	 */
	@Override
	public String toString() {
		String res = "(AND";
		for (Goal subgoal : subgoals) {
			res = res.concat(" " + subgoal.toString());
		}
		res = res.concat(")");
		return res;
	}
}