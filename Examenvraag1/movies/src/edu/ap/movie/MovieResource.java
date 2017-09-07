package edu.ap.movie;
import java.io.*;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.json.*;

//import redis.clients.jedis.Jedis; 

@Path("/movies")
public class MovieResource {
	
	private Jedis jedis;
	
	public MovieResource() {
		//Connecting to Redis server on localhost
		jedis = new Jedis("localhost"); 
		System.out.println("Connection to server sucessfully"); 
		//check whether server is running or not 
		System.out.println("Server is running: "+jedis.ping()); 
	}
	
	@GET
	@Produces({"text/html"})
	public String getProductsHTML() {
		films = jedis.keys('film:*');
	    film_list = [];
	    for (var film in films){
	    	f = jedis.get(film)
	    	film_list.append(f)
	    }
	    film_list.sort();   
		
		String htmlString = "<html><body>";		
		try {
			JsonReader reader = Json.createReader(new FileInputStream(FILE));
			JsonObject rootObj = reader.readObject();
			JsonArray array = rootObj.getJsonArray("products");
			System.out.println(array);
			
			for(int i = 0 ; i < array.size(); i++) {
				JsonObject obj = array.getJsonObject(i);
				//System.out.println(obj);
				htmlString += "<b>Name : " + obj.getString("name") + "</b><br>";
				htmlString += "ID : " + obj.getString("id") + "<br>";
				htmlString += "Brand : " + obj.getString("brand") + "<br>";
				htmlString += "Description : " + obj.getString("description") + "<br>";
				htmlString += "Price : " + obj.getJsonNumber("price") + "<br>";
				htmlString += "<br><br>";
			}
		}
		catch(Exception ex) {
			htmlString = "<html><body>" + ex.getMessage();
		}
		
		return htmlString + "</body></html>";
	}
	
	@GET
	@Produces({"application/json"})
	public String getProductsJSON() {
		String jsonString = "";
		try {
			InputStream fis = new FileInputStream(FILE);
	        JsonReader reader = Json.createReader(fis);
	        JsonObject obj = reader.readObject();
	        reader.close();
	        fis.close();
	        //System.out.println(obj);
	        jsonString = obj.toString();
		} 
		catch (Exception ex) {
			jsonString = ex.getMessage();
		}
		
		return jsonString;
	}
	
	@GET
	@Path("{id}")
	@Produces({"application/json"})
	public String getProductJSON(@PathParam("id") String id) {
		String jsonString = "";
		try {
			InputStream fis = new FileInputStream(FILE);
	        JsonReader reader = Json.createReader(fis);
	        JsonObject jsonObject = reader.readObject();
	        reader.close();
	        fis.close();
	        
	        JsonArray array = jsonObject.getJsonArray("products");
	        for(int i = 0; i < array.size(); i++) {
	        	JsonObject obj = array.getJsonObject(i);
	        	if(obj.getString("id").equalsIgnoreCase(id)) {
	            	jsonString = obj.toString();
	            	break;
	            }
	        }
		} 
		catch (Exception ex) {
			jsonString = ex.getMessage();
		}
		
		return jsonString;
	}
	
	@GET
	@Path("/add")
	@Produces("text/html")
	public String getProductForm() {
		String form = "<html><body><h1>Add Product</h1><form action='/jaxrs/products' method='post'><p>";
		form += "ID : <input type='text' name='id' /></p><p>Name : <input type='text' name='name' />";
		form += "</p><p>Brand : <input type='text' name='brand' /></p><p>Price : <input type='text' name='price' /></p>";
		form += "<p>Description : <input type='text' name='description' /></p><input type='submit' value='Add Product' />";
		form += "</form></body></html>";
		
		return form;
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addProduct(@FormParam("id") String id, @FormParam("name") String name, 
							 @FormParam("brand") String brand, @FormParam("price") float price, 
							 @FormParam("description") String description) {
	
		System.out.println(description);
		
		java.net.URI location = null;
		System.out.println(location);
		try {
			// read existing products
			InputStream fis = new FileInputStream(FILE);
			JsonReader jsonReader1 = Json.createReader(fis);
			JsonObject jsonObject = jsonReader1.readObject();
			jsonReader1.close();
			fis.close();
			
			JsonArray array = jsonObject.getJsonArray("products");
			JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
				        
			for(int i = 0; i < array.size(); i++){
				 // add existing products
				 JsonObject obj = array.getJsonObject(i);
				 arrBuilder.add(obj);
			}
			// add new product
			JsonObjectBuilder b = Json.createObjectBuilder().
					add("id", id).
					add("name", name).
					add("brand", brand).
					add("price", price).
					add("description", description);
			arrBuilder.add(b.build());
			
			// now wrap it in a JSON object
	        JsonArray newArray = arrBuilder.build();
	        JsonObjectBuilder builder = Json.createObjectBuilder();
	        builder.add("products", newArray);
	        JsonObject newJSON = builder.build();

	        // write to file
	        OutputStream os = new FileOutputStream(FILE);
	        JsonWriter writer = Json.createWriter(os);
	        writer.writeObject(newJSON);
	        writer.close();
			
			location = new java.net.URI("/jaxrs/products");
			System.out.println(location);
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(location);
		return Response.seeOther(location).build();
	}
}
