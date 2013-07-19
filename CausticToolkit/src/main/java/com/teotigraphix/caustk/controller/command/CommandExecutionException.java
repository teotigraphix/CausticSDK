////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

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
