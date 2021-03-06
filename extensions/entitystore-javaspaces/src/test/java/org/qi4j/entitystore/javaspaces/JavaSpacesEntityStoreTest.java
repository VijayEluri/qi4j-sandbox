/*  Copyright 2008 Jan Kronquist.
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
package org.qi4j.entitystore.javaspaces;

import java.io.File;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import net.jini.security.policy.DynamicPolicyProvider;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.qi4j.api.common.Visibility;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.entitystore.memory.MemoryEntityStoreService;
import org.qi4j.library.http.JettyServiceAssembler;
import org.qi4j.library.jini.javaspaces.JiniJavaSpacesServiceAssembler;
import org.qi4j.library.jini.lookup.JiniLookupServiceAssembler;
import org.qi4j.library.jini.transaction.JiniTransactionServiceAssembler;
import org.qi4j.library.spaces.javaspaces.JavaSpacesClientConfiguration;
import org.qi4j.library.spaces.javaspaces.JavaSpacesClientService;
import org.qi4j.spi.uuid.UuidIdentityGeneratorService;
import org.qi4j.test.entity.AbstractEntityStoreTest;

/**
 * JavaSpaces EntityStore test
 */
@Ignore
public class JavaSpacesEntityStoreTest
    extends AbstractEntityStoreTest
{
    static
    {
        Policy basePolicy = new AllPolicy();
        DynamicPolicyProvider policyProvider = new DynamicPolicyProvider( basePolicy );
        Policy.setPolicy( policyProvider );
    }

    @SuppressWarnings( "unchecked" )
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        super.assemble( module );
        module.addServices( JavaSpacesEntityStoreService.class,
                            MemoryEntityStoreService.class,
                            UuidIdentityGeneratorService.class )
            .instantiateOnStartup();

        ModuleAssembly configModule = module.layerAssembly().moduleAssembly( "JavaSpacesConfiguration" );
        new JiniTransactionServiceAssembler().assemble( configModule );
        new JiniLookupServiceAssembler().assemble( configModule );
        new JiniJavaSpacesServiceAssembler().assemble( configModule );
        new JettyServiceAssembler().assemble( configModule );
        configModule.addServices( JavaSpacesClientService.class ).visibleIn( Visibility.layer ).instantiateOnStartup();
        configModule.addEntities( JavaSpacesClientConfiguration.class );
        configModule.addServices( MemoryEntityStoreService.class ).instantiateOnStartup();
        configModule.addServices( MemoryEntityStoreService.class,
                                  UuidIdentityGeneratorService.class );
    }

    @Override
    @After
    public void tearDown()
        throws Exception
    {
        super.tearDown();
        delete( new File( "qi4jtemp" ) );
        Thread.sleep( 1000 );
    }

    @Test
    @Override
    public void givenConcurrentUnitOfWorksWhenUoWCompletesThenCheckConcurrentModification()
        throws UnitOfWorkCompletionException
    {
        super.givenConcurrentUnitOfWorksWhenUoWCompletesThenCheckConcurrentModification();
    }

    private void delete( File dir )
    {
        File[] files = dir.listFiles();
        if( files == null )
        {
            return;
        }
        for( File file : files )
        {
            if( file.isDirectory() )
            {
                delete( file );
            }
            file.delete();
        }
        dir.delete();
    }

    @Test
    public void enableTests()
    {
    }

    public static class AllPolicy
        extends Policy
    {

        public AllPolicy()
        {
        }

        public PermissionCollection getPermissions( CodeSource codeSource )
        {
            Permissions allPermission;
            allPermission = new Permissions();
            allPermission.add( new AllPermission() );
            return allPermission;
        }

        public void refresh()
        {
        }
    }
}