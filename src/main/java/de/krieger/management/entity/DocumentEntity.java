package de.krieger.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import java.util.Set;

@Entity
@Table(name = "Document")
@Data
@NoArgsConstructor
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "title")
    String title;
    @Column(name = "body")
    String body;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.DETACH,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "document_author",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @BatchSize(size = 20)
    Set<AuthorEntity> authors;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.DETACH,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "document_reference",
            joinColumns = @JoinColumn(name = "document_id"),
            inverseJoinColumns = @JoinColumn(name = "reference_id")
    )
    @BatchSize(size = 20)
    Set<DocumentEntity> references;
}
