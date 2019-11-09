package vjti.orchestrator;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;
import vjti.orchestrator.model.Request;
import vjti.orchestrator.service.Executor;
import vjti.orchestrator.service.RestClientService;

public class App 
{
    public static void main( String[] args )
    {
        Executor executor = new Executor();
        RestClientService restClientService = new RestClientService(executor);
        
        List<Request> requests = new ArrayList<>();
        requests.add(new Request("https://jsonplaceholder.typicode.com/todos/1"));
        requests.add(new Request("https://jsonplaceholder.typicode.com/todos/2"));
        
        restClientService
        .addRequests(requests)
        .then((List<JSONObject> result) -> {
        	System.out.println(result);
        	requests.clear();
        	requests.add(new Request("https://jsonplaceholder.typicode.com/todos/3"));	
        	return requests;})
        .then((List<JSONObject> result) -> {
        	System.out.println(result);
        	return null;
        });
        
        restClientService.execute();
    }
}
