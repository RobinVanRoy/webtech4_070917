package edu.ap.product;

import java.io.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.servlet.ServletContext;

@Path("/quotes")
public class QuoteResource {
	
	private String FILE;
	
	public QuoteResource(@Context ServletContext servletContext) {
		FILE = servletContext.getInitParameter("FILE_PATH_QUOTE");
		//System.out.println(FILE);
	}
	
	@GET
	@Produces({"text/html"})
	public String getQuotesHTML() {
		String line = null;
		String htmlString = "<html><body>";
		try {
			FileReader fReader = new FileReader(FILE);
			BufferedReader bReader = new BufferedReader(fReader);
			
			htmlString += "<h3>Quotes of Steve Jobs</h3>" + "<br>";
			htmlString += "<ul>";
			while((line = bReader.readLine()) != null){
				//System.out.println(line);
				if(line.isEmpty()){
					continue;
				}
				//System.out.println(line);
				htmlString += "<li>" + line + "</li>";
			}
			bReader.close();
			htmlString += "</ul><br><br>";
		}
		catch(FileNotFoundException ex){
			htmlString = "File not found<html><body>" + ex.getMessage();
		}
		catch(IOException ex){
			htmlString = "I/O Exception<html><body>" + ex.getMessage();
		}		
		catch(Exception ex) {
			htmlString = "<html><body>" + ex.getMessage();
		}
		
		return htmlString + "</body></html>";
	}

	@GET
	@Path("/add")
	@Produces("text/html")
	public String getProductForm() {
		String form = "<html><body><h1>Add Product</h1><form action='/jaxrs/quotes/add' method='post'><p>";
		form += "Quote : <input type='text' name='quote' /></p>";
		form += "<input type='submit' value='Add Product' />";
		form += "</form></body></html>";
		
		return form;
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response addProduct(@FormParam("quote") String quote) {
	
		System.out.println(quote);
		
		java.net.URI location = null;
		System.out.println(location);
		try {
			FileWriter fWriter = new FileWriter(FILE, true);
			BufferedWriter bWriter = new BufferedWriter(fWriter);
			
			bWriter.write(quote);
			bWriter.newLine();			
			bWriter.close();
			
			location = new java.net.URI("/jaxrs/quotes");
			System.out.println(location);
		}
		catch(FileNotFoundException ex){
			System.out.println("File not found:" + ex.getMessage());
		}
		catch(IOException ex){
			System.out.println("I/O error:" + ex.getMessage());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println(location);		
		return Response.seeOther(location).build();
	}
}
