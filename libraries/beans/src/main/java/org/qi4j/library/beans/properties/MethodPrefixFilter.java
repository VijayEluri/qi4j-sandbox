/*
 * Copyright 2008 Wen Tao. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package org.qi4j.library.beans.properties;

import org.qi4j.api.common.AppliesToFilter;

import java.beans.Introspector;
import java.lang.reflect.Method;

public class MethodPrefixFilter implements PropertyNameExtractor, AppliesToFilter
{
    private String prefix;

    public MethodPrefixFilter( String prefix )
    {
        this.prefix = prefix;
    }

    public boolean appliesTo( Method method, Class<?> mixin, Class<?> compositeType, Class<?> fragmentClass )
    {
        return matches( method.getName() );
    }

    public boolean matches( String methodName )
    {
        return methodName.startsWith( prefix );
    }

    public String extractPropertyName( String methodName )
    {
        if( !matches( methodName ) )
        {
            return null;
        }
        return Introspector.decapitalize( methodName.substring( prefix.length() ) );
    }
}
