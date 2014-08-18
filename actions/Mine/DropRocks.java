package scripts.actions.Mine;

import org.tribot.api2007.Inventory;

import scripts.Globals;
import scripts.sbf.action.Action;

public class DropRocks extends Action {

	@Override
	public void execute() {
		switch (selections.get("Dropping Method")) {
		case "M1D1":
			Inventory.drop(Inventory.getAll());
			break;
		case "Regular":
			if (Inventory.isFull())
				Inventory.drop(Inventory.getAll());

			break;
		}
	}

	@Override
	public boolean isValid() {
		return Globals.DROP_ROCKS.getStatus();
	}

}
