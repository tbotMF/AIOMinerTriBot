package scripts.sbf.skill;

import org.tribot.api2007.Player;

public enum SkillGlobals {
	ARRIVED_AT_DEPOSITORY {
		@Override
		public boolean  getUpdatedStatus() {
			//if...
			return status;
		}

		@Override
		public boolean toggleStatus() {
			return status = status == true ? false : true;
		}

		@Override
		public boolean setStatus(boolean s) {
			return status = s;
		}
	},
	SKILLING {
		@Override
		public boolean getUpdatedStatus() {
			//if...
			return status;
		}

		@Override
		public boolean toggleStatus() {
			return status = status == true ? false : true;
		}

		@Override
		public boolean setStatus(boolean s) {
			return status = s;
		}
	},
	IN_RESPAWN_LOCATION {
		@Override
		public boolean getUpdatedStatus() {
			boolean walking = false;
			DeathWalkAreas[] respawnLocations = SkillManager.getInstance()
					.getRespawnLocations();
			if (respawnLocations.length > 0)
				for (DeathWalkAreas area : respawnLocations)
					if (area != null
							&& area.getArea().contains(Player.getPosition())) {
						walking = true;
						break;
					}
			return status = walking;
		}

		@Override
		public boolean toggleStatus() {
			return status = status == true ? false : true;
		}

		@Override
		public boolean setStatus(boolean s) {
			return status = s;
		}
	},
	HOPPING{

		@Override
		public boolean getUpdatedStatus() {
			//if...
			return status;
		}

		@Override
		public boolean toggleStatus() {
			return status = status == true ? false : true;
		}

		@Override
		public boolean setStatus(boolean s) {
			return status = s;
		}
		
	};
	boolean status;
	public abstract boolean getUpdatedStatus();
	public abstract boolean toggleStatus();
	public abstract boolean setStatus(boolean s);

}
