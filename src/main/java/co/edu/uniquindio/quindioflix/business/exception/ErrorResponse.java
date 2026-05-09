package co.edu.uniquindio.quindioflix.business.exception;

import java.time.*;
import java.util.*;

public record ErrorResponse(String code,String message,int status,String path,LocalDateTime timestamp,List<FieldDetail> details) {
    public record FieldDetail(String field,String message) {

    }

}
