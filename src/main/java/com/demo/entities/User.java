package com.demo.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	private String email;
	
	private String password;
	
	private String username;
	
	private Integer points;
	
	private boolean isPremium;
	
	private Date premium_expire;
	
	private Date created_at;
	
	private Date updated_at;
}
