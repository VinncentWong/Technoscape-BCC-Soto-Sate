package com.demo.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class Balance {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long balanceId;
	
	private Integer available;
	
	private Integer current;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private DetailBankAccount detailBank;
}
