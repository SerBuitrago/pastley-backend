package com.pastley.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @project Pastley-Product.
 * @author Sergio Stives Barrios Buitrago.
 * @Github https://github.com/SerBuitrago.
 * @contributors leynerjoseoa.
 * @version 1.0.0.
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category implements Serializable{

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
    private Long id;
    
	@Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
	
	@Column(name = "statu", nullable = true, columnDefinition = "tinyint(1) default 1")
    private boolean statu;

	@Column(name="date_register", nullable = false)
	private String dateRegister;
	
	@Column(name="date_update", nullable = true)
	private String dateUpdate;
}
