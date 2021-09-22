package no.hvl.dat110.rest.counters;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;


public class App {
	
	static HashMap<Long, Todo> todos = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(5555);
		}

		todos = new HashMap<Long, Todo>();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		
        get("/todo/:id", (req, res) -> {
        	Long id = Long.parseLong(req.params("id"));
        	return todos.get(id).toJson();
        });
        
        get("/todo", (req, res) -> {
        	List<Todo> list = new ArrayList<Todo>(todos.values());;
        	Gson gson = new Gson();
        	return gson.toJson(list);
        	
        });
               
        put("/todo/:id", (req,res) -> {
        
        	Gson gson = new Gson();
        	
        	Long id = Long.parseLong(req.params("id"));
        	
        	Todo todo = todos.get(id);
        	
        	todo = gson.fromJson(req.body(), Todo.class);
        	
        	todo.setId(id);
        	
        	todos.replace(id, todo);
        
            return todo.toJson();
        	
        });
        
        post("/todo", (req,res) -> {
        	Gson gson = new Gson();
        	Todo todo = gson.fromJson(req.body(), Todo.class);
        	todos.put(todo.getId(), todo);
        	
        	return gson.toJson("Element " + todo.toJson() + "is added");
        });
        
        
        delete("/todo/:id", (req,res) -> {
        	Gson gson = new Gson();
        	Long id = Long.parseLong(req.params("id"));
        	
        	todos.remove(id);
        	
        	return gson.toJson("Element " + id + "has been removed");
        });
      
    }
    
}
