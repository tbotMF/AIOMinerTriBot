package scripts.actions.Mine;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.PathFinding;

import scripts.Globals;
import scripts.GlobalVars;
import scripts.sbf.action.Action;

public class WalkToRock extends Action {

	@Override
	public void execute() {
		if (PathFinding.aStarWalk(GlobalVars.getRockToMine().getPosition()))
			Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {
					General.sleep(100, 200);
					return GlobalVars.getRockToMine().isOnScreen();
				}

			}, General.random(5000, 6000));

	}

	@Override
	public boolean isValid() {
		return Globals.WALK_TO_ROCK.getStatus();
	}

}
