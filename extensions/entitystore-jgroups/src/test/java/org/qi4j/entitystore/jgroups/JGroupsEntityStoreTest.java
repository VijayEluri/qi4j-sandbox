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


package org.qi4j.entitystore.jgroups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.Ignore;
import org.qi4j.api.common.Optional;
import org.qi4j.api.entity.EntityBuilder;
import org.qi4j.api.entity.EntityComposite;
import org.qi4j.api.entity.association.Association;
import org.qi4j.api.entity.association.ManyAssociation;
import org.qi4j.api.property.Immutable;
import org.qi4j.api.property.Property;
import org.qi4j.api.unitofwork.NoSuchEntityException;
import org.qi4j.api.unitofwork.UnitOfWork;
import org.qi4j.api.unitofwork.UnitOfWorkCompletionException;
import org.qi4j.api.value.ValueBuilder;
import org.qi4j.api.value.ValueComposite;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.bootstrap.SingletonAssembler;
import org.qi4j.spi.uuid.UuidIdentityGeneratorService;
import org.qi4j.test.AbstractQi4jTest;

/**
 * Test of JGroups EntityStore backend.
 */
public class JGroupsEntityStoreTest
    extends AbstractQi4jTest
{
    public void assemble( ModuleAssembly module ) throws AssemblyException
    {
        module.addServices( UuidIdentityGeneratorService.class );
        module.addEntities( TestEntity.class );
        module.addValues( TestValue.class );
        module.addServices( JGroupsEntityStoreService.class );
    }

    @Test
    @Ignore
    public void whenNewEntityThenFindInReplica()
        throws Exception
    {
        // Create first app
        SingletonAssembler app1 = new SingletonAssembler()
        {
            public void assemble( ModuleAssembly module ) throws AssemblyException
            {
                module.addServices( JGroupsEntityStoreService.class, UuidIdentityGeneratorService.class ).instantiateOnStartup();
                module.addEntities( TestEntity.class );
            }
        };

        // Create second app
        SingletonAssembler app2 = new SingletonAssembler()
        {
            public void assemble( ModuleAssembly module ) throws AssemblyException
            {
                module.addServices( JGroupsEntityStoreService.class, UuidIdentityGeneratorService.class ).instantiateOnStartup();
                module.addEntities( TestEntity.class );
            }
        };

        // Create entity in app 1
        System.out.println( "Create entity" );
        UnitOfWork app1Unit = app1.unitOfWorkFactory().newUnitOfWork();
        EntityBuilder<TestEntity> builder = app1Unit.newEntityBuilder( TestEntity.class );
        TestEntity instance = builder.instance();
        instance.name().set( "Foo" );
        instance = builder.newInstance();
        app1Unit.complete();

//        Thread.sleep( 5000 );

        // Find entity in app 2
        System.out.println( "Find entity" );
        UnitOfWork app2Unit = app2.unitOfWorkFactory().newUnitOfWork();
        instance = app2Unit.get( instance );

        System.out.println( instance.name() );
        app2Unit.discard();

    }

    @Test
    @Ignore
    public void whenNewEntityThenCanFindEntity()
        throws Exception
    {
        try
        {
            UnitOfWork unitOfWork = unitOfWorkFactory.newUnitOfWork();
            try
            {
                TestEntity instance = createEntity( unitOfWork );
                unitOfWork.complete();

                // Find entity
                unitOfWork = unitOfWorkFactory.newUnitOfWork();
                instance = unitOfWork.get( instance );

                // Check state
                assertThat( "property has correct value", instance.name().get(), equalTo( "Test" ) );
                assertThat( "property has correct value", instance.unsetName().get(), equalTo( null ) );
                assertThat( "association has correct value", instance.association().get(), equalTo( instance ) );
                assertThat( "manyAssociation has correct value", instance.manyAssociation().iterator().next(), equalTo( instance ) );
                unitOfWork.discard();
            }
            catch( UnitOfWorkCompletionException e )
            {
                unitOfWork.discard();
                throw e;
            }
            catch( NoSuchEntityException e )
            {
                unitOfWork.discard();
                throw e;
            }
            catch( RuntimeException e )
            {
                unitOfWork.discard();
                throw e;
            }

        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void whenRemovedEntityThenCannotFindEntity()
        throws Exception
    {
        UnitOfWork unitOfWork = unitOfWorkFactory.newUnitOfWork();
        try
        {
            TestEntity newInstance = createEntity( unitOfWork );
            String identity = newInstance.identity().get();
            unitOfWork.complete();

            // Remove entity
            unitOfWork = unitOfWorkFactory.newUnitOfWork();
            TestEntity instance = unitOfWork.get( newInstance );
            unitOfWork.remove( instance );
            unitOfWork.complete();

            // Find entity
            unitOfWork = unitOfWorkFactory.newUnitOfWork();
            try
            {
                instance = unitOfWork.get( TestEntity.class, identity );
                fail( "Should not be able to find entity" );
            }
            catch( NoSuchEntityException e )
            {
                // Ok!
            }
            unitOfWork.discard();
        }
        catch( UnitOfWorkCompletionException e )
        {
            unitOfWork.discard();
            throw e;
        }
        catch( NoSuchEntityException e )
        {
            unitOfWork.discard();
            throw e;
        }
        catch( RuntimeException e )
        {
            unitOfWork.discard();
            throw e;
        }
    }

    protected TestEntity createEntity( UnitOfWork unitOfWork )
        throws UnitOfWorkCompletionException
    {
        // Create entity
        EntityBuilder<TestEntity> builder = unitOfWork.newEntityBuilder( TestEntity.class );
        TestEntity instance = builder.newInstance();
        String id = instance.identity().get();

        instance.name().set( "Test" );
        instance.association().set( instance );

        ValueBuilder<TestValue> valueBuilder = valueBuilderFactory.newValueBuilder( TestValue.class );
        TestValue state = valueBuilder.prototype();
        state.someValue().set( "Foo" );
        state.otherValue().set( 5 );

        TestValue value = valueBuilder.newInstance();

        instance.valueProperty().set( value );
        instance.manyAssociation().add( instance );

        return instance;
    }

    public interface TestEntity
        extends EntityComposite
    {
        @Optional Property<String> name();

        @Optional Property<String> unsetName();

        @Optional Property<TestValue> valueProperty();

        @Optional Association<TestEntity> association();

        @Optional Association<TestEntity> unsetAssociation();

        ManyAssociation<TestEntity> manyAssociation();
    }

    public interface TestValue extends ValueComposite
    {
        @Immutable Property<String> someValue();

        @Immutable Property<Integer> otherValue();
    }

}