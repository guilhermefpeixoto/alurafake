package br.com.alura.AluraFake.infra;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.alura.AluraFake.exceptions.ContinuousSequenceException;
import br.com.alura.AluraFake.exceptions.CourseMissingTaskTypeException;
import br.com.alura.AluraFake.exceptions.CourseNotBuildingException;
import br.com.alura.AluraFake.exceptions.CourseNotFoundException;
import br.com.alura.AluraFake.exceptions.DuplicateOptionException;
import br.com.alura.AluraFake.exceptions.DuplicateStatementException;
import br.com.alura.AluraFake.exceptions.EmailAlreadyRegisteredException;
import br.com.alura.AluraFake.exceptions.UserNotFoundException;
import br.com.alura.AluraFake.exceptions.UserNotInstructorException;
import br.com.alura.AluraFake.exceptions.WrongNumberOfCorrectOptionsException;
import br.com.alura.AluraFake.exceptions.WrongNumberOfWrongOptionsException;

import java.util.List;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestErrorMessage> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
                .toList();
        RestErrorMessage errorMessage = new RestErrorMessage("400", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    private ResponseEntity<RestErrorMessage> emailAlreadyRegisteredHandler(EmailAlreadyRegisteredException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("409", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RestErrorMessage> userNotFoundExceptionHandler(UserNotFoundException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("404", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(UserNotInstructorException.class)
    public ResponseEntity<RestErrorMessage> userNotInstructorExceptionHandler(UserNotInstructorException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("403", exception.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<RestErrorMessage> courseNotFoundExceptionHandler(CourseNotFoundException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("404", exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(CourseNotBuildingException.class)
    public ResponseEntity<RestErrorMessage> courseNotBuildingExceptionHandler(CourseNotBuildingException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("400", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(DuplicateStatementException.class)
    public ResponseEntity<RestErrorMessage> duplicateStatementExceptionHandler(DuplicateStatementException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("409", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(ContinuousSequenceException.class)
    public ResponseEntity<RestErrorMessage> continuousSequenceExceptionHandler(ContinuousSequenceException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("400", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(DuplicateOptionException.class)
    public ResponseEntity<RestErrorMessage> duplicateOptionExceptionHandler(DuplicateOptionException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("409", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(WrongNumberOfCorrectOptionsException.class)
    public ResponseEntity<RestErrorMessage> wrongNumberOfCorrectOptionsExceptionHandler(
            WrongNumberOfCorrectOptionsException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("409", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(WrongNumberOfWrongOptionsException.class)
    public ResponseEntity<RestErrorMessage> wrongNumberOfWrongOptionsExceptionHandler(
            WrongNumberOfWrongOptionsException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("409", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(CourseMissingTaskTypeException.class)
    public ResponseEntity<RestErrorMessage> courseMissingTaskTypeExceptionHandler(
            CourseMissingTaskTypeException exception) {
        RestErrorMessage errorMessage = new RestErrorMessage("409", exception.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }
}