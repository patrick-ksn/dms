package de.krieger.management.service;

import de.krieger.management.repository.AuthorRepository;
import de.krieger.management.entity.AuthorEntity;
import de.krieger.management.exception.AuthorNotFoundException;
import de.krieger.management.model.Author;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the AuthorService interface, providing CRUD operations for authors.
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;
    @Autowired
    AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * Creates a new author in the repository. The author's ID will be automatically
     * generated and assigned by the database.
     *
     * @param author the author object to be created
     * @return the saved author with the generated identifier
     */
    @Override
    @Transactional
    @CacheEvict(value = "authors", allEntries = true)
    public Author create(Author author) {
        author.setId(0);
        return toAuthor(authorRepository.save(toAuthorEntity(author)));
    }

    /**
     * Updates the details of an existing author in the repository.
     * If the author does not exist, an AuthorNotFoundException will be thrown.
     *
     * @param author the author object containing the updated details
     * @return the updated author
     * @throws AuthorNotFoundException if the author with the given ID does not exist
     */
    @Override
    @Transactional
    @CacheEvict(value = "authors", allEntries = true)
    public Author update(Author author) {
        checkIfAuthorExistElseThrowException(author.getId());
        return toAuthor(authorRepository.save(toAuthorEntity(author)));
    }
    /**
     * Deletes the author with the specified ID from the repository.
     * If the author does not exist, an AuthorNotFoundException will be thrown.
     *
     * @param id the ID of the author to be deleted
     * @throws AuthorNotFoundException if the author with the given ID does not exist
     */
    @Override
    @Transactional
    @CacheEvict(value = {"authors","documents"}, allEntries = true)
    public void delete(int id) {
        checkIfAuthorExistElseThrowException(id);
        authorRepository.deleteAuthorIdFromDocument(id);
        authorRepository.deleteById(id);
    }
    /**
     * Retrieves an author by their ID. If the author is not found, an AuthorNotFoundException is thrown.
     *
     * @param id the ID of the author to retrieve
     * @return the found author
     * @throws AuthorNotFoundException if no author with the given ID is found
     */
    @Override
    @Cacheable(value = "authors", key = "#id")
    public Author findById(int id) throws AuthorNotFoundException {
        checkIfAuthorExistElseThrowException(id);
        return toAuthor(authorRepository.findById(id).orElseThrow());
    }
    /**
     * Retrieves a list of all authors in the repository.
     *
     * @return A List of Author objects.
     */
    @Override
    @Cacheable(value = "authors")
    public List<Author> findAll() {
        List<AuthorEntity> authorEntities = authorRepository.findAll();
        return authorEntities.stream().
                map(this::toAuthor).
                collect(Collectors.toList());
    }
    /**
     * Converts an AuthorEntity to an Author model.
     *
     * @param authorEntity The AuthorEntity to convert.
     * @return The corresponding Author model or null if the entity is null.
     */
    private Author toAuthor(AuthorEntity authorEntity) {
        if (authorEntity != null) {
            Author author = new Author();
            author.setId(authorEntity.getId());
            author.setFirstName(authorEntity.getFirstName());
            author.setLastName(authorEntity.getLastName());
            return author;
        }
        return null;
    }
    /**
     * Converts an Author model to an AuthorEntity.
     *
     * @param author The Author model to convert.
     * @return The corresponding AuthorEntity.
     */
    private AuthorEntity toAuthorEntity(Author author) {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setId(author.getId());
        authorEntity.setFirstName(author.getFirstName());
        authorEntity.setLastName(author.getLastName());
        return authorEntity;
    }
    /**
     * Checks if an author exists in the repository and throws an exception if not.
     *
     * @param id The ID of the author to check.
     * @throws AuthorNotFoundException if an author with the specified ID does not exist.
     */
    private void checkIfAuthorExistElseThrowException(int id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException("Author id not found - Id: " + id);
        }
    }
}
