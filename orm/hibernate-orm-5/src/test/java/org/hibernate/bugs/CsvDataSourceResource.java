package org.hibernate.bugs;

import java.nio.charset.StandardCharsets;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = CsvDataSourceResource.TABLE_NAME)
@DiscriminatorValue("CSV")
@PrimaryKeyJoinColumn(name = "id")
@Access(AccessType.PROPERTY)
public class CsvDataSourceResource extends DataSourceResource {
    private static final long serialVersionUID = -7082596009054703218L;

    public static final String TABLE_NAME = "CSV_SETTINGS";

    private String codePage = StandardCharsets.UTF_8.name();
    private char columnDelimiter = ',';

    public CsvDataSourceResource() {
        super();
    }

    @Column(nullable = false)
    public String getCodePage() {
        return codePage;
    }

    public void setCodePage(String codePage) {
        this.codePage = codePage;
    }

    @Column(nullable = false)
    public char getColumnDelimiter() {
        return columnDelimiter;
    }

    public void setColumnDelimiter(char columnDelimiter) {
        this.columnDelimiter = columnDelimiter;
    }

    @Override
    public String toString() {
        return super.toString()
            + "[codePage=" + getCodePage()
            + ", delimiter='" + getColumnDelimiter() + "'"
            + "]";
    }
}
