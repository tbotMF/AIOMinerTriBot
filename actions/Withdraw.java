package scripts.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSItemDefinition;

import scripts.Globals;
import scripts.sbf.action.Action;

public class Withdraw extends Action {
	private final Filter<RSItem> pickaxeFilter = Filters.Items.nameNotEquals(
			"Broken pickaxe","Pickaxe handle").combine(
			Filters.Items.nameContains("pickaxe"), true);
	@Override
	public void execute() {
		General.println("Withdrawing");
		RSItem[] pickAxe = Banking.find(pickaxeFilter);

		if (pickAxe.length > 0 && pickAxe[0] != null) {
			final RSItemDefinition pickAxeDef = pickAxe[0].getDefinition();
			if (pickAxeDef != null)
				if (Banking.withdraw(1, pickAxeDef.getName()))
					Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Inventory.getCount(pickAxeDef.getName()) > 0;
						}

					}, General.random(4000, 5000));
		}

	}

	@Override
	public boolean isValid() {

		return Globals.WITHDRAW.getStatus();
	}
}
