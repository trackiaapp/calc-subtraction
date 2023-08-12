package trackia.app.example.calc.subtraction.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CalcResponse {
	private Integer result;
	private String journalId;
}
