package scripts.sbf.state;

import java.util.ArrayList;
import java.util.List;

import scripts.sbf.action.Action;
import scripts.sbf.execution.ActionExecutionHandler;

/**
 * Abstract state of all states
 * 
 * @author modulusfrank12
 * 
 */

public abstract class State {

	protected List<Action> actions = new ArrayList<Action>();
	private ActionExecutionHandler actionExecutor = new ActionExecutionHandler();

	/**
	 * Delegates execution of actions to ActionExecutionHandler
	 */
	public void processActions(int processSpeed) {
		actionExecutor.process(actions, processSpeed);

	}

	/**
	 * Adds actions to the list of actions associated with the state.
	 */
	public void addActions(Action... elements) {
		for (Action element : elements)
			actions.add(element);
	}

	/**
	 * Getter method for actions associated with a state.
	 * 
	 * @return actions associated with the state
	 */
	public List<Action> getActions() {
		return actions;
	}

	/**
	 * All states must override the toString() method to become valid
	 * identifiers of their actions
	 */
	public abstract String toString();

}
