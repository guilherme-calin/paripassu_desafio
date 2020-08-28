package calin.guilherme.paripassu.web.service;

import calin.guilherme.paripassu.exception.RequestInformationException;
import calin.guilherme.paripassu.exception.RequestBodyException;
import calin.guilherme.paripassu.exception.RequestHeaderException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Validator {
    public static HashMap<String,Object> validRequestAndGetBodyMap(HttpServletRequest request, HashMap<String, List<Object>> propertiesToValidate)
            throws RequestHeaderException,
            RequestBodyException,
            RequestInformationException {
        try{
            Validator.validRequestHeader(request);
        }catch(RequestHeaderException rhe){
            throw rhe;
        }

        HashMap<String,Object> bodyMap;

        try{
            bodyMap = getJsonRequestBodyMap(request);
        }catch(RequestBodyException rbe){
            throw rbe;
        }

        //Valida se as propriedades necessárias foram passadas no corpo da requisição
        byte necessaryPropertiesAmount = 0;
        String propertyName;
        Object propertyValue;

        //Passa pelas propriedades enviadas na requisição e verifica
        //se ela possui as propriedades necessárias
        for(Map.Entry<String, Object> property : bodyMap.entrySet()){
            propertyName = property.getKey();

            if(propertiesToValidate.containsKey(propertyName)){
                //Mesmo que a propriedade necessárioa tenha sido passada, é necessário verificar
                //se ela está dentro da lista de valores possíveis, caso tenha sido informada
                if(!(propertiesToValidate.get(propertyName) instanceof List)){
                    necessaryPropertiesAmount++;
                }else{
                    propertyValue = property.getValue();

                    if(propertiesToValidate.get(propertyName).contains(propertyValue)){
                        necessaryPropertiesAmount++;
                    }
                }
            }
        }

        if(necessaryPropertiesAmount != propertiesToValidate.size()){
            String necessaryProperties = "";

            for(Map.Entry<String, List<Object>> entry : propertiesToValidate.entrySet()){
                necessaryProperties += entry.getKey();
            }

            throw new RequestInformationException("Uma ou mais propriedades não foram informadas ou seus valores estão incorretos : "
                    + necessaryProperties);
        }

        return bodyMap;
    }

    private static boolean validRequestHeader(HttpServletRequest request)
    throws RequestHeaderException {
        HashMap<String, String> headers = Validator.getHeaders(request);

         if(!headers.containsKey("content-type")){
            throw new RequestHeaderException("Cabeçalho content-type não informado na requisição!", 400);
        }else{
            boolean containsAppJson = false;

            String contentHeader[] = headers.get("content-type").split(";");

            for(String contentProperties : contentHeader){
                if(contentProperties.trim().equals("application/json")){
                    containsAppJson = true;
                }
            }

            if(!containsAppJson){
                throw new RequestHeaderException("Tipo de conteúdo inválido ou não suportado! É necessário informar application/json", 415);
            }
            return containsAppJson;
        }
    }

    private static HashMap<String, Object> getJsonRequestBodyMap(HttpServletRequest request)
    throws RequestBodyException {
        BufferedReader bodyBR;
        try{
            bodyBR = request.getReader();
            String body = bodyBR.lines().collect(Collectors.joining());

            bodyBR.close();

            ObjectMapper jackson = new ObjectMapper();

            try{
                HashMap<String, Object> bodyMap = jackson.readValue(body, HashMap.class);
                return bodyMap;
            }catch(JsonProcessingException jpe){
                throw new RequestBodyException("JSON enviado é inválido! Verifique o corpo da requisição enviado!", 400);
            }
        }catch(IOException io){
            throw new RequestBodyException("Ocorreu o seguinte erro ao ler o corpo da requisição: " + io.getMessage(), 500);
        }
    }

     private static HashMap<String, String> getHeaders(HttpServletRequest request){
        Enumeration<String> headerAsEnum = request.getHeaderNames();
        HashMap<String, String> headers;

        if(headerAsEnum != null){
            List<String> headerNames = Collections.list(headerAsEnum);
            headers = new HashMap<String, String>();

            for(String name : headerNames){
                headers.put(name, request.getHeader(name));
            }

            return headers;
        }else{
            return null;
        }
    }
}
