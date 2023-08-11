/**
 * I declare that this code was written by me, leanne.
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism. 
 *
 * Student name: Leanne Quek  
 * Student ID: 21024009
 * Date created: 2023-May-27 4:12:12 pm 
 *
 */

package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author leanne
 *
 */
@Controller
public class CategoryController {
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProgramRepository programRepository;
	
	@GetMapping("/category")
	public String viewCategory(Model model) {
		List<Category> listOfCategories = categoryRepository.findAll();
		model.addAttribute("listOfCategories", listOfCategories);
		return "view_category";
	}
	
	@GetMapping("/category/add")
	public String addCategory(Model model) {
		model.addAttribute("category", new Category());
		return "add_category";
	}
	
	
	@PostMapping("/category/save")
	public String saveCategory(@Valid Category category, BindingResult bindingResult, @RequestParam("categoryImage")MultipartFile imgFile) {
		if (bindingResult.hasErrors()) {
			return "add_category";
		}
		
		String imageName = imgFile.getOriginalFilename();
		
		category.setImgName(imageName);
		
		Category savedCategory = categoryRepository.save(category);
		
		try {
			String uploadDir = "uploads/category/" + savedCategory.getId();
			Path uploadPath = Paths.get(uploadDir);
			System.out.println("Directory path:" + uploadPath);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			Path fileToCreatePath = uploadPath.resolve(imageName);
			
			Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException Io) {
			Io.printStackTrace();
		}
		
		return "redirect:/category";
	}
	
	@GetMapping("/category/edit/{id}")
	public String editCategory(@PathVariable("id") Integer id, Model model) {
		Category category = categoryRepository.getById(id);
		model.addAttribute("category", category);
		
		List<Category> listOfCategories = categoryRepository.findAll();
		model.addAttribute("listOfCategories", listOfCategories);
		return "edit_category";
	}
	
	@PostMapping("/category/edit/{id}")
	public String saveEditCategory(@PathVariable("id") Integer id,Program program, Category category, @RequestParam("categoryImage")MultipartFile imgFile) {
		categoryRepository.save(category);
		String imgName = imgFile.getOriginalFilename();
		category.setImgName(imgName);
		Category savedCategory = categoryRepository.save(category);
		
		try {
			String uploadDir = "uploads/category/" + savedCategory.getId();
			Path uploadPath = Paths.get(uploadDir);
			System.out.println("Directory path: " + uploadPath);
			
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			Path fileToCreatePath = uploadPath.resolve(imgName);
			System.out.println("File path: " + fileToCreatePath);
			
			Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException io) {
			io.printStackTrace();
		}
		
		categoryRepository.save(category);
		program.setCategory(savedCategory);
	    programRepository.save(program);
		return "redirect:/category";
	}
	
	@GetMapping("/category/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {
		categoryRepository.deleteById(id);
		return "redirect:/category";
	}
}
