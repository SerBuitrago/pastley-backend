package com.pastley.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pastley.dao.UserDAO;
import com.pastley.entity.User;
import com.pastley.util.PastleyInterface;

/**
 * @project Pastley-User.
 * @author Leyner Jose Ortega Arias.
 * @Github https://github.com/leynerjoseoa.
 * @contributors soleimygomez, serbuitrago, jhonatanbeltran.
 * @version 1.0.0.
 */
@Service
public class UserService implements PastleyInterface<Long, User>{
	@Autowired
	private UserDAO userDAO;
	
	///////////////////////////////////////////////////////
	// Method
	///////////////////////////////////////////////////////
	@Override
	public User findById(Long id) {
		try {
			return userDAO.findById(id).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<User> findAll() {
		try {
			return userDAO.findAll();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	@Override
	public User save(User entity) {
		try {
			return userDAO.save(entity);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean delete(Long id) {
		try {
			userDAO.deleteById(id);
			return findById(id)==null;
		} catch (Exception e) {
			return false;
		}
	}

}
