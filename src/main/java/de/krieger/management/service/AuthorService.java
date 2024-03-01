package de.krieger.management.service;
import de.krieger.management.model.Author;

import java.util.List;
/**
 * Service interface for managing authors.
 */
public interface AuthorService {

    /**
     * Creates a new author.
     *
     * @param author the author object to be created
     * @return the created author
     */
    Author create(Author author);

    /**
     * Updates an existing author.
     *
     * @param author the author object to be updated
     * @return the updated author
     */
    Author update(Author author);

    /**
     * Deletes an author by its ID.
     *
     * @param id the ID of the author to delete
     */
    void delete(int id);

    /**
     * Finds an author by its ID.
     *
     * @param id the ID of the author to find
     * @return the found author, or null if no author is found
     */
    Author findById(int id);

    /**
     * Retrieves all authors.
     *
     * @return a list of all authors
     */
    List<Author> findAll();
}
