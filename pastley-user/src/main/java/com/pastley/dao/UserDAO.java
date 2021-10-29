package com.pastley.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pastley.entity.User;

/**
 * @project Pastley-User.
 * @author Leyner Jose Ortega Arias.
 * @Github https://github.com/leynerjoseoa.
 * @contributors soleimygomez, serbuitrago, jhonatanbeltran.
 * @version 1.0.0.
 */

@Repository
public interface UserDAO extends JpaRepository<User,Long> {
	
	public User findByMail(String mail);
	public List<User> findByIdRole(Long idRole);	
	public List<User> findByIdPerson(Long idPerson);	
}
