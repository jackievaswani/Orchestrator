package vjti.orchestrator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.simple.JSONObject;

public class Executor {
	ExecutorService executorService = Executors.newFixedThreadPool(10);
			
	public List<JSONObject> executeCallablesInParallel(List<Callable<JSONObject>> callables) {
		List<JSONObject> result = new ArrayList<JSONObject>();
		try {
			 List<Future<JSONObject>> futures = executorService.invokeAll(callables);
			 
			 for(Future<JSONObject> future : futures) {
				 result.add(future.get());
			 }
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return result;
	}	
	
}
