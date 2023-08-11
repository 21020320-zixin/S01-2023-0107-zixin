/**
 * 
 * I declare that this code was written by me, 21020320. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Kok Zi Xin
 * Student ID: 21020320
 * Class: FYP
 * Date created: 2023-May-02 8:47:47 pm 
 * 
 */
package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 21020320
 *
 */
public interface AccountRepository extends JpaRepository<UserAccount, Integer>{
	public UserAccount findByUsername(String username);
	public UserAccount findById(int id);
	public UserAccount findByEmail(String email);
	public UserAccount findByResetPasswordToken(String token);
	public UserAccount findByVerificationCode(String code);
	
	@Query("UPDATE UserAccount a SET a.enabled = true WHERE a.id = ?1")
	@Modifying
	public void enable(Integer id);
	public List<UserAccount> findAllByRole(String string);
	public UserAccount findByRole(String string);
}
