/* IBatisDAO                                                                                                 /* IBatisDAO
 *
 * created: 2006
 *
 * This file is part of Artemis
 *
 * Copyright (C) 2005  Genome Research Limited
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

import com.ibatis.sqlmap.client.SqlMapClient;

import java.util.List;
import java.util.Hashtable;
import java.util.Vector;
import java.sql.*;

import javax.swing.JPasswordField;

/**
 *
 * iBATIS implemetation of the <code>ChadoDAO</code> data
 * access interface.
 *
 */
public class IBatisDAO implements ChadoDAO
{
  /**
   * Define a iBatis data access object. This uses <code>DbSqlConfig</code>
   * to read the configuration in. The system property <quote>chado</quote>
   * can be used to define the database location <i>e.g.</i>
   * -Dchado=host:port/database?user
   */
  public IBatisDAO(final JPasswordField pfield)
  {
    DbSqlConfig.init(pfield);
  }

  /**
   * Get the feature name given a feature_id.
   * @param feature_id  id of feature to query
   * @return    the feature name
   */
  public String getFeatureName(final ChadoFeature feature)
                throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    return (String)sqlMap.queryForObject("getFeatureName", feature);
  }

  /**
   *
   * Get the residues of a feature.
   * @param feature_id  id of feature to query
   * @return    the <code>ChadoFeature</code> with the residues
   * @throws SQLException
   */
  public String getFeatureName(final int feature_id)
                       throws SQLException
  {
    ChadoFeature feature = new ChadoFeature();
    feature.setId(feature_id);
    return getFeatureName(feature);
  }

  /**
   * This can be used to get individual features or children.
   * If ChadoFeature.featureloc.srcfeature_id is set this is used
   * to return the children of that srcfeature_id.
   * @param feature  the feature to query
   * @return    the <code>List</code> of child <code>ChadoFeature</code> objects
   * @throws SQLException
   */
  public List getFeature(final ChadoFeature feature)
                         throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    List feature_list = sqlMap.queryForList("getFeature", feature);

    // merge same features in the list
    return mergeList(feature_list);
  }

  /**
   * Get the properties of a feature.
   * @param uniquename  the unique name of the feature
   * @return  the <code>List</code> of <code>ChadoFeature</code>
   * @throws SQLException
   */
  public List getLazyFeature(final ChadoFeature feature)
                             throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    
    return sqlMap.queryForList("getLazyFeature", feature);
  }
  
  /**
   * Get the residues of a feature.
   * @param feature_id  id of feature to query
   * @throws SQLException
   */
  public ChadoFeature getSequence(final int feature_id)
                        throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    ChadoFeature feature = new ChadoFeature();
    feature.setId(feature_id);
    
    return (ChadoFeature)sqlMap.queryForObject("getSequence",
                                           feature);
  }

  /**
   * Given a list of distict cvterm_id/type_id's of feature types
   * that have residues (from getResidueType()) in the given schema 
   * and the schema name return a list of chado features in the schema
   * with residues.
   * @param cvterm_ids list of cvterm_id/type_id's
   * @param schema      schema/organism name or null
   * @return    the <code>List</code> of <code>ChadoFeature</code> objects
   * @throws SQLException
   */
  public List getResidueFeatures(List cvterm_ids, 
                                 final String schema)
                     throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    
    ChadoFeature feature = new ChadoFeature();
    feature.setSchema(schema);
    feature.setFeatureCvterms(cvterm_ids);

    return sqlMap.queryForList("getResidueFeatures",
                                feature);
  }

  /**
   *
   * For a schema return the type_id's with residues.
   * @param schema      schema/organism name or null
   * @return    the <code>List</code> of type_id's as <code>String</code>
   *            objects
   * @throws SQLException
   */
  public List getResidueType(final String schema)
                     throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    return sqlMap.queryForList("getResidueType", schema);
  }

  /**
   *
   * Get available schemas (as a <code>List</code> of <code>String</code>       
   * objects).
   * @return    the available schemas
   * @throws SQLException
   */
  public List getSchema()
                throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    return sqlMap.queryForList("getSchema", null);
  }

  /**
   *
   * Get the full list of cvterm_id and name as a <code>List</code> of 
   * <code>Cvterm</code> objects.
   * @return    the full list of cvterm_id and name
   * @throws SQLException
   */
  public List getCvterm()
              throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    return sqlMap.queryForList("getCvterm", null);
  }
  
  /**
   * Get the time a feature was last modified.
   * @param uniquename  the unique name of the feature
   * @return  number of rows changed
   * @throws SQLException
   */
  public Timestamp getTimeLastModified
                   (final String uniquename)
                   throws SQLException
  {
    ChadoFeature feature = new ChadoFeature();
    feature.setUniquename(uniquename);

    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    return (Timestamp)sqlMap.queryForObject("getTimeLastModified", feature);
  }

  
  /**
   * 
   * Get dbxref for a feature.
   * @param uniquename  the unique name for the feature. If set to NULL
   *                    all <code>Dbxref</code> are returned.
   * @return a <code>Hashtable</code> of dbxrefs.
   * @throws SQLException
   */
  public Hashtable getDbxref(final String uniquename)
              throws SQLException
  {
    ChadoFeature feature = new ChadoFeature();
    feature.setUniquename(uniquename);

    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    List list = sqlMap.queryForList("getDbxref", feature);  
    return mergeDbxref(list);
  }
  
  /**
   * Get dbxref for a feature.
   * @param uniquename  the unique name for the feature. If set to NULL
   *                    all <code>Dbxref</code> are returned.
   * @return a <code>Hashtable</code> of dbxrefs.
   * @throws SQLException
   */
  public Hashtable getAlias(final String uniquename)
              throws SQLException
  {
    ChadoFeatureSynonym alias = new ChadoFeatureSynonym();
    alias.setUniquename(uniquename);
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    List list = sqlMap.queryForList("getAlias", alias);  
    
    Hashtable synonym = new Hashtable();
    Integer feature_id;
    Vector value;
    for(int i=0; i<list.size(); i++)
    {
      alias = (ChadoFeatureSynonym)list.get(i);
      feature_id = alias.getFeature_id();
      if(synonym.containsKey(feature_id))
        value = (Vector)synonym.get(feature_id);
      else
        value = new Vector();
      
      value.add(alias);
      synonym.put(feature_id, value);
    }
    
    return synonym;
  }
  
  /**
   *
   * @param name cvterm name
   * @param cv_name ontology name (e.g. gene, sequence)
   * @throws SQLException
   */
  public static ChadoCvterm getCvtermID(String name, String cv_name)
                throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    ChadoCvterm cvterm   = new ChadoCvterm();
    ChadoCv cv = new ChadoCv();
    cv.setName(cv_name);
    cvterm.setCv(cv);
    cvterm.setName(name);

    return (ChadoCvterm)sqlMap.queryForObject("getCvterm", cvterm);
  }

//
// WRITE BACK
//
  /**
   *
   * Update attributes defined by the <code>ChadoTransaction</code>.
   * @param tsn         the <code>ChadoTransaction</code>
   * @return	number of rows changed
   * @throws SQLException
   */
  public int updateAttributes
                    (final ChadoTransaction tsn)
                     throws SQLException 
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    return sqlMap.update("updateAttributes", tsn);
  }

  /**
   * Insert attributes defined by the <code>ChadoTransaction</code>.
   * @param tsn         the <code>ChadoTransaction</code>
   * @throws SQLException
   */
  public void insertAttributes
                    (final ChadoTransaction tsn)
                     throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();

    // get the feature id's
    List feature_ids = sqlMap.queryForList("getFeatureID", tsn);

    for(int i=0; i<feature_ids.size(); i++)
    {
      tsn.setFeature_id( ((ChadoFeature)feature_ids.get(i)).getId() );
      sqlMap.insert("insertAttributes", tsn);
    }
  }

  /**
   * Delete attributes defined by the <code>ChadoTransaction</code>.
   * @param tsn         the <code>ChadoTransaction</code>
   * @throws SQLException
   */
  public void deleteAttributes
                    (final ChadoTransaction tsn)
                     throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
  
    // get the feature id's
    List feature_ids = sqlMap.queryForList("getFeatureID", tsn);

    for(int i=0; i<feature_ids.size(); i++)
    {
      tsn.setFeature_id( ((ChadoFeature)feature_ids.get(i)).getId() );
      sqlMap.delete("deleteAttributes", tsn);
    }
  }

  /**
   * Insert a feature into the database defined by the <code>ChadoTransaction</code>.
   * @param tsn                 the <code>ChadoTransaction</code>
   * @parma srcfeature_id       the parent feature identifier
   * @throws SQLException
   */
  public void insertFeature
                    (final ChadoTransaction tsn,
                     final String srcfeature_id)
                     throws SQLException
  {
    // get the organism id from the srcfeature_id 
    ChadoFeature feature = new ChadoFeature();
    ChadoFeatureLoc featureloc = new ChadoFeatureLoc();
    feature.setFeatureloc(featureloc);
    
    featureloc.setSrcfeature_id(Integer.parseInt(srcfeature_id));
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    Integer organism_id = (Integer)sqlMap.queryForObject("getOrganismID", feature);

    //
    // insert feature into feature table
    ChadoFeature chadoFeature = tsn.getChadoFeature();
    ChadoOrganism organism = new ChadoOrganism();
    organism.setId(organism_id.intValue());
    chadoFeature.setOrganism(organism);  
    sqlMap.insert("insertFeature", chadoFeature);

    //
    // get the current feature_id sequence value
    int feature_id = ((Integer)sqlMap.queryForObject("currval", 
                              "feature_feature_id_seq")).intValue();

    //
    // insert feature location into featureloc
    chadoFeature.getFeatureloc().setSrcfeature_id(Integer.parseInt(srcfeature_id));
    chadoFeature.setId(feature_id);
    sqlMap.insert("insertFeatureLoc", chadoFeature);
    
    // insert feature relationship
    if(tsn.getParents() != null || 
       tsn.getDerives_from() != null)
    {
      List feature_ids = sqlMap.queryForList("getFeatureID", tsn);
      
      for(int i=0; i<feature_ids.size(); i++)
      {
        ChadoFeatureRelationship feature_relationship =
              new ChadoFeatureRelationship();
        feature_relationship.setObject_id( ((ChadoFeature)feature_ids.get(i)).getId() );
        feature_relationship.setSubject_id(feature_id);
        
        ChadoCvterm cvterm = new ChadoCvterm();
        if(tsn.getParents().contains( 
            ((ChadoFeature)feature_ids.get(i)).getUniquename() ))
          cvterm.setName("part_of");
        else
          cvterm.setName("derives_from");
          
        feature_relationship.setCvterm(cvterm);
        chadoFeature.setFeature_relationship(feature_relationship);
        sqlMap.insert("insertFeatureRelationship", chadoFeature);
      }
   
    }
  }


  /**
   * Delete a feature from the database defined by the <code>ChadoTransaction</code>.
   * @param tsn         the <code>ChadoTransaction</code>
   * @return    number of rows deleted
   * @throws SQLException
   */
  public int deleteFeature
                    (final ChadoTransaction tsn)
                     throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    ChadoFeature chadoFeature = new ChadoFeature();
    chadoFeature.setUniquename(tsn.getUniqueName());

    return sqlMap.delete("deleteFeature", chadoFeature);
  }

  /**
   * Insert a dbxref for a feature.
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int insertFeatureDbxref(final ChadoTransaction tsn)
                     throws SQLException
  {
    ChadoFeatureDbxref dbxref = tsn.getFeatureDbxref();
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    Integer db_id = (Integer)sqlMap.queryForObject("getDbId", 
                                 dbxref.getDbxref().getDb());
    if(db_id == null)
      throw new SQLException("No database called "+
                             dbxref.getDbxref().getDb().getName()+" found (for "+
                             tsn.getUniqueName()+
                             ") check the spelling!");
    
    dbxref.getDbxref().setDb_id(db_id.intValue());
    
    Integer dbxref_id = (Integer)sqlMap.queryForObject("getDbxrefId", dbxref.getDbxref());
    if(dbxref_id == null)
    {
      // create a new accession entry in dbxref
      sqlMap.insert("insertDbxref", dbxref.getDbxref());
      // now get the new dbxref_id
      dbxref_id = (Integer)sqlMap.queryForObject("getDbxrefId", dbxref.getDbxref());
    }
    
    dbxref.setDbxref_id(dbxref_id.intValue());
    
    //  get the feature id's
    List feature_ids = sqlMap.queryForList("getFeatureID", tsn);
    dbxref.setFeature_id( ((ChadoFeature)feature_ids.get(0)).getId() );
    
    sqlMap.insert("insertFeatureDbxref", dbxref);

    return 1;
  }
  
  /**
   * Delete a dbxref for a feature.
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int deleteFeatureDbxref(final ChadoTransaction tsn)
                     throws SQLException
  {
    ChadoFeatureDbxref dbxref = tsn.getFeatureDbxref();
    
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();

    // get the feature id's
    List feature_ids = sqlMap.queryForList("getFeatureID", tsn);
    
    dbxref.setFeature_id( ((ChadoFeature)feature_ids.get(0)).getId() );
    return sqlMap.delete("deleteFeatureDbxref", dbxref);
  }
  
  /**
   * Insert a synonym for a feature.
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int insertFeatureAlias(final ChadoTransaction tsn)
                     throws SQLException
  {
    final ChadoFeatureSynonym alias = tsn.getAlias();
    
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    Object synonym_id  = sqlMap.queryForObject("getSynonymId", alias);
    
    if(synonym_id == null)
    {
      // create a new synonym name     
      //Long type_id = alias.getType_id();
      //alias.setType_id(type_id);
      sqlMap.insert("insertAlias", alias);
      
      synonym_id  = sqlMap.queryForObject("getSynonymId", alias);
    }
    
    alias.setSynonym_id((Integer)synonym_id);
    sqlMap.insert("insertFeatureAlias", alias);
    return 1;
  }
  
  /**
   * Delete a synonym for a feature.
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int deleteFeatureAlias(final ChadoTransaction tsn)
                     throws SQLException
  {
    final ChadoFeatureSynonym alias = tsn.getAlias();
    
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    List synonym_id_list = sqlMap.queryForList("getFeatureSynonymId", alias);
    
    final Integer synonym_id = (Integer)synonym_id_list.get(0); 
    alias.setSynonym_id(synonym_id);
    
    // check this name is not used some where else, 
    // i.e. in more than one row
    if(synonym_id_list.size() > 1)
      return sqlMap.delete("deleteFeatureAlias", alias);
    else
      return sqlMap.delete("deleteAlias", alias);
  }
  
  /**
   * Update feature_relationship for a feature.
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public void updateFeatureRelationshipsForSubjectId(
      final ChadoTransaction tsn)
                     throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    sqlMap.update("updateFeatureRelationshipsForSubjectId", tsn);
  }
  
  /**
   * Write the time a feature was last modified
   * @param uniquename  the unique name of the feature
   * @param timestamp   the time stamp to use, 
   *                    if NULL use CURRENT_TIMESTAMP
   * @return  number of rows changed
   * @throws SQLException
   */
  public int writeTimeLastModified
                    (final String uniquename,
                     final Timestamp timestamp)
                     throws SQLException
  {
    ChadoTransaction tsn = new ChadoTransaction(ChadoTransaction.UPDATE,
                                                uniquename, "feature", 
                                                null, null);
    if(timestamp == null)
      tsn.addProperty("timelastmodified", "CURRENT_TIMESTAMP");
    else
      tsn.addProperty("timelastmodified", "'"+ timestamp.toString() + "'");
    
    return updateAttributes(tsn);
  }

  /**
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
                     throws SQLException
  {
    ChadoTransaction tsn = new ChadoTransaction(ChadoTransaction.UPDATE,
                                                uniquename, "feature", 
                                                null, null);
    if(timestamp == null)
      tsn.addProperty("timelastmodified", "CURRENT_TIMESTAMP");
    else
      tsn.addProperty("timelastmodified", "'"+ timestamp.toString() + "'");
    
    return updateAttributes(tsn);
  }
  
  public void startTransaction() throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    sqlMap.startTransaction();
  }
  
  public void endTransaction() throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    sqlMap.endTransaction();
  }
  
  public void commitTransaction() throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    sqlMap.commitTransaction();
  }
  
  /**
   * Takes a list and creates a new one merging all feature objects
   * within it with the same feature and stores the qualifiers/attributes
   *  as a hash
   * @param list of feature objects
   * @return list of flattened/merged feature objects
   */
  protected static List mergeList(final List list)
  {
    // merge same features in the list
    int feature_size  = list.size();
    final List flatten_list = new Vector();
    ChadoFeature featNext  = null;

    for(int i = 0; i < feature_size; i++)
    {
      ChadoFeature feat = (ChadoFeature)list.get(i);
      String name  = feat.getUniquename();

      feat.addQualifier(feat.getFeatureprop().getCvterm().getCvtermId(),
                        feat.getFeatureprop());

      if(i < feature_size - 1)
        featNext = (ChadoFeature)list.get(i + 1);

      // merge next line if part of the same feature
      while(featNext != null && featNext.getUniquename().equals(name))
      {
        feat.addQualifier(featNext.getFeatureprop().getCvterm().getCvtermId(),
                          featNext.getFeatureprop());
        i++;

        if(i < feature_size - 1)
          featNext = (ChadoFeature)list.get(i + 1);
        else
          break;
      }

      flatten_list.add(feat);
    }

    return flatten_list;
  }

  /**
   * Takes a list and creates a <code>Hashtable</code> with the keys
   * being the feature_id and the value a <code>Vector</code> of the dbxrefs.
   * @param list  a <code>List</code> of <code>Dbxref</code> objects.
   * @return a <code>Hashtable</code> of dbxrefs.
   */
  protected static Hashtable mergeDbxref(final List list)
  {
    Hashtable dbxrefHash = new Hashtable();
    for(int i = 0; i < list.size(); i++)
    {
      ChadoFeatureDbxref dbxref = (ChadoFeatureDbxref)list.get(i);
      Integer feature_id = new Integer(dbxref.getFeature_id());
      String value = dbxref.getDbxref().getDb().getName() + ":" + 
                     dbxref.getDbxref().getAccession();
      if(dbxrefHash.containsKey(feature_id))
      {
        Vector v = (Vector)dbxrefHash.get(feature_id);
        v.add(value);
        dbxrefHash.put(feature_id, v);
      }  
      else
      {
        Vector v = new Vector();
        v.add(value);
        dbxrefHash.put(feature_id, v);
      }
    }
    return dbxrefHash;
  }

}

