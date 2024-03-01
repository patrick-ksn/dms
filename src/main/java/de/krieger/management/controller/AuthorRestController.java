package de.krieger.management.controller;

import de.krieger.management.messaging.RabbitMQSender;
import de.krieger.management.model.Author;
import de.krieger.management.service.AuthorService;
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

@RestController()
@RequestMapping("/api")
@SuppressWarnings("unused")
@Validated
public class AuthorRestController {
    private static final Logger log = LoggerFactory.getLogger(AuthorRestController.class);

    AuthorService authorService;
    private final RabbitMQSender rabbitMQSender;
    @Autowired
    AuthorRestController(AuthorService authorService, RabbitMQSender rabbitMQSender) {
        this.authorService = authorService;
        this.rabbitMQSender = rabbitMQSender;
    }

    @Operation(summary = "Insert a new author", description = "Insert a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Author created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing required fields"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    @PostMapping(path = "/author")
    public ResponseEntity<Void> insertAuthor(@RequestBody @Valid Author author) {
        log.debug("create author: {}", author);
        authorService.create(author);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update an existing author", description = "Update an existing author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing required fields"),
            @ApiResponse(responseCode = "401", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Author id not found")
    })
    @PutMapping(path = "/author/{id}")
    public ResponseEntity<Void> updateAuthor(@PathVariable(name = "id") int id, @RequestBody @Valid Author author) {
        log.debug("update author: {}", author);
        author.setId(id);
        authorService.update(author);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "delete an existing author", description = "delete an existing author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Author delete successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing required parameter"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    @DeleteMapping(path = "/author/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable(name = "id") int id) {
        log.debug("delete author: {}", id);
        authorService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Sends a message via RabbitMQ to the queue for delete a specific author.", description = "Sends a message via RabbitMQ to the queue for delete a specific author.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Message send successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing required parameter"),
            @ApiResponse(responseCode = "401", description = "Not authorized")
    })
    @DeleteMapping("/author/queue/{id}")
    public ResponseEntity<Void> sendMessage(@PathVariable(name = "id") int id) {
        log.debug("send command to delete author: {}", id);
        rabbitMQSender.sendCommand(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "get an existing author", description = "get an existing author by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Author successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request format or missing required parameter"),
            @ApiResponse(responseCode = "401", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Author id not found")
    })
    @GetMapping(path = "/author/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable(name = "id") int id) {
        log.debug("get author :{}" , id );
        return ResponseEntity.ok(authorService.findById(id));
    }

    @Operation(summary = "get all authors as list", description = "get all authors as list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Author list successfully"),
            @ApiResponse(responseCode = "401", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "No Authors found.")
    })
    @GetMapping(path = "/authors")
    public ResponseEntity<List<Author>> getAllAuthors() {
        log.debug("getAll Authors" );
        List<Author> authorList = authorService.findAll();
        if (authorList == null || authorList.isEmpty()) {

            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(authorList);
    }
}
