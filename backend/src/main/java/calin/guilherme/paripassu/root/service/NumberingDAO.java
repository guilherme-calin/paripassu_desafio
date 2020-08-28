package calin.guilherme.paripassu.root.service;

import calin.guilherme.paripassu.root.model.Numbering;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NumberingDAO {
    private JdbcTemplate jdbc;
    private SimpleJdbcInsert simpleInsert;
    private SimpleJdbcCall simpleCall;

    public NumberingDAO(JdbcTemplate template, SimpleJdbcInsert simpleInsert, SimpleJdbcCall simpleCall)
    throws DataAccessException {
        this.jdbc = template;
        this.simpleInsert = simpleInsert;
        this.simpleCall = simpleCall;
    }

    public Map<String, Object> generateNumbering(String numberingType){
        String sql = String.format("SELECT *\n" +
                "FROM insertQueueNumbering('%s')", numberingType);
        //insertQueueNumbering é uma função PL/pgSQL para inserir um novo registro de numeração baseado no tipo da fila

        Map<String, Object> result = this.jdbc.queryForMap(sql);
        return result;
    }

    public List<Map<String, Object>> getLastServed(int number){
        String sql = String.format("SELECT *\n" +
                "FROM QUEUE_NUMBERING\n" +
                "WHERE SERVED = TRUE\n" +
                "ORDER BY DATETIME_SERVED DESC\n" +
                "LIMIT(%d)", number);

        List<Map<String, Object>> result = this.jdbc.queryForList(sql);
        return result;
    }

    public List<Map<String, Object>> getPendingToServe(){
        String sql = "SELECT *\n" +
                "FROM QUEUE_NUMBERING\n" +
                "WHERE SERVED = false\n" +
                "ORDER BY NUMBERING_TYPE DESC, NUMBERING_ID";

        List<Map<String, Object>> result = this.jdbc.queryForList(sql);
        return result;
    }

    public Map<String, Object> getById(Numbering item){
        String sql = String.format("SELECT *\n" +
                "FROM QUEUE_NUMBERING\n" +
                "WHERE NUMBERING_ID = %d", item.getId());

        Map<String, Object> result = this.jdbc.queryForMap(sql);
        return result;
    }

    public boolean serveNext(Numbering item){
        String sql = String.format("UPDATE QUEUE_NUMBERING\n" +
                "SET SERVED=TRUE, DATETIME_SERVED=NOW()\n" +
                "WHERE NUMBERING_ID=%d", item.getId());

        int rowsAffected = this.jdbc.update(sql);

        if(rowsAffected > 0){
            Timestamp timestamp = (Timestamp) this.getById(item).get("datetime_served");
            item.setDateTimeServed(timestamp);

            return true;
        }else{
            return false;
        }
    }

    public Boolean resetNumbering(){
        String sql = "SELECT * FROM resetQueueNumbering()";
        //resetQueueNumbering é uma função PL/pgSQL para reset das numerações

        Boolean result = this.jdbc.queryForObject(sql, Boolean.class);

        return result != null ? result : false;
    }
}
