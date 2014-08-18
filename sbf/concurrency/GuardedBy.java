package scripts.sbf.concurrency;

/**
 * Indicates which lock is used for the state member.
 * 
 * @author modulusfrank12
 * 
 */
public @interface GuardedBy {
	String lock();
}
