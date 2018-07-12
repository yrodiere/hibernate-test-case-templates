package org.hibernate.bugs;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = DataBaseSourceResource.TABLE_NAME)
@DiscriminatorValue("DATA_BASE")
@PrimaryKeyJoinColumn(name = "id")
@Access(AccessType.PROPERTY)
public class DataBaseSourceResource extends DataSourceResource {
    private static final long serialVersionUID = 2003685890186487451L;

    public static final String TABLE_NAME = "DATA_BASE_SETTINGS";

    private String dbName;
    private String query;

    public DataBaseSourceResource() {
        super();
    }

    @Column(nullable = false)
    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Column(nullable = false)
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return super.toString()
            + "[dbName=" + getDbName()
            + ", query=" + getQuery()
            + "]";
    }
}
