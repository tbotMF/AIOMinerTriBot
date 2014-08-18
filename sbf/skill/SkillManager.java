package scripts.sbf.skill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.tribot.api2007.types.RSArea;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSObject;
import org.tribot.api2007.types.RSTile;

import scripts.sbf.concurrency.Threadsafe;
import scripts.sbf.util.MFUtil;

/**
 * Singleton for user-specific data retrieval. Creation based on eminent
 * frequency of skill related usage.
 * 
 * @author modulusfrank12
 */

@Threadsafe
public class SkillManager {
	private RSTile skillTile;
	private RSTile bankTile;
	private int childIndex;
	private int masterIndex;
	private List<String> inventoryResources = new ArrayList<String>();
	private Map<String, short[]> skillResourcesColour = new HashMap<String, short[]>();
	private List<String> rareResources = new ArrayList<String>();
	private int currentLevel;
	private RSArea skillArea;
	private short[] primaryResourceColour;
	private RSObject primaryResourceObject;
	private SkillManager() {

	}

	private static class InstanceHolder {
		private static final SkillManager INSTANCE = new SkillManager();
	}

	public static SkillManager getInstance() {
		return InstanceHolder.INSTANCE;
	}

	public void loadSkillResourcesByModifiedColour(String resource,
			short... modifiedColours) {
		this.skillResourcesColour.put(resource, modifiedColours);
	}

	public void loadRareResources(String... resourceNames) {
		for (String rsrc : resourceNames)
			this.rareResources.add(rsrc);
	}

	public void loadCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public void loadPrimaryResourceColor(short[] pResourceColor){
		this.primaryResourceColour = pResourceColor;
	}

	public void loadChildIndex(int childIndex) {
		this.childIndex = childIndex;
	}

	public void loadInventoryResources(String... resources) {
		for (String resource : resources)
			this.inventoryResources.add(resource);

	}

	public void loadSkillArea(int radius) {
		this.skillArea = new RSArea(this.skillTile, radius);
	}

	public void loadMasterIndex(int masterIndex) {
		this.masterIndex = masterIndex;
	}

	public void loadSkillTile(RSTile location) {
		this.skillTile = new RSTile(location.getX(), location.getY(),
				location.getPlane());
	}

	public RSTile getSkillTile() {
		return this.skillTile;
	}

	public void loadBankTile(RSTile location) {
		this.bankTile = new RSTile(location.getX(), location.getY(),
				location.getPlane());
	}

	public RSTile getBankTile() {
		return this.bankTile;
	}

	public short[] getSkillResourcesByModifiedColour(String resource) {
		return this.skillResourcesColour.get(resource);
	}

	public String getSkillResourceNameByModifiedColour(short[] modColor) {
		for (Entry<String, short[]> entry : skillResourcesColour.entrySet()) {
			if (modColor.equals(entry.getValue()))
				return entry.getKey();
		}
		return null;
	}

	public Map<String, short[]> getSkillResourcesByModifiedColourMap() {
		return this.skillResourcesColour;
	}

	public List<String> getRareResourceList() {
		return this.rareResources;
	}

	public String[] getInventoryResources() {
		return MFUtil.toArrayString(this.inventoryResources);
	}

	public int getMasterIndex() {
		return this.masterIndex;
	}

	public int getCurrentLevel() {
		return this.currentLevel;
	}

	public void updateCurrentLevel() {
		this.currentLevel++;
	}

	public int getChildIndex() {
		return this.childIndex;
	}
	

	public RSArea getSkillArea() {
		return this.skillArea;
	}
	
	public short[] getPrimaryResourceColor(){
		return this.primaryResourceColour;
	}

	public DeathWalkAreas getDeathLocation(RSTile playerPosition) {
		for (DeathWalkAreas dWalkArea : DeathWalkAreas.values())
			if (dWalkArea.getArea().contains(playerPosition))
				return dWalkArea;

		return null;
	}

	public RSInterface getChildInterfaceFor(RSInterface master, String textName) {
		if (master != null)
			for (RSInterface child : master.getChildren())
				if (child != null
						&& child.getText() != null
						&& (child.getText().contains(textName) || (child
								.getComponentName() != null && child
								.getComponentName().contains(textName))))
					return child;
		return null;
	}

	public DeathWalkAreas[] getRespawnLocations() {
		return DeathWalkAreas.values();
	}
}
