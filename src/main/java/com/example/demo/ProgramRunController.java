package com.example.demo;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Controller
public class ProgramRunController {
	@Autowired
	private ProgramRunRepository programRunRepo;

	@Autowired
	private ProgramRepository programRepo;

	@Autowired
	private TimesheetRepository timesheetRepo;

	@Autowired
	private AccountRepository memberRepo;

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@GetMapping("/programrun")
	public String runProgram(Model model, Principal principal) {
		AccountDetails loggedInMember = (AccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getAccount().getId();
		UserAccount member = memberRepo.getReferenceById(loggedInMemberId);
		model.addAttribute("member", member);
		List<ProgramRun> programrunList = programRunRepo.findAll();
		model.addAttribute("programrunList", programrunList);

		return "view_programrun";
	}

	@PostMapping("/programrun")
	public String programrunReport(Model model, @RequestParam("from") String from, @RequestParam("to") String to,
			Principal principal) throws ParseException {
		if (from.equals("") || to.equals(null) || from.equals("") || to.equals(null)) {
			model.addAttribute("message", "Please select date first!");
			return "view_trainers_programrun";
		}

		// Get currently logged in user
		AccountDetails loggedInMember = (AccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getAccount().getId();
		UserAccount member = memberRepo.getReferenceById(loggedInMemberId);
		
		model.addAttribute("member", member);
		// get date object
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date sd = formatter.parse(from);
		Date ed = formatter.parse(to);

		// get all transactions by Date
		List<ProgramRun> pList = programRunRepo.findAll();
		List<ProgramRun> programrunList = new ArrayList<>();
		for (ProgramRun pr : pList) {
			Date current = pr.getAppointmentDate();
			System.out.println(current);
			if (current.after(sd) && current.before(ed)) {
				programrunList.add(pr);

			}

		}

		// add list to model
		model.addAttribute("programrunList", programrunList);

		return "view_programrun";

	}

	@GetMapping("/programrun/add")
	public String addProgramRun(Model model) {
		ProgramRun programrun = new ProgramRun();// create object
		model.addAttribute("programrun", programrun);
		List<Program> programList = programRepo.findAll();
		model.addAttribute("programList", programList);
		return "add_program_run";
	}

	@PostMapping("/programrun/save")
	public String saveProgramRun(ProgramRun programrun, Principal principal) {
		// Get currently logged in user
		AccountDetails loggedInMember = (AccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getAccount().getId();
		UserAccount member = memberRepo.getReferenceById(loggedInMemberId);
//			System.out.println(programrun.getVenue());
//			if(programrun.getVenue().equals("1")) {
//				programrun.setVenue("Opera Estate Primary School");
//			}
//			else if(programrun.getVenue().equals("2")) {
//				programrun.setVenue("Meridian Secondary School");
//			}
//			else {
//				programrun.setVenue("School of Science and Technology");
//			}
		
		programrun.setMember(member);

		programrun.setStatus("Book");

		programRunRepo.save(programrun);

		Event event = new Event();
		if (programrun.getId() % 5 == 0) {
			event.setColor("BLUE");
		} else if (programrun.getId() % 4 == 0) {
			event.setColor("GREEN");
		} else if (programrun.getId() % 3 == 0) {
			event.setColor("YELLOW");
		} else {
			event.setColor("RED");
		}
		LocalDate date = LocalDate.of(programrun.getAppointmentDate().getYear(),
				programrun.getAppointmentDate().getMonth(), programrun.getAppointmentDate().getDate());
		LocalTime time = LocalTime.of(programrun.getStartTime().getHour(), programrun.getStartTime().getMinute(),
				programrun.getStartTime().getSecond());
		LocalTime time1 = LocalTime.of(programrun.getEndTime().getHour(), programrun.getEndTime().getMinute(),
				programrun.getEndTime().getSecond());
		LocalDateTime dateTimeStart = LocalDateTime.of(programrun.getAppointmentDate().getYear() + 1900,
				programrun.getAppointmentDate().getMonth() + 1, programrun.getAppointmentDate().getDate(),
				programrun.getStartTime().getHour(), programrun.getStartTime().getMinute(),
				programrun.getStartTime().getSecond());
		System.out.println(dateTimeStart);

		LocalDateTime dateTimeEnd = LocalDateTime.of(programrun.getAppointmentDate().getYear() + 1900,
				programrun.getAppointmentDate().getMonth() + 1, programrun.getAppointmentDate().getDate(),
				programrun.getEndTime().getHour(), programrun.getEndTime().getMinute(),
				programrun.getEndTime().getSecond());
		event.setStart(dateTimeStart);
		event.setEnd(dateTimeEnd);
		String text = programrun.getProgram().getName() + "\n" + programrun.getVenue();
		event.setText(text);
		event.setProgramrun(programrun);
		eventRepository.save(event);
		return "redirect:/programrun";
	}

	@GetMapping("/trainers/programrun")
	public String viewTrainsersProgram(Model model) {
		List<ProgramRun> pList = programRunRepo.findAll();
		List<ProgramRun> programrunList = new ArrayList<>();
		for (ProgramRun programRun : pList) {
			if (programRun.getAppointmentDate().after(new Date())) {
				programrunList.add(programRun);
			}
		}
		model.addAttribute("programrunList", programrunList);
		return "view_trainers_programrun";
	}

	// same with view_trainers_programrun
	@GetMapping("/trainer/programrun")
	public String viewIndexTrainers(Model model) {
		List<ProgramRun> programrunList = programRunRepo.findAll();
		model.addAttribute("programrunList", programrunList);
		return "index_trainer";
	}
	@GetMapping("/programrun/delete/{id}")
	public String deleteProgramrun(@PathVariable("id") Integer id) {
		programRunRepo.deleteById(id);
		return "redirect:/programrun";
	}

	@GetMapping("/range/report")
	public String rangeReport(Model model) {
		return "view_range_programrun";
	}

	@PostMapping("/range/report")
	public String rangeReport(Model model, @RequestParam("from") String from,
			@RequestParam("to") String to/* , Principal principal */) throws ParseException {
		if (from.equals("") || to.equals(null) || from.equals("") || to.equals(null)) {
			model.addAttribute("message", "Please select date first!");
			return "view_trainers_programrun";
		}

		// Get currently logged in user
//		MemberDetails loggedInMember = (MemberDetails) SecurityContextHolder.getContext().getAuthentication()
//				.getPrincipal();
//		int loggedInMemberId = loggedInMember.getMember().getId();

		// get date object
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date sd = formatter.parse(from);
		Date ed = formatter.parse(to);

		// get all transactions by Date
		List<ProgramRun> pList = programRunRepo.findAll();
		List<ProgramRun> programrunList = new ArrayList<>();
		for (ProgramRun pr : pList) {
			Date current = pr.getAppointmentDate();
			System.out.println(current);
			if (current.after(sd) && current.before(ed)) {
				programrunList.add(pr);

			}

		}

		// add list to model
		model.addAttribute("programrunList", programrunList);

		return "view_range_programrun";

	}

	@GetMapping("/programrun/edit/{id}")
	public String editProgramrun(@PathVariable("id") Integer id, Model model) {

		ProgramRun programrun = programRunRepo.getReferenceById(id);
		model.addAttribute("programrun", programrun);

		List<Program> programList = programRepo.findAll();
		model.addAttribute("programList", programList);

		return "edit_program_trainer";
	}
	
	

//	@GetMapping("/programrun/changeStatus/{id}")
//	public String changeStatus(@PathVariable("id") Integer id, Model model) {
//		// Get currently logged in user
//		AccountDetails loggedInMember = (AccountDetails) SecurityContextHolder.getContext().getAuthentication()
//				.getPrincipal();
//		int loggedInMemberId = loggedInMember.getAccount().getId();
//		UserAccount member = memberRepo.getReferenceById(loggedInMemberId);
//		ProgramRun programrun = programRunRepo.getReferenceById(id);
//		if (programrun.getStatus().equals("Book")) {
//			programrun.setMember(member);
//			programrun.setStatus("Unbook");
//		} else {
//			programrun.setMember(null);
//			programrun.setStatus("Book");
//		}
//		programRunRepo.save(programrun);
//
//		List<ProgramRun> programrunList = programRunRepo.findAll();
//		model.addAttribute("programrunList", programrunList);
//		return "redirect:/trainers/programrun";
//
//	}
	
	@GetMapping("/programrun/changeStatus/{id}")
	public String changeStatus(@PathVariable("id") Integer id, Model model) {
	    // Get currently logged in user
	    AccountDetails loggedInMember = (AccountDetails) SecurityContextHolder.getContext().getAuthentication()
	            .getPrincipal();
	    int loggedInMemberId = loggedInMember.getAccount().getId();
	    UserAccount member = memberRepo.getById(loggedInMemberId);
	    ProgramRun programrun = programRunRepo.getById(id);
	    if (programrun.getStatus().equals("Book")) {
	        programrun.setMember(member);
	        programrun.setStatus("Unbook");
	        Event event = new Event();
	        String subject = "Booking Confirmation!";
	        String content = "Your slot has been booked:\n";
	        content += "Trainer: " + programrun.getMember().getFirstName() + " " + programrun.getMember().getLastName();
	        content += "\n Date: " + programrun.getAppointmentDate();
	        content += "\n Start Time: " + programrun.getStartTime();
	        content += "\n End Time: " + programrun.getEndTime();
	        content += "\n Venue: " + programrun.getVenue();

	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom("thiritharclown@gmail.com");
	        message.setTo(member.getEmail());
	        message.setSubject(subject);
	        message.setText(content);

	        javaMailSender.send(message);
	    } else {
	        programrun.setMember(null);
	        programrun.setStatus("Book");

	        // Send email
	    }

	    programRunRepo.save(programrun);

	    List<ProgramRun> programrunList = programRunRepo.findAll();
	    model.addAttribute("programrunList", programrunList);
	    return "redirect:/trainers/programrun";
	}
	

	@GetMapping("/index/trainer")
	public String indexTrainer() {
		return "index_trainer";
	}

	@PostMapping("/programrun/edit/{id}")
	public String saveUpdatedProgramRunInTimesheet(ProgramRun programRun, @PathVariable("id") Integer id, Model model,
			Principal principal) {

		// Get currently logged in user
		AccountDetails loggedInMember = (AccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getAccount().getId();
		UserAccount member = memberRepo.getReferenceById(loggedInMemberId);

		programRun.setStatus("Completed");
		programRun.setMember(member);
		programRunRepo.save(programRun);

		// delete completed programrun from event
		List<Event> eventList = eventRepository.findAll();
		for (Event event : eventList) {
			if (event.getProgramrun().getId() == id) {
				eventRepository.deleteById(event.getId());
				break;
			}
		}
		System.out.println(id);
		return "redirect:/trainers/programrun";

	}
	@GetMapping("/delete/programrun/{id}")
	public String deleteProgramrun(@PathVariable("id") Integer id, Model model, Principal principal) {
		
		programRunRepo.deleteById(id);
		
		// delete completed programrun from event

		// ***********************
		List<Event> eventList = eventRepository.findAll();
		for (Event event : eventList) {
			if (event.getProgramrun().getId() == id) {
				eventRepository.deleteById(event.getId());
				break;
			}
		}
		return "redirect:/programrun";
	}
	@GetMapping("/admin/programrun/edit/{id}")
	public String admineditProgramrun(@PathVariable("id") Integer id, Model model) {

		ProgramRun programrun = programRunRepo.getReferenceById(id);
		model.addAttribute("programrun", programrun);

		List<Program> programList = programRepo.findAll();
		model.addAttribute("programList", programList);

		return "edit_adminprogramrun_trainer";
	}
	@PostMapping("/admin/programrun/edit/{id}")
	public String editUpdatedProgramRunInTimesheet(ProgramRun programRun, @PathVariable("id") Integer id, Model model,
			Principal principal) {

		// Get currently logged in user
		AccountDetails loggedInMember = (AccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getAccount().getId();
		UserAccount member = memberRepo.getReferenceById(loggedInMemberId);

		programRun.setStatus("Book");
		programRun.setMember(member);
		programRunRepo.save(programRun);

		// save completed programrun from event
		Event event= eventRepository.findByprogramrunId(id);
		event.setProgramrun(programRun);
		eventRepository.save(event);
		return "redirect:/programrun";

	}

}
