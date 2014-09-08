
package com.teotigraphix.gdx.app;

import java.io.File;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer.Tag;

public abstract class ApplicationProject {

    @Tag(5)
    private boolean created = false;

    @Tag(10)
    private String name;

    @Tag(11)
    private String location;

    public boolean isCreated() {
        return created;
    }

    public void setIsCreated() {
        created = true;
    }

    public String getName() {
        return name;
    }

    public File getLocation() {
        return new File(location);
    }

    public ApplicationProject() {
    }

    public ApplicationProject(String name, String location) {
        this.name = name;
        this.location = location;
    }

}
