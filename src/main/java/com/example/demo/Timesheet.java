package com.example.demo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



@Entity
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    
  //to save current date to database
 	@Column(name = "payDay")
 	@CreationTimestamp
 	@Temporal(TemporalType.DATE)
 	private java.util.Date date;

 	@PrePersist
 	private void onCreate() {
 		date = new Date();
 	}
 	
 	
 	


	@ManyToOne
	@JoinColumn(name="account_id")
	private UserAccount member;
 	
 	 @OneToOne
 	@JoinColumn(name="programrun_id")
     private ProgramRun programrun;
 	 
 	@NotNull
	@NotEmpty(message="Bank Account name cannot be empty!")
	@Size(min=3, max=50, message="Account name length must be between 5 and 50")
	private String bankAccountName;
	
	@NotNull
	@NotEmpty(message="Bank name cannot be empty!")
	@Size(min=3, max=50, message="Bank length must be between 3 and 50")
	private String bank;
	
	@NotNull
	@NotEmpty(message="Account number name cannot be empty!")
	@Size(min=10, max=20, message="Account number length must be between 10 and 20")
	private String accountNo;
	
	@NotNull
	private String lessonTime;
	
	@Min(value = 0,message="Pay rate name cannot be empty!")
	private int rate;
	
	@DecimalMin(value = "0.0", message = "Total cannot be empty!")
	private double total;

	@Min(value = 0)
	private int status;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserAccount getMember() {
		return member;
	}

	public void setMember(UserAccount member) {
		this.member = member;
	}

	public ProgramRun getProgramrun() {
		return programrun;
	}

	public void setProgramrun(ProgramRun programrun) {
		this.programrun = programrun;
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

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getLessonTime() {
		return lessonTime;
	}

	public void setLessonTime(String lessonTime) {
		this.lessonTime = lessonTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	

	public java.util.Date getDate() {
		return date;
	}

	public void setDate(java.util.Date date) {
		this.date = date;
	}

	
}