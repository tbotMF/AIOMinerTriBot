package scripts.sbf.manager;

import java.util.List;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.types.generic.Condition;

import scripts.sbf.execution.StateExecutionHandler;
import scripts.sbf.graphics.AbstractGUI;
import scripts.sbf.state.State;

/**
 * The primary initialization method for initializing the execution handlers and/or the GUI.
 * 
 * @author modulusfrank12
 * 
 */
public class Init {

	private final Manager manager = Manager.getInstance();

	/**
	 * Initializes the script. A GUI is mandated.
	 * 
	 * @param gui
	 *            - the GUI associated with the script
	 * @param processSpeed
	 *            - the process speed to loop through actions
	 * @param initCBE
	 *            - true to initialize the combat engine, false otherwise
	 */
	public void init(final AbstractGUI gui, final int processSpeed,
			final boolean initCBE) {
		manager.loadCurrentStatus("Waiting for user to press start.");

		if(!Timing.waitCondition(new Condition() {

			@Override
			public boolean active() {
				return gui.startButtonPressed();
			}

		}, 3600000))
			return;
		

		List<State> associations = gui.createStateAssociations();

		gui.dispose();

		StateExecutionHandler executionHandler = new StateExecutionHandler();
		manager.loadStateExecutionHandler(executionHandler);

		executionHandler.process(associations, processSpeed);

	}

	/**
	 * Initializes the script. A GUI is not mandated.
	 * 
	 * @param processSpeed
	 *            - the process speed to loop through actions
	 * @param initCBE
	 *            - true to initialize the combat engine, false otherwise
	 */
	public void init(final int processSpeed, final boolean initCBE) {
		General.println("Script started.");
		List<State> associations = manager.getStateAssociations();

		StateExecutionHandler executionHandler = new StateExecutionHandler();
		manager.loadStateExecutionHandler(executionHandler);
		executionHandler.process(associations, processSpeed);

	}

}
