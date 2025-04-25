package org.hopto.fjavierjp.petregistry.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetails
{
    private Date date;
    private String message;
    private String details;
    private String stackTrace;
}
