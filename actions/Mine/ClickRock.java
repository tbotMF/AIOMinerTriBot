package scripts.actions.Mine;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSModel;

import scripts.Globals;
import scripts.GlobalVars;
import scripts.sbf.action.Action;
import scripts.sbf.util.MFUtil;

public class ClickRock extends Action {

	@Override
	public void execute() {
		RSModel rockModel = GlobalVars.getRockToMine().getModel();
		if (MFUtil.clickModel(rockModel, "Mine"))
			Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {
					General.sleep(100, 200);
					return Player.getAnimation() != -1;
				}

			}, General.random(7000, 8500));
	}

	@Override
	public boolean isValid() {
		return Globals.CLICK_ROCK.getStatus();
	}

}
