package com.heaven7.java.data.mediator.compiler;

import javax.lang.model.element.TypeElement;
import java.util.List;

/**
 * the super field delegate which is used to get field from super interface that is annotated by
 * {@literal @}{@linkplain com.heaven7.java.data.mediator.Fields}. <br>
 * Created by heaven7 on 2017/9/14 0014.
 */
public interface ISuperFieldDelegate {

    /**
     * get dependFields for target type element.
     * @param te the current type element which to generate .java file,
     * @return the all depend fields.
     */
    List<FieldData>  getDependFields(TypeElement te);

}
