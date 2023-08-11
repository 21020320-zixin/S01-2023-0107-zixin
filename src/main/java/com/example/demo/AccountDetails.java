/**
 * 
 * I declare that this code was written by me, 21020320. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Kok Zi Xin
 * Student ID: 21020320
 * Class: FYP
 * Date created: 2023-May-20 11:10:15 pm 
 * 
 */
package com.example.demo;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author 21020320
 *
 */
public class AccountDetails implements UserDetails {
	
	private UserAccount account;
	
	public AccountDetails(UserAccount account) {
		this.account = account;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(account.getRole());
		return Arrays.asList(authority);
	}
	
	@Override
	public String getPassword() {
		return account.getPassword();
	}
	
	@Override
	public String getUsername() {
		return account.getUsername();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}
	
}
 	
