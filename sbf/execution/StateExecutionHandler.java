package scripts.sbf.execution;

import java.util.List;

import scripts.sbf.manager.Manager;
import scripts.sbf.state.State;

/**
 * Processes states, can be used to prioritize certain states for faster
 * execution.
 * 
 * @author modulusfrank12
 * 
 */
public class StateExecutionHandler implements ExecutionHandler {
	/**
	 * State processor
	 * 
	 * @param states
	 *            - List of states to be passed in
	 * @param processSpeed
	 *            - The speed at which to process each action
	 */
	public void process(List<?> states, int processSpeed) {
		while (true) {
			for (Object s : states) {

				/*
				 * if (state.toString().equalsIgnoreCase("Fighting")) { while
				 * (CombatGlobals.IN_COMBAT_AREA.getStatus() &&
				 * !CombatGlobals.NEEDS_TO_BANK.getStatus())
				 * state.processActions(10); }
				 */

				State state = (State) s;
				state.processActions(processSpeed);
				if (Manager.getInstance().getTerminationCondition())
					return;

			}

		}

	}

}
