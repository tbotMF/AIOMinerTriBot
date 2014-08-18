package scripts.actions;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.Objects;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Player;
import org.tribot.api2007.WebWalking;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.Globals;
import scripts.sbf.action.Action;

public final class WalkToMine extends Action {
	private final RSTile skillTile = skillManager.getSkillTile();
	private final String primaryZPlaneAction = "Climb-down";

	private void climbDownStairs() {
		final int z = Player.getPosition().getPlane();
		final RSObject[] stairs = Objects.find(20,
				Filters.Objects.actionsContains(primaryZPlaneAction));
		if (stairs.length > 0 && stairs[0] != null) {
			if (!stairs[0].isOnScreen())
				if (PathFinding.aStarWalk(stairs[0].getPosition()))
					Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {
							General.sleep(100, 200);
							return stairs[0].isOnScreen();
						}

					}, General.random(4000, 5000));

			if (stairs[0].isOnScreen()
					&& DynamicClicking.clickRSObject(stairs[0], 3))
				if (ChooseOption.isOptionValid(primaryZPlaneAction)
						&& ChooseOption.select(primaryZPlaneAction))
					Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Player.getPosition().getPlane() < z;
						}

					}, General.random(2000, 3000));

		}
	}

	@Override
	public void execute() {
		General.println("Walking to mine");
		if (Player.getPosition().getPlane() == 0) {
			if (WebWalking.walkTo(skillTile))
				if (!Timing.waitCondition(new Condition() {

					@Override
					public boolean active() {
						General.sleep(100, 200);
						return Globals.MINE.getStatus();
					}

				}, General.random(5000, 6000)))
					return;

		} else {
			climbDownStairs();
		}

	}

	@Override
	public boolean isValid() {

		return Globals.WALK_TO_MINE.getStatus() && skillTile != null;
	}

}
