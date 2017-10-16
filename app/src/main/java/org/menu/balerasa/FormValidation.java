package org.menu.balerasa;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

/**
 * Created by Andrya on 3/12/2016.
 */
public abstract class  FormValidation implements Validator.ValidationListener {
    public abstract void setValidator();

    @Override
    public abstract void onValidationSucceeded();

    @Override
    public abstract void onValidationFailed(List<ValidationError> errors);
}
