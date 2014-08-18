package scripts.actions;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Banking;

import scripts.Globals;
import scripts.sbf.action.Action;

public class OpenBank extends Action{
	@Override
	public void execute() {
		print("Opening bank.");
		if(Banking.openBank())
			Timing.waitCondition(new Condition(){

				@Override
				public boolean active() {
					General.sleep(100,200);
					return Banking.isBankScreenOpen();
				}
				
			}, General.random(5000, 6000));
		
		
		
	}

	@Override
	public boolean isValid() {
		return Globals.OPEN_BANK.getStatus();
	}

}
