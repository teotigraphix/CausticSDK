
package com.teotigraphix.caustk.library.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MetadataInfo {

    private String name = "Untitled";

    private String author = "Unamed";

    private String description = "";

    private Date created;

    private Date modified;

    private List<String> tags = new ArrayList<String>();

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getAuthor() {
        return author;
    }

    public final void setAuthor(String author) {
        this.author = author;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        this.description = description;
    }

    public final Date getCreated() {
        return created;
    }

    public final void setCreated(Date created) {
        this.created = created;
    }

    public final Date getModified() {
        return modified;
    }

    public final void setModified(Date modified) {
        this.modified = modified;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public MetadataInfo() {
    }

}
