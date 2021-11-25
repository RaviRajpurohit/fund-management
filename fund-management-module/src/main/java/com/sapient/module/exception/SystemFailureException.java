package com.sapient.module.exception;

import java.io.FileNotFoundException;

public class SystemFailureException extends Exception {
    public SystemFailureException(String s, FileNotFoundException e) {
        super(s, e);
    }
}
