/* IBatisDAO
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
   *
   * Get the feature name given a feature_id and schema.
   * @param feature_id  id of feature to query
   * @param schema      schema/organism name or null
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
   * @param schema      schema/organism name or null
   * @return    the <code>ChadoFeature</code> with the residues
   * @throws SQLException
   */
  public String getFeatureName(final int feature_id,
                               final String schema)
                       throws SQLException
  {
    ChadoFeature feature = new ChadoFeature();
    feature.setId(feature_id);
    if(schema != null)
      feature.setSchema(schema);
    return getFeatureName(feature);
  }

  /**
   *
   * Get child feature properties for a given parent
   * feature to be able to construct a GFF like feature.
   * @param parentFeatureID  the id of parent feature to query
   * @param schema           the schema/organism name or null
   * @return    the <code>List</code> of child <code>ChadoFeature</code> objects
   * @throws SQLException
   */
  public List getGff(final int feature_id,
                     final String schema)
                     throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    ChadoFeature feature = new ChadoFeature();
    feature.setId(feature_id);
    if(schema != null)
      feature.setSchema(schema);

    List feature_list = sqlMap.queryForList("getGffLine", feature);

    // merge same features in the list
    return JdbcDAO.mergeList(feature_list);
  }

  /**
   * Get the properties of a feature.
   * @param uniquename  the unique name of the feature
   * @param schema_list the <code>List</code> of schemas to search
   * @return  the <code>List</code> of <code>ChadoFeature</code>
   * @throws SQLException
   */
  public List getFeature(final String uniquename,
                         final List schema_list)
                         throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    ChadoFeature feature = new ChadoFeature();
    feature.setUniquename(uniquename);
    
    List list = new Vector();
    for(int i=0; i<schema_list.size(); i++)
    {  
      String schema = (String)schema_list.get(i);
      feature.setSchema(schema);
      List res_list = sqlMap.queryForList("getFeature", feature);
      
      for(int j=0; j<res_list.size(); j++)
        ((ChadoFeature)res_list.get(j)).setSchema(schema);
      list.addAll( JdbcDAO.mergeList(res_list) );
    }
    
    return list;
  }
  
  /**
   *
   * Get the residues of a feature.
   * @param feature_id  id of feature to query
   * @param schema      schema/organism name or null
   * @throws SQLException
   */
  public ChadoFeature getSequence(final int feature_id,
                             final String schema)
                        throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    ChadoFeature feature = new ChadoFeature();
    feature.setId(feature_id);
    if(schema != null)
      feature.setSchema(schema);
    return (ChadoFeature)sqlMap.queryForObject("getSequence",
                                           feature);
  }

  /**
   *
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
    SchemaCVList schema_CVlist = new SchemaCVList();
    schema_CVlist.setSchema(schema);
    schema_CVlist.setCvlist(cvterm_ids);

    return sqlMap.queryForList("getSchemaResidueFeatures",
                                schema_CVlist);
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
   * @param schema      schema/organism name or null
   * @param uniquename  the unique name of the feature
   * @return  number of rows changed
   * @throws SQLException
   */
  public Timestamp getTimeLastModified
                   (final String schema, final String uniquename)
                   throws SQLException
  {
    ChadoFeature feature = new ChadoFeature();
    feature.setSchema(schema);
    feature.setUniquename(uniquename);

    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    return (Timestamp)sqlMap.queryForObject("getTimeLastModified", feature);
  }

  
  /**
   * 
   * Get dbxref for a feature.
   * @param schema      the postgres schema name
   * @param uniquename  the unique name for the feature. If set to NULL
   *                    all <code>Dbxref</code> are returned.
   * @return a <code>Hashtable</code> of dbxrefs.
   * @throws SQLException
   */
  public Hashtable getDbxref(final String schema, final String uniquename)
              throws SQLException
  {
    ChadoFeature feature = new ChadoFeature();
    feature.setSchema(schema);
    feature.setUniquename(uniquename);

    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    List list = sqlMap.queryForList("getDbxref", feature);  
    return JdbcDAO.mergeDbxref(list);
  }
  
  /**
   * Get dbxref for a feature.
   * @param schema      the postgres schema name
   * @param uniquename  the unique name for the feature. If set to NULL
   *                    all <code>Dbxref</code> are returned.
   * @return a <code>Hashtable</code> of dbxrefs.
   * @throws SQLException
   */
  public Hashtable getAlias(final String schema, final String uniquename)
              throws SQLException
  {
    Alias alias = new Alias();
    alias.setSchema(schema);
    alias.setUniquename(uniquename);
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    List list = sqlMap.queryForList("getAlias", alias);  
    
    Hashtable synonym = new Hashtable();
    Integer feature_id;
    Vector value;
    for(int i=0; i<list.size(); i++)
    {
      alias = (Alias)list.get(i);
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
  public static Cvterm getCvtermID(String name, String cv_name)
                throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    Cvterm cvterm   = new Cvterm();
    cvterm.setName(name);
    cvterm.setCv_name(cv_name);
    return (Cvterm)sqlMap.queryForObject("getCvterm", cvterm);
  }

//
// WRITE BACK
//
  /**
   *
   * Update attributes defined by the <code>ChadoTransaction</code>.
   * @param schema      schema/organism name or null
   * @param tsn         the <code>ChadoTransaction</code>
   * @return	number of rows changed
   * @throws SQLException
   */
  public int updateAttributes
                    (final String schema, final ChadoTransaction tsn)
                     throws SQLException 
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    tsn.setSchema(schema);
    return sqlMap.update("updateAttributes", tsn);
  }

  /**
   * Insert attributes defined by the <code>ChadoTransaction</code>.
   * @param schema      schema/organism name or null
   * @param tsn         the <code>ChadoTransaction</code>
   * @throws SQLException
   */
  public void insertAttributes
                    (final String schema, final ChadoTransaction tsn)
                     throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    tsn.setSchema(schema);

    // get the feature id's
    List feature_ids = sqlMap.queryForList("getFeatureID", tsn);

    for(int i=0; i<feature_ids.size(); i++)
    {
      tsn.setFeature_id( ((Integer)feature_ids.get(i)).intValue() );
      sqlMap.insert("insertAttributes", tsn);
    }
  }

  /**
   * Delete attributes defined by the <code>ChadoTransaction</code>.
   * @param schema      schema/organism name or null
   * @param tsn         the <code>ChadoTransaction</code>
   * @throws SQLException
   */
  public void deleteAttributes
                    (final String schema, final ChadoTransaction tsn)
                     throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    tsn.setSchema(schema);
  
    // get the feature id's
    List feature_ids = sqlMap.queryForList("getFeatureID", tsn);

    for(int i=0; i<feature_ids.size(); i++)
    {
      tsn.setFeature_id( ((Integer)feature_ids.get(i)).intValue() );
      sqlMap.delete("deleteAttributes", tsn);
    }
  }

  /**
   * Insert a feature into the database defined by the <code>ChadoTransaction</code>.
   * @param schema              schema/organism name or null
   * @param tsn                 the <code>ChadoTransaction</code>
   * @parma srcfeature_id       the parent feature identifier
   * @throws SQLException
   */
  public void insertFeature
                    (final String schema, final ChadoTransaction tsn,
                     final String srcfeature_id)
                     throws SQLException
  {
    // get the organism id from the srcfeature_id 
    ChadoFeature feature = new ChadoFeature();
    ChadoFeatureLoc featureloc = new ChadoFeatureLoc();
    feature.setFeatureloc(featureloc);
    feature.setSchema(schema);
    
    featureloc.setSrcfeature_id(Integer.parseInt(srcfeature_id));
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    Integer organism_id = (Integer)sqlMap.queryForObject("getOrganismID", feature);

    //
    // insert feature into feature table
    ChadoFeature chadoFeature = tsn.getChadoFeature();
    Organism organism = new Organism();
    organism.setId(organism_id.intValue());
    chadoFeature.setSchema(schema);
    chadoFeature.setOrganism(organism);  
    sqlMap.insert("insertFeature", chadoFeature);

    //
    // get the current feature_id sequence value
    int feature_id = ((Integer)sqlMap.queryForObject("currval", 
                              schema+".feature_feature_id_seq")).intValue();

    //
    // insert feature location into featureloc
    chadoFeature.getFeatureloc().setSrcfeature_id(Integer.parseInt(srcfeature_id));
    chadoFeature.setId(feature_id);
    sqlMap.insert("insertFeatureLoc", chadoFeature);
  }


  /**
   * Delete a feature from the database defined by the <code>ChadoTransaction</code>.
   * @param schema      schema/organism name or null
   * @param tsn         the <code>ChadoTransaction</code>
   * @return    number of rows deleted
   * @throws SQLException
   */
  public int deleteFeature
                    (final String schema, final ChadoTransaction tsn)
                     throws SQLException
  {
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    ChadoFeature chadoFeature = new ChadoFeature();
    chadoFeature.setSchema(schema);
    chadoFeature.setUniquename(tsn.getUniqueName());

    return sqlMap.delete("deleteFeature", chadoFeature);
  }

  /**
   * Insert a dbxref for a feature.
   * @param schema        schema/organism name or null
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int insertFeatureDbxref(final String schema, final ChadoTransaction tsn)
                     throws SQLException
  {
    Dbxref dbxref = tsn.getFeatureDbxref();
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    Integer db_id = (Integer)sqlMap.queryForObject("getDbId", dbxref);
    if(db_id == null)
      throw new SQLException("No database called "+
                             dbxref.getName()+" found (for "+
                             tsn.getUniqueName()+
                             ") check the spelling!");
    
    dbxref.setDb_id(db_id.intValue());
    
    Integer dbxref_id = (Integer)sqlMap.queryForObject("getDbxrefId", dbxref);
    if(dbxref_id == null)
    {
      // create a new accession entry in dbxref
      sqlMap.insert("insertDbxref", dbxref);
      // now get the new dbxref_id
      dbxref_id = (Integer)sqlMap.queryForObject("getDbxrefId", dbxref);
    }
    
    dbxref.setDbxref_id(dbxref_id.intValue());
    
    //  get the feature id's
    tsn.setSchema(schema);
    List feature_ids = sqlMap.queryForList("getFeatureID", tsn);
    dbxref.setFeature_id( ((Integer)feature_ids.get(0)).intValue() );
    
    dbxref.setSchema(schema);
    sqlMap.insert("insertFeatureDbxref", dbxref);

    return 1;
  }
  
  /**
   * Delete a dbxref for a feature.
   * @param schema        schema/organism name or null
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int deleteFeatureDbxref(final String schema, final ChadoTransaction tsn)
                     throws SQLException
  {
    Dbxref dbxref = tsn.getFeatureDbxref();
    
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    tsn.setSchema(schema);
    dbxref.setSchema(schema);
    // get the feature id's
    List feature_ids = sqlMap.queryForList("getFeatureID", tsn);
    
    dbxref.setFeature_id( ((Integer)feature_ids.get(0)).intValue() );
    return sqlMap.delete("deleteFeatureDbxref", dbxref);
  }
  
  /**
   * Insert a synonym for a feature.
   * @param schema        schema/organism name or null
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int insertFeatureAlias(final String schema, final ChadoTransaction tsn)
                     throws SQLException
  {
    final Alias alias = tsn.getAlias();
    alias.setSchema(schema);
    
    SqlMapClient sqlMap = DbSqlConfig.getSqlMapInstance();
    Object synonym_id  = sqlMap.queryForObject("getSynonymId", alias);
    
    if(synonym_id == null)
    {
      // create a new synonym name     
      Long type_id = alias.getType_id();
      alias.setType_id(type_id);
      sqlMap.insert("insertAlias", alias);
      
      synonym_id  = sqlMap.queryForObject("getSynonymId", alias);
    }
    
    alias.setSynonym_id((Integer)synonym_id);
    sqlMap.insert("insertFeatureAlias", alias);
    return 1;
  }
  
  /**
   * Delete a synonym for a feature.
   * @param schema        schema/organism name or null
   * @param tsn           the <code>ChadoTransaction</code>
   * @return    number of rows changed
   * @throws SQLException
   */
  public int deleteFeatureAlias(final String schema, final ChadoTransaction tsn)
                     throws SQLException
  {
    final Alias alias = tsn.getAlias();
    alias.setSchema(schema);
    
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
   * Write the time a feature was last modified
   * @param schema      schema/organism name or null
   * @param uniquename  the unique name of the feature
   * @param timestamp   the time stamp to use, 
   *                    if NULL use CURRENT_TIMESTAMP
   * @return  number of rows changed
   * @throws SQLException
   */
  public int writeTimeLastModified
                    (final String schema, final String uniquename,
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
    
    return updateAttributes(schema, tsn);
  }

  /**
   * Write the time a feature was last accessed
   * @param schema      schema/organism name or null
   * @param uniquename  the unique name of the feature
   * @param timestamp   the time stamp to use, 
   *                    if NULL use CURRENT_TIMESTAMP
   * @return  number of rows changed
   * @throws SQLException
   */
  public int writeTimeAccessioned
                    (final String schema, final String uniquename,
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
    
    return updateAttributes(schema, tsn);
  }
}

