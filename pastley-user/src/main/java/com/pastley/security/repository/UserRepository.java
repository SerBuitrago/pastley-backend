package com.pastley.security.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pastley.security.entity.User;

/**
 * @project Pastley-User.
 * @author Leyner Jose Ortega Arias.
 * @Github https://github.com/leynerjoseoa.
 * @contributors soleimygomez, serbuitrago, jhonatanbeltran.
 * @version 1.0.0.
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	@Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.role.id = :idRole")
	public List<User> findByIdRole(Long idRole);	
	
	@Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.person.id = :idPerson")
	public List<User> findByIdPerson(Long idPerson);	
	
	@Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.id = :id AND u.role.id = :idRole")
	public User findByIdAndIdRol(Long id, Long idRole);
	
	@Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.person.document = :documentPerson AND u.role.id = :idRole")
	public User findByPersonAndIdRol(Long documentPerson, Long idRole);
	
    public boolean existsByNickname(String nickname);
    
    public User findByNickname(String nickname);
    
    @Query(nativeQuery = false, value = "SELECT u FROM User u WHERE u.person.email = :email")
    public User existsByEmail(String email);
}
