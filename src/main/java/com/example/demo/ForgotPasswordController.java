/**
 * 
 * I declare that this code was written by me, 21020320. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Kok Zi Xin
 * Student ID: 21020320
 * Class: FYP
 * Date created: 2023-Jun-13 4:41:21 pm 
 * 
 */
package com.example.demo;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import javax.security.auth.login.AccountNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import net.bytebuddy.utility.RandomString;

/**
 * @author 21020320
 *
 */
@Controller
public class ForgotPasswordController {
	
	@Autowired
	private AccountDetailsService accountDetailsService;
	
	@Autowired
	private JavaMailSender mailSender;

	@GetMapping("/forgotPassword")
	public String showForgotPassword(Model model) {
		model.addAttribute("pageTitle",  "Forget Password");
		return "forgot_password";
	}
	@PostMapping("/forgotPassword")
	public String processForgotPassword(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		String token = RandomString.make(45);
		try {
			accountDetailsService.updateResetPasswordToken(token, email);
			//generate reset password link
			String resetPasswordLink = Utility.getSiteURL(request) + "/resetPassword?token=" + token;
			
			//send email
			sendEmail(email, resetPasswordLink);
			
			model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
		}
		catch(AccountNotFoundException ex) {
			model.addAttribute("error", ex.getMessage());
		} 
		catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Error while sending email");
		} 
		
		model.addAttribute("pageTitle",  "Forget Password");
		return "forgot_password";
	}
	
	private void sendEmail (String email, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("thiritharclown@gmail.com", "Edublitz Support");
		helper.setTo(email);
		
		String subject = "Here's a link to reset your password";
		
		String content = "<p>Hello,</p>" 
				+ "You have requested to reset your password.</p>" 
				+ "<p>Click the link below to change your password:</p>" 
				+ "<p><b><a href=\"" + resetPasswordLink + "\">Change my password</a><b></p>" 
				+ "<p>Ingore this email if you do remember your password, or you have not made the request.</p>";
		
		helper.setSubject(subject);
		helper.setText(content, true);
		
		mailSender.send(message);
	}
	
	@GetMapping("/resetPassword")
	public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
		UserAccount userAccount = accountDetailsService.get(token);
		
		if(userAccount == null) {
			model.addAttribute("title", "Reset your password");
			model.addAttribute("message", "Invalid Token");
			return "message";
		}
		
	
		model.addAttribute("token", token);
		model.addAttribute("pageTitle",  "Reset Your Password");
		return "resetPassword";
	}
	
	@PostMapping("/resetPassword")
	public String processResetPassword(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		UserAccount userAccount = accountDetailsService.get(token);
		
		if(userAccount == null) {
			model.addAttribute("message", "Invalid Token");
		}
		else {
			accountDetailsService.updatePassword(userAccount, password);
			model.addAttribute("message", "You have successfully changed your password. Please Login Again");
		}
		
		return "message";
	}
}
