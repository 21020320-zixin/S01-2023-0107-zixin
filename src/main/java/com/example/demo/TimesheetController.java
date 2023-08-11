package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

import java.security.Principal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//@RestController
@Controller
//@RequestMapping("/timesheets")
public class TimesheetController {
	@Autowired
	private TimesheetRepository timesheetRepository;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private AccountRepository memberRepo;

	@Autowired
	private ProgramRunRepository programrunRepo;

	@GetMapping("/timesheet/add")
	public String addTimesheet(Model model) {
		List<UserAccount> mList = memberRepo.findAllByRole("ROLE_TRAINER");
		List<UserAccount> memberList = new ArrayList<>();
		for (UserAccount member : mList) {
			if (member.getRate().equals("Preferred")) {
				memberList.add(member);
			}
		}
		model.addAttribute("memberList", memberList);
		return "add_timesheet";
	}

	@GetMapping("/view/single/timesheet/{memberId}")
	public String viewSingleTimesheet(@PathVariable("memberId") Integer id, HttpSession session, Model model) {

		UserAccount member = memberRepo.getReferenceById(id);
		model.addAttribute("member", member);
		List<UserAccount> mList = memberRepo.findAllByRole("ROLE_TRAINER");
		List<UserAccount> memberList = new ArrayList<>();
		for (UserAccount m : mList) {
			if (m.getRate().equals("Preferred")) {
				memberList.add(m);
			}
		}
		model.addAttribute("memberList", memberList);

		// select Program-run by memberid
		List<ProgramRun> prList = programrunRepo.findAllByMemberId(id);
		List<ProgramRun> programrunList = new ArrayList<>();

		for (ProgramRun pr : prList) {
			if (pr.getStatus().equals("Completed")) {

				programrunList.add(pr);

			}
		}

//		filter by the programrun already added
		boolean flag = false;
		List<Timesheet> tsList = timesheetRepository.findAllByMemberId(id);
		List<ProgramRun> removeList = new ArrayList<>();

		for (Timesheet ts : tsList) {
			for (ProgramRun p : programrunList) {
				if (ts.getProgramrun().getId() == p.getId()) {
					removeList.add(p);
					System.out.println(ts.getProgramrun().getId() + " : " + p.getId());
					flag = true;
				}
			}
		}
		if (flag) {
			for (ProgramRun pr : removeList) {
				programrunList.remove(pr);
			}

		}

		model.addAttribute("programrunList", programrunList);
		List<ProgramRun> prunList = (List<ProgramRun>) session.getAttribute("prunList");
		if (prunList != null) {
			prunList.clear();
		}
		return "add_single_timesheet";
	}

	@PostMapping("/view/single/timesheet/{memberId}")
	public String viewLoadProgramRun(@PathVariable("memberId") Integer id, @RequestParam("programrunId") Integer prId,
			@RequestParam("action") String action, @RequestParam("accountName") String accountName,
			@RequestParam("accountNo") String accountNo, @RequestParam("bankName") String bankName, HttpSession session,
			Model model) {

		UserAccount member = memberRepo.getReferenceById(id);
		model.addAttribute("member", member);
		List<UserAccount> mList = memberRepo.findAllByRole("ROLE_TRAINER");
		List<UserAccount> memberList = new ArrayList<>();
		for (UserAccount m : mList) {
			if (m.getRate().equals("Preferred")) {
				memberList.add(m);
			}
		}
		model.addAttribute("memberList", memberList);

		System.out.println(prId);

		// select Program-run by memberid
		List<ProgramRun> prList = programrunRepo.findAllByMemberId(id);
		List<ProgramRun> programrunList = new ArrayList<>();
		for (ProgramRun pr : prList) {
			if (pr.getStatus().equals("Completed")) {
//				Timesheet timesheet=timesheetRepository.findByProgramrunId(pr.getId());
//				
//				if(timesheet.getProgramrun().equals(pr)) {
//					break;
//				}else {
				programrunList.add(pr);
//				}

			}
		}
//		filter by the programrun already added
		/*
		 * boolean flag=false; List<Timesheet> tsList =
		 * timesheetRepository.findAllByMemberId(id); // ProgramRun pr=new ProgramRun();
		 * int index=0; for (Timesheet ts : tsList) { // for (ProgramRun p :
		 * programrunList) { for(int i=0;i<programrunList.size();i++) { // if
		 * (ts.getProgramrun().getId() == p.getId()) {
		 * if(ts.getProgramrun().getId()==programrunList.get(i).getId()) {
		 * System.out.println(ts.getProgramrun().getId()+"="+programrunList.get(i).getId
		 * ()); // pr=p; index=i; flag=true; break; } } } if(flag) {
		 * programrunList.remove(index); }
		 */
//		filter by the programrun already added
		boolean flag = false;
		List<Timesheet> tsList = timesheetRepository.findAllByMemberId(id);
		List<ProgramRun> removeList = new ArrayList<>();

		for (Timesheet ts : tsList) {
			for (ProgramRun p : programrunList) {
				if (ts.getProgramrun().getId() == p.getId()) {
					removeList.add(p);
					System.out.println(ts.getProgramrun().getId() + " : " + p.getId());
					flag = true;
				}
			}
		}
		if (flag) {
			for (ProgramRun pr : removeList) {
				programrunList.remove(pr);
			}

		}
		model.addAttribute("programrunList", programrunList);

		// add Program-run
//    	List<ProgramRun> prunList = null;
//    	if(prunList==null) {
//    		prunList=new ArrayList<>();
//    	}
//    	
//    		prunList.add(programrunRepo.getReferenceById(prId));

		List<ProgramRun> prunList = (List<ProgramRun>) session.getAttribute("prunList");
		if (prunList == null) {
			prunList = new ArrayList<>();
		}
		boolean flag1 = true;
		for (ProgramRun prun : prunList) {
			if (prun.getId() == prId) {
				flag1 = false;
				break;
			}
		}
		if (flag1) {
			prunList.add(programrunRepo.getReferenceById(prId));
		}
		// for total
		int total = 0;
		int rate = Integer.valueOf(member.getHrsPay());
//		if (member.getRate().equals("hourly")) {
//			rate = 20;
//		} else {
//			rate = 30;
//		}
		for (ProgramRun prun : prunList) {

			Duration diff = Duration.between(prun.getStartTime(), prun.getEndTime());
			// pay rate - hourly=> 20, standard => 30
			// 60 min => 20 $, total mins = ?

			total += (int) (rate * diff.toMinutes() / 60);

			System.out.println("Single: " + total);

		}
		System.out.println(total);
		// end for total
		session.setAttribute("prunList", prunList);
		model.addAttribute("prunList", prunList);
		model.addAttribute("total", total);
		System.out.println("Single: " + prunList);
		System.out.println(action);
		if (action.equals("Add New Timesheet")) {
			member.setAccountNo(accountNo);
			member.setBank(bankName);
			member.setBankAccountName(accountName);
			memberRepo.save(member);

			//
			for (ProgramRun programrun : prunList) {
				Timesheet timesheet = new Timesheet();
				timesheet.setAccountNo(accountNo);
				timesheet.setBank(bankName);
				timesheet.setBankAccountName(accountName);
				timesheet.setRate(rate);
				timesheet.setMember(member);
				Duration diff = Duration.between(programrun.getStartTime(), programrun.getEndTime());
				timesheet.setLessonTime(diff.toHoursPart() + ":" + diff.toMinutesPart());
				// pay rate - hourly=> 20, standard => 30
				// 60 min => 20 $, total mins = ?

				total = (int) (rate * diff.toMinutes() / 60);
//				timesheet.setDate(new Date());
				timesheet.setTotal(total);
				timesheet.setProgramrun(programrun);
				timesheet.setStatus(0);
				timesheetRepository.save(timesheet);
			}
			return "redirect:/timesheets";

		} else {
			return "add_single_timesheet";
		}
//    	redirectAttributes.addAttribute("prunList", prunList);

	}

	@GetMapping("/timesheet/delete/{id}")
	public String deleteTimesheet(@PathVariable("id") Integer id) {
		timesheetRepository.deleteById(id);
		return "redirect:/timesheets";
	}

	// change data by monthly timesheet
	@GetMapping("/monthly/timesheet/edit/{memberId}")
	public String editMonthlyTimesheet(@PathVariable("memberId") Integer id, Model model) {
//		Integer id=Integer.valueOf(memberId);
		List<Timesheet> timesheetList = timesheetRepository.findAllByMemberId(id);
		Timesheet timesheet = new Timesheet();
		for (Timesheet ts : timesheetList) {
			if (ts.getStatus() == 0) {
				timesheet = ts;
				break;
			}
		}
//		Timesheet timesheet=timesheetRepository.getReferenceById(id);
//		System.out.println(timesheet);
		model.addAttribute("timesheet", timesheet);

		return "edit_monthly_timesheet";
	}

	@PostMapping("/monthly/timesheet/edit/{memberId}")
	public String saveUpdatedMonthlyTimesheet(@PathVariable("memberId") Integer id,
			@RequestParam("accountName") String accountName, @RequestParam("accountNo") String accountNo,
			@RequestParam("bankName") String bankName) {
		List<Timesheet> timesheetList = timesheetRepository.findAllByMemberId(id);
		for (Timesheet ts : timesheetList) {
			if (ts.getStatus() == 0) {
				ts.setAccountNo(accountNo);
				ts.setBank(bankName);
				ts.setBankAccountName(accountName);
				timesheetRepository.save(ts);
			}
		}
//		List<Timesheet> timesheetList=timesheetRepository.findAllByMemberId(timesheet.getMember().getId());
//		System.out.println(timesheetList);
//		for(Timesheet ts:timesheetList) {
//			ts.setAccountNo(accountNo);
//			ts.setBank(bankName);
//			ts.setBankAccountName(accountName);
//			timesheetRepository.save(ts);
//		}

		// also change member table
		UserAccount member = memberRepo.getReferenceById(id);
		member.setAccountNo(accountNo);
		member.setBank(accountName);
		member.setBankAccountName(bankName);
		memberRepo.save(member);
		return "redirect:/monthly/timesheets";

	}

	@GetMapping("/monthly/timesheet/delete/{id}")
	public String deleteMonthlyTimesheet(@PathVariable("id") Integer id) {
		List<Timesheet> timesheetList = timesheetRepository.findAllByMemberId(id);
		for (Timesheet ts : timesheetList) {
			if (ts.getStatus() == 0) {
				timesheetRepository.deleteById(ts.getId());
			}
		}
//		timesheetRepository.deleteById(id);
		return "redirect:/monthly/timesheets";
	}

	@GetMapping("/timesheet/edit/{id}")
	public String editTimesheet(@PathVariable("id") Integer id, Model model) {
		Timesheet timesheet = timesheetRepository.getReferenceById(id);
		System.out.println(timesheet);
		model.addAttribute("timesheet", timesheet);
		return "edit_timesheet";
	}

	@PostMapping("/timesheet/edit/{id}")
	public String saveUpdatedTimesheet(@PathVariable("id") Integer id, @RequestParam("accountName") String accountName,
			@RequestParam("accountNo") String accountNo, @RequestParam("bankName") String bankName) {
		Timesheet timesheet = timesheetRepository.getReferenceById(id);
//		timesheet.setAccountNo(accountNo);
//		timesheet.setBank(bankName);
//		timesheet.setBankAccountName(accountName);
//		timesheetRepository.save(timesheet);
		List<Timesheet> timesheetList = timesheetRepository.findAllByMemberId(timesheet.getMember().getId());
		System.out.println(timesheetList);
		for (Timesheet ts : timesheetList) {
			ts.setAccountNo(accountNo);
			ts.setBank(bankName);
			ts.setBankAccountName(accountName);
			timesheetRepository.save(ts);
		}

		// also change member table
		UserAccount member = memberRepo.getReferenceById(timesheet.getMember().getId());
		member.setAccountNo(timesheet.getAccountNo());
		member.setBank(timesheet.getBank());
		member.setBankAccountName(timesheet.getBankAccountName());
		memberRepo.save(member);
		return "redirect:/timesheets";

	}

	// payment
	@GetMapping("/pay/{id}")
	public String confirmPayment(@PathVariable("id") Integer timesheetId, Model model, Principal principal) {
		// get total
		model.addAttribute("timesheet", timesheetRepository.getReferenceById(timesheetId));
		return "confirm_transaction";
	}

	@PostMapping("/pay/{id}")
	public String transferSuccess(Model model, @PathVariable("id") Integer timesheetId,
			@RequestParam("transferAmount") String transferAmount, @RequestParam("accountNo") String accountNo,
			@RequestParam("transactionId") String transactionId, @RequestParam("email") String email,
			RedirectAttributes redirectAttributes) {

		Timesheet timesheet = timesheetRepository.getReferenceById(timesheetId);
		timesheet.setStatus(1);
		timesheet.setDate(new Date());
		timesheetRepository.save(timesheet);
		UserAccount member = memberRepo.getReferenceById(timesheet.getMember().getId());
		redirectAttributes.addFlashAttribute("success", "Already Sent!");
		// Send email
		String subject = "Payment Successful\n-" + transferAmount + " $";
		String body = "Thank you for your transaction!\n" + "Transaction ID:     " + transactionId
				+ "\nTransaction Time: " + new Date() + "\nTransaction Type: Transfer" + "\nTransfer To:      "
				+ member.getBankAccountName() + "\nAmount:           -" + transferAmount + " $";
		String subject1 = "Payment Successful\n+" + transferAmount + " $";
		String body1 = "Payment Received!\n" + "Transaction ID:     " + transactionId + "\nTransaction Time: "
				+ new Date() + "\nTransaction Type: Receive" + "\nTransfer To:      " + member.getBankAccountName()
				+ "\nAmount:           +" + transferAmount + " $";
		UserAccount admin = memberRepo.findByRole("ROLE_ADMIN");
		String to = member.getEmail();

		String cc = admin.getEmail();

		sendEmail(to, subject1, body1);
		sendEmail(cc, subject, body);//
		// change 'paid' in programrun
		ProgramRun pr = timesheet.getProgramrun();
		List<ProgramRun> prList = programrunRepo.findAll();
		for (ProgramRun prun : prList) {
			if (prun.getMember().equals(member) && prun.equals(pr)) {
				prun.setStatus("Paid");
				programrunRepo.save(prun);
				break;
			}
		}
		return "redirect:/timesheets";
	}

	public void sendEmail(String to, String subject, String body) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(body);

		System.out.println("Sending");
		javaMailSender.send(msg);
		System.out.println("Sent");

	}

//	
	@GetMapping("/remove/timesheet/{memberId}/{prId}")
	public String removeSingleProgramRun(@PathVariable("memberId") Integer id, @PathVariable("prId") Integer prId,
			HttpSession session, Model model) {
		UserAccount member = memberRepo.getReferenceById(id);
		model.addAttribute("member", member);
		List<UserAccount> memberList = memberRepo.findAllByRole("ROLE_TRAINER");
		model.addAttribute("memberList", memberList);

		// select Program-run by memberid
		List<ProgramRun> prList = programrunRepo.findAllByMemberId(id);
		List<ProgramRun> programrunList = new ArrayList<>();
		for (ProgramRun pr : prList) {
			if (pr.getStatus().equals("Completed")) {
				programrunList.add(pr);
			}
		}
//		filter by the programrun already added**************
		boolean flag = false;
		List<Timesheet> tsList = timesheetRepository.findAllByMemberId(id);
		List<ProgramRun> removeList = new ArrayList<>();

		for (Timesheet ts : tsList) {
			for (ProgramRun p : programrunList) {
				if (ts.getProgramrun().getId() == p.getId()) {
					removeList.add(p);
					System.out.println(ts.getProgramrun().getId() + " : " + p.getId());
					flag = true;
				}
			}
		}
		if (flag) {
			for (ProgramRun pr : removeList) {
				programrunList.remove(pr);
			}

		}

		model.addAttribute("programrunList", programrunList);
		System.out.println(prId);
		// add Program-run
//    	List<ProgramRun> prunList = null;
//    	if(prunList==null) {
//    		prunList=new ArrayList<>();
//    	}
//    	
//    		prunList.add(programrunRepo.getReferenceById(prId));

		List<ProgramRun> prunList = (List<ProgramRun>) session.getAttribute("prunList");
		if (prunList == null) {
			prunList = new ArrayList<>();
		}

		Iterator<ProgramRun> it = prunList.iterator();
		while (it.hasNext()) {
			ProgramRun prun = it.next();
			if (prun.getId() == prId) {
				it.remove();
			}
		}
		// for total
		int total = 0;
		int rate = Integer.valueOf(member.getHrsPay());
//		if (member.getRate().equals("hourly")) {
//			rate = 20;
//		} else {
//			rate = 30;
//		}
		for (ProgramRun prun : prunList) {

			Duration diff = Duration.between(prun.getStartTime(), prun.getEndTime());
			// pay rate - hourly=> 20, standard => 30
			// 60 min => 20 $, total mins = ?

			total += (int) (rate * diff.toMinutes() / 60);

		}

		session.setAttribute("prunList", prunList);
		model.addAttribute("total", total);
		model.addAttribute("prunList", prunList);
		return "add_single_timesheet";
	}

	@GetMapping("/timesheets")
	public String viewTimesheets(Model model) {
		List<Timesheet> tList = timesheetRepository.findAllByStatus(0);
		List<Timesheet> timesheetList = new ArrayList<>();
		for (Timesheet t : tList) {
			if (t.getMember().getRate().equals("Preferred")) {
				timesheetList.add(t);
			}
		}
		model.addAttribute("timesheetList", timesheetList);
		for (Timesheet timesheet : timesheetList) {
			Duration diff = Duration.between(timesheet.getProgramrun().getStartTime(),
					timesheet.getProgramrun().getEndTime());
//			System.out.println("Diff " + diff);
//			System.out.println("DIff Hours" + diff.toHours() + " " + diff.toHoursPart());
//			System.out.println("Diff Minutes: " + diff.toMinutesPart());
		}

		return "view_timesheets";
	}

	// filter preferred timesheet(each)
	@PostMapping("/timesheets")
	public String preferredTimesheetReport(Model model, @RequestParam("from") String from,
			@RequestParam("to") String to) throws ParseException {
		if (from.equals("") || to.equals(null) || from.equals("") || to.equals(null)) {
			model.addAttribute("message", "Please select date first!");
			return "view_timesheets";
		}

		// get date object
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date sd = formatter.parse(from);
		Date ed = formatter.parse(to);

		List<Timesheet> tList = timesheetRepository.findAllByStatus(0);
		List<Timesheet> timesheetList = new ArrayList<>();
		for (Timesheet t : tList) {
			if (t.getMember().getRate().equals("Preferred")) {
				Date current = t.getProgramrun().getAppointmentDate();
				if (current.after(sd) && current.before(ed)) {
					timesheetList.add(t);
				}
			}
		}

		// add list to model
		model.addAttribute("timesheetList", timesheetList);

		return "view_timesheets";

	}

	@GetMapping("/monthly/timesheets")
	public String viewMonthlyTimesheets(Model model) {
		List<Timesheet> tList = timesheetRepository.findAllByStatus(0);
		List<Timesheet> timesheetList = new ArrayList<>();
		for (Timesheet t : tList) {
			if (t.getMember().getRate().equals("Preferred")) {
				timesheetList.add(t);
			}
		}

		// change all programrun to timesheet
		HashMap<Integer, List<Timesheet>> tsMap = new HashMap<>();
		for (Timesheet ts : timesheetList) {
			List<Timesheet> tsheetList = new ArrayList<>();
			for (Timesheet ts1 : timesheetList) {
				if (ts.getMember().equals(ts1.getMember())) {
//					ProgramRun pr=ts1.getProgramrun();
//					programrunList.add(pr);
					tsheetList.add(ts1);
//					prMap.put(ts.getMember().getId(), programrunList);
					tsMap.put(ts.getMember().getId(), tsheetList);
				}
			}

		}
		for (Map.Entry<Integer, List<Timesheet>> entry : tsMap.entrySet()) {
			int memberId = entry.getKey();
			List<Timesheet> tsList = entry.getValue();
			for (Timesheet ts : tsList) {
				System.out.println(ts.getTotal());
			}
			break;
		}

		model.addAttribute("tsMap", tsMap);

		return "view_monthly_timesheets";
	}

	// filter preferred timesheet (all)
	@PostMapping("/monthly/timesheets")
	public String monthlyTimesheetReport(Model model, @RequestParam("from") String from, @RequestParam("to") String to)
			throws ParseException {
		if (from.equals("") || to.equals(null) || from.equals("") || to.equals(null)) {
			model.addAttribute("message", "Please select date first!");
			return "view_monthly_timesheets";
		}

		// get date object
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date sd = formatter.parse(from);
		Date ed = formatter.parse(to);

		List<Timesheet> tList = timesheetRepository.findAllByStatus(0);
		List<Timesheet> timesheetList = new ArrayList<>();
		for (Timesheet t : tList) {
			if (t.getMember().getRate().equals("Preferred")) {
				Date current = t.getProgramrun().getAppointmentDate();
				if (current.after(sd) && current.before(ed)) {
					timesheetList.add(t);
				}
			}
		}
		// change all programrun to timesheet
		HashMap<Integer, List<Timesheet>> tsMap = new HashMap<>();
		for (Timesheet ts : timesheetList) {
			List<Timesheet> tsheetList = new ArrayList<>();
			for (Timesheet ts1 : timesheetList) {
				if (ts.getMember().equals(ts1.getMember())) {
					tsheetList.add(ts1);
					tsMap.put(ts.getMember().getId(), tsheetList);
				}
			}
		}
		for (Map.Entry<Integer, List<Timesheet>> entry : tsMap.entrySet()) {
			int memberId = entry.getKey();
			List<Timesheet> tsList = entry.getValue();
			for (Timesheet ts : tsList) {
				System.out.println(ts.getTotal());
			}
			break;
		}
		// add hashmap to model
		model.addAttribute("tsMap", tsMap);

		return "view_monthly_timesheets";

	}

	// Standard Timesheets
	@GetMapping("/standard/timesheets")
	public String viewStandardTimesheets(Model model) {
		List<Timesheet> tList = timesheetRepository.findAllByStatus(0);
		List<Timesheet> timesheetList = new ArrayList<>();
		for (Timesheet t : tList) {
			if (t.getMember().getRate().equals("Standard")) {
				timesheetList.add(t);
			}
		}

		// change all programrun to timesheet
		HashMap<Integer, List<Timesheet>> tsMap = new HashMap<>();
		for (Timesheet ts : timesheetList) {
			List<Timesheet> tsheetList = new ArrayList<>();
			for (Timesheet ts1 : timesheetList) {
				if (ts.getMember().equals(ts1.getMember())) {
//					ProgramRun pr=ts1.getProgramrun();
//					programrunList.add(pr);
					tsheetList.add(ts1);
//					prMap.put(ts.getMember().getId(), programrunList);
					tsMap.put(ts.getMember().getId(), tsheetList);
				}
			}

		}
		for (Map.Entry<Integer, List<Timesheet>> entry : tsMap.entrySet()) {
			int memberId = entry.getKey();
			List<Timesheet> tsList = entry.getValue();
			for (Timesheet ts : tsList) {
//				System.out.println(ts.getTotal());
				model.addAttribute("member", ts.getMember());
			}
			break;
		}

		model.addAttribute("tsMap", tsMap);

		return "view_standard_timesheets";
	}

	// filter standard timesheet
	@PostMapping("/standard/timesheets")
	public String standardTimesheetReport(Model model, @RequestParam("from") String from, @RequestParam("to") String to)
			throws ParseException {
		if (from.equals("") || to.equals(null) || from.equals("") || to.equals(null)) {
			model.addAttribute("message", "Please select date first!");
			return "view_standard_timesheets";
		}

		// get date object
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date sd = formatter.parse(from);
		Date ed = formatter.parse(to);

		List<Timesheet> tList = timesheetRepository.findAllByStatus(0);
		List<Timesheet> timesheetList = new ArrayList<>();
		for (Timesheet t : tList) {
			if (t.getMember().getRate().equals("Standard")) {
				Date current = t.getProgramrun().getAppointmentDate();
				if (current.after(sd) && current.before(ed)) {
					timesheetList.add(t);
				}
			}
		}
		// change all programrun to timesheet
		HashMap<Integer, List<Timesheet>> tsMap = new HashMap<>();
		for (Timesheet ts : timesheetList) {
			List<Timesheet> tsheetList = new ArrayList<>();
			for (Timesheet ts1 : timesheetList) {
				if (ts.getMember().equals(ts1.getMember())) {
					tsheetList.add(ts1);
					tsMap.put(ts.getMember().getId(), tsheetList);
				}
			}

		}
		for (Map.Entry<Integer, List<Timesheet>> entry : tsMap.entrySet()) {
			int memberId = entry.getKey();
			List<Timesheet> tsList = entry.getValue();
			for (Timesheet ts : tsList) {
				model.addAttribute("member", ts.getMember());
			}
			break;
		}
		// add hashmap to model
		model.addAttribute("tsMap", tsMap);

		return "view_standard_timesheets";
	}

	// add standard
	@GetMapping("/standard/timesheet/add")
	public String addStandardTimesheet(Model model) {
		List<UserAccount> mList = memberRepo.findAllByRole("ROLE_TRAINER");
		List<UserAccount> memberList = new ArrayList<>();
		for (UserAccount member : mList) {
			if (member.getRate().equals("Standard")) {
				memberList.add(member);
			}
		}
		model.addAttribute("memberList", memberList);
		return "add_standard_timesheet";
	}

	@GetMapping("/view/single/standard/timesheet/{memberId}")
	public String viewSingleStandardTimesheet(@PathVariable("memberId") Integer id, HttpSession session, Model model) {

		UserAccount member = memberRepo.getReferenceById(id);
		model.addAttribute("member", member);
		List<UserAccount> mList = memberRepo.findAllByRole("ROLE_TRAINER");
		List<UserAccount> memberList = new ArrayList<>();
		for (UserAccount m : mList) {
			if (m.getRate().equals("Standard")) {
				memberList.add(m);
			}
		}
		model.addAttribute("memberList", memberList);

		// select Program-run by memberid
		List<ProgramRun> prList = programrunRepo.findAllByMemberId(id);
		List<ProgramRun> programrunList = new ArrayList<>();

		for (ProgramRun pr : prList) {
			if (pr.getStatus().equals("Completed")) {

				programrunList.add(pr);

			}
		}

//		filter by the programrun already added
		boolean flag = false;
		List<Timesheet> tsList = timesheetRepository.findAllByMemberId(id);
		List<ProgramRun> removeList = new ArrayList<>();

		for (Timesheet ts : tsList) {
			for (ProgramRun p : programrunList) {
				if (ts.getProgramrun().getId() == p.getId()) {
					removeList.add(p);
					System.out.println(ts.getProgramrun().getId() + " : " + p.getId());
					flag = true;
				}
			}
		}
		if (flag) {
			for (ProgramRun pr : removeList) {
				programrunList.remove(pr);
			}

		}

		model.addAttribute("programrunList", programrunList);
		List<ProgramRun> prunList = (List<ProgramRun>) session.getAttribute("prunList");
		if (prunList != null) {
			prunList.clear();
		}
		return "add_single_standard_timesheet";
	}

	@Transactional
	@PostMapping("/view/single/standard/timesheet/{memberId}")
	public String viewLoadStandardProgramRun(@PathVariable("memberId") Integer id,
			@RequestParam("programrunId") Integer prId, @RequestParam("action") String action,
			@RequestParam("accountName") String accountName, @RequestParam("accountNo") String accountNo,
			@RequestParam("bankName") String bankName, HttpSession session, Model model) {
		UserAccount member = memberRepo.getReferenceById(id);
		model.addAttribute("member", member);

		List<UserAccount> mList = memberRepo.findAllByRole("ROLE_TRAINER");
		List<UserAccount> memberList = new ArrayList<>();
		for (UserAccount m : mList) {
			if (m.getRate().equals("Standard")) {
				memberList.add(m);
			}
		}
		model.addAttribute("memberList", memberList);

		System.out.println(prId);

		// select Program-run by memberid
		List<ProgramRun> prList = programrunRepo.findAllByMemberId(id);
		List<ProgramRun> programrunList = new ArrayList<>();
		for (ProgramRun pr : prList) {
			if (pr.getStatus().equals("Completed")) {
//				Timesheet timesheet=timesheetRepository.findByProgramrunId(pr.getId());
//				
//				if(timesheet.getProgramrun().equals(pr)) {
//					break;
//				}else {
				programrunList.add(pr);
//				}

			}
		}
//		filter by the programrun already added
		boolean flag = false;
		List<Timesheet> tsList = timesheetRepository.findAllByMemberId(id);
		List<ProgramRun> removeList = new ArrayList<>();

		for (Timesheet ts : tsList) {
			for (ProgramRun p : programrunList) {
				if (ts.getProgramrun().getId() == p.getId()) {
					removeList.add(p);
					System.out.println(ts.getProgramrun().getId() + " : " + p.getId());
					flag = true;
				}
			}
		}
		if (flag) {
			for (ProgramRun pr : removeList) {
				programrunList.remove(pr);
			}

		}
		model.addAttribute("programrunList", programrunList);

		// add Program-run
//    	List<ProgramRun> prunList = null;
//    	if(prunList==null) {
//    		prunList=new ArrayList<>();
//    	}
//    	
//    		prunList.add(programrunRepo.getReferenceById(prId));

		List<ProgramRun> prunList = (List<ProgramRun>) session.getAttribute("prunList");
		if (prunList == null) {
			prunList = new ArrayList<>();
		}
		boolean flag1 = true;
		for (ProgramRun prun : prunList) {
			if (prun.getId() == prId) {
				flag1 = false;
				break;
			}
		}
		if (flag1) {
			prunList.add(programrunRepo.getReferenceById(prId));
		}
		// for total
		int total = 0;
		int rate = 0;
//		if (member.getRate().equals("hourly")) {
//			rate = 20;
//		} else {
//			rate = 30;
//		}
//		for (ProgramRun prun : prunList) {
//
//			Duration diff = Duration.between(prun.getStartTime(), prun.getEndTime());
//			// pay rate - hourly=> 20, standard => 30
//			// 60 min => 20 $, total mins = ?
//
//			total += (int) (rate * diff.toMinutes() / 60);
//
//			System.out.println("Single: " + total);
//
//		}
//		System.out.println(total);
		// end for total
		session.setAttribute("prunList", prunList);
		model.addAttribute("prunList", prunList);
//		model.addAttribute("total", total);
		System.out.println("Single: " + prunList);
		System.out.println(action);
		if (action.equals("Add New Timesheet")) {
			member.setAccountNo(accountNo);
			member.setBank(bankName);
			member.setBankAccountName(accountName);
			memberRepo.save(member);

			//
			for (ProgramRun programrun : prunList) {
				Timesheet timesheet = new Timesheet();
				timesheet.setAccountNo(accountNo);
				timesheet.setBank(bankName);
				timesheet.setBankAccountName(accountName);
				timesheet.setRate(rate);
				timesheet.setMember(member);
				Duration diff = Duration.between(programrun.getStartTime(), programrun.getEndTime());
				timesheet.setLessonTime(diff.toHoursPart() + ":" + diff.toMinutesPart());
				// pay rate - hourly=> 20, standard => 30
				// 60 min => 20 $, total mins = ?

				total = (int) (rate * diff.toMinutes() / 60);
//				timesheet.setDate(new Date());
				timesheet.setTotal(0);
				timesheet.setProgramrun(programrun);
				timesheet.setStatus(0);
				timesheetRepository.save(timesheet);
			}
			return "redirect:/standard/timesheets";

		} else {
			return "add_single_standard_timesheet";
		}
//    	redirectAttributes.addAttribute("prunList", prunList);

	}

	// remove standard timesheet
	@GetMapping("/remove/standard/timesheet/{memberId}/{prId}")
	public String removeSingleStandardProgramRun(@PathVariable("memberId") Integer id,
			@PathVariable("prId") Integer prId, HttpSession session, Model model) {
		UserAccount member = memberRepo.getReferenceById(id);
		model.addAttribute("member", member);
		List<UserAccount> memberList = memberRepo.findAllByRole("ROLE_TRAINER");
		model.addAttribute("memberList", memberList);

		// select Program-run by memberid
		List<ProgramRun> prList = programrunRepo.findAllByMemberId(id);
		List<ProgramRun> programrunList = new ArrayList<>();
		for (ProgramRun pr : prList) {
			if (pr.getStatus().equals("Completed")) {
				programrunList.add(pr);
			}
		}
//		filter by the programrun already added
		boolean flag = false;
		List<Timesheet> tsList = timesheetRepository.findAllByMemberId(id);
		List<ProgramRun> removeList = new ArrayList<>();

		for (Timesheet ts : tsList) {
			for (ProgramRun p : programrunList) {
				if (ts.getProgramrun().getId() == p.getId()) {
					removeList.add(p);
					System.out.println(ts.getProgramrun().getId() + " : " + p.getId());
					flag = true;
				}
			}
		}
		if (flag) {
			for (ProgramRun pr : removeList) {
				programrunList.remove(pr);
			}

		}

		model.addAttribute("programrunList", programrunList);
		System.out.println(prId);
		// add Program-run
//    	List<ProgramRun> prunList = null;
//    	if(prunList==null) {
//    		prunList=new ArrayList<>();
//    	}
//    	
//    		prunList.add(programrunRepo.getReferenceById(prId));

		List<ProgramRun> prunList = (List<ProgramRun>) session.getAttribute("prunList");
		if (prunList == null) {
			prunList = new ArrayList<>();
		}

		Iterator<ProgramRun> it = prunList.iterator();
		while (it.hasNext()) {
			ProgramRun prun = it.next();
			if (prun.getId() == prId) {
				it.remove();
			}
		}
		// for total
		int total = 0;
//		int rate = Integer.valueOf(member.getHrsPay());
//		if (member.getRate().equals("hourly")) {
//			rate = 20;
//		} else {
//			rate = 30;
//		}

		// no need total of standard for each programrun
//		for (ProgramRun prun : prunList) {
//
//			Duration diff = Duration.between(prun.getStartTime(), prun.getEndTime());
//			// pay rate - hourly=> 20, standard => 30
//			// 60 min => 20 $, total mins = ?
//
//			total += (int) (rate * diff.toMinutes() / 60);
//
//		}

		session.setAttribute("prunList", prunList);
//		model.addAttribute("total", total);
		model.addAttribute("prunList", prunList);
		return "add_single_standard_timesheet";
	}

	// edit standard timesheet
	@GetMapping("/standard/timesheet/edit/{memberId}")
	public String editStandardTimesheet(@PathVariable("memberId") Integer id, Model model) {
//			Integer id=Integer.valueOf(memberId);
		List<Timesheet> timesheetList = timesheetRepository.findAllByMemberId(id);
		Timesheet timesheet = new Timesheet();
		for (Timesheet ts : timesheetList) {
			if (ts.getStatus() == 0) {
				timesheet = ts;
				break;
			}
		}

		model.addAttribute("timesheet", timesheet);

		return "edit_standard_timesheet";
	}

	@PostMapping("/standard/timesheet/edit/{memberId}")
	public String saveUpdatedStandardTimesheet(@PathVariable("memberId") Integer id,
			@RequestParam("accountName") String accountName, @RequestParam("accountNo") String accountNo,
			@RequestParam("bankName") String bankName) {
		List<Timesheet> timesheetList = timesheetRepository.findAllByMemberId(id);
		for (Timesheet ts : timesheetList) {
			if (ts.getStatus() == 0) {
				ts.setAccountNo(accountNo);
				ts.setBank(bankName);
				ts.setBankAccountName(accountName);
				timesheetRepository.save(ts);
			}
		}
		// also change member table
		UserAccount member = memberRepo.getReferenceById(id);
		member.setAccountNo(accountNo);
		member.setBank(bankName);
		member.setBankAccountName(accountName);
		memberRepo.save(member);
		return "redirect:/standard/timesheets";

	}

	@GetMapping("/standard/timesheet/delete/{id}")
	public String deleteStandardTimesheet(@PathVariable("id") Integer id) {
		List<Timesheet> timesheetList = timesheetRepository.findAllByMemberId(id);
		for (Timesheet ts : timesheetList) {
			if (ts.getStatus() == 0) {
				timesheetRepository.deleteById(ts.getId());
			}
		}
//			timesheetRepository.deleteById(id);
		return "redirect:/standard/timesheets";
	}

	// standard payment
	@GetMapping("/standard/pay/{memberId}")
	public String confirmStandardPayment(@PathVariable("memberId") Integer memberId, Model model) {

//				model.addAttribute("timesheet", timesheetRepository.getReferenceById(timesheetId));
		List<Timesheet> tsList = timesheetRepository.findAllByMemberId(memberId);
		List<Timesheet> timesheetList = new ArrayList<>();
		Timesheet timesheet = new Timesheet();
		double total = 0;
		int hour = 0, minutes = 0;
		for (Timesheet ts : tsList) {
			if (ts.getStatus() == 0) {
				timesheetList.add(ts);
				timesheet = ts;
//					total += ts.getTotal();
				Duration diff = Duration.between(ts.getProgramrun().getStartTime(), ts.getProgramrun().getEndTime());
				hour += diff.toHoursPart();
				minutes += diff.toMinutesPart();

			}
		}
		if (minutes >= 60) {
			hour += minutes / 60;
			minutes = minutes % 60;
		}
		UserAccount member = memberRepo.getReferenceById(memberId);
		total = Double.valueOf(member.getSalary());
		model.addAttribute("hour", hour);
		model.addAttribute("minutes", minutes);
		model.addAttribute("total", total);
		model.addAttribute("timesheet", timesheet);
		model.addAttribute("timesheetList", timesheetList);
		return "confirm_standard_transaction";
	}

	@PostMapping("/standard/pay/{memberId}")
	public String transferStandardSuccess(Model model, @PathVariable("memberId") Integer memberId,
			@RequestParam("transferAmount") String transferAmount, @RequestParam("accountNo") String accountNo,
			@RequestParam("transactionId") String transactionId, @RequestParam("email") String email,
			RedirectAttributes redirectAttributes) {

		List<Timesheet> timesheetList = timesheetRepository.findAllByMemberId(memberId);
		for (Timesheet timesheet : timesheetList) {
			timesheet.setStatus(1);
			timesheet.setDate(new Date());
			timesheetRepository.save(timesheet);
			ProgramRun pr = programrunRepo.getReferenceById(timesheet.getProgramrun().getId());
			pr.setStatus("Paid");
			programrunRepo.save(pr);
		}
		UserAccount member = memberRepo.getReferenceById(memberId);
		redirectAttributes.addFlashAttribute("success", "Already Sent!");

		// Send email
		String subject = "Payment Successful\n-" + transferAmount + " $";
		String body = "Thank you for your transaction!\n" + "Transaction ID:     " + transactionId
				+ "\nTransaction Time: " + new Date() + "\nTransaction Type: Transfer" + "\nTransfer To:      "
				+ member.getBankAccountName() + "\nAmount:           -" + transferAmount + " $";
		String subject1 = "Payment Successful\n+" + transferAmount + " $";
		String body1 = "Payment Received!\n" + "Transaction ID:     " + transactionId + "\nTransaction Time: "
				+ new Date() + "\nTransaction Type: Receive" + "\nTransfer To:      " + member.getBankAccountName()
				+ "\nAmount:           +" + transferAmount + " $";
		UserAccount admin = memberRepo.findByRole("ROLE_ADMIN");
		String to = member.getEmail();

		String cc = admin.getEmail();

		sendEmail(to, subject1, body1);
		sendEmail(cc, subject, body);//
		// change 'paid' in programrun

		return "redirect:/standard/timesheets";
	}

	// monthly payment
	// payment
	@GetMapping("/monthly/pay/{memberId}")
	public String confirmMonthlyPayment(@PathVariable("memberId") Integer memberId, Model model) {

//			model.addAttribute("timesheet", timesheetRepository.getReferenceById(timesheetId));
		List<Timesheet> tsList = timesheetRepository.findAllByMemberId(memberId);
		List<Timesheet> timesheetList = new ArrayList<>();
		Timesheet timesheet = new Timesheet();
		double total = 0;
		int hour = 0, minutes = 0;
		for (Timesheet ts : tsList) {
			if (ts.getStatus() == 0) {
				timesheetList.add(ts);
				timesheet = ts;
				total += ts.getTotal();
				Duration diff = Duration.between(ts.getProgramrun().getStartTime(), ts.getProgramrun().getEndTime());
				hour += diff.toHoursPart();
				minutes += diff.toMinutesPart();

			}
		}
		if (minutes >= 60) {
			hour += minutes / 60;
			minutes = minutes % 60;
		}
		model.addAttribute("hour", hour);
		model.addAttribute("minutes", minutes);
		model.addAttribute("total", total);
		model.addAttribute("timesheet", timesheet);
		model.addAttribute("timesheetList", timesheetList);
		return "confirm_monthly_transaction";
	}

	@PostMapping("/monthly/pay/{memberId}")
	public String transferMonthlySuccess(Model model, @PathVariable("memberId") Integer memberId,
			@RequestParam("transferAmount") String transferAmount, @RequestParam("accountNo") String accountNo,
			@RequestParam("transactionId") String transactionId, @RequestParam("email") String email,
			RedirectAttributes redirectAttributes) {

		List<Timesheet> timesheetList = timesheetRepository.findAllByMemberId(memberId);
		for (Timesheet timesheet : timesheetList) {
			timesheet.setStatus(1);
			timesheet.setDate(new Date());
			timesheetRepository.save(timesheet);
			ProgramRun pr = programrunRepo.getReferenceById(timesheet.getProgramrun().getId());
			pr.setStatus("Paid");
			programrunRepo.save(pr);
		}
		UserAccount member = memberRepo.getReferenceById(memberId);
		redirectAttributes.addFlashAttribute("success", "Already Sent!");

		// Send email
		String subject = "Payment Successful\n-" + transferAmount + " $";
		String body = "Thank you for your transaction!\n" + "Transaction ID:     " + transactionId
				+ "\nTransaction Time: " + new Date() + "\nTransaction Type: Transfer" + "\nTransfer To:      "
				+ member.getBankAccountName() + "\nAmount:           -" + transferAmount + " $";
		String subject1 = "Payment Successful\n+" + transferAmount + " $";
		String body1 = "Payment Received!\n" + "Transaction ID:     " + transactionId + "\nTransaction Time: "
				+ new Date() + "\nTransaction Type: Receive" + "\nTransfer To:      " + member.getBankAccountName()
				+ "\nAmount:           +" + transferAmount + " $";
		UserAccount admin = memberRepo.findByRole("ROLE_ADMIN");
		String to = member.getEmail();

		String cc = admin.getEmail();

		sendEmail(to, subject1, body1);
		sendEmail(cc, subject, body);//
		// change 'paid' in programrun

		return "redirect:/monthly/timesheets";
	}

	@GetMapping("/view/single/payment/{id}")
	public String viewSinglePayment(@PathVariable("id") Integer tsheetId, Model model) {
		Timesheet timesheet = timesheetRepository.getReferenceById(tsheetId);
		model.addAttribute("timesheet", timesheet);
		return "view_single_payment";

	}

}
