package ClassAdminBackEnd;

import java.awt.Color;

public class GreaterThanFormat extends Format {

	public GreaterThanFormat(int condition, int priority, Double value1, Color textColor, Color highlightColor,
			String description) {
		super( priority, value1, textColor, highlightColor,
				description);
		// TODO Auto-generated constructor stub
	}
	public Boolean evaluate(double mark){
		return this.getValue1() <= mark;
	}

}
