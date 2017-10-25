package com.heaven7.java.data.mediator.compiler;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.data.mediator.Fields;
import com.heaven7.java.data.mediator.compiler.util.MockTypeMirror;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;

import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.NAME_STRING;
import static com.heaven7.java.data.mediator.compiler.DataMediatorConstants.NAME_boolean;

/**
 * indicate the field data of {@linkplain com.heaven7.java.data.mediator.Field}.
 * note flags shouldn't changed . it will effect the lib data-mediator.
 * @author heaven7
 */
public class FieldData {

    public static final int COMPLEXT_ARRAY = 1;
    public static final int COMPLEXT_LIST = 2;
    public static final int COMPLEXT_SPARSE_ARRAY = 3;

    public static final int FLAG_TRANSIENT = 0x00000001;
    public static final int FLAG_VOLATILE = 0x00000002;

    public static final int FLAG_SNAP = 0x00000004;
    public static final int FLAG_SHARE = 0x00000008;
    public static final int FLAG_COPY = 0x00000010;//16
    public static final int FLAG_RESET = 0x00000020;//32

    public static final int FLAG_TO_STRING    = 0x00000040; //64
    public static final int FLAG_PARCELABLE   = 0x00000080; //128

    /**
     * @ Expose : serialize, deserialize, default is true
     */
    public static final int FLAG_EXPOSE_DEFAULT = 0x00000100;//256
    /**
     * @ Expose : serialize = false
     */
    public static final int FLAG_EXPOSE_SERIALIZE_FALSE = 0x00000200;//512
    /**
     * @ Expose : deserialize = false
     */
    public static final int FLAG_EXPOSE_DESERIALIZE_FALSE = 0x00000400;//1024

    /**
     * hashCode and equals.
     */
    public static final int FLAG_HASH_EQUALS             = 0x00000800; //2048

    /**
     * a scope flag of gson serialize/deserialize json for field.
     * Note this only effect the auto generate TypeAdapter.
     * @since 1.2.0
     */
    public static final int FLAG_GSON_PERSISTENCE         = 0x00001000;

    /** the common flags */
    public static final int FLAGS_MAIN = FLAG_COPY | FLAG_TO_STRING | FLAG_PARCELABLE | FLAG_GSON_PERSISTENCE;
    public static final int FLAGS_MAIN_2 = FLAGS_MAIN | FLAG_HASH_EQUALS;


    //========================================================================================
    /** only used in compile lib */
    public static final int FLAG_SELECTABLE = 0x08000000;

    private static final TypeCompat sTC_STRING = new StringTypeCompat();

    private String propertyName;
    private String serializeName;
    private int flags = FLAGS_MAIN_2;  //default to main flags
    private int complexType;
    private TypeCompat mTypeCompat = sTC_STRING; //default to string

    private double since = -1;
    private double until = -1;

    public boolean isSinceEnabled(){
        return since >= 1.0;
    }
    public boolean isUntilEnabled(){
        return until >= 1.0;
    }
    public double getSince() {
        return since;
    }
    public void setSince(double since) {
        this.since = since;
    }
    public double getUntil() {
        return until;
    }
    public void setUntil(double until) {
        this.until = until;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getSerializeName() {
        return serializeName;
    }

    public void setSerializeName(String serializeName) {
        this.serializeName = serializeName;
    }

    public TypeCompat getTypeCompat() {
        return mTypeCompat;
    }

    public void setTypeCompat(TypeCompat mTypeCompat) {
        this.mTypeCompat = mTypeCompat;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getComplexType() {
        return complexType;
    }

    public void setComplexType(int complexType) {
        this.complexType = complexType;
    }

    public boolean isList() {
        return complexType == COMPLEXT_LIST;
    }

    public boolean isArray() {
        return complexType == COMPLEXT_ARRAY;
    }

    public String getFieldConstantName(){
        return "PROP_" + getPropertyName();
    }
    public String getGetMethodPrefix(){
        if(complexType == 0){
            if(NAME_boolean.equals(getTypeCompat().toString())){
                return DataMediatorConstants.IS_PREFIX;
            }
        }
        return DataMediatorConstants.GET_PREFIX;
    }

    @Override
    public String toString() {
        return "FieldData{" +
                "propertyName='" + propertyName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldData fieldData = (FieldData) o;

        if (complexType != fieldData.complexType) return false;
        if (propertyName != null ? !propertyName.equals(fieldData.propertyName) : fieldData.propertyName != null)
            return false;
        return mTypeCompat != null ? mTypeCompat.equals(fieldData.mTypeCompat) : fieldData.mTypeCompat == null;
    }

    @Override
    public int hashCode() {
        int result = propertyName != null ? propertyName.hashCode() : 0;
        result = 31 * result + complexType;
        result = 31 * result + (mTypeCompat != null ? mTypeCompat.hashCode() : 0);
        return result;
    }

    public static class TypeCompat {

        private final TypeMirror tm;
        private final Types types;
        private TypeName mTypeName_impl;

        public TypeCompat(Types types, TypeMirror tm) {
            this.tm = tm;
            this.types = types;
        }
        public @Nullable TypeName getSuperClassTypeName(){
            if(mTypeName_impl != null){
                return mTypeName_impl;
            }
            return null;
        }
        public TypeName getInterfaceTypeName(){
            return TypeName.get(tm);
        }
        public TypeMirror getTypeMirror() {
            return tm;
        }
        public TypeElement getElementAsType() {
            return (TypeElement) types.asElement(tm);
        }
        public Element getElement() {
            return types.asElement(tm);
        }

        /**
         * @since 1.3.0
         */
        public boolean hasAnnotationFields(){
            return mTypeName_impl != null;
        }

        public void replaceIfNeed(Elements elements, ProcessorPrinter pp) {
            pp.note("TypeCompat", "replaceIfNeed", "start check element: " + tm.toString());
            Element te = getElement();
            //when TypeMirror is primitive , here te is null.
            if(te == null) {
                pp.note("TypeCompat", "replaceIfNeed", "Element = null");
            }else{
                boolean needReplace = false;
                //when depend another interface(@Fields) need reply.
                List<? extends AnnotationMirror> mirrors = getElementAsType().getAnnotationMirrors();
                for(AnnotationMirror am : mirrors){
                    DeclaredType type = am.getAnnotationType();
                    pp.note("TypeCompat", "replaceIfNeed", "type = " + type);
                   // pp.note("TypeCompat", "replaceIfNeed", "am = " + am);
                    if(type.toString().equals(Fields.class.getName())){
                        //need replace.
                        needReplace = true;
                        break;
                    }
                }
                if(needReplace){
                    final String str = tm.toString();
                    int lastIndexOfDot = str.lastIndexOf(".");
                    mTypeName_impl = ClassName.get(str.substring(0, lastIndexOfDot),
                            str.substring(lastIndexOfDot + 1)+  DataMediatorConstants.IMPL_SUFFIX );
                    pp.note("TypeCompat", "replaceIfNeed", "find depend , interface = " + str);
                }else{
                    pp.note("TypeCompat", "replaceIfNeed", " [can't] find depend , interface = " + tm.toString());
                     /*
                     * here have a bug . if one module depend another.(all have annotation @Fields)
                     * To resolve it . we just judge it has 'IMPL_SUFFIX' or not.
                     */
                    String  name = tm.toString();
                    String expectImplName = name + DataMediatorConstants.IMPL_SUFFIX;
                    if(elements.getTypeElement(expectImplName) != null){
                        int lastIndexOfDot = name.lastIndexOf(".");
                        mTypeName_impl = ClassName.get(name.substring(0, lastIndexOfDot),
                                name.substring(lastIndexOfDot + 1)+  DataMediatorConstants.IMPL_SUFFIX);
                    }
                }
            }
        }

        public String toString(){
            return tm.toString();
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) {
                return true;
            }
            if (obj== null || getClass() != obj.getClass())
                return false;
            TypeCompat other = (TypeCompat) obj;

            return other.toString().equals(other.toString());
        }
    }

    //mock String.
    public static class StringTypeCompat extends FieldData.TypeCompat{
        public StringTypeCompat() {
            super(null, new MockTypeMirror(NAME_STRING));
        }
        @Override
        public String toString() {
            return NAME_STRING;
        }
        @Override
        public TypeName getInterfaceTypeName() {
            return ClassName.get("java.lang", "String");
        }
    }
}
