
package com.teotigraphix.gdx.app;

public interface IApplicationModel extends IApplicationComponent {

    ApplicationPreferences getApplicationPreferences();

    ApplicationProject getProject();

    /**
     * @param project
     * @see ApplicationModelProjectCreateEvent
     * @see ApplicationModelProjectLoadEvent
     */
    void setProject(ApplicationProject project);

    public static class ApplicationModelProjectEvent {

        private ApplicationProject project;

        public ApplicationProject getProject() {
            return project;
        }

        public ApplicationModelProjectEvent(ApplicationProject project) {
            this.project = project;
        }
    }

    public static class ApplicationModelProjectCreateEvent extends ApplicationModelProjectEvent {

        public ApplicationModelProjectCreateEvent(ApplicationProject project) {
            super(project);
        }
    }

    public static class ApplicationModelProjectLoadEvent extends ApplicationModelProjectEvent {

        public ApplicationModelProjectLoadEvent(ApplicationProject project) {
            super(project);
        }
    }
}
