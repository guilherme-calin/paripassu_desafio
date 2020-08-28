package calin.guilherme.paripassu.web.service;

import calin.guilherme.paripassu.exception.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class JsonResponseEntityFactory {
    public static ResponseEntity<String> createSuccessResponse(Map<String, Object> properties){
        LinkedHashMap<String, Object> mapper = new LinkedHashMap<String, Object>();
        ObjectMapper jackson = new ObjectMapper();
        String jsonResponse = "";
        ResponseEntity<String> successResponse;

        mapper.put("success", true);
        mapper.put("result", properties);

        try {
            jsonResponse = jackson.writeValueAsString(mapper);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            jsonResponse = "{\"success\" : true}";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json; charset=UTF-8");
        //headers.add("Access-Control-Allow-Origin", "*");
        successResponse = new ResponseEntity<String>(jsonResponse, headers, HttpStatus.valueOf(200));

        return successResponse;
    }

    public static ResponseEntity<String> createErrorResponse(HttpException error){
        LinkedHashMap<String, Object> mapper = new LinkedHashMap<String, Object>();
        ObjectMapper jackson = new ObjectMapper();
        String jsonResponse = "";
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<String> errorResponse;

        mapper.put("sucess", false);

        if(error instanceof RequestHeaderException){
            mapper.put("errorOverview", "Um ou mais erros ocorreram em relação ao cabeçalho da requisição!");
        }else if(error instanceof RequestBodyException){
            mapper.put("errorOverview", "Um ou mais erros ocorreram em relação ao processamento do corpo da requisição!");
        }else if(error instanceof RequestInformationException){
            mapper.put("errorOverview", "Um ou mais erros ocorreram em relação às informações enviadas!");
        }else if(error instanceof DatabaseOperationException){
            mapper.put("errorOverview", "Um ou mais erros ocorreram ao realizar uma operação no banco de dados!");
        }else if(error instanceof LongPollingException){
            mapper.put("errorOverview", "Um ou mais erros ocorreram ao realizar long polling!");
            headers.add("Retry-After", "3");
        }else if(error instanceof ResourceNotFoundException){
            mapper.put("errorOverview", "Um ou mais erros ocorreram em relação à requisição enviada pelo cliente!");
        }else{
            mapper.put("errorOverview", "Um ou mais erros ocorreram!");
        }

        mapper.put("errorMessage", error.getMessage());

        try {
            jsonResponse = jackson.writeValueAsString(mapper);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            jsonResponse = "{\"success\" : false}";
        }

        headers.add("Content-Type", "application/json; charset=UTF-8");
       // headers.add("Access-Control-Allow-Origin", "*");

        errorResponse = new ResponseEntity<String>(jsonResponse, headers, HttpStatus.valueOf(error.getStatusCode()));

        return errorResponse;
    }
}
