
package com.teotigraphix.caustk.project;

import java.util.Date;

public class ProjectInfo {

    private String name;

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

    private String author;

    private String description;

    private Date created;

    private Date modified;

    public ProjectInfo() {
    }

}
