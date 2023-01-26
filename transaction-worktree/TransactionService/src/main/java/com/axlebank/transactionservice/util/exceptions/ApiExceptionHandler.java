package com.axlebank.transactionservice.util.exceptions;

import com.axlebank.transactionservice.util.exceptions.accountexceptions.*;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientIdLengthException;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotActiveException;
import com.axlebank.transactionservice.util.exceptions.clientexceptions.ClientNotPresentException;
import com.axlebank.transactionservice.util.exceptions.institutionexceptions.InstitutionNotPresentException;
import com.axlebank.transactionservice.util.exceptions.transactionexceptions.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.Buffer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = ClientNotPresentException.class)
    public ResponseEntity<Object> handleApiRequestException(ClientNotPresentException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = TransactionNotPresentException.class)
    public ResponseEntity<Object> handleApiRequestException(TransactionNotPresentException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = AccountNotPresentException.class)
    public ResponseEntity<Object> handleApiRequestException(AccountNotPresentException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DateTimeParseException.class)
    public ResponseEntity<Object> handleApiRequestException(DateTimeParseException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidFormatException.class)
    public ResponseEntity<Object> handleApiRequestException(InvalidFormatException e){
        String message = e.getMessage();
        StringBuffer sb = new StringBuffer(message.substring(message.indexOf("`"), message.lastIndexOf("`")));
        var result = sb.toString();
        String fieldName = result.substring(result.lastIndexOf("."));
        sb.delete(0, sb.length());

        String expectedValues = message
                .substring(message.indexOf("["), message.indexOf("]")+1);

        var apiException = new ApiException(
                fieldName + " must be one of the following: " + expectedValues,
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ClientIdLengthException.class)
    public ResponseEntity<Object> handleApiRequestException(ClientIdLengthException e){
        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = TransactionTypeNotPresent.class)
    public ResponseEntity<Object> handleApiRequestException(TransactionTypeNotPresent e){
        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(value = FundsException.class)
    public ResponseEntity<Object> handleApiRequestException(FundsException e){
        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ClientNotActiveException.class)
    public ResponseEntity<Object> handleApiRequestException(ClientNotActiveException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(value = AccountNotBelongsToClientException.class)
    public ResponseEntity<Object> handleApiRequestException(AccountNotBelongsToClientException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AccountNotActiveException.class)
    public ResponseEntity<Object> handleApiRequestException(AccountNotActiveException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AccountTypeException.class)
    public ResponseEntity<Object> handleApiRequestException(AccountTypeException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = TransactionFailedException.class)
    public ResponseEntity<Object> handleApiRequestException(TransactionFailedException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InstitutionNotPresentException.class)
    public ResponseEntity<Object> handleApiRequestException(InstitutionNotPresentException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = AmountRequestedException.class)
    public ResponseEntity<Object> handleApiRequestException(AmountRequestedException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = DailyWithdrawalLimitException.class)
    public ResponseEntity<Object> handleApiRequestException(DailyWithdrawalLimitException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = DailyPurchaseLimitException.class)
    public ResponseEntity<Object> handleApiRequestException(DailyPurchaseLimitException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = WithdrawalTransactionMediumException.class)
    public ResponseEntity<Object> handleApiRequestException(WithdrawalTransactionMediumException e){

        var apiException = new ApiException(
                e.getMessage(),
                HttpStatus.CONFLICT,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT);
    }

}
