package org.hibernate.bugs;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MyEntity {

    @Id
    private Long id;

    public MyEntity() {
    }

    public MyEntity(long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}