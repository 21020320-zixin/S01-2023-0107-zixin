/**
 * I declare that this code was written by me, leanne.
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism. 
 *
 * Student name: Leanne Quek  
 * Student ID: 21024009
 * Date created: 2023-May-27 4:09:44 pm 
 *
 */

package com.example.demo;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author leanne
 *
 */
@Entity
public class Category {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
//	@NotNull
//	@NotEmpty(message="Category name cannot be empty.")
//	@Size(min=2, max=70, message="Category length must be between 2 and 70 characters.")
	private String name;
//	
//	@NotNull
//	@NotEmpty(message="Description cannot be empty.")
//	@Size(min=2, max=100, message="Description length must be between 2 and 100 characters.")
	private String description;
//	
//	@NotNull
//	@NotEmpty(message="Age group cannot be empty.")
//	@Size(min=2, max=50, message="Age group length must be between 2 and 50 characters.")
	private String ageGroup;
	
	@OneToMany(mappedBy="category")
	private Set<Program> program;
	
	private String imgName;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	public String getImgName() {
		return imgName;
	}
	
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}	
	
	

}
