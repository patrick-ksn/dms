package de.krieger.management.service;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import de.krieger.management.entity.AuthorEntity;
import de.krieger.management.entity.DocumentEntity;
import de.krieger.management.exception.AuthorNotFoundException;
import de.krieger.management.exception.DocumentNotFoundException;
import de.krieger.management.model.Author;
import de.krieger.management.model.Document;
import de.krieger.management.repository.AuthorRepository;
import de.krieger.management.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private Document document;
    private DocumentEntity documentEntity;
    private AuthorEntity authorEntity;

    @BeforeEach
    void setUp() {
        // Initialization of test data
        Author author = new Author();
        author.setId(1);
        author.setFirstName("Olivia");
        author.setLastName("Johnson");

        document = new Document();
        document.setId(1);
        document.setTitle("Test Document");
        document.setBody("This is a test document.");
        document.setAuthors(Collections.singleton(author));
        document.setReferences(Collections.singleton(new Document()));

        authorEntity = new AuthorEntity();
        authorEntity.setId(1);
        authorEntity.setFirstName("Olivia");
        authorEntity.setLastName("Johnson");

        DocumentEntity referencedDocumentEntity = new DocumentEntity();
        referencedDocumentEntity.setId(document.getId());
        referencedDocumentEntity.setTitle("Test referenced Document");
        referencedDocumentEntity.setBody("This is a test referenced document.");
        referencedDocumentEntity.setAuthors(new HashSet<>());
        referencedDocumentEntity.setReferences(new HashSet<>());

        documentEntity = new DocumentEntity();
        documentEntity.setId(document.getId());
        documentEntity.setTitle("Test Document");
        documentEntity.setBody("This is a test document.");
        documentEntity.setAuthors(Collections.singleton(authorEntity));
        documentEntity.setReferences(Collections.singleton(referencedDocumentEntity));
    }

    @Test
    void shouldReturnStoredDocumentWhenCallCreateDocument() {
        // Configure MockObjects DocumentRepository for save a Document
        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(documentEntity);
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(authorEntity));
        when(documentRepository.findById(anyInt())).thenReturn(Optional.of(documentEntity));
        // invoke create Method
        Document createdDocument = documentService.create(document);

        assertAll(
                // check, if updated Document is returned
                () -> assertNotNull(createdDocument,"The created document should not be null."),
                () -> assertNotEquals(0,createdDocument.getId(),"Document ID should not be 0 after saving."),
                () -> assertEquals(document.getTitle(), createdDocument.getTitle(),"Document title should match after saving."),
                () -> assertEquals(document.getBody(), createdDocument.getBody(),"Document Body should match after saving."),
                () -> assertEquals(1, createdDocument.getAuthors().size(),"Document Authors size should be 1 after saving."),
                () -> assertEquals(1, createdDocument.getReferences().size(),"Document References size should be 1 after saving.")
        );
        // verify that all methods are called
        verify(documentRepository).save(any(DocumentEntity.class));
        verify(authorRepository, times(document.getAuthors().size())).findById(anyInt());
    }
    @Test
    void shouldThrowExceptionWhenCallCreateDocumentWithoutAuthor() {
        // detach authors in document
        document.setAuthors(null);

        // invoke findById Method and check Exception
        assertThrows(AuthorNotFoundException.class,
                () -> documentService.create(document),
                "Expected AuthorNotFoundException to be thrown when attempting add document without attached Author.");
    }

    @Test
    void shouldReturnUpdatedDocumentWhenCallUpdateDocument() {
        // Configure MockObjects DocumentRepository for update a Document
        when(documentRepository.existsById(anyInt())).thenReturn(true);
        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(documentEntity);
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(authorEntity));
        when(documentRepository.findById(anyInt())).thenReturn(Optional.of(documentEntity));
        // invoke update Method
        Document updatedDocument = documentService.update(document);

        assertAll(
                // check, if created Document is returned
                () -> assertNotNull(updatedDocument,"Updated Document should not be null."),
                () -> assertEquals(document.getTitle(), updatedDocument.getTitle(),"Document Title should match after saving."),
                () -> assertEquals(document.getBody(), updatedDocument.getBody(), "Document Body should match after saving.")
        );
        // verify that all methods are called
        verify(documentRepository).existsById(document.getId());
        verify(documentRepository).save(any(DocumentEntity.class));
        verify(authorRepository, times(document.getAuthors().size())).findById(anyInt());
        verify(documentRepository, times(document.getReferences().size())).findById(anyInt());
    }

    @Test
    void shouldCallDeleteAndDeleteIdAndExistByIdWhenDeleteDocument() {
        // Configure MockObjects DocumentRepository for delete a Document
        when(documentRepository.existsById(anyInt())).thenReturn(true);

        // invoke delete Method
        documentService.delete(document.getId());

        // verify that all methods are called
        verify(documentRepository).existsById(document.getId());
        verify(documentRepository).deleteById(document.getId());
        verify(documentRepository).deleteByReferenceId(document.getId());
    }

    @Test
    void ShouldFindDocumentByIdWhenDocumentExists() {
        // Configure MockObjects DocumentRepository for get a Document
        when(documentRepository.existsById(anyInt())).thenReturn(true);
        when(documentRepository.findById(anyInt())).thenReturn(Optional.of(documentEntity));
        // invoke findById Method
        Document foundDocument = documentService.findById(document.getId());

        assertAll(
                // check, if Document is returned
                () -> assertNotNull(foundDocument, "Found document should not be null"),
                () -> assertEquals(document.getTitle(), foundDocument.getTitle(), "Document title should match"),
                () -> assertEquals(document.getBody(), foundDocument.getBody(), "Document body should match"),
                () -> assertEquals(document.getAuthors().size(), foundDocument.getAuthors().size(), "Authors size should match"),
                () -> assertEquals(document.getReferences().size(), foundDocument.getReferences().size(), "References size should match")
        );
        // verify that all methods are called
        verify(documentRepository).existsById(document.getId());
        verify(documentRepository).findById(document.getId());
    }

    @Test
    void ShouldThrowExceptionWhenFindByDocumentNotExists() {
        // Configure Mocked Method for check existsById
        when(documentRepository.existsById(anyInt())).thenReturn(false);
        // invoke findById Method and check Exception
        assertThrows(DocumentNotFoundException.class,
                () -> documentService.findById(anyInt()),
                "Expected DocumentNotFoundException to be thrown when attempting to find a non-existent document.");
    }

    @Test
    void ShouldThrowExceptionWhenUpdateDocumentNotExists() {
        // Configure Mocked Method for check existsById
        when(documentRepository.existsById(anyInt())).thenReturn(false);
        // invoke update Method and check Exception
        assertThrows(DocumentNotFoundException.class,
                () -> documentService.update(document),
                "Expected DocumentNotFoundException to be thrown when attempting to update a non-existent document.");
    }

    @Test
    void ShouldThrowExceptionWhenDeleteDocumentNotExists() {
        // Configure Mocked Method for check existsById
        when(documentRepository.existsById(anyInt())).thenReturn(false);
        // invoke delete Method and check Exception
        assertThrows(DocumentNotFoundException.class,
                () -> documentService.delete(anyInt()),
                "Expected DocumentNotFoundException to be thrown when attempting to delete a non-existent document.");
    }

    @Test
    void ShouldFindAllDocumentsWhenDocumentsExisting() {
        // Configure Mocked Method for check existsById
        when(documentRepository.findAllDocumentWithAuthorsAndReferences()).thenReturn(Collections.singleton(documentEntity));
        // invoke getAll Method and check Exception
        List<Document> documents = documentService.getAll();

        assertAll(
                // check, if List of Documents is returned
                () -> assertNotNull(documents,"Documents should not be null."),
                () -> assertFalse(documents.isEmpty(),"Documents should not be empty."),
                () -> assertEquals(1, documents.size(),"Documents size should not be 1.")
        );
        // verify that findAll methods is called
        verify(documentRepository).findAllDocumentWithAuthorsAndReferences();
    }
}