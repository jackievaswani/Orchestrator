# Orchestrator  
Rest client framework for sending async parallel & sequential http requests.

Example-
Suppose some rest api(/api3) requires input param whose values depends upon other 2 apis(/api1 & /api2)
This framework can be used to get data async/parallely from /api1 & /api2 & then /api3 is called sequentially after 1st two api response is received.

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
