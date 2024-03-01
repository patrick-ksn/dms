package de.krieger.management.model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Document model")
public class Document {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 50;


    int id;

    @NotBlank(message = "Title should not be empty!")
    @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "title length should be between "+MIN_LENGTH +" and "+MAX_LENGTH+".")
    String title;

    @NotBlank(message = "body should not be empty!")
    @Size(min = MIN_LENGTH, message = "body length should be more than "+MIN_LENGTH)
    String body;

    @Schema(description = "List of authors")
    Set<Author> authors;

    @Schema(description = "List of referenced documents")
    Set<Document> references;
}
