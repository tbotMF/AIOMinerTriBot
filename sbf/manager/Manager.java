package scripts.sbf.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSTile;
import org.tribot.script.Script;

import scripts.sbf.concurrency.Threadsafe;
import scripts.sbf.execution.ActionExecutionHandler;
import scripts.sbf.execution.StateExecutionHandler;
import scripts.sbf.graphics.AbstractGUI;
import scripts.sbf.state.State;
import scripts.sbf.util.ABC;
import scripts.sbf.util.Zybez.ZybezItem;

/**
 * Singleton used for generic data retrieval.
 * 
 * @author modulusfrank12
 */
@Threadsafe
public final class Manager {

	private StateExecutionHandler stateExecutionHandler = new StateExecutionHandler();
	private ActionExecutionHandler actionExecutionHandler = new ActionExecutionHandler();
	private List<State> stateAssociations = new ArrayList<State>();
	private int mouseSpeed;
	private long time;
	private RSTile bankTile;
	private boolean[] worldHopOps = new boolean[3];
	private Script script;
	private int worldNumber;
	private ABC abc;
	private long startTime;
	private AtomicInteger profit = new AtomicInteger();
	private String currentStatus;
	private RSArea teleporArea;
	private boolean reachedTerminationCondition;
	private int activityRadius = 1;
	private RSArea activityArea;
	//private ExecutorService executorService = Executors.newCachedThreadPool();

	private Manager() {

	}

	private static class InstanceHolder {
		private static final Manager INSTANCE = new Manager();
	}

	public static Manager getInstance() {
		return InstanceHolder.INSTANCE;
	}

	public void initialize(AbstractGUI gui, int pSpeed, boolean initCBEngine) {
		new Init().init(gui, pSpeed, initCBEngine);
	}

	public void initialize(int pSpeed, boolean initCBEngine) {
		new Init().init(pSpeed, initCBEngine);
	}

	public void loadActivityRadius(int r) {
		this.activityRadius = r;
	}

	public void updateProfitCounter(int profitable) {
		ZybezItem i = new ZybezItem(profitable);
		if (i != null)
			this.profit.addAndGet(i.getAverage());

	}

	public void updateProfitCounter(String profitable) {
		if (profitable != null) {
			ZybezItem i = new ZybezItem(profitable);
			if (i != null)
				this.profit.addAndGet(i.getAverage());
		}
	}

	public void loadABC(ABC abc) {
		if (this.abc == null)
			this.abc = abc;
	}

	public void loadStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void loadCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public void loadTeleportArea(RSTile tile, int radius) {
		if (tile != null)
			this.teleporArea = new RSArea(new RSTile(tile.getX(), tile.getY(),
					tile.getPlane()), radius);

	}

	public void loadTerminationCondition(boolean terminationCondition) {
		this.reachedTerminationCondition = terminationCondition;
	}

	public void loadStateAssociations(List<State> associations) {
		stateAssociations = associations;
	}

	public void loadStateExecutionHandler(StateExecutionHandler stateHandler) {
		this.stateExecutionHandler = stateHandler;
	}

	public void loadActionExecutionhandler(ActionExecutionHandler actionHandler) {
		this.actionExecutionHandler = actionHandler;
	}

	public void loadABCLastBusyTime(long time) {
		this.time = time;
	}

	public void loadBankTile(RSTile bankTile) {
		this.bankTile = bankTile;

	}

	public void loadWorldHopOps(boolean excludeFree, boolean excludePvP,
			boolean excludePopular) {
		worldHopOps = new boolean[] { excludeFree, excludePvP, excludePopular };

	}

	public long getProfitForFxnTime(int value, long totalT) {
		return (long) (value * 3600000D / totalT);
	}

	public long getExpPerHrForFxnTime(int value, long totalT) {
		return (long) (value * 3600000D / totalT);
	}

	public void loadScript(Script script) {
		this.script = script;
	}

	public void loadWorldNumber(int worldNumber) {
		this.worldNumber = worldNumber;
	}

	public boolean getTerminationCondition() {
		return this.reachedTerminationCondition;
	}

	public StateExecutionHandler getStateExecutionHandler() {
		return stateExecutionHandler;
	}

	public ActionExecutionHandler getActionExecutionhandler() {
		return actionExecutionHandler;
	}

	public void setMouseSpeed(int mouseSpeed) {
		this.mouseSpeed = mouseSpeed;
	}

	public RSArea getTeleportArea() {
		return this.teleporArea;
	}

	public int getMouseSpeed() {
		return this.mouseSpeed;
	}

	public int getProfit() {
		return this.profit.get();
	}

	

	public List<State> getStateAssociations() {
		return this.stateAssociations;

	}

	public State getState(String stateString) {
		return stateAssociations.get(stateAssociations.indexOf(stateString));
	}

	public long getABCLastBusyTime() {
		return this.time;
	}

	public int getActivityRadius() {
		return this.activityRadius;
	}

	public RSTile getBankTile() {
		return this.bankTile;
	}

	public boolean[] getWorldHopOps() {
		return this.worldHopOps;
	}

	public Script getScript() {
		return this.script;
	}
	


	public int getWorldNumber() {
		return this.worldNumber;
	}

	

	public String getCurrentStatus() {
		return this.currentStatus;
	}

	public long getStartTime() {
		return this.startTime;
	}

	public ABC getABC() {
		return this.abc;
	}

}
