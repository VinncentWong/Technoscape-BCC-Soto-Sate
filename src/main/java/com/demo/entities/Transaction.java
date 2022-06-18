package com.demo.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long transactionId;
	
	private Integer amount;
	
	private String description;
	
	private String status;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private DetailBankAccount detailBank;
}
