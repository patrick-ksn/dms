package de.krieger.management.service;
import de.krieger.management.model.Document;

import java.util.List;

/**
 * Service interface for managing documents.
 */
public interface DocumentService {

    /**
     * Creates a new document.
     *
     * @param document the document object to be created
     * @return the created document
     */
    Document create(Document document);

    /**
     * Updates an existing document.
     *
     * @param document the document object to be updated
     * @return the updated document
     */
    Document update(Document document);

    /**
     * Deletes a document by its ID.
     *
     * @param id the ID of the document to delete
     */
    void delete(int id);

    /**
     * Finds a document by its ID.
     *
     * @param id the ID of the document to find
     * @return the found document, or null if no document is found
     */
    Document findById(int id);

    /**
     * Retrieves all documents.
     *
     * @return a list of all documents
     */
    List<Document> getAll();
}
