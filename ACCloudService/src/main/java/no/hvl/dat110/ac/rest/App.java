package no.hvl.dat110.ac.rest;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service
		
		post("/accessdevice/log", (req, res) -> {
			
			String message = req.body();
			Gson gson = new Gson();
		 	
			int id = accesslog.add(message);
		 	message = gson.toJson(accesslog.get(id),AccessEntry.class);
		 	res.body(message);
		 	
		 	return message;
		
		
		});
		
		get("/accessdevice/log", (req, res) -> {
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson(accesslog,AccessLog.class);
		});
		
		get("/accessdevice/log/:id", (req, res) -> {
		 	
			int id = Integer.parseInt(req.params(":id"));
			
			Gson gson = new Gson();
		 
		 	return gson.toJson(accesslog.get(id),AccessEntry.class);
		
		});
		
		put("/accessdevice/code", (req, res) -> {
				
			Gson gson = new Gson();
			
			accesscode = gson.fromJson(req.body(), AccessCode.class);
			String ac = gson.toJson(accesscode,AccessCode.class);
			res.body(ac);

			return ac;
		
		});
		
		get("/accessdevice/code", (req, res) -> {
			
			Gson gson = new Gson();		
			 
		 	return gson.toJson(accesscode, AccessCode.class);
		
		});
		delete("/accessdevice/code", (req, res) -> {
			
			Gson gson = new Gson();
			
			accesslog.clear();
			String log = gson.toJson(accesslog,AccessLog.class);
			res.body(log);
			
		 	return log;
		
		});
		
    }
    
}
