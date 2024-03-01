package de.krieger.management.service;

import de.krieger.management.exception.AuthorNotFoundException;
import de.krieger.management.repository.AuthorRepository;
import de.krieger.management.repository.DocumentRepository;
import de.krieger.management.entity.AuthorEntity;
import de.krieger.management.entity.DocumentEntity;
import de.krieger.management.exception.DocumentNotFoundException;
import de.krieger.management.model.Author;
import de.krieger.management.model.Document;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the DocumentService interface, providing CRUD operations for documents.
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    DocumentRepository documentRepository;
    AuthorRepository authorRepository;

    /**
     * Constructs a new DocumentServiceImpl with necessary repositories.
     *
     * @param documentRepository the repository for document data access
     * @param authorRepository   the repository for author data access
     */
    @Autowired
    DocumentServiceImpl(DocumentRepository documentRepository, AuthorRepository authorRepository) {
        this.documentRepository = documentRepository;
        this.authorRepository = authorRepository;
    }

    /**
     * Creates a new document in the repository. The document's ID will be automatically
     * set to 0 to ensure the creation of a new entity. The actual ID will be generated
     * and assigned by the database.
     *
     * @param document the document object to be created
     * @return the saved document with the generated identifier
     */
    @Override
    @Transactional
    @CacheEvict(value = "documents", allEntries = true)
    public Document create(Document document) {
        document.setId(0);
        if (document.getAuthors() == null || document.getAuthors().isEmpty() )
        {
            throw new AuthorNotFoundException("No author is attached to the document. Please add at least one author to create the document.");
        }
        return getDocument(documentRepository.save(getDocumentEntity(document)));
    }

    /**
     * Updates an existing document in the repository. If the document does not exist,
     * a DocumentNotFoundException is thrown.
     *
     * @param document the document object to be updated
     * @return the updated document
     * @throws DocumentNotFoundException if the document does not exist
     */
    @Override
    @Transactional
    @CacheEvict(value = "documents", allEntries = true)
    public Document update(Document document) {
        checkIfDocumentExistElseThrowException(document.getId());
        return getDocument(documentRepository.save(getDocumentEntity(document)));
    }

    /**
     * Deletes a document from the repository by its ID. If the document does not exist,
     * a DocumentNotFoundException is thrown.
     *
     * @param id the ID of the document to delete
     * @throws DocumentNotFoundException if the document does not exist
     */
    @Override
    @Transactional
    @CacheEvict(value = "documents", allEntries = true)
    public void delete(int id) {
        checkIfDocumentExistElseThrowException(id);
        documentRepository.deleteByReferenceId(id);
        documentRepository.deleteById(id);
    }

    /**
     * Finds a document by its ID. If the document does not exist,
     * a DocumentNotFoundException is thrown.
     *
     * @param id the ID of the document to find
     * @return the found document, or null if no document is found
     * @throws DocumentNotFoundException if the document does not exist
     */
    @Override
    @Cacheable(value = "documents", key = "#id")
    public Document findById(int id) {
        checkIfDocumentExistElseThrowException(id);
        return getDocument(documentRepository.findById(id).orElse(null));
    }

    /**
     * Retrieves all documents from the repository.
     *
     * @return a list of all documents
     */
    @Override
    @Cacheable("documents")
    public List<Document> getAll() {
        return documentRepository.
                findAllDocumentWithAuthorsAndReferences().stream()
                .map(this::getDocument)
                .collect(Collectors.toList());
    }

    private Document getDocument(DocumentEntity documentEntity) {
        if (documentEntity != null) {
            Document document = new Document();
            document.setId(documentEntity.getId());
            document.setTitle(documentEntity.getTitle());
            document.setBody(documentEntity.getBody());

            populateAuthors(documentEntity, document);
            populateReferences(documentEntity, document);

            return document;
        }
        return null;
    }

    private void populateReferences(DocumentEntity documentEntity, Document document) {
        if (documentEntity.getReferences() != null) {
            document.setReferences(documentEntity.getReferences()
                    .stream()
                    .map(this::getLazyDocument)
                    .collect(Collectors.toSet()));
        }
    }

    private void populateAuthors(DocumentEntity documentEntity, Document document) {
        if (documentEntity.getAuthors() != null) {
            document.setAuthors(documentEntity.getAuthors()
                    .stream()
                    .map(this::getAuthor)
                    .collect(Collectors.toSet()));
        }
    }

    private Document getLazyDocument(DocumentEntity documentEntity) {
        if (documentEntity != null) {
            Document document = new Document();
            document.setId(documentEntity.getId());
            document.setTitle(documentEntity.getTitle());
            document.setBody(documentEntity.getBody());
            document.setAuthors(new HashSet<>());
            document.setReferences(new HashSet<>());
            return document;
        }
        return null;
    }

    private Author getAuthor(AuthorEntity authorEntity) {
        if (authorEntity != null) {
            Author author = new Author();
            author.setId(authorEntity.getId());
            author.setFirstName(authorEntity.getFirstName());
            author.setLastName(authorEntity.getLastName());
            return author;
        }
        return null;
    }

    private DocumentEntity getDocumentEntity(Document document) {
        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setId(document.getId());
        documentEntity.setTitle(document.getTitle());
        documentEntity.setBody(document.getBody());

        populateAuthors(document, documentEntity);
        populateReferences(document, documentEntity);

        return documentEntity;
    }

    private void populateReferences(Document document, DocumentEntity documentEntity) {
        if (document.getReferences() != null) {
            Set<DocumentEntity> referenceDocumentEntities = new HashSet<>();
            for (Document referenceDocument : document.getReferences()) {
                DocumentEntity referenceDocumentEntity = documentRepository.findById(referenceDocument.getId())
                        .orElseThrow(() -> new AuthorNotFoundException("Document id not found - Id: " + referenceDocument.getId()));
                referenceDocumentEntities.add(referenceDocumentEntity);
            }
            documentEntity.setReferences(referenceDocumentEntities);
        }
    }
    private void populateAuthors(Document document, DocumentEntity documentEntity) {
        if (document.getAuthors() != null) {
            Set<AuthorEntity> authorEntities = new HashSet<>();
            for (Author author : document.getAuthors()) {
                AuthorEntity authorEntity = authorRepository.findById(author.getId())
                        .orElseThrow(() -> new AuthorNotFoundException("Author id not found - Id: " + author.getId()));
                authorEntities.add(authorEntity);
            }
            documentEntity.setAuthors(authorEntities);
        }
    }

    private void checkIfDocumentExistElseThrowException(int id) {
        if (!documentRepository.existsById(id)) {
            throw new DocumentNotFoundException("document id not found - Id: " + id);
        }
    }
}
