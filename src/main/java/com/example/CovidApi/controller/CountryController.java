package com.example.CovidApi.controller;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.CovidApi.dao.CountryRepo;
import com.example.CovidApi.model.Country;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;


/**
 * This is the controller for Country Entity of this service.
 * The updateCountry method receives the Entity from an external service.
 * The method processes the JSON Array and converts it into a Java Objects.
 * And saves it into a database.
 * 
 * The other methods with @GetMapping is simple in their execution returning JSON
 * Objects with various different queries.
 * 
 * The method with @PutMapping lets you update an entry.
 *
 * @author  hisham2k9
 * 
 */
@Controller 
public class CountryController 
{
	//method to make request and save country repo
	//method to fetch by a country name and today
	//method to fetch by date
	//method to produde timeseries of a country
	//method to update a a country
	
	
	@Autowired
	CountryRepo repo;
	
	
	@GetMapping("/updateCountry")
	@ResponseBody
	public String updateCountry()
	{
		System.out.println("updatecountry");
		URL url;
		String inline = "";
		try {
			url = new URL("https://hisham2k9.pythonanywhere.com/createJsonAllCountry");
			try {
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestProperty("Content-Type", "application/json");
				con.setConnectTimeout(15000);
				con.setReadTimeout(15000);
				int status = con.getResponseCode();
				System.out.println(status);
				con.connect();
				int responsecode = con.getResponseCode();
				Scanner sc = new Scanner(url.openStream());
				
				while(sc.hasNext())
				{
				inline+=sc.nextLine();
				}
				System.out.println("\nJSON data in string format");
				System.out.println(inline);
				sc.close();
				@SuppressWarnings("deprecation")
				JSONParser parse = new JSONParser();
				JSONObject jobj;
				try {
					jobj = (JSONObject)parse.parse(inline);
					JSONArray jsonarr_1 = (JSONArray) jobj.get("objs");
					Country c=new Country();
					for(int i=0;i<jsonarr_1.size();i++)
					{
					//Store the JSON objects in an array
					//Get the index of the JSON object and saves the values as per the index
					c=new Country();
					JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
					//c.setCid(i+1);
					c.setName((String) jsonobj_1.get("country"));
					c.setTotal((int) jsonobj_1.get("total"));
					c.setActive((int) jsonobj_1.get("active"));
					c.setCode((String) jsonobj_1.get("ccode"));
					c.setDate((String) jsonobj_1.get("str_date"));
					c.setDeath((int) jsonobj_1.get("death"));
					c.setRecovery((int) jsonobj_1.get("recovery"));
					repo.save(c);
					
					}
				} 
				catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "List Updated";
	}
	
	
	@GetMapping("/getCountry/{code}")
	@ResponseBody
	public List<Country> findCountry(@PathVariable String code)
	{
		List<Country> c= repo.findByCode(code);
		
		return c;
	}
	
	@GetMapping("/getByDate/{date}")
	@ResponseBody
	public List<Country> findCountryByDate(@PathVariable String date)
	{
		List<Country> c= repo.findByDate(date);
		
		return c;
	}
	
	@PutMapping("/updateCountry")
	@ResponseBody
	public Country updateCountry(@RequestBody Country c)
	{
		System.out.println("At body put");
		repo.save(c);
		return c;
	}
}
