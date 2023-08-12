package trackia.app.example.calc.subtraction.service;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import trackia.app.Trackia;
import trackia.app.annotations.R;
import trackia.app.example.calc.subtraction.dao.SubtractionDao;
import trackia.app.example.calc.subtraction.to.CalcRequest;
import trackia.app.example.calc.subtraction.to.CalcResponse;
import trackia.app.to.Journal;

@Service
@AllArgsConstructor
@Log4j2
public class SubtractionService {
	private final SubtractionDao dao;
	
	@Trackia(value = "SUBTRACTION_SERVICE", description = "Subtraction logic")
	public CalcResponse calc(@R CalcRequest request, Journal journal){
		log.info("calc start");
		
		return dao.calc(request.getLeft(), request.getRight(), journal);
	}
}
