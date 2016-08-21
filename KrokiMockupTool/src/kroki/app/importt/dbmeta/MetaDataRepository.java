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
package kroki.app.importt.dbmeta;

import java.sql.Connection;
import java.sql.SQLException;

import kroki.app.importt.dbmeta.model.Tables;
/**
 * 
 * @version <code>
 * <li type="square"><b>1.0</b></li> 
 * <li type="square"><b>Date:</b> Jan 4, 2005</li>
 * <li type="square"><b>Time:</b> 5:38:03 PM</li>
 * </code>
 * @author Igor Dejanovic, FTN, 
 * <a href="mailto:igord@uns.ns.ac.yu">igord@uns.ns.ac.yu</a>
 */
public class MetaDataRepository {
    
    private Tables tables;
    private static MetaDataRepository mdr = null;
    
    private MetaDataRepository(){
        super();
    }
    
    public static MetaDataRepository getDefault(){
        if(mdr == null){
            mdr = new MetaDataRepository();
        }
        return mdr;
    }
    
    
    /**
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * 
     */
    public synchronized void readFromDB(Connection conn) throws ClassNotFoundException, SQLException {
        tables = new Tables();
        tables.readFromDB(conn);
    }
    /**
     * @return tables.
     */
    public Tables getTables() {
        return tables;
    }
    /**
     * @param aTables The tables to set.
     */
    public void setTables(Tables aTables) {
        tables = aTables;
    }
}
