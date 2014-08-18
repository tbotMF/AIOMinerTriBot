package scripts.actions.Mine;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.PathFinding;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSModel;
import org.tribot.api2007.types.RSObject;

import scripts.GlobalVars;
import scripts.Globals;
import scripts.sbf.action.Action;
import scripts.sbf.util.ABC;
import scripts.sbf.util.MFUtil;

public class Mine extends Action {
	private final ABC abc = manager.getABC();

	private final Filter<RSObject> nonSmokeFilter = new Filter<RSObject>() {

		@Override
		public boolean accept(RSObject rock) {
			RSModel model = rock.getModel();
			return model != null && model.getVertexCount() < 150;
		}

	};

	private boolean checkLevelUp() {
		return SKILLS.MINING.getActualLevel() > skillManager.getCurrentLevel();
	}

	private boolean detectSmokingRock(RSObject rock) {
		return !nonSmokeFilter.accept(rock);
	}

	private void handleSmokingRock() {
		if (PathFinding.aStarWalk(skillManager.getSkillTile()))
			Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {
					General.sleep(100, 200);
					return MFUtil.isOnTile(skillManager.getSkillTile());
				}

			}, General.random(5000, 6000));
	}

	@Override
	public void execute() {
		print("Mining");

		while (Globals.MINING.getStatus()) {
			General.sleep(100, 200);
			this.abc.doAllIdleActions(SKILLS.MINING, GameTab.TABS.INVENTORY);
			if (checkLevelUp()) {
				skillManager.updateCurrentLevel();
				break;
			}
			if (detectSmokingRock(GlobalVars.getRockToMine())) {
				handleSmokingRock();
				break;
			}
		}
		GlobalVars.loadRockToMine(null);

		if (Inventory.isFull())
			if (!GlobalVars.getListOfRocks().isEmpty())
				GlobalVars.getListOfRocks().poll();
	}

	@Override
	public boolean isValid() {
		return Globals.MINING.getStatus();
	}
}
