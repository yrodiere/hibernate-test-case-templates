package org.hibernate.search.bugs;

import javax.persistence.MappedSuperclass;

import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency;

@MappedSuperclass
public abstract class SearchResult {
    public void setResultTypeSort() {
        // No-op
    }

    @GenericField
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.NO)
    public abstract int getResultTypeSort();
}
