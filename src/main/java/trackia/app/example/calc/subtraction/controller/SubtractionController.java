package trackia.app.example.calc.subtraction.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import trackia.app.Trackia;
import trackia.app.example.calc.subtraction.service.SubtractionService;
import trackia.app.example.calc.subtraction.to.CalcRequest;
import trackia.app.example.calc.subtraction.to.CalcResponse;
import trackia.app.module.sla.Slable;
import trackia.app.module.sysinfo.Infoable;

@RestController
@AllArgsConstructor
@Log4j2
public class SubtractionController {
	
	private final SubtractionService service;
	
	@Trackia(
      description = "Calc Subtraction TrackIA example",
  		  	write = true
	)
	@Slable(3000)
	@Infoable
	
	@PostMapping("subtraction")
	public ResponseEntity<CalcResponse> subtraction(@Validated @RequestBody CalcRequest request) {
		log.info("subtraction start");
		return new ResponseEntity<>(service.calc(request), HttpStatus.OK);
	}
}
