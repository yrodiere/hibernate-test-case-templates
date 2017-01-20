package org.hibernate.search.bugs;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class Book {
    private String _text;
    private long id;

    public Book() {
    }

    public Book(String text) {
        this._text = text;
    }

    @Field(name="txtfld")
    public String getText() {
        return _text;
    }

    public void setText(String text) {
        this._text = text;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    // For Hibernate
    protected void setId(long id) {
    	this.id = id;
    }

}
