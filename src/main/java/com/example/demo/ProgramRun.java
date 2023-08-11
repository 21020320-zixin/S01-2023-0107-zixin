package com.example.demo;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class ProgramRun {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="account_id")
	private UserAccount member;
	
	@ManyToOne
	@JoinColumn(name="program_id")
	private Program program;
	
	@OneToOne(mappedBy = "programrun")
	private Timesheet timesheet;
	
	@OneToOne(mappedBy = "programrun")
	private Event event;
	
	/*
	 * @OneToMany(mappedBy="programrun") private Set<Timesheet> timesheet;
	 */
    
	
	@Column(name = "appointment_date", nullable = false)
//	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd",timezone = "UTC")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date appointmentDate;
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm",timezone = "UTC")
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIME)
	private LocalTime  startTime;
	
//	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "HH:mm",timezone = "UTC")
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIME)
	private LocalTime endTime;
	
	private String venue;
	
	private String status;
	

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

	/*
	 * public Set<Timesheet> getTimesheet() { return timesheet; }
	 * 
	 * public void setTimesheet(Set<Timesheet> timesheet) { this.timesheet =
	 * timesheet; }
	 */

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Date getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getVenue() {
		return venue;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	

}
