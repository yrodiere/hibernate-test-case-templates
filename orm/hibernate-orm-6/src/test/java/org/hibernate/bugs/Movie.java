package org.hibernate.bugs;

import com.mongodb.hibernate.annotations.ObjectIdGenerator;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.bson.types.ObjectId;

import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @ObjectIdGenerator
    public ObjectId id;
    public String title;
    public String plot;
    public int year;
    public List<String> cast;
    @Embedded
    public Script script = null;
    public Movie(String title, String plot, int year, List<String> cast) {
        this.title = title;
        this.plot = plot;
        this.year = year;
        this.cast = cast;
    }
    public Movie() {
    }
}