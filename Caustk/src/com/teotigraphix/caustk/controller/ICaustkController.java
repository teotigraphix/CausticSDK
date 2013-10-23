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

package com.teotigraphix.caustk.controller;

import java.io.File;

import com.teotigraphix.caustk.controller.command.ICommand;
import com.teotigraphix.caustk.controller.command.ICommandHistory;
import com.teotigraphix.caustk.controller.command.ICommandManager;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.ICausticEngine;
import com.teotigraphix.caustk.library.ILibraryManager;
import com.teotigraphix.caustk.project.IProjectManager;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.rack.IRack;
import com.teotigraphix.caustk.service.ISerializeService;

/**
 * The {@link ICaustkController} manages the high level components of the
 * CaustkSDK.
 * <p>
 * To use the controller, more care needs to be taken to setup an application
 * correctly with the {@link ICaustkApplication} and
 * {@link ICaustkConfiguration} implementations.
 * 
 * @author Michael Schmalle
 */
public interface ICaustkController extends ICausticEngine, IDispatcher {

    /**
     * The controller's internal dispatcher.
     */
    IDispatcher getDispatcher();

    //----------------------------------
    // components
    //----------------------------------

    /**
     * Adds an API to the controller.
     * <p>
     * A simple service locator pattern.
     * 
     * @param clazz The class type API key.
     * @param component The implementing instance of the class type.
     */
    void addComponent(Class<?> clazz, Object component);

    /**
     * Removes a component previously registered with
     * {@link #addComponent(Class, Object)}.
     * 
     * @param clazz The class type API used to register the component.
     * @return The removed component instance or <code>null</code> if the
     *         component was not found on the controller.
     */
    Object removeComponent(Class<?> clazz);

    /**
     * Returns a registered API component.
     * 
     * <pre>
     * ISoundSource api = context.getComponent(ISoundSource.class);
     * api.setMasterVolume(0.5f);
     * </pre>
     * 
     * @param clazz The class type API key.
     */
    <T> T getComponent(Class<T> clazz);

    //----------------------------------
    // application
    //----------------------------------

    /**
     * Returns the top level application created at startup.
     * <p>
     * This application instance will also hold the single
     * {@link ICaustkConfiguration} created at startup as well.
     */
    ICaustkApplication getApplication();

    /**
     * Returns the base directory the application is installed add.
     * <p>
     * This directory will include the name of the application as a directory.
     * 
     * @see ICaustkConfiguration#getApplicationRoot()
     */
    File getApplicationRoot();

    /**
     * Returns the application's id used in command messages with the
     * {@link ICommandManager}.
     * 
     * @see ICaustkConfiguration#getApplicationId()
     */
    String getApplicationId();

    //----------------------------------
    // logger
    //----------------------------------

    /**
     * Returns the application logger implementation.
     */
    ICausticLogger getLogger();

    //----------------------------------
    // rack
    //----------------------------------

    /**
     * Returns the single instance of the {@link IRack} created at startup.
     */
    IRack getRack();

    void setRack(IRack value);

    //----------------------------------
    // Services API
    //----------------------------------

    /**
     * JSon serialization service using Google GSon, this may be deprecated in
     * the future.
     */
    ISerializeService getSerializeService();

    /**
     * The application's project manager for maintaing {@link Project} sessions.
     * and storage areas within the {@link #getApplicationRoot()}.
     */
    IProjectManager getProjectManager();

    /**
     * The {@link ILibraryManager} can save and load .ctk libraries.
     */
    ILibraryManager getLibraryManager();

    //----------------------------------
    // ICommandManger API
    //----------------------------------

    /**
     * Adds an {@link ICommand} to the {@link ICommandManager}.
     * 
     * @param message The String message key without the application id.
     * @param command The {@link ICommand} implementation class type.
     */
    void put(String message, Class<? extends ICommand> command);

    /**
     * Removes a message from the {@link ICommandManager}.
     * 
     * @param message The String message key without the application id.
     */
    void remove(String message);

    /**
     * Executes a registered command from the {@link ICommandManager}.
     * 
     * @param message The String message key without the application id.
     * @param args The command arguments, each command should document the in
     *            the API.
     */
    void execute(String message, Object... args) throws CausticException;

    /**
     * Undos the last command in the {@link ICommandHistory}.
     * 
     * @throws CausticException
     */
    void undo() throws CausticException;

    /**
     * Redos the next command in the {@link ICommandHistory}.
     * 
     * @throws CausticException
     */
    void redo() throws CausticException;

    /**
     * Clears the whole {@link ICommandHistory} on the {@link ICommandManager}.
     */
    void clearHistory();

    //----------------------------------
    // Method API
    //----------------------------------

    /**
     * Updated sub components.
     */
    void update();

}
