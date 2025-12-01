package com.berksozcu.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorMessage {

    private MessageType messageType;


    public String prepareError() {
        return "Hata Kodu: "+ messageType.getCode() + " " + messageType.getMessage() ;
    }
}
