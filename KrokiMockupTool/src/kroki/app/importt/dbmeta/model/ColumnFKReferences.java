package kroki.app.importt.dbmeta.model;

public class ColumnFKReferences {

	private String tableName;
	private String tableColumnName;
	
	private String foreignTableName;
	private String foreignTableColumn;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableColumnName() {
		return tableColumnName;
	}
	public void setTableColumnName(String tableColumnName) {
		this.tableColumnName = tableColumnName;
	}
	
	public String getForeignTableName() {
		return foreignTableName;
	}
	public void setForeignTableName(String foreignTableName) {
		this.foreignTableName = foreignTableName;
	}
	
	public String getForeignTableColumn() {
		return foreignTableColumn;
	}
	public void setForeignTableColumn(String foreignTableColumn) {
		this.foreignTableColumn = foreignTableColumn;
	}
	

}
