package de.krieger.management.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Author")
@Data
@NoArgsConstructor

public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    int id;
    @Column(name = "first_name", columnDefinition = "VARCHAR(128)")
    String firstName;
    @Column(name = "last_name", columnDefinition = "VARCHAR(128)")
    String lastName;
}
