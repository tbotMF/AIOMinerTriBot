package scripts.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;

import scripts.Globals;
import scripts.sbf.action.Action;

public class Deposit extends Action {

	@Override
	public void execute() {
		print("Depositing");
		if (Banking.depositAll() == Inventory.getAll().length)
			Timing.waitCondition(new Condition() {

				@Override
				public boolean active() {
					General.sleep(100, 200);
					return Inventory.getAll().length < 1;
				}

			}, 5000);

	}

	@Override
	public boolean isValid() {
		return Globals.DEPOSIT.getStatus();
	}

}
