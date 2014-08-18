package scripts.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api.types.generic.Filter;
import org.tribot.api2007.Equipment;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.ext.Filters;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSItemDefinition;

import scripts.Globals;
import scripts.sbf.action.Action;
import scripts.sbf.util.MFUtil;

public class Equip extends Action {
	private final Filter<RSItem> pickaxeFilter = Filters.Items.nameNotEquals(
			"Broken pickaxe", "Pickaxe handle").combine(
			Filters.Items.nameContains("pickaxe"), true);

	@Override
	public void execute() {
		print("Equipping");
		if (MFUtil.closeBank() && MFUtil.switchTab(GameTab.TABS.INVENTORY)) {
			RSItem[] pickAxe = Inventory.find(pickaxeFilter);

			if (pickAxe.length > 0 && pickAxe[0] != null) {
				final RSItemDefinition pickAxeDef = pickAxe[0].getDefinition();

				if (pickAxeDef == null)
					return;

				if (pickAxe[0].click("W")) {
					Timing.waitCondition(new Condition() {

						@Override
						public boolean active() {
							General.sleep(100, 200);
							return Equipment.isEquipped(pickAxeDef.getName());
						}

					}, General.random(3000, 4000));
				}
			}
		}

	}

	@Override
	public boolean isValid() {
		return Globals.EQUIP.getStatus();
	}

}
