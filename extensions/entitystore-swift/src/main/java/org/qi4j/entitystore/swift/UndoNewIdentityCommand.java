/*
 * Copyright 2008 Niclas Hedhman.
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
package org.qi4j.entitystore.swift;

import org.qi4j.spi.entity.QualifiedIdentity;
import org.qi4j.api.entity.EntityReference;

import java.io.IOException;
import java.io.RandomAccessFile;

public class UndoNewIdentityCommand
    implements UndoCommand
{
    private EntityReference reference;

    public UndoNewIdentityCommand( EntityReference reference )
    {
        this.reference = reference;
    }

    public void undo( RandomAccessFile dataFile, IdentityFile idFile ) throws IOException
    {
        idFile.drop( reference );
    }

    public void save( RandomAccessFile undoJournal ) throws IOException
    {
        undoJournal.writeUTF( reference.toString() );
    }

    static UndoNewIdentityCommand load( RandomAccessFile undoJournal )
        throws IOException
    {
        String idString = undoJournal.readUTF();
        EntityReference ref = new EntityReference( idString );
        return new UndoNewIdentityCommand( ref );
    }
}
