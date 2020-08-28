package calin.guilherme.paripassu.web.controller;

import calin.guilherme.paripassu.exception.*;
import calin.guilherme.paripassu.root.model.Numbering;
import calin.guilherme.paripassu.root.service.NumberingDAO;

import calin.guilherme.paripassu.web.service.JsonResponseEntityFactory;
import calin.guilherme.paripassu.web.service.Validator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@Controller
public class RestApi {
    private NumberingDAO dao;
    private Environment environment;
    private Vector<Numbering> pendingQueue;
    private Vector<Numbering> lastServed;

    public RestApi(Environment environment, NumberingDAO dao, @Qualifier("pendingQueue") Vector<Numbering> pending, @Qualifier("lastServedList") Vector<Numbering> served) throws InterruptedException {
        this.environment = environment;
        this.dao = dao;
        this.pendingQueue = pending;
        this.lastServed = served;
    }

    @ResponseBody
    @PostMapping("api/user/generate")
    public CompletableFuture<ResponseEntity<String>> generateNumbering(HttpServletRequest request){
        return CompletableFuture.supplyAsync(() -> {
            HashMap<String, List<Object>> propertiesToValidate = new HashMap<String, List<Object>>();
            propertiesToValidate.put("numberingType", Arrays.asList("P", "N"));

            ResponseEntity<String> response;
            HashMap<String, Object> bodyMap;

            try{
                 bodyMap = Validator.validRequestAndGetBodyMap(request, propertiesToValidate);
            } catch (RequestInformationException|RequestBodyException|RequestHeaderException e) {
                response = JsonResponseEntityFactory.createErrorResponse(e);
                return response;
            }

            Map<String, Object> newTuple;

            try{
                newTuple = this.dao.generateNumbering(bodyMap.get("numberingType").toString());
            }catch(DataAccessException e){
                response = JsonResponseEntityFactory.createErrorResponse(new DatabaseOperationException(e.getMessage(), 500));
                return response;
            }

            Long id = (Long) newTuple.get("numbering_id");
            Integer numbering = (Integer) newTuple.get("numbering");
            String numberingType = (String) newTuple.get("numbering_type");
            Timestamp dateRequest = (Timestamp) newTuple.get("datetime_request");

            Numbering item = new Numbering(id.longValue(), numbering, numberingType, dateRequest);

            if(numberingType.equals("N")){
                this.pendingQueue.add(item);
            }else{
                int index;
                for(index = 0; index < this.pendingQueue.size(); index++){
                    while(this.pendingQueue.get(index).getNumberingType().equals("P")){
                        index++;
                    }
                    break;
                }

                this.pendingQueue.add(index, item);
            }

            LinkedHashMap<String, Object> mapper = new LinkedHashMap<String, Object>();
            mapper.put("message", "Registro inserido com sucesso!");
            mapper.put("newNumbering", item);

            response = JsonResponseEntityFactory.createSuccessResponse(mapper);

            return response;
        });
    }

    @ResponseBody
    @GetMapping("api/user/getLastServed")
    public CompletableFuture<ResponseEntity<String>> getLastServed(HttpServletRequest request){
        return CompletableFuture.supplyAsync(() -> {
            boolean dummyVar = false;
            ResponseEntity<String> response;
            LinkedHashMap<String, Object> mapper = new LinkedHashMap<>();
            String lastServedId = request.getParameter("lastServedId");

            if(lastServedId != null && this.lastServed.size() > 0){
                if(lastServedId.contains(";")){
                    lastServedId = lastServedId.replace(";", "");
                }
                boolean validId = Pattern.compile("[0-9]*").matcher(lastServedId).matches();

                if(validId){
                    int integerLastServedId = Integer.parseInt(lastServedId);
                    boolean sameLastServed = true;

                    while(sameLastServed){
                        if(this.lastServed.get(0).getId() != integerLastServedId){
                            sameLastServed = false;
                        }
                    }

                    mapper.put("message", "Consulta da lista das últimas numerações realizadas com sucesso!");
                    mapper.put("list", this.lastServed);

                    response = JsonResponseEntityFactory.createSuccessResponse(mapper);
                }else{
                    String message = "Valor inválido passado para a query parameter lastServedId! Apenas dígitos numéricos são permitidos!";
                    response = JsonResponseEntityFactory.createErrorResponse(new RequestInformationException(message, 400));
                }
            }else{
                mapper.put("message", "Consulta da lista das últimas numerações realizadas com sucesso!");
                mapper.put("list", this.lastServed);

                response = JsonResponseEntityFactory.createSuccessResponse(mapper);
            }

            return response;
        });
    }

    @ResponseBody
    @PutMapping("api/manager/serveNext")
    public CompletableFuture<ResponseEntity<String>> serveNext(HttpServletRequest request){
        return CompletableFuture.supplyAsync(() -> {
            LinkedHashMap<String, Object> mapper = new LinkedHashMap<>();
            ResponseEntity<String> response;

            if(!this.pendingQueue.isEmpty()){
                Numbering pending = this.pendingQueue.get(0);

                if(this.dao.serveNext(pending)){
                    this.pendingQueue.remove(0);

                    if(this.lastServed.size() == this.lastServed.capacity()){
                        this.lastServed.remove(this.lastServed.capacity() - 1);
                    }

                    this.lastServed.add(0, pending);

                    mapper.put("message", "Nova numeração marcada para atendimento!");
                    mapper.put("numbering", pending);

                    response = JsonResponseEntityFactory.createSuccessResponse(mapper);
                }else{
                    String message = "Não foi possivel modificar o status do atendimento!";
                    response = JsonResponseEntityFactory.createErrorResponse(new DatabaseOperationException(message, 500));
                }
            }else{
                mapper.put("message", "Não há numerações pendentes na fila para serem atendidas!");
                response = JsonResponseEntityFactory.createSuccessResponse(mapper);
            }


            mapper.put("message", "Consulta da lista das últimas numerações realizadas com sucesso!");
            mapper.put("list", this.lastServed);

            return response;
        });
    }

    @ResponseBody
    @PutMapping("api/manager/resetNumbering")
    public CompletableFuture<ResponseEntity<String>> resetNumbering(HttpServletRequest request){
        return CompletableFuture.supplyAsync(() -> {
            LinkedHashMap<String, Object> mapper = new LinkedHashMap<>();
            ResponseEntity<String> response;

            if(this.dao.resetNumbering()){
                mapper.put("message", "Reset das numerações realizadas com sucesso!");
                response = JsonResponseEntityFactory.createSuccessResponse(mapper);
            }else{
                String message = "Erro no reset das numerações!";
                response = JsonResponseEntityFactory.createErrorResponse(new DatabaseOperationException(message, 500));
            }

            return response;
        });
    }

    @ResponseBody
    @RequestMapping(value = "api/**", method = {RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.POST})
    public CompletableFuture<ResponseEntity<String>> test(HttpServletRequest request){
        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<String> response;

            String message = "Endpoint inválido!";
            response = JsonResponseEntityFactory.createErrorResponse(new ResourceNotFoundException(message, 400));

            return response;
        });
    }

    @ResponseBody
    @GetMapping("/**")
    public ResponseEntity<String> getPage() throws IOException {

        InputStream fileStream = this.getClass().getResourceAsStream("/static/index.html");
        StringBuilder contentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(fileStream, "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                contentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        String indexHtml = contentBuilder.toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "text/html; charset=UTF-8");
        return new ResponseEntity<String>(indexHtml,headers ,HttpStatus.valueOf(200));

        // return "forward:/WEB-INF/classes/static/index.html";
    }
}
