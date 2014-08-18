package scripts.sbf.graphics;

import java.io.Serializable;
import java.util.List;

import javax.swing.JFrame;

import scripts.sbf.manager.Manager;
import scripts.sbf.skill.SkillManager;
import scripts.sbf.state.State;

/**
 * The abstract class for all JFrames used in the script.
 * 
 * @author modulusfrank12
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractGUI extends JFrame implements Serializable {
	protected final UserSelections selections = UserSelections.getInstance();
	protected final Manager manager = Manager.getInstance();
	protected final SkillManager skillManager = SkillManager.getInstance();
	private boolean started;

	/**
	 * Flag for verifying state of start button.
	 * 
	 * @return true if start button has been pressed, false otherwise
	 */
	public boolean startButtonPressed() {
		return started;
	}

	/**
	 * Associates states with their actions.
	 * 
	 * @return list of states with their associated actions.
	 */
	public abstract List<State> createStateAssociations();

	/**
	 * Processes user inputs.
	 * 
	 */
	public abstract void processUserSelections();
	/**
	 * Processes user selections and sets the start button flag to true when the start
	 * button has been pressed.
	 * @param evt
	 */
	public void startScriptButtonActionPerformed(java.awt.event.ActionEvent evt) {
		processUserSelections();
		started = true;
	}

}
