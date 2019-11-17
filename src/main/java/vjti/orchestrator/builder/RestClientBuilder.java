package vjti.orchestrator.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import vjti.orchestrator.model.Request;
import vjti.orchestrator.service.Executor;

public class RestClientBuilder {
	private Executor executor;
	private List<Request> requests = new ArrayList<Request>();
	private List<Function<List<JSONObject>,List<Request>>> callbacks = new ArrayList<>();
	
	public RestClientBuilder(Executor executor) {
		this.executor=executor;
	}
	
	public RestClientBuilder addRequests(List<Request> requests) {
		this.requests.addAll(requests);
		return this;
	}
	
	public RestClientBuilder then(Function<List<JSONObject>, List<Request>> function) {
		callbacks.add(function);
		return this;
	}
	
	
	public void clearRequest() {
		requests.clear();
	}
	
	public void execute(Consumer<List<JSONObject>> consumerFn){
		List<JSONObject> results = execute(requests);
		for(Function<List<JSONObject>,List<Request> > fn : callbacks) {
			results=execute(fn.apply(results));
		}
		consumerFn.accept(results);
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
