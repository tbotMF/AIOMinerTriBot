package scripts.sbf.util.worldhop;

public enum HopperGlobals {
	LOGGED_OUT, ON_WORLD_SCREEN, READY_TO_LOGIN;
	
	private boolean status;
	
	public boolean getStatus(){ return this.status; }
	
	public void setStatus(boolean status){ this.status = status; }
	
}
