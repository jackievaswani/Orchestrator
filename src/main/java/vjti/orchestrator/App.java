package vjti.orchestrator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONObject;
import vjti.orchestrator.model.Request;
import vjti.orchestrator.service.Executor;
import vjti.orchestrator.builder.RestClientBuilder;

public class App 
{
    public static void main( String[] args )
    {
        Executor executor = new Executor();
        RestClientBuilder restClientBuilder = new RestClientBuilder(executor);
        
        List<Request> requests = new ArrayList<>();
        requests.add(new Request("https://jsonplaceholder.typicode.com/todos/1"));
        requests.add(new Request("https://jsonplaceholder.typicode.com/todos/2"));
        
        // use case: 
        // Call 2 api in parallel & derive the input params for 3rd api from output of first 2 api 
        // and finally call 3 api to get final result
        
        restClientBuilder
        .addRequests(requests)
        .then((List<JSONObject> result) -> {
        	System.out.println(result);
        	Integer param1=-1, param2=-1;
        	if(result.get(0) != null && result.get(1) != null ) {
        		param1=(Integer)result.get(0).get("id");
        		param1=(Integer)result.get(1).get("id");
        	}
        	return Arrays.asList(new Request("https://jsonplaceholder.typicode.com/todos/3?param1="+param1 +"&param2="+param2));})
        .execute((List<JSONObject> result) -> {
        	System.out.println(result);
        });
    }
}
