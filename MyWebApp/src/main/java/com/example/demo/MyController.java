package com.example.demo;

import java.net.URI;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class MyController {
	
	
	@GetMapping("login")
	public String getLogin() {
		
		return "login";
	}
	
	@PostMapping("validate")
	public String validateUser(User user,HttpSession session) {
		
		
		try {
		String token=new RestTemplate().postForObject("http://13.235.103.185:8087/authenticate",user, String.class);
		
		session.setAttribute("token", token);
		
		}catch(Exception e) {
			return "login";
		}
		
		return "home";
		
		
	}
	
	@GetMapping("another")
	public String getAnotherPage(@RequestParam String id,HttpSession session,Model model) {
		
		
		
		RestTemplate rt=new RestTemplate();
		
		try {
	   RequestEntity re= RequestEntity.get("http://13.235.103.185:8087/validate")
			   .header("Authorization", "Bearer "+session.getAttribute("token"))
			   .build();
              
       ResponseEntity<String> response = rt.exchange(re, String.class);
 
       if(response.getBody().equals("true")) {
    	   
    	   Employee emp=new RestTemplate().getForObject("http://13.235.103.185:8087/getEmp/"+id, Employee.class);
    	   
    	
    	   model.addAttribute("emp", emp);
    	   
    	   return "another";
    	   
    	   
       }else {
    	   return "home";
       }
		}catch(Exception e) {
			return "home";
		}
				
				
		
	}

}
