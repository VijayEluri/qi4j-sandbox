/*
 * Copyright 2008 Niclas Hedhman. All rights Reserved.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.qi4j.library.beans.support;

import org.qi4j.api.composite.TransientBuilderFactory;
import org.qi4j.api.composite.TransientBuilder;
import org.qi4j.api.entity.association.AssociationInfo;

public class Wrapper
{
    static Object wrap( Object resultObject, AssociationInfo info, TransientBuilderFactory cbf )
    {
        Class type = (Class) info.type();
        if( type.isInterface() )
        {
            TransientBuilder<?> builder = cbf.newTransientBuilder( type );
            builder.use( resultObject );
            return builder.newInstance();
        }
        return resultObject;
    }

    static Object unwrap( Object object )
    {
        if( object instanceof JavabeanSupport )
        {
            return ( (JavabeanSupport) object ).getJavabean();
        }
        return object;
    }

}
