package trackia.app.example.calc.subtraction.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonSyntaxException;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import trackia.app.Trackia;
import trackia.app.example.calc.subtraction.config.AppConfiguration;
import trackia.app.example.calc.subtraction.to.CalcRequest;
import trackia.app.example.calc.subtraction.to.CalcResponse;
import trackia.app.exception.BussinesException;
import trackia.app.util.RestTemplateJournal;
import trackia.app.util.Util;

@Repository
@Log4j2
@AllArgsConstructor
public class CalcDao {
	final RestTemplateJournal restTemplate;
	final AppConfiguration appConfiguration;

	private static final String OPERATOR_ADDITION = "+";
	private static final String OPERATOR_SUBTRACTION = "-";
	private static final String OPERATOR_MULTIPLICATION = "*";
	private static final String OPERATOR_DIVISION = "/";

	@Trackia(value = "DAO_VALUE_CALC", description = "Calc Value ")
	public Integer value(Object exp, String expPart) {
		log.info("value start");
		
		String strVal = exp.toString();
		if(exp.toString().startsWith("{")){
			return parseRemote(exp);
		}else {
			return parseInt(strVal);
		}
	}
	
	private Integer parseRemote(Object exp) {
		String operation = "";
		try {
			final CalcRequest calcExp =  Util.toObject(Util.toJson(exp), CalcRequest.class);
			operation = "[" + calcExp.getOperation() + "]";
	    	
			CalcResponse respose = restTemplate.postForObject(getUrl(calcExp), exp, CalcResponse.class);
			return respose == null ?null :respose.getResult();
        
		}catch(ResourceAccessException e) {
			throw new BussinesException(HttpStatus.NOT_FOUND, "Servicio indisponible momentaneamente en operacion "+ operation, "0006", e);			
		}catch(HttpClientErrorException | HttpServerErrorException e) {
			throw parseRemoteException(e);
		}catch(JsonSyntaxException e) {
			throw new BussinesException(HttpStatus.BAD_REQUEST, "La expresion contiene un error de sintaxis[" +exp+ "]", "0005", e);
		}catch(BussinesException e) {
			throw e;
		}catch(Exception e) {
			throw new BussinesException(HttpStatus.NOT_FOUND, "Servicio no encontrado", "0004", e);
		}
	}
	
	private Integer parseInt(String val) {
		try {
			return Integer.parseInt(val);
		}catch(BussinesException e) {
			throw e;
		}catch(Exception e) {
			throw new BussinesException(HttpStatus.BAD_REQUEST, "Valor [" +val+ "] invalido", "0003");
		}
	}
	
	private RuntimeException parseRemoteException(HttpStatusCodeException e) {
		try {
			final ObjectMapper objectMapper = new ObjectMapper();
			final JsonNode root = objectMapper.readTree(e.getResponseBodyAsString());
			final JsonNode arrNode = root.get("errorList");
			
			List<String> listErr = new ArrayList<>();
			
			if (arrNode.isArray()) {
			    for (final JsonNode objNode : arrNode) {
			    	listErr.add(objNode.textValue());
			    }
			}
			
			throw new BussinesException(e.getStatusCode(), root.at("/message").textValue(), root.at("/errorCode").textValue(), listErr, e);
			
		} catch (JsonProcessingException e1) {
			throw e;
		}
	}
	
	private String getUrl(CalcRequest calcExp) {
		if(OPERATOR_ADDITION.equals(calcExp.getOperation())) {
			return appConfiguration.getAddition();
		}else if(OPERATOR_SUBTRACTION.equals(calcExp.getOperation())) {
			return appConfiguration.getSubtraction();
		}else if(OPERATOR_MULTIPLICATION.equals(calcExp.getOperation())) {
			return appConfiguration.getMultiplication();
		}else if(OPERATOR_DIVISION.equals(calcExp.getOperation())) {
			return appConfiguration.getDivision();
		
		}else if(calcExp.getOperation() == null) {
			throw new BussinesException(HttpStatus.BAD_REQUEST, "Debe indicar una operacion", "0002");
		}
		
		throw new BussinesException(HttpStatus.BAD_REQUEST, "Operador [" +calcExp.getOperation()+ "] no valido", "0001");
	}
}
