package scripts.sbf.util.worldhop;

import java.awt.Color;

import org.tribot.api.General;
import org.tribot.api.Screen;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;

import scripts.sbf.action.Action;

public class GoToWorldScreen extends Action {

	private final int minX = 14;
	private final int minY = 471;
	private final int maxX = 97;
	private final int maxY = 488;

	@Override
	public void execute() {
		Mouse.clickBox(minX, minY, maxX, maxY, 1);
		if (Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {
				General.sleep(100, 120);
				return Screen.getColourAt(80, 364).equals(new Color(0, 0, 0));
			}

		}, General.random(5000, 6000))) {
			HopperGlobals.ON_WORLD_SCREEN.setStatus(true);
			HopperGlobals.LOGGED_OUT.setStatus(false);
			
		}
	}

	@Override
	public boolean isValid() {

		return HopperGlobals.LOGGED_OUT.getStatus();
	}

}
