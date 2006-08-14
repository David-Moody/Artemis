/* 
 *
 * created: 2006
 *
 * This file is part of Artemis
 *
 * Copyright (C) 2006  Genome Research Limited
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
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

package uk.ac.sanger.artemis.chado;

import java.sql.*;
import java.util.List;
import java.util.Hashtable;

/**
 *
 * Data access object
 * @see		uk.ac.sanger.artemis.chado.JdbcDAO
 * @see		uk.ac.sanger.artemis.chado.IBatisDAO
 *
 */
public interface ChadoDAO
{

  /**
   *
   * Get the residues of a feature.
   * @param feature_id  id of feature to query
   * @return 	the <code>ChadoFeature</code> with the residues
   * @throws SQLException
   */
  public ChadoFeature getSequence(final int feature_id)
                        throws SQLException;

  /**
   *
   * Get the feature name given a feature_id.
   * @param feature_id  id of feature to query
   * @return	the feature name
   * @throws SQLException
   */
  public String getFeatureName(final int feature_id)
                       throws SQLException;

  /**
   * This can be used to get individual features or children.
   * If ChadoFeature.featureloc.srcfeature_id is set this is used
   * to return the children of that srcfeature_id.
   * @param feature     the feature used to query
   * @return	the <code>List</code> of child <code>ChadoFeature</code> objects
   * @throws SQLException
   */
  public List getFeature(final ChadoFeature feature)
                         throws SQLException;
  
  /**
   * Get the properties of a feature.
   * @param uniquename  the unique name of the feature
   * @return  the <code>List</code> of <code>ChadoFeature</code>
   * @throws SQLException
   */
  public List getLazyFeature(final ChadoFeature feature)
                             throws SQLException;

  /**
   * Given a list of distict cvterm_id/type_id's of feature types
   * that have residues (from getResidueType()) in the given schema
   * and the schema name return a list of features in the schema
   * with residues.
   * @param cvterm_ids list of cvterm_id/type_id's
   * @param schema      schema/organism name or null
   * @return	the <code>List</code> of <code>ChadoFeature</code> objects
   * @throws SQLException
   */
  public List getResidueFeatures(List cvterm_ids,
                                 final String schema)
                     throws SQLException;

  /**
   * For a schema return the type_id's with residues.
   * @param schema      schema/organism name or null
   * @return 	the <code>List</code> of type_id's as <code>String</code>
   *            objects
   * @throws SQLException
   */
  public List getResidueType(String schema)
                     throws SQLException;

  /**
   * 
   * Get available schemas (as a <code>List</code> of <code>String</code>       
   * objects).
   * @return    the available schemas
   * @throws SQLException
   */
  public List getSchema()
              throws SQLException;

  /**
   * 
   * Get the full list of cvterm_id and name as a <code>List</code> of 
   * <code>Cvterm</code> objects.
   * @return	the full list of cvterm_id and name
   * @throws SQLException
   */
  public List getCvterm()
              throws SQLException;

  /**
   * Get the time a feature was last modified.
   * @param uniquename  the unique name of the feature
   * @return  number of rows changed
   * @throws SQLException
   */
  public Timestamp getTimeLastModified
                   (final String uniquename)
                   throws SQLException;
  

  /**
   * Get dbxref for a feature.
   * @param uniquename  the unique name for the feature. If set to NULL
   *                    all <code>Dbxref</code> are returned.
   * @return a <code>Hashtable</code> of dbxrefs.
   * @throws SQLException
   */
  public Hashtable getDbxref(final String uniquename)
              throws SQLException;
  
  /**
   * Get dbxref for a feature.
   * @param uniquename  the unique name for the feature. If set to NULL
   *                    all <code>Dbxref</code> are returned.
   * @return a <code>Hashtable</code> of dbxrefs.
   * @throws SQLException
   */
  public Hashtable getAlias(final String uniquename)
              throws SQLException;
  
//
// WRITE BACK
//
  /**
   * Update attributes defined by the <code>ChadoTransaction</code>.
   * @param tsn		the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int updateAttributes
                    (final ChadoTransaction tsn)
                     throws SQLException;

  /**
   * Insert attributes defined by the <code>ChadoTransaction</code>.
   * @param tsn         the <code>ChadoTransaction</code>
   * @throws SQLException
   */
  public void insertAttributes
                    (final ChadoTransaction tsn)
                     throws SQLException;

  /**
   * Delete attributes defined by the <code>ChadoTransaction</code>.
   * @param tsn         the <code>ChadoTransaction</code>
   * @throws SQLException
   */
  public void deleteAttributes
                    (final ChadoTransaction tsn)
                     throws SQLException;

  /**
   * Insert a feature into the database defined by the <code>ChadoTransaction</code>.
   * @param tsn         	the <code>ChadoTransaction</code>
   * @parma srcfeature_id	the parent feature identifier
   * @throws SQLException
   */
  public void insertFeature
                    (final ChadoTransaction tsn,
                     final String srcfeature_id)
                     throws SQLException;

  /**
   * Delete a feature from the database defined by the <code>ChadoTransaction</code>.
   * @param tsn         the <code>ChadoTransaction</code>
   * @return    number of rows deleted
   * @throws SQLException
   */
  public int deleteFeature
                    (final ChadoTransaction tsn)
                     throws SQLException;
  
  /**
   * Insert a dbxref for a feature.
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int insertFeatureDbxref(final ChadoTransaction tsn)
                     throws SQLException;
  
  /**
   * Delete a dbxref for a feature.
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int deleteFeatureDbxref(final ChadoTransaction tsn)
                     throws SQLException;
  
  /**
   * Insert a synonym for a feature.
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int insertFeatureAlias(final ChadoTransaction tsn)
                     throws SQLException;

  /**
   * Delete a synonym for a feature.
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int deleteFeatureAlias(final ChadoTransaction tsn)
                     throws SQLException;

  
  /**
   * Update feature_relationship for a feature.
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public void updateFeatureRelationshipsForSubjectId(
      final ChadoTransaction tsn)
                     throws SQLException;
  /**
   * 
   * Write the time a feature was last modified
   * @param uniquename	the unique name of the feature
   * @param timestamp   the time stamp to use, 
   *                    if NULL use CURRENT_TIMESTAMP
   * @return  number of rows changed
   * @throws SQLException
   */
  public int writeTimeLastModified
                    (final String uniquename,
                     final Timestamp timestamp)
                     throws SQLException;

  /**
   *
   * Write the time a feature was last accessed
   * @param uniquename  the unique name of the feature
   * @param timestamp   the time stamp to use, 
   *                    if NULL use CURRENT_TIMESTAMP
   * @return  number of rows changed
   * @throws SQLException
   */
  public int writeTimeAccessioned
                    (final String uniquename,
                     final Timestamp timestamp)
                     throws SQLException;
}
