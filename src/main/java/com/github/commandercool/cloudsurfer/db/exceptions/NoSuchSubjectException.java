package com.github.commandercool.cloudsurfer.db.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NoSuchSubjectException extends Exception {

    private final String name;

    @Override
    public String getMessage() {
        return "There is no subject with name " + name;
    }

}
