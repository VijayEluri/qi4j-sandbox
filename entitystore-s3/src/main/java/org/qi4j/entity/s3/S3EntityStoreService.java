/*  Copyright 2008 Rickard Öberg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qi4j.entity.s3;

import org.qi4j.composite.Mixins;
import org.qi4j.library.locking.LockingAbstractComposite;
import org.qi4j.service.Activatable;
import org.qi4j.service.ServiceComposite;
import org.qi4j.spi.entity.EntityStore;

/**
 * EntityStore service backed by Amazon S3 store.
 */

@Mixins( { S3SerializationStoreMixin.class } )
public interface S3EntityStoreService
    extends EntityStore, ServiceComposite, Activatable, LockingAbstractComposite

{
}