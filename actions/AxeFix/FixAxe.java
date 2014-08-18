package scripts.actions.AxeFix;

import org.tribot.api.General;
import org.tribot.api2007.Inventory;

import scripts.Globals;
import scripts.sbf.action.Action;
import scripts.sbf.util.ABC;

public class FixAxe extends Action {
	private final String PICK_AXE_HEAD = "Pickaxe head";
	private final String PICK_AXE_HANDLE = "Pickaxe handle";
	private final ABC abc = manager.getABC();

	@Override
	public void execute() {
		print("Fixing axe");
		if (Inventory.find(PICK_AXE_HEAD)[0].click("Use"))
			General.sleep(this.abc.itemInteractionDelay());
		else
			return;
		if (Inventory.find(PICK_AXE_HANDLE)[0].click("Use"))
			General.sleep(this.abc.itemInteractionDelay());
		else
			return;

	}

	@Override
	public boolean isValid() {
		return Globals.FIX_AXE.getStatus();
	}
}
