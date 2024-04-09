package Management.journalism.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Management.journalism.model.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

}
