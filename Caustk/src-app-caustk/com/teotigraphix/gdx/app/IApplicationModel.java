
package com.teotigraphix.gdx.app;

import java.io.IOException;

import com.teotigraphix.caustk.core.CaustkProject;

public interface IApplicationModel extends IApplicationComponent {

    ApplicationPreferences getApplicationPreferences();

    <T extends CaustkProject> T getProject();

    /**
     * @param project
     * @throws IOException
     * @see ApplicationModelProjectCreateEvent
     * @see ApplicationModelProjectLoadEvent
     */
    <T extends CaustkProject> void setProject(T project) throws IOException;

    void save();

    void dispose();

    //    public enum ApplicationModelProjectEventKind {
    //        Create, Load, Save, Close;
    //    }
    //
    //    public static class ApplicationModelProjectEvent {
    //
    //        private ApplicationModelProjectEventKind kind;
    //
    //        private CaustkProject project;
    //
    //        public ApplicationModelProjectEventKind getKind() {
    //            return kind;
    //        }
    //
    //        public CaustkProject getProject() {
    //            return project;
    //        }
    //
    //        public ApplicationModelProjectEvent(ApplicationModelProjectEventKind kind,
    //                CaustkProject project) {
    //            this.kind = kind;
    //            this.project = project;
    //        }
    //    }

}
