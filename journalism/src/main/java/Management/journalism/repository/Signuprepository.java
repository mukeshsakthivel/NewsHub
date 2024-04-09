package Management.journalism.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Management.journalism.model.Signup;

public interface Signuprepository extends JpaRepository<Signup,Integer> {

	
	boolean existsByName(String name);
    boolean existsByEmail(String email);
    Signup findByName(String name);
    
}
