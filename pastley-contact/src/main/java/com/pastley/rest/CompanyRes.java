package com.pastley.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pastley.entity.Company;
import com.pastley.entity.Contact;
import com.pastley.service.CompanyService;
import com.pastley.service.ContactService;
import com.pastley.util.PastleyResponse;

@RestController
@RequestMapping("company")
public class CompanyRes {

	@Autowired
	private CompanyService companyService;

	///////////////////////////////////////////////////////
	// Method - Get
	///////////////////////////////////////////////////////
	/**
	 * Method that allows consulting a payment method by its id.
	 * 
	 * @param id, Represents the identifier of the payment method.
	 * @return The generated response.
	 */

	@RequestMapping(value = "id")
	public ResponseEntity<?> findById(@PathVariable("id") Long id) {
		PastleyResponse response = new PastleyResponse();
		Company company = companyService.findById(id);
		if (company != null) {
			response.add("company", company, HttpStatus.OK);
		} else {
			response.add("message", "No hay ninguna Compañia Registrado con ese ID " + id + ".", HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(response.getMap());
	}

	/**
	 * Method that allows you to obtain all payment methods.
	 * 
	 * @return The generated response.
	 */

	@GetMapping
	public ResponseEntity<?> findAll() {
		PastleyResponse response = new PastleyResponse();
		List<Company> list = companyService.findAll();
		if (list.isEmpty()) {
			response.add("message", "No hay ninguna compañia registrado", HttpStatus.NOT_FOUND);
		} else {
			response.add("company", list, HttpStatus.OK);
		}
		return ResponseEntity.ok(response.getMap());

	}

	///////////////////////////////////////////////////////
	// Method - Post
	///////////////////////////////////////////////////////
	/**
	 * Method that allows you to register a sale.
	 * 
	 * @param sale, Represents the sale to register.
	 * @return The generated response.
	 */
	@PostMapping(value = "/create")
	public ResponseEntity<?> create(@RequestBody Company company) {
		PastleyResponse response = new PastleyResponse();
		return ResponseEntity.ok(response.getMap());
	}

	///////////////////////////////////////////////////////
	// Method - Put
	///////////////////////////////////////////////////////
	/**
	 * Method that allows updating a sale.
	 * 
	 * @param sale, Represents the sale to update.
	 * @return The generated response.
	 */
	@PutMapping(value = "/update")
	public ResponseEntity<?> update(@RequestBody Company company) {
		PastleyResponse response = new PastleyResponse();
		return ResponseEntity.ok(response.getMap());
	}

	///////////////////////////////////////////////////////
	// Method - Delete
	///////////////////////////////////////////////////////
	/**
	 * Method that allows you to delete a sale by means of its id.
	 * 
	 * @param id, Represents the identifier of the sale to be deleted.
	 * @return The generated response.
	 */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		PastleyResponse response = new PastleyResponse();
		return ResponseEntity.ok(response.getMap());
	}

}
