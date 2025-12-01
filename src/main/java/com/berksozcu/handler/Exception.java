package com.berksozcu.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exception<T> {
    private T message;
    private String hostName;
    private Date createTime;
    private String path;
}
