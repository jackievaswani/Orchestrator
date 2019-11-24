# Orchestrator  
Rest client framework for sending async parallel & sequential http api calls.

Example-
Suppose some rest api(/api3) requires input param whose values depends upon other 2 apis(/api1 & /api2)
This framework can be used to get data async/parallelly from /api1 & /api2 & then /api3 is called sequentially after 1st two api response is received.
