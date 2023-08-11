/**
 * 
 * I declare that this code was written by me, 21020320. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Kok Zi Xin
 * Student ID: 21020320
 * Class: FYP
 * Date created: 2023-May-02 1:39:34 pm 
 * 
 */
package com.example.demo;

import java.time.LocalDate;



import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * @author 21020320
 *
 */
@Entity
public class UserAccount {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message="Last Name cannot be empty!")
	@Size(min=1, max=100, message="Last Name length must be between 2 and 100 characters!")
	private String lastName;
	
	@NotNull(message="First Name cannot be empty!")
	@Size(min=1, max=100, message="First Name length must be between 2 and 100 characters!")
	private String firstName;
	
	private String username;
	
	private String password;
	
	@NotNull(message="NRIC cannot be empty!")
	@Size(min=2, max=100, message="NRIC length must be between 2 and 100 characters!")
	private String nric;
	
	@NotNull(message="Gender cannot be empty!")
	private String gender;
	
	@Email(message="Invalid email format")
	private String email;
	
	@Pattern(regexp = "[0-9]+", message = "Please enter a valid contact number (only digits allowed)")
	@Size(min = 2, max = 8, message = "Contact number must be between 2 and 8 digits")
	private String contactNo;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	
	@NotNull(message="Residing cannot be empty!")
	@Size(min=2, max=100, message="Residing length must be between 2 and 100 characters!")
	private String residing;
	
	@NotNull(message="Qulification cannot be empty!")
	@Size(min=2, max=100, message="Qualification length must be between 2 and 100 characters!")
	private String qualification;
	
	@NotNull(message="Certificate Status cannot be empty!")
	private String certificateStatus;

	private String rate;
	
	private String salary;
	
	private String hrsPay;
	
	private String role;
	
	private String resetPasswordToken;
	
	private String verificationCode;
	
	private boolean enabled;
	

	@NotNull(message="Bank Account Name cannot be empty!")
	@Size(min=2, max=100, message="Bank Account Name length must be between 2 and 100 characters!")
	private String bankAccountName;
	
	@NotNull(message="Bank Name cannot be empty!")
	private String bank;
	
	@NotNull(message="Bank Account Number cannot be empty!")
	@Size(min=2, max=100, message="Bank Account Number length must be between 2 and 100 characters!")
	private String accountNo;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNric() {
		return nric;
	}
	public void setNric(String nric) {
		this.nric = nric;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	public String getResiding() {
		return residing;
	}
	public void setResiding(String residing) {
		this.residing = residing;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getCertificateStatus() {
		return certificateStatus;
	}
	public void setCertificateStatus(String certificateStatus) {
		this.certificateStatus = certificateStatus;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getHrsPay() {
		return hrsPay;
	}
	public void setHrsPay(String hrsPay) {
		this.hrsPay = hrsPay;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getResetPasswordToken() {
		return resetPasswordToken;
	}
	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}
	public String getVerificationCode() {
		return verificationCode;
	}
	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getBankAccountName() {
		return bankAccountName;
	}
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

}
