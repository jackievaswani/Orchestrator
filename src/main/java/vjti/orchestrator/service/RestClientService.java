package vjti.orchestrator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;
import vjti.orchestrator.model.Request;

public class RestClientService {

	private Executor executor;
	private List<Request> requests = new ArrayList<Request>();
	private List<Function<List<JSONObject>,List<Request>>> callbacks = new ArrayList<>();
	
	public RestClientService(Executor executor) {
		this.executor=executor;
	}
	
	public RestClientService addRequests(List<Request> requests) {
		this.requests.addAll(requests);
		return this;
	}
	
	public RestClientService then(Function<List<JSONObject>, List<Request>> function) {
		callbacks.add(function);
		return this;
	}
	
	
	public void clearRequest() {
		requests.clear();
	}
	
	public void execute(){
		List<JSONObject> results = execute(requests);
		//work around for assigning result variable in lambda
		final List[] resultList = {results};
		callbacks.forEach(fn -> {
			resultList[0]=execute(fn.apply(resultList[0]));
			});
	}
	
	public List<JSONObject> execute(List<Request> apiRequest){
		if(apiRequest == null) {
			return null;
		}
		
		List<Callable<JSONObject>> callables = apiRequest
				.stream()
				.map(request -> createRestCallable(request))
				.collect(Collectors.toList());
		return executor.executeCallablesInParallel(callables);
	}
	
	private Callable<JSONObject> createRestCallable(Request request){
		Callable<JSONObject> callable = new Callable<JSONObject>() {
			public JSONObject call(){
				Client client =ClientBuilder.newClient();
				return new JSONObject(client.target(request.getUrl()).request(MediaType.APPLICATION_JSON).get(Map.class));
			};
			
		};
		return callable;
	}
	
	
	
}
