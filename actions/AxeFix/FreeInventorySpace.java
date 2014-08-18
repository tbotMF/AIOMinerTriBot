package scripts.actions.AxeFix;

import org.tribot.api2007.Inventory;
import org.tribot.api2007.ext.Filters;

import scripts.Globals;
import scripts.sbf.action.Action;

public class FreeInventorySpace extends Action {

	@Override
	public void execute() {
		Inventory
				.drop(Inventory.find(Filters.Items.nameContains("ore", "Clay")));

	}

	@Override
	public boolean isValid() {
		return Globals.NEEDS_FREE_INV_SPACE.getStatus();
	}

}
