/**
 * 
 * I declare that this code was written by me, 21020320. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Kok Zi Xin
 * Student ID: 21020320
 * Class: FYP
 * Date created: 2023-May-20 11:20:55 pm 
 * 
 */
package com.example.demo;

import java.io.UnsupportedEncodingException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import javax.security.auth.login.AccountNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//
import net.bytebuddy.utility.RandomString;

/**
 * @author 21020320
 *
 */
@Service
@Transactional
public class AccountDetailsService implements UserDetailsService{
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public AccountDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    UserAccount account = accountRepository.findByUsername(username);

	    if (account == null) {
	        throw new UsernameNotFoundException("Could not find user");
	    }

	    if (!account.isEnabled()) {
	        throw new DisabledException("Account is disabled");
	    }

	    return new AccountDetails(account);
	}

	
	public void updateResetPasswordToken(String token, String email) throws AccountNotFoundException {
		UserAccount userAccount = accountRepository.findByEmail(email);
		if (userAccount != null) {
			userAccount.setResetPasswordToken(token);
			accountRepository.save(userAccount);
		}
		else {
			throw new AccountNotFoundException("Could not find any user with the email " + email);
		}
	}
	
	public UserAccount get(String resetPasswordToken) {
		return accountRepository.findByResetPasswordToken(resetPasswordToken);
	}
	
	public void updatePassword(UserAccount userAccount, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		userAccount.setPassword(encodedPassword);
		
		userAccount.setResetPasswordToken(null);
		accountRepository.save(userAccount);
	}
	
	public UserAccount register(UserAccount account) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(account.getPassword());
		account.setPassword(encodedPassword);
		
		String randomCode = RandomString.make(64);
		account.setVerificationCode(randomCode);
		account.setEnabled(false);
		
		return accountRepository.save(account);
	}
	
	public void sendVerificationEmail(UserAccount account, String siteURL) throws MessagingException, UnsupportedEncodingException {
		String toAddress = account.getEmail();
		String fromAddress = "thiritharclown@gmail.com";
		String senderName = "Edublitz";
		String subject = "Please verify your registration";
		
		String content = "<p>Dear " + account.getFirstName() + " " + account.getLastName() + ",</p>";
		
		content += "<p>Please click the link below to verify your registration:</p>";
		
		String verifyURL = siteURL + "/verify?code=" + account.getVerificationCode();
		
		content += "<h3><a href=\"" + verifyURL + "\">VERIFY</a></h3>";
		
		content += "<p>Thank you, <br>Edublitz</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);
		helper.setText(content, true);
		
		mailSender.send(message);
	

	}
	
	public boolean verify(String verificationCode) {
		UserAccount account = accountRepository.findByVerificationCode(verificationCode);
		
		if (account == null || account.isEnabled()) {
			return false;
		}
		else {
			accountRepository.enable(account.getId());
			account.setVerificationCode(null);
			account.setEnabled(true);
			accountRepository.save(account);
			return true;
		}
	}

}
