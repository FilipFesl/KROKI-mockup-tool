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
 * 
 */
package kroki.app.importt.dbmeta.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 
 * @version <code>
 * <li type="square"><b>1.0</b></li> 
 * <li type="square"><b>Date:</b> Jan 4, 2005</li>
 * <li type="square"><b>Time:</b> 5:36:30 PM</li>
 * </code>
 * @author Igor Dejanovic, FTN, 
 * <a href="mailto:igord@uns.ns.ac.yu">igord@uns.ns.ac.yu</a>
 */
public class Columns {
    Hashtable<String, Column> columns;
    Vector<Column> columnsVec;
    
    public Columns(){
        super();
        columns = new Hashtable<String, Column>();
    }
    
    public Column getColumn(String aName){
        return (Column) columns.get(aName);
    }
    
    public Column getColumnAt(int aIndex){
        return (Column) columnsVec.get(aIndex);
    }
    
    public void putColumn(Column aColumn){
        if(columnsVec == null)
            columnsVec = new Vector<Column>(10, 10);
        columns.put(aColumn.getName(), aColumn);
        columnsVec.add(aColumn);
    }
    
    public Enumeration getColumnIterator(){
        return columns.elements(); 
    }

    /**
     * @return
     */
    public int size() {
        return columnsVec.size();
    }
    public Vector<Column> getColumnsVec() {
        return columnsVec;
    }
    public void setColumnsVec(Vector<Column> aColumnsVec) {
        columnsVec = aColumnsVec;
        Enumeration iter = columnsVec.elements();
        while(iter.hasMoreElements()){
            Column col = (Column) iter.nextElement();
            columns.put(col.getName(), col);
        }
        
    }
}
