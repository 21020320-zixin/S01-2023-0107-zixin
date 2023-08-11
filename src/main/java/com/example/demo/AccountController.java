/**
 * 
 * I declare that this code was written by me, 21020320. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Kok Zi Xin
 * Student ID: 21020320
 * Class: FYP
 * Date created: 2023-May-02 1:22:32 pm 
 * 
 */
package com.example.demo;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountController {
	
  @Autowired
  private AccountRepository accountRepository;
  
  @Autowired
  private AccountDetailsService service;
  
  
  public AccountController(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
  
  @GetMapping("/adminAccount")
  public String adminViewAccount(Model model) {
    List <UserAccount> listAccount = accountRepository.findAll();
     model.addAttribute("listAccount", listAccount);
     return "admin_view_account";
  }
  
  @GetMapping("/trainerAccount")
  public String trainerViewAccount(Model model, Principal principal) {
      String username = principal.getName(); // Get the username of the currently logged-in user
      UserAccount account = accountRepository.findByUsername(username); // Fetch the account using the username
      model.addAttribute("account", account);
      return "trainer_view_account";
  }
  
  @GetMapping("/adminAccount/add")
  public String addAccount(Model model) {
    model.addAttribute("account", new UserAccount());
    return "admin_add_account";
  }
  
  @PostMapping("/adminAccount/save")
  public String saveAccount(@Valid UserAccount account, @RequestParam("rateType") String rateType, RedirectAttributes redirectAttribute, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
      BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
      String encodedPassword = passwordEncoder.encode(account.getPassword());
      account.setPassword(encodedPassword);
      account.setRate(rateType);
      
	  service.register(account);
	  
	  String siteURL = Utility.getSiteURL(request);
	  service.sendVerificationEmail(account, siteURL);

      if (rateType.equals("Standard")) {
          account.setSalary(account.getSalary());
      } 
      else if (rateType.equals("Preferred")) {
          account.setHrsPay(account.getHrsPay());
      }
      
      accountRepository.save(account);
      
      redirectAttribute.addFlashAttribute("success", "Account registered!");
      
      return "redirect:/adminAccount";
  }
  
  @GetMapping("/adminAccount/edit/{id}")
  public String editAdminAccount(@PathVariable("id") Integer id, Model model) {
    UserAccount account = accountRepository.getById(id);
    model.addAttribute("account", account);
    return "admin_edit_account";
  }
  
  @PostMapping("/adminAccount/edit/{id}")
  public String saveAdminAccount(@PathVariable("id") Integer id, UserAccount updatedAccount, String rateType, RedirectAttributes redirectAttribute) {
    updatedAccount.setRole("ROLE_TRAINER");
    UserAccount existingAccount = accountRepository.findById(id).orElse(null);
    updatedAccount.setRate(rateType);
    System.out.println(existingAccount.getRole());
    updatedAccount.setRole(existingAccount.getRole());
    updatedAccount.setEnabled(true);
      
      if (updatedAccount.getPassword().isEmpty()) {
          // Retrieve the existing password and set it in the updated account
          updatedAccount.setPassword(existingAccount.getPassword());
          System.out.println(existingAccount.getPassword());
      } else {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
          String encodedPassword = passwordEncoder.encode(existingAccount.getPassword());
          updatedAccount.setPassword(encodedPassword);
      }
      
      if (rateType.equals("Standard")) {
        System.out.println(updatedAccount.getSalary());
          updatedAccount.setSalary(updatedAccount.getSalary());
      } else if (rateType.equals("Preferred")) {
        System.out.println(updatedAccount.getHrsPay());
          updatedAccount.setHrsPay(updatedAccount.getHrsPay());
      }
      
      accountRepository.save(updatedAccount);
      
      redirectAttribute.addFlashAttribute("success", "Account edited!");
      
      return "redirect:/adminAccount";
  }
  
  @GetMapping("/trainerAccount/edit/{id}")
  public String editTrainerAccount(@PathVariable("id") Integer id, Model model) {
    UserAccount account = accountRepository.getById(id);
    model.addAttribute("account", account);
    return "trainer_edit_account";
  }
  
  @PostMapping("/trainerAccount/edit/{id}")
  public String saveTrainerAccount(@PathVariable("id") Integer id, UserAccount updatedAccount, String rateType, RedirectAttributes redirectAttribute) {
	  UserAccount existingAccount = accountRepository.findById(id).orElse(null);
      updatedAccount.setRate(rateType);
      updatedAccount.setRole(existingAccount.getRole());
      updatedAccount.setEnabled(true);
      
      if (updatedAccount.getPassword().isEmpty()) {
          // Retrieve the existing password and set it in the updated account
          updatedAccount.setPassword(existingAccount.getPassword());
      } 
      else {
    	  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
          String encodedPassword = passwordEncoder.encode(existingAccount.getPassword());
          updatedAccount.setPassword(encodedPassword);
      }
      
      if (rateType.equals("Standard")) {
          updatedAccount.setSalary(updatedAccount.getSalary());
      } 
      else if (rateType.equals("Preferred")) {
          updatedAccount.setHrsPay(updatedAccount.getHrsPay());
      }
      
      accountRepository.save(updatedAccount);
      
      redirectAttribute.addFlashAttribute("success", "Account edited!");
      
      return "redirect:/trainerAccount";
  }
  
  @GetMapping("/adminAccount/delete/{id}")
  public String deleteAccount(@PathVariable("id") Integer id) {
    accountRepository.deleteById(id);
    return "redirect:/adminAccount";
  }
  
  @GetMapping("/signup")
  public String signupAccount(Model model) {
    model.addAttribute("account", new UserAccount());
    return "sign_up_account";
  }
  
  @PostMapping("/signup/save")
  public String saveSignupAccount(@Valid UserAccount account, String rateType, Model model, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
	  
	  service.register(account);
	  
	  String siteURL = Utility.getSiteURL(request);
	  service.sendVerificationEmail(account, siteURL);
      accountRepository.save(account);
      
	  account.setRate(rateType);
      if (rateType.equals("Standard")) {
          account.setSalary(account.getSalary());
      } 
      else if (rateType.equals("Preferred")) {
          account.setHrsPay(account.getHrsPay());
      }
      
      model.addAttribute("success", "Account registered!");
      
      return "register_success";
  }
  
  @GetMapping("/verify")
  public String verifyAccount(@Param("code") String code, Model model) {
	  
	  boolean verified = service.verify(code);
	  
	  String pageTitle = verified ? "Verification Succeeded!" : "Verification Failed";
	  model.addAttribute("pageTitle", pageTitle);
	  
	  return (verified ? "verify_success" : "verify_fail");
  }
}
	
	
	

