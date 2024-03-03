package com.springblogapp.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Role {
	
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private int id;
	private String name;
}
