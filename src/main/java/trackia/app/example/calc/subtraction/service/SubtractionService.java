package trackia.app.example.calc.subtraction.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import trackia.app.Trackia;
import trackia.app.example.calc.subtraction.dao.SubtractionDao;
import trackia.app.example.calc.subtraction.to.CalcRequest;
import trackia.app.example.calc.subtraction.to.CalcResponse;

@Service
@AllArgsConstructor
@Log4j2
public class SubtractionService {
	private final SubtractionDao dao;
	
	@Trackia(value = "SUBTRACTION_SERVICE", description = "Subtraction logic")
	public CalcResponse calc(CalcRequest request){
		log.info("calc start");
		
		return dao.calc(request.getLeft(), request.getRight());
	}
}
