package scripts.actions.Mine;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.api2007.Players;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSArea;
import org.tribot.script.Script;

import scripts.sbf.action.Action;
import scripts.sbf.skill.SkillGlobals;
import scripts.sbf.util.worldhop.HopperGlobals;

public class LogOut extends Action {
	private final Script scriptInstance = manager.getScript();
	private final String numOfPlayer = selections.get("Player hop amount");
	private final RSArea skillArea = skillManager.getSkillArea();

	@Override
	public void execute() {

		print("Hopping worlds.");
		SkillGlobals.HOPPING.setStatus(true);
		scriptInstance.setRandomSolverState(false);

		scriptInstance.setLoginBotState(false);

		while (Login.getLoginState() != Login.STATE.LOGINSCREEN) {
			Login.logout();
			General.sleep(200, 300);
		}
		HopperGlobals.LOGGED_OUT.setStatus(true);

	}

	@Override
	public boolean isValid() {

		return SkillGlobals.HOPPING.getUpdatedStatus()
				|| Game.getCurrentWorld() == 385
				|| Game.getCurrentWorld() == 386
				|| (numOfPlayer != null && numOfPlayer.length() > 0 && Players
						.find(Filters.Players.inArea(skillArea)).length >= Integer
						.parseInt(numOfPlayer));
	}
}
