/* ReadAndWriteEntry.java

 * This file is part of Artemis
 *
 * Copyright(C) 2008  Genome Research Limited
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or(at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package uk.ac.sanger.artemis.components;

import java.io.File;
import java.io.IOException;

import uk.ac.sanger.artemis.components.database.DatabaseEntrySource;
import uk.ac.sanger.artemis.io.DocumentEntryFactory;
import uk.ac.sanger.artemis.io.EntryInformationException;
import uk.ac.sanger.artemis.sequence.NoSequenceException;
import uk.ac.sanger.artemis.util.InputStreamProgressEvent;
import uk.ac.sanger.artemis.util.InputStreamProgressListener;
import uk.ac.sanger.artemis.util.OutOfRangeException;
import uk.ac.sanger.artemis.Entry;

class ReadAndWriteEntry
{

  private static org.apache.log4j.Logger logger4j = 
    org.apache.log4j.Logger.getLogger(ReadAndWriteEntry.class);
  

  
  public static Entry readEntryFromDatabase(final String srcFeatureId) 
         throws OutOfRangeException, NoSequenceException, IOException
  {
    DatabaseEntrySource entry_source = new DatabaseEntrySource();
    boolean promptUser = true;
    if(System.getProperty("read_only") != null)
      promptUser = false;

    if(!entry_source.setLocation(promptUser))
      return null;
    
    String url = (String)entry_source.getLocation();
    int index  = url.indexOf("?");
    
    String userName = url.substring(index+1).trim();
    if(userName.startsWith("user="))
      userName = userName.substring(5);
    
    final InputStreamProgressListener stream_progress_listener =
        new InputStreamProgressListener() 
    {
      public void progressMade(final InputStreamProgressEvent event) 
      {
        final int char_count = event.getCharCount();
        if(char_count != -1) 
          logger4j.debug("chars read so far: " + char_count);
      }
      public void progressMade(String progress)
      {
        logger4j.debug(progress);
      }
    };
    return entry_source.getEntry(srcFeatureId, userName, 
                                    stream_progress_listener);
  }
  
  public static void writeEntry(final Entry entry) 
         throws IOException, EntryInformationException
  {
    File file = new File("x");
    entry.save(file, DocumentEntryFactory.EMBL_FORMAT, false);
  }
  
  public static void main(final String args[])
  {
    try
    {
      Entry entry = ReadAndWriteEntry.readEntryFromDatabase("1");
      ReadAndWriteEntry.writeEntry(entry);
    }
    catch(Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
}