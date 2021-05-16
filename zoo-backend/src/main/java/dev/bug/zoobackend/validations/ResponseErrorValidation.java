package dev.bug.zoobackend.validations;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;

@Service
public class ResponseErrorValidation {

    public ResponseEntity<Object> mapValidationService(BindingResult result) {
        if (result.hasErrors()) {
            var errorMap = new HashMap<String, String>();
            var errors = result.getAllErrors();
            if (!CollectionUtils.isEmpty(errors)) {
                for (ObjectError error : errors) {
                    errorMap.put(error.getCode(), error.getDefaultMessage());
                }
            }

            for (FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMap);
        }
        return null;
    }
}
