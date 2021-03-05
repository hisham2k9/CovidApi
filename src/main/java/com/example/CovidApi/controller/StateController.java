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

import com.example.CovidApi.dao.StateRepo;
import com.example.CovidApi.model.Country;
import com.example.CovidApi.model.State;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

@Controller
public class StateController 
{
	@Autowired
	StateRepo repo;
	
	
	@GetMapping("/updateState")
	@ResponseBody
	public String updateState()
	{
		System.out.println("updatestate");
		URL url;
		String inline = "";
		try {
			url = new URL("https://hisham2k9.pythonanywhere.com/createJsonAllState");
			try {
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestProperty("Content-Type", "application/json");
				con.setConnectTimeout(15000);
				con.setReadTimeout(15000);
				int status = con.getResponseCode();
				System.out.println(status);
				con.connect();
				Scanner sc = new Scanner(url.openStream());
				
				while(sc.hasNext())
				{
					//System.out.println(inline);
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
					State s=new State();
					System.out.println(jsonarr_1);
					//Get data for Results array
					for(int i=0;i<jsonarr_1.size();i++)
					{
					//Store the JSON objects in an array
					//Get the index of the JSON object and print the values as per the index
						s=new State();
						JSONObject jsonobj_1 = (JSONObject)jsonarr_1.get(i);
						s.setName((String) jsonobj_1.get("state"));
						s.setTotal((int) jsonobj_1.get("confirmed"));
						s.setActive((int) jsonobj_1.get("active"));
						s.setCode((String) jsonobj_1.get("statecode"));
						s.setDistrict((String) jsonobj_1.get("district"));
						s.setDeath((int) jsonobj_1.get("deceased"));
						s.setRecovery((int) jsonobj_1.get("recovered"));
						repo.save(s);
					
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
	
	@GetMapping("/getState/{code}")
	@ResponseBody
	public List<State> findState(@PathVariable String code)
	{
		List<State> s= repo.findByCode(code);
		
		return s;
	}

	
	@PutMapping("/updateState")
	@ResponseBody
	public State updateState(@RequestBody State s)
	{
		System.out.println("At body put");
		repo.save(s);
		return s;
	}
	
}
