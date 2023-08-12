package trackia.app.example.calc.subtraction.exception;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.log4j.Log4j2;
import trackia.app.exception.BussinesException;
import trackia.app.util.TrackiaTransactionTrace;

@ControllerAdvice
@Log4j2
public class BussinesErrorHandler extends ResponseEntityExceptionHandler{
	private static final String MESSAGE = "message";
	private static final String ERROR = "errorCode";
	private static final String JOURNALID = "journalId";
	private static final String ERRORLIST = "errorList";

    @ExceptionHandler({ BussinesException.class })
    public ResponseEntity<Object> handleBussinesException(BussinesException ex, WebRequest request) {
    	
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(MESSAGE, ex.getMessage());
        body.put(ERROR, ex.getCodeError());
        if(ex.getErrorList() != null) {
        	body.put(ERRORLIST, ex.getErrorList());
        }
        body.put(JOURNALID, TrackiaTransactionTrace.getTransactionId());
        
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(body, new HttpHeaders(), ex.getHttpStatus());
    }
    
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
    	
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(MESSAGE, ex.getMessage());
        body.put(ERROR, "0000");
        body.put(JOURNALID, TrackiaTransactionTrace.getTransactionId());
        
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        List<String> errs = new ArrayList<>();
        
        for(int i=0; i<ex.getAllErrors().size(); i++) {
        	errs.add(ex.getAllErrors().get(i).getDefaultMessage());
        }
        
        body.put(MESSAGE, "Valid Error");
        body.put(ERROR, "0011");
        body.put(ERRORLIST, errs);
        body.put(JOURNALID, TrackiaTransactionTrace.getTransactionId());
        
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        
    }    

}
