/**
 * I declare that this code was written by me, leanne.
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism. 
 *
 * Student name: Leanne Quek  
 * Student ID: 21024009
 * Date created: 2023-May-02 7:38:58 pm 
 *
 */

package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author leanne
 *
 */
public interface ProgramRepository extends JpaRepository<Program, Integer>{
	public List<Program> findByCategoryId(int id);

}
