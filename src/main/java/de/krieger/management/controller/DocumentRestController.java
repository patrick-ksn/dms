package de.krieger.management.controller;

import de.krieger.management.model.Document;
import de.krieger.management.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SuppressWarnings("unused")
@Validated
public class DocumentRestController {

    private static final Logger log = LoggerFactory.getLogger(DocumentRestController.class);

    DocumentService documentService;

    @Autowired
    DocumentRestController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(summary = "Insert a new document", description = "Insert a new document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing required fields"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    @PostMapping("/document")
    public ResponseEntity<Void> insertDocument(@RequestBody @Valid Document document) {
        log.debug("create document: {}", document);
        documentService.create(document);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update an existing document", description = "Update an existing document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing required fields"),
            @ApiResponse(responseCode = "401", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Document id not found")
    })
    @PutMapping("/document/{id}")
    public ResponseEntity<Void> updateDocument(@PathVariable(name = "id") int id, @RequestBody @Valid Document document) {
        log.debug("update document: {}", document);
        document.setId(id);
        documentService.update(document);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete an existing document", description = "Delete an existing document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing required parameter"),
            @ApiResponse(responseCode = "404", description = "Document id not found")
    })
    @DeleteMapping("/document/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable(name = "id") int id) {
        log.debug("delete document id: {}", id);
        documentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get an existing document", description = "Get an existing document by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Document successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing required parameter"),
            @ApiResponse(responseCode = "401", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Document id not found")
    })
    @GetMapping("/document/{id}")
    public ResponseEntity<Document> getDocument(@PathVariable(name = "id") int id) {
        log.debug("get document id: {}", id);
        Document document = documentService.findById(id);
        return ResponseEntity.ok(document);
    }

    @Operation(summary = "Get all documents as list", description = "Get all documents as list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Document list successfully"),
            @ApiResponse(responseCode = "401", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "No Documents found.")
    })
    @GetMapping(path = "/documents")
    public ResponseEntity<List<Document>> getAllDocuments() {
        log.debug("get Documents");
        List<Document> documentList = documentService.getAll();
        if (documentList == null || documentList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(documentList);
    }
}