package com.example.demo;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentController {
	@Autowired
	TimesheetRepository timesheetRepository;
	
	@Autowired
	AccountRepository memberRepository;
	
	@GetMapping("/pay/list")
	public String paymentList(Model model,Principal principal) {

		// Get currently logged in user
		AccountDetails loggedInMember = (AccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		int loggedInMemberId = loggedInMember.getAccount().getId();
		UserAccount member=memberRepository.getReferenceById(loggedInMemberId);
		System.out.println("Member : "+member.getId());
		List<Timesheet> payList=timesheetRepository.findAllByMemberId(loggedInMemberId);
		System.out.println(payList);
		model.addAttribute("payList", payList);
		model.addAttribute("no", 0);
		
		return "payment_list";
	}

}
