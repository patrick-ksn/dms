package de.krieger.management.service;

import de.krieger.management.repository.AuthorRepository;
import de.krieger.management.entity.AuthorEntity;
import de.krieger.management.exception.AuthorNotFoundException;
import de.krieger.management.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorEntity authorEntity;

    @BeforeEach
    public void setUp() {
        author = new Author();
        author.setId(1);
        author.setFirstName("Mary");
        author.setLastName("Muller");

        authorEntity = new AuthorEntity();
        authorEntity.setId(1);
        authorEntity.setFirstName("Mary");
        authorEntity.setLastName("Muller");
    }

    @Test
    void shouldReturnStoredAuthorWhenCallCreateAuthor() {
        // Configure MockObject AuthorRepository for save an AuthorEntity
        when(authorRepository.save(any(AuthorEntity.class))).thenReturn(authorEntity);

        // invoke create Method
        Author createdAuthor = authorService.create(author);
        assertAll(
                // check, if updated Author is returned
                () -> assertNotNull(createdAuthor, "Created Author should not be null."),
                () -> assertNotEquals(0, createdAuthor.getId(), "Author ID should not be 0 after saving."),
                () -> assertEquals(author.getFirstName(), createdAuthor.getFirstName(), "Author first name should match after saving."),
                () -> assertEquals(author.getLastName(), createdAuthor.getLastName(), "Author last name should match after saving.")
        );
        // Verify Method call save Method 1 time
        verify(authorRepository).save(any(AuthorEntity.class));
    }

    @Test
    void shouldReturnUpdatedAuthorWhenCallUpdateAuthor() {
        // Configure Mocked Methods
        when(authorRepository.existsById(author.getId())).thenReturn(true);
        when(authorRepository.save(any(AuthorEntity.class))).thenReturn(authorEntity);

        // invoke update Method
        Author updatedAuthor = authorService.update(author);

        assertAll(
                // check, if updated Author is returned
                () -> assertNotNull(updatedAuthor, "Updated Author should not be null."),
                () -> assertEquals(author.getId(), updatedAuthor.getId(), "Author ID should match after update."),
                () -> assertEquals(author.getFirstName(), updatedAuthor.getFirstName(), "Author first name should match after update."),
                () -> assertEquals(author.getLastName(), updatedAuthor.getLastName(), "Author last name should match after update.")
        );
        // check, if Methods are invoked
        verify(authorRepository).existsById(author.getId());
        verify(authorRepository).save(any(AuthorEntity.class));
    }

    @Test
    void shouldCallDeleteAndDeleteIdWhenDeleteAuthor() {
        // Configure Mocked Method
        when(authorRepository.existsById(anyInt())).thenReturn(true);
        // invoke delete Method
        authorService.delete(author.getId());

        // check, if Methods are invoked
        verify(authorRepository).existsById(anyInt());
        verify(authorRepository).deleteById(anyInt());
        verify(authorRepository).deleteAuthorIdFromDocument(anyInt());

    }

    @Test
    void ShouldFindAuthorByIdWhenAuthorExists() {
        // Configure Mocked Methods
        when(authorRepository.existsById(anyInt())).thenReturn(true);
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(authorEntity));
        // invoke findById Method
        Author foundAuthor = authorService.findById(author.getId());

        assertAll(
                // check, if requested Author is returned
                () -> assertNotNull(foundAuthor,"Found Author should not be null."),
                () -> assertEquals(authorEntity.getId(), foundAuthor.getId(), "Author ID should match when found by ID."),
                () -> assertEquals(authorEntity.getFirstName(), foundAuthor.getFirstName(), "Author first name should match when found by ID."),
                () -> assertEquals(authorEntity.getLastName(), foundAuthor.getLastName(), "Author last name should match when found by ID.")
        );
        // check, if Methods are invoked
        verify(authorRepository).existsById(anyInt());
        verify(authorRepository).findById(anyInt());
    }

    @Test
    void ShouldThrowExceptionWhenFindAuthorNotExists() {
        // Configure Mocked Methods
        when(authorRepository.existsById(anyInt())).thenReturn(false);

        // invoke findById Method and check Exception
        assertThrows(AuthorNotFoundException.class,
                () -> authorService.findById(anyInt()),
                "Expected AuthorNotFoundException to be thrown when attempting to find a non-existent author.");
    }
    @Test
    void ShouldThrowExceptionWhenUpdateAuthorNotExists() {
        // Configure Mocked Methods
        when(authorRepository.existsById(anyInt())).thenReturn(false);

        // invoke findById Method and check Exception
        assertThrows(AuthorNotFoundException.class,
                () -> authorService.update(author),
                "Expected AuthorNotFoundException to be thrown when attempting to update a non-existent author.");
    }
    @Test
    void ShouldThrowExceptionWhenDeleteAuthorNotExists() {
        // Configure Mocked Methods
        when(authorRepository.existsById(anyInt())).thenReturn(false);

        // invoke findById Method and check Exception
        assertThrows(AuthorNotFoundException.class,
                () -> authorService.delete(anyInt()),
                "Expected AuthorNotFoundException to be thrown when attempting to delete a non-existent author.");
    }

    @Test
    void ShouldFindAllAuthorsWhenElementsExisting() {
        // Configure Mocked Methods
        when(authorRepository.findAll()).thenReturn(Arrays.asList(authorEntity));
        // invoke findAll Method
        List<Author> authors = authorService.findAll();

        // check, if returned list size is correct.
        assertEquals(1, authors.size(), "The size of the returned author list should be 1.");
        // check, if Methods are invoked
        verify(authorRepository).findAll();
    }

    // Test cases for cache implementation


}
