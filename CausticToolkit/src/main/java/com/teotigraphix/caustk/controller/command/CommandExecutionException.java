
package com.teotigraphix.caustk.controller.command;

public class CommandExecutionException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3602002464749099348L;

    public CommandExecutionException() {
        // TODO Auto-generated constructor stub
    }

    public CommandExecutionException(String detailMessage) {
        super(detailMessage);
        // TODO Auto-generated constructor stub
    }

    public CommandExecutionException(Throwable throwable) {
        super(throwable);
        // TODO Auto-generated constructor stub
    }

    public CommandExecutionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

}
