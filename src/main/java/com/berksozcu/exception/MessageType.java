package com.berksozcu.exception;

import lombok.Getter;

@Getter

public enum MessageType {
    PRINTER_NOT_FOUND(1001, "Yazıcı bulunamadı"),
    PRINTER_ARIZALI(1002, "Yazıcı Arızalı"),
    PRINTER_KULLANIMDA(1003, "Yazıcı kullanımda"),
    YAZICI_MEVCUT(1006, "Yazıcı mevcut"),

    YANLIS_TIP(1004, "Yanlış tip"),
    IS_BULUNAMADI(1005, "İş bulunamadı"),
    PRINTER_HATA(9999, "Genel bir sorun oluştu");

    private Integer code;
    private String message;

     MessageType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
