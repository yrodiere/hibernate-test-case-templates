package org.hibernate.search.bugs;

import javax.persistence.MappedSuperclass;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;

@MappedSuperclass
public class GameInfo {

    @FullTextField(analyzer = "gameName")
    private String name;

    @FullTextField(analyzer = "gameDescription")
    private String description;

    @KeywordField
    private String platform;

    @KeywordField
    private String platformId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }
}