package de.telran.gartenshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@EqualsAndHashCode
//@ToString
public class CategoryEntity {
    @Id
    @Column(name = "CategoryID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "Name")
    private String name;

    @OneToMany(mappedBy = "category")
    private Set<ProductEntity> products = new HashSet<>();
}
