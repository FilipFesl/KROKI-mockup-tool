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

/**
 * 
 * @version <code>
 * <li type="square"><b>1.0</b></li> 
 * <li type="square"><b>Date:</b> Jan 4, 2005</li>
 * <li type="square"><b>Time:</b> 5:36:21 PM</li>
 * </code>
 * @author Igor Dejanovic, FTN, 
 * <a href="mailto:igord@uns.ns.ac.yu">igord@uns.ns.ac.yu</a>
 */
public class Column {
    private String name;
    private boolean partOfPK = false;
    private boolean partOfFK = false;
    private String type;
    private int sqlDataType; //corresponds with java.sql.Types (jdbc types)
    private int length = 0;
    private int decimalDigits = 0;
    private boolean nullable = false;
    
    private String fkColumnName;
    private String fkTableName;
    
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
     * @return partOfFK.
     */
    public boolean isPartOfFK() {
        return partOfFK;
    }
    /**
     * @param aPartOfFK The partOfFK to set.
     */
    public void setPartOfFK(boolean aPartOfFK) {
        partOfFK = aPartOfFK;
    }
    /**
     * @return partOfPK.
     */
    public boolean isPartOfPK() {
        return partOfPK;
    }
    /**
     * @param aPartOfPK The partOfPK to set.
     */
    public void setPartOfPK(boolean aPartOfPK) {
        partOfPK = aPartOfPK;
    }

    /**
     * @return type.
     */
    public String getType() {
        return type;
    }
    /**
     * @param aType The type to set.
     */
    public void setType(String aType) {
        type = aType;
    }
    /**
     * @return
     */
    public int getLength() {
        return length;
    }
    /**
     * @param aLength
     */
    public void setLength(int aLength) {
        length = aLength;
    }
    /**
     * @return
     */
    public boolean isNullable() {
        return nullable;
    }
    /**
     * @param aNullable
     */
    public void setNullable(boolean aNullable) {
        nullable = aNullable;
    }
    /**
     * @return
     */
    public int getDecimalDigits() {
        return decimalDigits;
    }
    /**
     * @param aDecimalDigits
     */
    public void setDecimalDigits(int aDecimalDigits) {
        decimalDigits = aDecimalDigits;
    }
    /**
     * @return
     */
	public int getSQLDataType() {
		return sqlDataType;
	}
    /**
     * @param aDecimalDigits
     */
	public void setSQLDataType(int sqlDataType) {
		this.sqlDataType = sqlDataType;
	}

	public String getFkColumnName() {
		return fkColumnName;
	}
	
	public void setFkColumnName(String fkColumnName) {
		this.fkColumnName = fkColumnName;
	}
	
	public String getFkTableName() {
		return fkTableName;
	}
	
	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}
}
