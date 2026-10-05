package com.sunoza.model;
import jakarta.persistence.*;
@Entity
@Table(name="roles", uniqueConstraints=@UniqueConstraint(columnNames="name"))
public class Role {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=30) private String name;
    protected Role() {}
    public Role(String name) { this.name=name; }
    public Long getId() { return id; }
    public String getName() { return name; }
}
