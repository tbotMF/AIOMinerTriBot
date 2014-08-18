package scripts.sbf.util.worldhop;

import org.tribot.api.General;
import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.script.Script;

import scripts.sbf.action.Action;
import scripts.sbf.skill.SkillGlobals;

public class LogIn extends Action {
	private Script scriptInstance = manager.getScript();
	@Override
	public void execute() {

		if (Game.getCurrentWorld() != manager.getWorldNumber()) {
			HopperGlobals.LOGGED_OUT.setStatus(true);
			HopperGlobals.READY_TO_LOGIN.setStatus(false);
			return;
		}
		
		while (Login.getLoginState() != Login.STATE.INGAME) {
			print("Attempting to login.");
			Login.login();
			General.sleep(500, 750);
		}
		scriptInstance.setRandomSolverState(true);

		scriptInstance.setLoginBotState(true);
		HopperGlobals.READY_TO_LOGIN.setStatus(false);
		SkillGlobals.HOPPING.setStatus(false);
		

	}

	@Override
	public boolean isValid() {

		return HopperGlobals.READY_TO_LOGIN.getStatus();
	}

}
