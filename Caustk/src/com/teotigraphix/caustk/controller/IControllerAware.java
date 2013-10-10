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

/**
 * The {@link IControllerAware} API is for sub components of the
 * {@link ICaustkController}.
 * <p>
 * When {@link ICaustkController#addComponent(Class, Object)} is called, the
 * {@link #onAttach()} method of the aware component is called. Then
 * {@link ICaustkController#removeComponent(Class)} will call
 * {@link #onDetach()}.
 * <p>
 * Clients should implement this API on the concrete class, so it's very obvious
 * that the client needs to know when it is added and removed from the
 * controller.
 * 
 * @author Michael Schmalle
 */
public interface IControllerAware {

    /**
     * Called when the component is added to the {@link ICaustkController}
     * through the {@link ICaustkController#addComponent(Class, Object)}.
     * 
     * @param controller The {@link ICaustkController}.
     */
    void onAttach(ICaustkController controller);

    /**
     * Called when the component is removed from the {@link ICaustkController}
     * through the {@link ICaustkController#removeComponent(Class)}.
     */
    void onDetach();
}
