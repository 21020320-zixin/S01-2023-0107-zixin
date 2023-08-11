/**
 * 
 * I declare that this code was written by me, 21009696 KYAW THIRI THAR. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: KYAW THIRI THAR
 * Student ID: 21009696
 * Class: C372-5D-E63D-A
 * Date created: 2022-NOV-20 11:45:24 PM 
 * 
 */

package com.example.demo;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {
	@Autowired
	ProgramRunRepository programRunRepository;
	
	@GetMapping("/")//mapping name for index.html
	public String index() {//any name
//		List<ProgramRun> pList=programRunRepository.findAll();
//		model.addAttribute("pList", pList);
		return "index";//html file name
		
	}
	@GetMapping("/home")
	public String home() {
		return "home";
	}
	@GetMapping("/homepage")
	public String homepage() {
		return "homepage";
	}
	@GetMapping("/contactus")//mapping name for contactUs.html
	public String contact() {
		return "contactUs";//contactUs.html
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/";
	}
	@GetMapping("/profile")//mapping name for profile.html
	public String profile() {
		return "profile";//must be same html file name
	}
	
	  @GetMapping("/showinfo")//showinfo.html 
	  public String showInfo() { 
		  return "showinfo"; 
	}
	  @GetMapping("/404")
	  public String pagenotfound() {
		  return "404";
	  }
	  
	@GetMapping("/calendar")
	public String showCalendar(Model model) {
		List<ProgramRun> pList=programRunRepository.findAll();
		List<ProgramRun> programrunList=new ArrayList<>();
		for(ProgramRun programRun:pList) {
			if(programRun.getAppointmentDate().after(new Date())) {
				programrunList.add(programRun);
			}
		}
		model.addAttribute("programrunList", programrunList);
		
		return "calendar";
	}
	
}
