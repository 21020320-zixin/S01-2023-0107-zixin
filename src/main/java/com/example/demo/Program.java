
package com.example.demo;
/**
 * I declare that this code was written by me, leanne.
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism. 
 *
 * Student name: Leanne Quek  
 * Student ID: 21024009
 * Date created: 2023-May-02 7:30:59 pm 
 *
 */

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author leanne
 *
 */
@Entity
public class Program {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@NotEmpty(message="Name cannot be empty.")
	@Size(min=2, max=50, message="Name length must be between 2 and 50 characters.")
	private String name;
	
	@ManyToOne
	@JoinColumn(name="categoryId", nullable=false)
	@NotNull(message="Category cannot be empty.")
	private Category category;
	
	@OneToMany(mappedBy="program")
	private Set<ProgramRun> programrun;
	

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
	
	public Category getCategory() {
	    return category;
	  }
	  
	public void setCategory(Category category) {
	   this.category = category;
	  }
	
}
	

	
	
	