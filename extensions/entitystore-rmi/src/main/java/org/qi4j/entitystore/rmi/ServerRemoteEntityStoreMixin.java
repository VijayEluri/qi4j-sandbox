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
package org.qi4j.entitystore.rmi;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.locks.ReadWriteLock;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.injection.scope.This;
import org.qi4j.api.injection.scope.Uses;
import org.qi4j.api.service.Activatable;
import org.qi4j.api.service.ServiceReference;
import org.qi4j.api.structure.Module;
import org.qi4j.library.locking.WriteLock;
import org.qi4j.spi.Qi4jSPI;
import org.qi4j.spi.entity.EntityState;
import org.qi4j.spi.entity.QualifiedIdentity;
import org.qi4j.spi.entitystore.EntityStore;
import org.qi4j.spi.entitystore.EntityStoreException;
import org.qi4j.spi.service.ServiceDescriptor;

/**
 * RMI server implementation of EntityStore
 */
public class ServerRemoteEntityStoreMixin
    implements RemoteEntityStore, Activatable
{
    @Uses
    private ServiceDescriptor descriptor;

    @This
    private RemoteEntityStore remote;

    @This
    private ReadWriteLock lock;

    @Structure
    private Module module;

    @Structure
    private Qi4jSPI spi;

    @Service
    private EntityStore entityStore;

    @Service
    private ServiceReference<Registry> registry;

    // Activatable implementation
    public void activate()
        throws Exception
    {
        RemoteEntityStore stub = (RemoteEntityStore) UnicastRemoteObject.exportObject( remote, 0 );
        registry.get().bind( descriptor.identity(), stub );
    }

    public void passivate()
        throws Exception
    {
        if( registry.isActive() )
        {
            registry.get().unbind( descriptor.identity() );
        }
        UnicastRemoteObject.unexportObject( remote, true );
    }

    // EntityStore implementation
    @WriteLock
    public EntityState getEntityState( QualifiedIdentity identity )
        throws IOException
    {
//        EntityState state = getEntityState( entityStore, identity );
//
//        Map<QualifiedName, Object> properties = copyProperties( state );
//        Map<QualifiedName, QualifiedIdentity> associations = copyAssociations( state );
//        Map<QualifiedName, Collection<QualifiedIdentity>> manyAssociations = copyManyAssociations( state );
//
//        return new DefaultEntityState( state.version(),
//                                       state.lastModified(),
//                                       identity,
//                                       state.status(),
//                                       getEntityType( identity ),
//                                       properties,
//                                       associations,
//                                       manyAssociations );
        return null;
    }

//    private Map<QualifiedName, Collection<QualifiedIdentity>> copyManyAssociations( EntityState state )
//    {
//        Map<QualifiedName, Collection<QualifiedIdentity>> manyAssociations = new HashMap<QualifiedName, Collection<QualifiedIdentity>>();
//        for( QualifiedName associationName : state.manyAssociationNames() )
//        {
//            Collection<QualifiedIdentity> idCollection = state.getManyAssociation( associationName );
//            if( idCollection instanceof Set )
//            {
//                Set<QualifiedIdentity> collectionCopy = new HashSet<QualifiedIdentity>( idCollection );
//                manyAssociations.put( associationName, collectionCopy );
//            }
//            else if( idCollection instanceof List )
//            {
//                List<QualifiedIdentity> collectionCopy = new ArrayList<QualifiedIdentity>( idCollection );
//                manyAssociations.put( associationName, collectionCopy );
//            }
//        }
//        return manyAssociations;
//    }

//    private Map<QualifiedName, QualifiedIdentity> copyAssociations( EntityState state )
//    {
//        Map<QualifiedName, QualifiedIdentity> associations = new HashMap<QualifiedName, QualifiedIdentity>();
//        for( QualifiedName associationName : state.associationNames() )
//        {
//            QualifiedIdentity id = state.getAssociation( associationName );
//            associations.put( associationName, id );
//        }
//        return associations;
//    }
//
//    private Map<QualifiedName, Object> copyProperties( EntityState state )
//    {
//        Map<QualifiedName, Object> properties = new HashMap<QualifiedName, Object>();
//        for( QualifiedName propertyName : state.propertyNames() )
//        {
//            Object value = state.getProperty( propertyName );
//            properties.put( propertyName, value );
//        }
//        return properties;
//    }

//    private EntityType getEntityType( QualifiedIdentity identity )
//    {
//        final String typeName = identity.type();
//        try
//        {
//            return spi.getEntityDescriptor( module.classLoader().loadClass( typeName ), module ).entityType();
//        }
//        catch( ClassNotFoundException e )
//        {
//            throw new EntityStoreException( "Error accessing CompositeType for type " + typeName, e );
//        }
//    }

    public void prepare( Iterable<EntityState> newStates,
                         Iterable<EntityState> loadedStates,
                         Iterable<QualifiedIdentity> removedStates
    )
    {
//        lock.writeLock().lock();
//        try
//        {
//            final StateCommitter committer = entityStore.prepare( newStates, loadedStates, removedStates );
//            try
//            {
//                committer.commit();
//            }
//            catch( EntityStoreException e )
//            {
//                committer.cancel();
//                throw e;
//            }
//
//        }
//
//        finally
//        {
//            lock.writeLock().unlock();
//        }
    }

    public EntityState getEntityState( EntityStore entityStore, QualifiedIdentity qid )
        throws EntityStoreException
    {
        EntityState entityState = null;
//        do
//        {
//            try
//            {
//                entityState = entityStore.getEntityState( qid );
//            }
//            catch( UnknownEntityTypeException e )
//            {
//                // Check if it is this type that the store doesn't understand
//                EntityType entityType = getEntityType( qid );
//                if( e.getMessage().equals( entityType.type() ) )
//                {
//                    entityStore.registerEntityType( entityType );
//                    // Try again
//                }
//                else
//                {
//                    throw e; // ???
//                }
//            }
//        }
//        while( entityState == null );

        return entityState;
    }
}