package com.fedormamaevv.SpringProjectTemp;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class BadRequestException extends ResponseStatusException {
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST, "400 Bad Request");
    }
}

class ForbiddenException extends ResponseStatusException {
    public ForbiddenException() {
        super(HttpStatus.FORBIDDEN, "403 Forbidden");
    }
}

class NotFoundException extends ResponseStatusException {
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, "404 Not found");
    }
}

class ConflictException extends ResponseStatusException {
    public ConflictException() {
        super(HttpStatus.BAD_REQUEST, "409 Conflict");
    }
}