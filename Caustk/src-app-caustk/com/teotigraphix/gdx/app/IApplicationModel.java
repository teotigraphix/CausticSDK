
package com.teotigraphix.gdx.app;

import java.io.IOException;

import com.teotigraphix.caustk.core.CaustkProject;

public interface IApplicationModel extends IApplicationComponent {

    ApplicationPreferences getApplicationPreferences();

    CaustkProject getProject();

    /**
     * @param project
     * @throws IOException
     * @see ApplicationModelProjectCreateEvent
     * @see ApplicationModelProjectLoadEvent
     */
    void setProject(CaustkProject project) throws IOException;

    public static class ApplicationModelProjectEvent {

        private CaustkProject project;

        public CaustkProject getProject() {
            return project;
        }

        public ApplicationModelProjectEvent(CaustkProject project) {
            this.project = project;
        }
    }

    public static class ApplicationModelProjectCreateEvent extends ApplicationModelProjectEvent {

        public ApplicationModelProjectCreateEvent(CaustkProject project) {
            super(project);
        }
    }

    public static class ApplicationModelProjectLoadEvent extends ApplicationModelProjectEvent {

        public ApplicationModelProjectLoadEvent(CaustkProject project) {
            super(project);
        }
    }
}
