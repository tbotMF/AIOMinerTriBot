package scripts.sbf.execution;

import java.util.List;

import org.tribot.api.General;

import scripts.sbf.action.Action;
import scripts.sbf.manager.Manager;

/**
 * Processes the actions of its parent state.
 * 
 * @author modulusfrank12
 * 
 */
public class ActionExecutionHandler implements ExecutionHandler {
	/**
	 * Action processor
	 * 
	 * @param actions
	 *            - the list of actions to be passed in
	 * @param processSpeed
	 *            - The speed at which to loop through the actions associated with a state
	 */
	public void process(List<?> actions, int processSpeed) {

		for (Object act : actions) {
			Action action = (Action) act;
			if (action.isValid())
				action.execute();
			if (Manager.getInstance().getTerminationCondition())
				break;
			General.sleep(processSpeed, processSpeed + 10);
		}

	}

}
