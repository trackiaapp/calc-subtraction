package trackia.app.example.calc.subtraction.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CalcRequest {
	private String operation;

	@NotNull(message = "Left value is required")
	private Object left;
	
	@NotNull(message = "Right value is required")
	private Object right;
}
