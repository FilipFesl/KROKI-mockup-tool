/*
 * Created on Jan 4, 2005
 * 
 *   JSQLWriter - interactive template-based stored procedure generator
 *   Copyright (C) 2005 Igor Dejanovic, igord@uns.ns.ac.yu
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program; if not, write to the Free Software
 *   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package kroki.app.importt.dbmeta.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import kroki.app.importt.dbmeta.MetaDataRepository;

/**
 * 
 * @version <code>
 * <li type="square"><b>1.0</b></li> 
 * <li type="square"><b>Date:</b> Jan 4, 2005</li>
 * <li type="square"><b>Time:</b> 5:35:56 PM</li>
 * </code>
 * @author Igor Dejanovic, FTN, 
 * <a href="mailto:igord@uns.ns.ac.yu">igord@uns.ns.ac.yu</a>
 */
public class Table {
    private String name;
    private Columns columns;
    private ArrayList<ColumnFKReferences> columnFKReferences = new ArrayList<ColumnFKReferences>();
    
    private Connection conn;
    
    public Table(Connection aConn, String aTableName) throws SQLException{
        super();
        name = aTableName;
        conn = aConn;
        this.setColumnFKReferences(getFKReferences());
        initColumns();
    }

    /**
     * @throws SQLException
     * 
     */
    private void initColumns() throws SQLException {
        columns = new Columns();
        Hashtable<String, String> pkCols = getPKCols();
        Hashtable<String, String> fkCols = getFKCols();
        
        DatabaseMetaData dbMeta = conn.getMetaData();
        ResultSet rst = dbMeta.getColumns(null, null, name, null);
	    while(rst.next()){
	    	Column col = new Column();
	    	col.setName(rst.getString("COLUMN_NAME"));
	    	col.setType(rst.getString("TYPE_NAME"));
	    	col.setSQLDataType(rst.getInt("DATA_TYPE"));
	    	col.setLength(rst.getInt("COLUMN_SIZE"));
	    	col.setDecimalDigits(rst.getInt("DECIMAL_DIGITS"));
	    	col.setNullable(rst.getInt("NULLABLE") == DatabaseMetaData.columnNullable ? true : false);
	    	col.setPartOfPK(pkCols.containsKey(rst.getString("COLUMN_NAME")));
	    	col.setPartOfFK(fkCols.containsKey(rst.getString("COLUMN_NAME")));
	        columns.putColumn(col);
    	}       
        
    }

    /**
     * @return
     * @throws SQLException
     */
    private Hashtable<String, String> getPKCols() throws SQLException {
        Hashtable<String, String> pkCols = new Hashtable<String, String>();
        DatabaseMetaData dbMeta = conn.getMetaData();
        ResultSet rst = dbMeta.getPrimaryKeys(null, null, name);
	    while(rst.next()){
	    		pkCols.put(rst.getString("COLUMN_NAME"), rst.getString("COLUMN_NAME"));
    	}        
        return pkCols;
    }

    /**
     * @return
     * @throws SQLException
     */
    private Hashtable<String, String> getFKCols() throws SQLException {
        Hashtable<String, String> fkCols = new Hashtable<String, String>();
        DatabaseMetaData dbMeta = conn.getMetaData();
        ResultSet rst = dbMeta.getImportedKeys(null, null, name);
	    while(rst.next()){
	    	fkCols.put(rst.getString("FKCOLUMN_NAME"), rst.getString("FKCOLUMN_NAME"));
    	}        
	    return fkCols;
	}        
    
	private ArrayList<ColumnFKReferences> getFKReferences() throws SQLException {
		ArrayList<ColumnFKReferences> columnFKReferences = new ArrayList<ColumnFKReferences>();
        DatabaseMetaData dbMeta = conn.getMetaData();
	    ResultSet rst = dbMeta.getExportedKeys(null, null, name);
	    while(rst.next()){
	    	ColumnFKReferences colFkRef = new ColumnFKReferences();
	    	colFkRef.setTableName(rst.getString("PKTABLE_NAME"));
	   		colFkRef.setTableColumnName(rst.getString("PKCOLUMN_NAME"));
	   		colFkRef.setForeignTableName(rst.getString("FKTABLE_NAME"));
	   		colFkRef.setForeignTableColumn(rst.getString("FKCOLUMN_NAME"));
	   		columnFKReferences.add(colFkRef);
	    }		
	    return columnFKReferences;
    }
	
	public void calculateFkRefs() {
		Tables tables = MetaDataRepository.getDefault().getTables();
		for(Column column : columns.getColumnsVec()) {
			if(column.isPartOfFK()){
				for(Table table : tables.getTablesList()){
					for(ColumnFKReferences ref : table.columnFKReferences) {
						if(ref.getForeignTableColumn().equals(column.getName()) && ref.getForeignTableName().equals(name)) {
							column.setFkColumnName(ref.getTableColumnName());
							column.setFkTableName(ref.getTableName());
						}
					}
				}
			}
		}
	}

    public Table(){
        super();
    }
    
    /**
     * @return name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param aName The name to set.
     */
    public void setName(String aName) {
        name = aName;
    }
    /**
     * @return columns.
     */
    public Columns getColumns() {
        return columns;
    }
    /**
     * @param aColumns The columns to set.
     */
    public void setColumns(Columns aColumns) {
        columns = aColumns;
    }
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }

	public ArrayList<ColumnFKReferences> getColumnFKReferences() {
		return columnFKReferences;
	}

	public void setColumnFKReferences(ArrayList<ColumnFKReferences> columnFKReferences) {
		this.columnFKReferences = columnFKReferences;
	}
}
