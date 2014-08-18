package scripts.actions.AxeFix;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.GroundItems;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSGroundItem;

import scripts.Globals;
import scripts.sbf.action.Action;

public class FindPickAxeHead extends Action {
	private final String PICK_AXE_HEAD = "Pickaxe head";

	@Override
	public void execute() {
		RSGroundItem[] pickAxeHead = GroundItems.find(PICK_AXE_HEAD);
		if (pickAxeHead.length > 0 && pickAxeHead[0] != null) {
			if (pickAxeHead[0].click("Take " + PICK_AXE_HEAD))
				Timing.waitCondition(new Condition() {

					@Override
					public boolean active() {
						General.sleep(100, 200);
						return Inventory.find(PICK_AXE_HEAD).length > 0;
					}
				}, General.random(3000, 4000));
		}

	}

	@Override
	public boolean isValid() {
		return Globals.FIND_PICK_AXE_HEAD.getStatus();
	}

}
