
package com.teotigraphix.libgdx.controller;

import com.google.inject.Singleton;
import com.teotigraphix.libgdx.model.CaustkModelBase;

@Singleton
public class MessageManager extends CaustkModelBase implements IMessageManager {

    public MessageManager() {
    }

    @Override
    public void clearStatus() {
        getController().trigger(new OnMessageManagerChange(MessageManagerChangeKind.Status, ""));
    }

    @Override
    public void setStatus(String status) {
        getController()
                .trigger(new OnMessageManagerChange(MessageManagerChangeKind.Status, status));
    }

    public enum MessageManagerChangeKind {
        Main,

        Status
    }

    public static class OnMessageManagerChange {

        private MessageManagerChangeKind kind;

        private String text;

        public MessageManagerChangeKind getKind() {
            return kind;
        }

        public String getText() {
            return text;
        }

        public OnMessageManagerChange(MessageManagerChangeKind kind, String text) {
            this.kind = kind;
            this.text = text;
        }
    }
}
