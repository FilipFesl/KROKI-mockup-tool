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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 
 * @version <code>
 * <li type="square"><b>1.0</b></li> 
 * <li type="square"><b>Date:</b> Jan 4, 2005</li>
 * <li type="square"><b>Time:</b> 5:36:03 PM</li>
 * </code>
 * @author Igor Dejanovic, FTN, 
 * <a href="mailto:igord@uns.ns.ac.yu">igord@uns.ns.ac.yu</a>
 */
public class Tables {

    Hashtable<String, Table> tables;
    Vector<Table> tablesList;
    boolean dirty = false;
    
    /**
     * 
     */
    public Tables() {
        super();
        tables = new Hashtable<String, Table>();
        tablesList = null;
    }
    /**
     * @param aDriverClass
     * @param aUrl
     * @param aUsername
     * @param aPassword
     * @throws SQLException
     */
    public void readFromDB(Connection aConn) throws SQLException {
        tables = new Hashtable<String, Table>();
        tablesList = new Vector<Table>(10, 10);
        DatabaseMetaData dbMeta = aConn.getMetaData();
        String[] tableKind = {"TABLE"};
        ResultSet rst = dbMeta.getTables(null, null, null, tableKind);
	    while(rst.next()){
	    	putTable(new Table(aConn, rst.getString("TABLE_NAME")));
    	}
	    
	    for(Table table : tablesList){
	    	table.calculateFkRefs();
	    }
	    setDirty(true);
    }
    
    public Table getTable(String aName){
        return (Table)tables.get(aName);
    }
    
    public void putTable(Table aTable){
        tables.put(aTable.getName(), aTable);
        tablesList.add(aTable);
    }

    public int size(){
        return tables.size();
    }
    
    /**
     * @return tables.
     */
/*    public Hashtable getTables() {
        return tables;
    }
    *//**
     * @param aTables The tables to set.
     *//*
    public void setTables(Hashtable aTables) {
        tables = aTables;
        tablesList.addAll((Collection) aTables);
    }*/

    /**
     * @param aArg0
     */
    public Table getTableAt(int aArg0) {
        return (Table) tablesList.elementAt(aArg0);
        
    }
    public Vector<Table> getTablesList() {
        return tablesList;
    }
    public void setTablesList(Vector<Table> aTablesList) {
        tablesList = aTablesList;
        tables = new Hashtable<String, Table>();
        Enumeration iter = tablesList.elements();
        while(iter.hasMoreElements()){
            Table tbl = (Table)iter.nextElement();
            tables.put(tbl.getName(), tbl);
        }
    }
    public boolean isDirty() {
        return dirty;
    }
    public void setDirty(boolean aDirty) {
        dirty = aDirty;
    }
}
