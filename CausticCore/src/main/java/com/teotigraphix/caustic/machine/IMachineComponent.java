////////////////////////////////////////////////////////////////////////////////
// Copyright 2011 Michael Schmalle - Teoti Graphix, LLC
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

package com.teotigraphix.caustic.machine;

import com.teotigraphix.caustic.core.IPersist;
import com.teotigraphix.caustic.core.IRestore;
import com.teotigraphix.caustic.device.IDeviceAware;
import com.teotigraphix.caustic.device.IDeviceComponent;

/**
 * The {@link IMachineComponent} interface creates a client that is a sub
 * component of a machine.
 * <p>
 * Note: this differs from the {@link IDeviceComponent} in that the machine
 * component does not have a name. This interface is an abstraction for machines
 * that allow for greater composition and reuse through machine plugins.
 * 
 * @author Michael Schmalle
 * @copyright Teoti Graphix, LLC
 * @since 1.0
 */
public interface IMachineComponent extends IDeviceAware, IRestore, IPersist {

}
