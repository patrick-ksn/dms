package de.krieger.management.repository;


import de.krieger.management.entity.DocumentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface DocumentRepository extends JpaRepository<DocumentEntity,Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM document_reference WHERE reference_id = :referenceId", nativeQuery = true)
    void deleteByReferenceId(@Param("referenceId") int referenceId);

    @Transactional
    @Query("SELECT DISTINCT d FROM DocumentEntity d " +
            "LEFT JOIN FETCH d.references r " +
            "LEFT JOIN FETCH r.authors ra ")
    Set<DocumentEntity> findAllDocumentWithAuthorsAndReferences();
}
