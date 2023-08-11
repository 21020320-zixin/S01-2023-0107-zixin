package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimesheetRepository extends JpaRepository<Timesheet, Integer> {

	public List<Timesheet> findAllByMemberId(int id);

	public List<Timesheet> findAllByStatus(int i);


//	public Timesheet findByProgramrunId(int id);

	

	

}
