/**
 * I declare that this code was written by me, leanne.
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism. 
 *
 * Student name: Leanne Quek  
 * Student ID: 21024009
 * Date created: 2023-May-02 7:35:12 pm 
 *
 */

package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



/**
 * @author leanne
 *
 */
@Controller
public class ProgramController {
	@Autowired
	private ProgramRepository programRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@GetMapping("/program")
	public String viewProgram(Model model) {
		List<Program> listOfProgram = programRepository.findAll();
		model.addAttribute("listOfProgram", listOfProgram);
		return "view_program";
	}
	
	@GetMapping("/program/add")
	public String addProgram(Model model) {
		List<Category> listOfCategories = categoryRepository.findAll();
		model.addAttribute("listOfCategories", listOfCategories);
		model.addAttribute("program", new Program());
		return "add_program";
	}
	
	@PostMapping("/program/save")
	public String saveProgram(Program program) {
		programRepository.save(program);
		return "redirect:/program";
	}
	
	@GetMapping("/program/{id}")
	public String viewSingleProgram(@PathVariable("id") Integer id, Model model ) {
		Program program = programRepository.getById(id);
		
		model.addAttribute("program", program);
		return "view_single_program";
	}
	
	
	@GetMapping("/program/edit/{id}")
	public String editProgram(@PathVariable("id") Integer id, Model model) {
			// get the category that has the id
			Program program = programRepository.getById(id);
			model.addAttribute("program", program);
			
			// get the category that is associated with the program
			Category category = program.getCategory();
			
			List<Category> listOfCategories = categoryRepository.findAll();
			List<Program> listOfProgram = programRepository.findAll(); 
			// getting all the categories
			
			model.addAttribute("listOfProgram", listOfProgram);
			model.addAttribute("listOfCategories", listOfCategories);
			
			model.addAttribute("category", category);
			
			return "edit_program";
	}
		

	@PostMapping("/program/edit/{id}")
	public String saveEditProgram(@PathVariable("id") Integer id, Program program, @ModelAttribute("categoryId") Integer categoryIdString) {
		//if (categoryIdString == null || categoryIdString.isEmpty()) {
		  //  throw new NumberFormatException("Category ID cannot be empty");
		//}
	    // Get the current category from the database
	    Category category = categoryRepository.getById(categoryIdString);

	    // Set the category on the program
	    program.setCategory(category);

	    // Save the program
	    programRepository.save(program);

	    return "redirect:/program";
	}
	
//	@PostMapping("/program/edit/{id}")
//	public String saveEditProgram(@PathVariable Long id, Program program, Category category) {
//        // Get the existing category from the program table
//        Category existingCategory = programRepository.findById(id).get().getCategory();
//
//        // Update the category of the program
//        program.setCategory(category);
//
//        // Save the program to the database
//        programRepository.save(program);
//
//        // If the category has changed, update the category table
//        if (!existingCategory.equals(category)) {
//            categoryRepository.save(category);
//        }
//
//        return "redirect:/program";
//    }
	
	@GetMapping("/program/delete/{id}")
	public String deleteProgram(@PathVariable("id") Integer id) {
		
		programRepository.deleteById(id);
		return "redirect:/program";
	}
}