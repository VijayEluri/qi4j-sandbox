package org.qi4j.library.beans.properties;

import java.lang.reflect.Method;
import org.qi4j.composite.AppliesToFilter;

/**
 * Filter for getter methods. Method name must match "get*" or "is*" or "has*".
 */
public class Getters implements AppliesToFilter
{
    public static final MethodNamePrefixAppliesToFilter GET = new MethodNamePrefixAppliesToFilter( "get" );
    public static final MethodNamePrefixAppliesToFilter IS = new MethodNamePrefixAppliesToFilter( "is" );
    public static final MethodNamePrefixAppliesToFilter HAS = new MethodNamePrefixAppliesToFilter( "has" );
    public static final AppliesToFilter GETTERS = new OrAppliesToFilter( GET, IS, HAS );

    public boolean appliesTo( Method method, Class mixin, Class compositeType, Class modelClass )
    {
        return GETTERS.appliesTo( method, mixin, compositeType, modelClass );
    }
}
