package org.hibernate.search.bugs;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@Entity
public class DataPackage {
    @Id
    private Long id;

    @GenericField
    private Calendar startDate;

    @GenericField
    private Calendar endDate;

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    public DataPackage() {}

    public DataPackage(Long id, Calendar startDate, Calendar endDate, Asset asset) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.asset = asset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }
}
