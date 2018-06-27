package com.heaven7.java.data.mediator.compiler;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * the @ImplClass and @ImplMethod info
 * @since 1.3.0
 */
public class ImplInfo {

    private final ArrayList<MethodInfo> mMethods = new ArrayList<>();
    private FieldData.TypeCompat implClass;

    public FieldData.TypeCompat getImplClass() {
        return implClass;
    }
    public void setImplClass(FieldData.TypeCompat implClass) {
        this.implClass = implClass;
    }
    public void addMethodInfo(MethodInfo info){
        mMethods.add(info);
    }
    public List<MethodInfo> getMethodInfo(){
        return mMethods;
    }
    public boolean isValid(){
        return !mMethods.isEmpty();
    }

    public void addImplMethods(TypeSpec.Builder curBuilder){
          for(MethodInfo info : getMethodInfo()){
              MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(info.getMethodName())
                      .addModifiers(Modifier.PUBLIC)
                      .returns(info.getReturnType());

              final List<ParamInfo> paramInfos = info.getParamInfos();
              final Object[] params = new Object[ 3 + paramInfos.size()]; //implClass + implName + module + dependProp + other params.
              params[0] = info.getImplClass().getTypeName();
              params[1] = info.getImplMethodName();
              params[2] = "this";

              StringBuilder sb = new StringBuilder();
              sb.append( "$T.$N( $N " );

              if(!paramInfos.isEmpty()){
                  int i = 3;
                  for (ParamInfo pInfo : paramInfos){
                      sb.append(", ").append("$N");
                      params[i++] = pInfo.paramName;
                      methodBuilder.addParameter(pInfo.paramType, pInfo.paramName);
                  }
              }
              sb.append(")");
              if(info.isReturnVoid()){
                  methodBuilder.addStatement(sb.toString(), params);
              }else{
                  methodBuilder.addStatement("return " + sb.toString(), params);
              }
              curBuilder.addMethod(methodBuilder.build());
          }
    }

    /** comes from @ImplMethod */
    public static class MethodInfo{
        /** method name defined in module */
        private String methodName;
        private TypeName returnType;
        private List<ParamInfo> paramInfos;
        private FieldData.TypeCompat implClass;
        private boolean returnVoid;
        private String implMethodName;
        private List<FieldData> dependProps;//currently not used.

        public List<FieldData> getDependProps() {
            return dependProps;
        }
        public boolean hasDependProp(){
            return dependProps != null && dependProps.size() >0;
        }
        public void setDependProps(List<FieldData> dependProps) {
            this.dependProps = dependProps;
        }

        public TypeName getReturnType() {
            return returnType;
        }
        public void setReturnType(TypeName returnType) {
            this.returnType = returnType;
        }
        public List<ParamInfo> getParamInfos() {
            return paramInfos;
        }
        public void setParamInfos(List<ParamInfo> paramInfos) {
            this.paramInfos = paramInfos;
        }
        public String getMethodName() {
            return methodName;
        }
        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }
        public FieldData.TypeCompat getImplClass() {
            return implClass;
        }
        public void setImplClass(FieldData.TypeCompat implClass) {
            this.implClass = implClass;
        }
        public void setReturnVoid(boolean returnVoid) {
            this.returnVoid = returnVoid;
        }
        public boolean isReturnVoid() {
            return returnVoid;
        }
        public void setImplMethodName(String implName) {
            implMethodName = implName;
        }
        public String getImplMethodName() {
            return implMethodName;
        }
    }
    public static class ParamInfo{
        final String paramName;
        final TypeName paramType;

        public ParamInfo(String paramName, TypeName paramType) {
            this.paramName = paramName;
            this.paramType = paramType;
        }
    }
}
