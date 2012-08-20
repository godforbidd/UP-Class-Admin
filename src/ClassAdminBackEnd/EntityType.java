package ClassAdminBackEnd;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class EntityType {
	private String name;
	private LinkedList<Format> formatting;
	private LinkedList<BorderCase> borderCasing;
	private LinkedList<SuperEntity> entityList;
	private EntityType parentEntitytype;
	private LinkedList<EntityType> subEntityType;
	private Boolean isTextField;
	private Date date;	 
	private Double defaultWeight;
	private int ID;
	public EntityType getParentEntitytype() {
		return parentEntitytype;
	}

	public void setParentEntitytype(EntityType parentEntitytype) {
		this.parentEntitytype = parentEntitytype;
	}

	public LinkedList<EntityType> getSubEntityType() {
		if(this.subEntityType == null)
			this.subEntityType = new LinkedList<EntityType>();
		return subEntityType;
	}
	
	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	public EntityType(String n){
		name = n;		
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public LinkedList<Format> getFormatting() {
		if(formatting == null)
			formatting = new LinkedList<Format>();
		return formatting;
	}



	public LinkedList<BorderCase> getBorderCasing() {
		if(borderCasing == null)
			borderCasing = new LinkedList<BorderCase>();
		return borderCasing;
	}


	public LinkedList<SuperEntity> getEntityList() {
		if (entityList == null)
			entityList = new LinkedList<SuperEntity>();
		return entityList;
	}


	public Boolean getIsTextField() {
		return isTextField;
	}

	public void setIsTextField(Boolean isTextField) {
		this.isTextField = isTextField;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double getDefaultWeight() {
		return defaultWeight;
	}

	public void setDefaultWeight(Double defaultWeight) {
		this.defaultWeight = defaultWeight;
	}
	
	public void saveToDB(SqlJetDb db, int parentID, PDatIDGenerator idgen) throws SqlJetException{
		db.beginTransaction(SqlJetTransactionMode.WRITE);
        try {
        	//TODO
        	ISqlJetTable table = db.getTable(PDatExport.ENTITY_TYPE_TABLE);
        	//insert statements
        	this.ID = idgen.getID();
        	table.insert(this.ID+", "+this.name+", "+this.isTextField+", "+this.date+", "+this.defaultWeight);
        } finally {
            db.commit();
            
        }
        for(int x = 0;x<this.getBorderCasing().size();++x){
        	this.getBorderCasing().get(x).saveToDB(db, this.ID, idgen);
        }
        for(int x = 0;x<this.getFormatting().size();++x){
        	this.getFormatting().get(x).saveToDB(db, this.ID, idgen);
        }
	}

}
