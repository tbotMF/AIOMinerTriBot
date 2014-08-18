package scripts.sbf.execution;

import java.util.List;
/**
 * Interface for all execution handlers.
 * @author modulusfrank12
 *
 */
public interface ExecutionHandler {
	
	public void process(List<?> elements, int processSpeed);
	
}
