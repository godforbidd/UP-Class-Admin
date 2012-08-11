package ClassAdminBackEnd;

public class LeafMarkEntity extends MarkEntity{


	public LeafMarkEntity(EntityType type, SuperEntity parentEntity, double mark) {
		super(type, parentEntity);
		// TODO Auto-generated constructor stub
	}
	
	public LeafMarkEntity(SuperEntity replacedEntity) {
		super(replacedEntity);
		// TODO Auto-generated constructor stub
	}

	private Double doMarkMath() throws AbsentException{
		return this.getMark();
	}
	
	public SuperEntity unLeaf(){
		return new MarkEntity(this);
	}
}
