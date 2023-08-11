package com.example.demo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoogleChartsController {
	@Autowired
	TimesheetRepository timesheetRepository;
	@Autowired
	AccountRepository memberRepository;
	@Autowired
	ProgramRunRepository programRunRepository;

	@GetMapping("/bar/chart")
	public String getPieChart(Model model) {
		List<Timesheet> timesheetList = timesheetRepository.findAll();
		double total = 0;
		int hour = 0, minutes = 0;
		List<DataPoint> dataPoints = new ArrayList<>();
		HashMap<Integer, Integer> dateList = new HashMap<>();
		int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0, count8 = 0, count9 = 0,
				count10 = 0, count11 = 0, count12 = 0;
		LocalDate date;
		Date d;
		Date dd = new Date();
		System.out.println("Current Month: " + dd.getMonth());
		for (Timesheet ts : timesheetList) {
			total += ts.getTotal();
			Duration diff = Duration.between(ts.getProgramrun().getStartTime(), ts.getProgramrun().getEndTime());
			hour += diff.toHoursPart();
			minutes += diff.toMinutesPart();

			d = ts.getProgramrun().getAppointmentDate();

//				 System.out.println(d.getMonth());
			int year = d.getYear();
			// date=LocalDate.ofInstant(d.toInstant(), ZoneId.systemDefault());
			if (year == new Date().getYear()) {
				if (d.getMonth() == 0) {// Jan
					count1++;
					dateList.put(1, count1);
				} else if (d.getMonth() == 1) {// Feb
					count2++;
					dateList.put(2, count2);
				} else if (d.getMonth() == 2) {// March
					count3++;
					dateList.put(3, count3);
				} else if (d.getMonth() == 3) {// April
					count4++;
					dateList.put(4, count4);
				}

				else if (d.getMonth() == 4) {// May
					count5++;

					dateList.put(5, count5);
				} else if (d.getMonth() == 5) {// June
					count6++;
					dateList.put(6, count6);

				} else if (d.getMonth() == 6) {// July
					count7++;
					dateList.put(7, count7);
				} else if (d.getMonth() == 7) {// Aug
					count8++;
					dateList.put(8, count8);
				} else if (d.getMonth() == 8) {
					count9++;
					dateList.put(9, count9);
				} else if (d.getMonth() == 9) {
					count10++;
					dateList.put(10, count10);
				} else if (d.getMonth() == 10) {
					count11++;
					dateList.put(11, count11);
				} else if (d.getMonth() == 11) {
					count12++;
					dateList.put(12, count12);
				}
//					  if(date.getMonthValue()==5) {
//						  count1++;
//						  dateList.put(5, count1);
//					  }else if(date.getMonthValue()==6) {
//						  count2++;
//						  dateList.put(6, count2);
//					  }else {
//						  count3++;
//						  dateList.put(7, count3);
//					  }
//					 
			}

		}
		System.out.println(count1 + " : " + count2 + " : " + count3);
		System.out.println(dateList);
		if (minutes >= 60) {
			hour += minutes / 60;
			minutes = minutes % 60;
			/*
			 * if (minutes > 30) { hour += 1; }
			 */
		}
		DataPoint dp;
		for (Entry<Integer, Integer> entry : dateList.entrySet()) {
			if (entry.getKey() == 1) {
				dp = new DataPoint("Jan", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 2) {
				dp = new DataPoint("Feb", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 3) {
				dp = new DataPoint("Mar", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 4) {
				dp = new DataPoint("Apr", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 5) {
				dp = new DataPoint("May", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 6) {
				dp = new DataPoint("June", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 7) {
				dp = new DataPoint("July", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 8) {
				dp = new DataPoint("Aug", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 9) {
				dp = new DataPoint("Sep", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 10) {
				dp = new DataPoint("Oct", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 11) {
				dp = new DataPoint("Nov", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 12) {
				dp = new DataPoint("Dec", entry.getValue());
				dataPoints.add(dp);
			}

		}

//		List<DataPoint> dataPoints=new ArrayList<>();
//		DataPoint dp1=new DataPoint("Jan", 23);
//		DataPoint dp2=new DataPoint("Feb", 50);
//		DataPoint dp3=new DataPoint("Mar", 40);
//		dataPoints.add(dp1);
//		dataPoints.add(dp2);
//		dataPoints.add(dp3);

		int count = count1 + count2 + count3 + count4 + count5 + count6 + count7 + count8 + count9 + count10 + count11
				+ count12;

		model.addAttribute("chartData", dataPoints);
		model.addAttribute("event", count);
		model.addAttribute("total", total);
		model.addAttribute("time", hour + " hrs : " + minutes + " mins");
		model.addAttribute("year", Year.now().getValue());

//        Map<String, Integer> graphData = new TreeMap<>();
//        graphData.put("JAN", 147);
//        graphData.put("FEB", 1256);
//        graphData.put("MAR", 3856);
//        graphData.put("APR", 19807);
//        graphData.put("MAY", 147);
//        graphData.put("JUN", 1256);
//        graphData.put("JUL", 3856);
//        graphData.put("AUG", 19807);
//        graphData.put("SEP", 147);
//        graphData.put("OCT", 1256);
//        graphData.put("NOV", 3856);
//        graphData.put("DEC", 19807);
//        model.addAttribute("chartData", graphData);
		return "bar_chart";
	}

	// line-graph
	@GetMapping("/line/graph")
	public String getLineGraphp(Model model) {
		List<Timesheet> timesheetList = timesheetRepository.findAll();
		double total = 0;
		int hour = 0, minutes = 0;
		List<DataPoint> dataPoints = new ArrayList<>();
		HashMap<Integer, Integer> dateList = new HashMap<>();
		int count1 = 0, count2 = 0, count3 = 0, count4 = 0, count5 = 0, count6 = 0, count7 = 0, count8 = 0, count9 = 0,
				count10 = 0, count11 = 0, count12 = 0;
		LocalDate date;
		Date d;
		Date dd = new Date();
		System.out.println("Current Month: " + dd.getMonth());
		for (Timesheet ts : timesheetList) {
			total += ts.getTotal();
			Duration diff = Duration.between(ts.getProgramrun().getStartTime(), ts.getProgramrun().getEndTime());
			hour += diff.toHoursPart();
			minutes += diff.toMinutesPart();

			d = ts.getProgramrun().getAppointmentDate();

//				 System.out.println(d.getMonth());
			int year = d.getYear();
			// date=LocalDate.ofInstant(d.toInstant(), ZoneId.systemDefault());
			if (year == new Date().getYear()) {
				if (d.getMonth() == 0) {// Jan
					count1++;
					dateList.put(1, count1);
				} else if (d.getMonth() == 1) {// Feb
					count2++;
					dateList.put(2, count2);
				} else if (d.getMonth() == 2) {// March
					count3++;
					dateList.put(3, count3);
				} else if (d.getMonth() == 3) {// April
					count4++;
					dateList.put(4, count4);
				}

				else if (d.getMonth() == 4) {// May
					count5++;

					dateList.put(5, count5);
				} else if (d.getMonth() == 5) {// June
					count6++;
					dateList.put(6, count6);

				} else if (d.getMonth() == 6) {// July
					count7++;
					dateList.put(7, count7);
				} else if (d.getMonth() == 7) {// Aug
					count8++;
					dateList.put(8, count8);
				} else if (d.getMonth() == 8) {
					count9++;
					dateList.put(9, count9);
				} else if (d.getMonth() == 9) {
					count10++;
					dateList.put(10, count10);
				} else if (d.getMonth() == 10) {
					count11++;
					dateList.put(11, count11);
				} else if (d.getMonth() == 11) {
					count12++;
					dateList.put(12, count12);
				}
//					  if(date.getMonthValue()==5) {
//						  count1++;
//						  dateList.put(5, count1);
//					  }else if(date.getMonthValue()==6) {
//						  count2++;
//						  dateList.put(6, count2);
//					  }else {
//						  count3++;
//						  dateList.put(7, count3);
//					  }
//					 
			}

		}
		System.out.println(count1 + " : " + count2 + " : " + count3);
		System.out.println(dateList);
		if (minutes >= 60) {
			hour += minutes / 60;
			minutes = minutes % 60;
			/*
			 * if (minutes > 30) { hour += 1; }
			 */
		}
		DataPoint dp;
		for (Entry<Integer, Integer> entry : dateList.entrySet()) {
			if (entry.getKey() == 1) {
				dp = new DataPoint("Jan", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 2) {
				dp = new DataPoint("Feb", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 3) {
				dp = new DataPoint("Mar", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 4) {
				dp = new DataPoint("Apr", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 5) {
				dp = new DataPoint("May", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 6) {
				dp = new DataPoint("June", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 7) {
				dp = new DataPoint("July", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 8) {
				dp = new DataPoint("Aug", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 9) {
				dp = new DataPoint("Sep", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 10) {
				dp = new DataPoint("Oct", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 11) {
				dp = new DataPoint("Nov", entry.getValue());
				dataPoints.add(dp);
			} else if (entry.getKey() == 12) {
				dp = new DataPoint("Dec", entry.getValue());
				dataPoints.add(dp);
			}

		}

//		List<DataPoint> dataPoints=new ArrayList<>();
//		DataPoint dp1=new DataPoint("Jan", 23);
//		DataPoint dp2=new DataPoint("Feb", 50);
//		DataPoint dp3=new DataPoint("Mar", 40);
//		dataPoints.add(dp1);
//		dataPoints.add(dp2);
//		dataPoints.add(dp3);

		int count = count1 + count2 + count3 + count4 + count5 + count6 + count7 + count8 + count9 + count10 + count11
				+ count12;

		model.addAttribute("chartData", dataPoints);
		model.addAttribute("event", count);
		model.addAttribute("total", total);
		model.addAttribute("time", hour + " hrs : " + minutes + " mins");
		model.addAttribute("year", Year.now().getValue());

//        Map<String, Integer> graphData = new TreeMap<>();
//        graphData.put("JAN", 147);
//        graphData.put("FEB", 1256);
//        graphData.put("MAR", 3856);
//        graphData.put("APR", 19807);
//        graphData.put("MAY", 147);
//        graphData.put("JUN", 1256);
//        graphData.put("JUL", 3856);
//        graphData.put("AUG", 19807);
//        graphData.put("SEP", 147);
//        graphData.put("OCT", 1256);
//        graphData.put("NOV", 3856);
//        graphData.put("DEC", 19807);
//        model.addAttribute("chartData", graphData);
		return "line_graph";
	}

	@GetMapping("/report/trainer/hour")
	public String reportTrainers(Model model) {

		double total = 0;
		int hour = 0, minutes = 0;
		List<DataPoint> dataPoints = new ArrayList<>();
		List<UserAccount> memberList = memberRepository.findAll();
		List<ProgramRun> programRunList;
		TreeMap<String, Integer> hashMap = new TreeMap();
		for (UserAccount member : memberList) {
			if (member.getRole().equals("ROLE_TRAINER")) {
				programRunList = programRunRepository.findAllByMemberId(member.getId());
				for (ProgramRun programRun : programRunList) {
					if (programRun.getStatus().equals("Paid")) {
						Duration diff = Duration.between(programRun.getStartTime(), programRun.getEndTime());
						hour += diff.toHoursPart();
						minutes += diff.toMinutesPart();
						if (minutes >= 60) {
							hour += minutes / 60;
							minutes = minutes % 60;
							if (minutes > 30) {
								hour += 1;
							}

						}
						hashMap.put(member.getFirstName(), hour);
					}
//					hashMap.put(member.getFirstName(), hour);
				}
//				if (minutes >= 60) {
//					hour += minutes / 60;
//					minutes = minutes % 60;
//					if (minutes > 30) {
//						hour += 1;
//					}
//
//				}
//				hashMap.put(member.getFirstName(), hour);
			}
		}

		for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			DataPoint dp = new DataPoint(key, value);
			dataPoints.add(dp);

		}
		System.out.println(dataPoints);
		model.addAttribute("chartData", dataPoints);
		model.addAttribute("time", hour + " hrs : " + minutes + " mins");
		model.addAttribute("year", Year.now().getValue());

//        Map<String, Integer> graphData = new TreeMap<>();
//        graphData.put("JAN", 147);
//        graphData.put("FEB", 1256);
//        graphData.put("MAR", 3856);
//        graphData.put("APR", 19807);
//        graphData.put("MAY", 147);
//        graphData.put("JUN", 1256);
//        graphData.put("JUL", 3856);
//        graphData.put("AUG", 19807);
//        graphData.put("SEP", 147);
//        graphData.put("OCT", 1256);
//        graphData.put("NOV", 3856);
//        graphData.put("DEC", 19807);
//        model.addAttribute("chartData", graphData);
		return "reportByTrainersHour";
	}
//	start report preferred
	@GetMapping("/report/trainer/preferred")
	public String reportTrainerPreferred(Model model) {
		Date d;
		List<Timesheet> tList = timesheetRepository.findAllByStatus(1);
		List<Timesheet> timesheetList=new ArrayList<>();
		for(Timesheet t:tList) {
			if(t.getMember().getRate().equals("Preferred")) {

				d = t.getProgramrun().getAppointmentDate();

//					 System.out.println(d.getMonth());
				int year = d.getYear();
				// date=LocalDate.ofInstant(d.toInstant(), ZoneId.systemDefault());
				if (year == new Date().getYear()) {
					timesheetList.add(t);
				}
				
			}
		}

		// change all programrun to timesheet
		TreeMap<Integer, List<Timesheet>> tsMap = new TreeMap<>();
		for (Timesheet ts : timesheetList) {
			List<Timesheet> tsheetList = new ArrayList<>();
			for (Timesheet ts1 : timesheetList) {
				if (ts.getMember().equals(ts1.getMember())) {
					tsheetList.add(ts1);
					tsMap.put(ts.getMember().getId(), tsheetList);
				}
			}

		}
		int total=0,netTotal=0;;
		Timesheet timesheet=new Timesheet();
		List<DataPoint> dataPoints = new ArrayList<>();
		for (Map.Entry<Integer, List<Timesheet>> entry : tsMap.entrySet()) {
			total=0;
			int memberId = entry.getKey();
			UserAccount member=memberRepository.getReferenceById(memberId);
			List<Timesheet> tsList = entry.getValue();
			
			for (Timesheet ts : tsList) {
				total+=ts.getTotal();//total for each trainer
				netTotal+=ts.getTotal();//total for all trainer of preferred pay
				timesheet=ts;
			}
			DataPoint dp=new DataPoint(member.getFirstName(), total);
			dataPoints.add(dp);
			
		}

		model.addAttribute("chartData", dataPoints);
		
		model.addAttribute("year", Year.now().getValue());
		model.addAttribute("total", netTotal);
		List<UserAccount> memberList=memberRepository.findAll();
		for(UserAccount account:memberList) {
			System.out.println(account.getId()+" "+account.getFirstName());
		}
		
		return "reportByTrainersPreferred";
		
	}
	
//	end report preferred
	
	
//	start report standard
	@GetMapping("/report/trainer/standard")
	public String reportTrainerStandard(Model model) {
		Date d;
		List<Timesheet> tList = timesheetRepository.findAllByStatus(1);
		List<Timesheet> timesheetList=new ArrayList<>();
		for(Timesheet t:tList) {
			if(t.getMember().getRate().equals("Standard")) {

				d = t.getProgramrun().getAppointmentDate();

//					 System.out.println(d.getMonth());
				int year = d.getYear();
				// date=LocalDate.ofInstant(d.toInstant(), ZoneId.systemDefault());
				if (year == new Date().getYear()) {
					timesheetList.add(t);
				}
				
			}
		}
		// change all programrun to timesheet
		TreeMap<Integer, List<Timesheet>> tsMap = new TreeMap<>();
		for (Timesheet ts : timesheetList) {
			List<Timesheet> tsheetList = new ArrayList<>();
			for (Timesheet ts1 : timesheetList) {
				if (ts.getMember().equals(ts1.getMember())) {
					tsheetList.add(ts1);
					tsMap.put(ts.getMember().getId(), tsheetList);
				}
			}

		}
		int total=0,netTotal=0;
		Timesheet timesheet=new Timesheet();
		List<DataPoint> dataPoints = new ArrayList<>();
		for (Map.Entry<Integer, List<Timesheet>> entry : tsMap.entrySet()) {
			total=0;
			int memberId = entry.getKey();

			UserAccount member=memberRepository.getReferenceById(memberId);
			List<Timesheet> tsList = entry.getValue();
			
			for (Timesheet ts : tsList) {
				total=Integer.valueOf(ts.getMember().getSalary());//total for each trainer
				netTotal+=total;
				timesheet=ts;
			}
			DataPoint dp=new DataPoint(member.getFirstName(), total);
			dataPoints.add(dp);
			
		}

		model.addAttribute("chartData", dataPoints);
		
		model.addAttribute("year", Year.now().getValue());
		model.addAttribute("total", netTotal);
		List<UserAccount> memberList=memberRepository.findAll();
		for(UserAccount account:memberList) {
			System.out.println(account.getId()+" "+account.getFirstName());
		}
		
		return "reportByTrainersPreferred";
		
	}
//end report standard
}
