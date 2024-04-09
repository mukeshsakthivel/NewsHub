package Management.journalism.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Management.journalism.model.Login;

public interface loginrepository extends JpaRepository<Login,Integer> {
			
}
