package de.krieger.management.repository;

import de.krieger.management.entity.AuthorEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM document_author WHERE author_id = :authorId", nativeQuery = true)
    void deleteAuthorIdFromDocument(@Param("authorId") int authorId);
}
