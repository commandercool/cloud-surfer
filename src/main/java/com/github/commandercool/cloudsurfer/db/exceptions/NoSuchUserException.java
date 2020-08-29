package com.github.commandercool.cloudsurfer.db.exceptions;

public class NoSuchUserException extends IllegalArgumentException {

    public NoSuchUserException(String s) {
        super(s);
    }
}
