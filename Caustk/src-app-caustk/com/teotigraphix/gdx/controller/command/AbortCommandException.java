
package com.teotigraphix.gdx.controller.command;

public class AbortCommandException extends CommandExecutionException {

    private static final long serialVersionUID = 1L;

    public AbortCommandException() {
    }

    public AbortCommandException(String detailMessage) {
        super(detailMessage);
    }

    public AbortCommandException(Throwable throwable) {
        super(throwable);
    }

    public AbortCommandException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
