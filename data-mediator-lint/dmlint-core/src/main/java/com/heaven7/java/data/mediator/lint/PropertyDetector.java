package com.heaven7.java.data.mediator.lint;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiModifierList;

import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UMethod;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.heaven7.java.data.mediator.lint.PropertyUtils.getProp;
import static com.heaven7.java.data.mediator.lint.PropertyUtils.getPropInfoWithSupers;

/**
 * Created by heaven7 on 2017/11/29 0029.
 */

public class PropertyDetector extends Detector implements Detector.UastScanner {

    static final Issue ISSUE_PROP = Issue.create("com.heaven7.java.data.mediator.lint",
            "Lint Data-Mediator-Property",
            "help you fast find incorrect methods for data-module",
            Category.CORRECTNESS, 6,
            Severity.WARNING,
            new Implementation(PropertyDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.singletonList(UClass.class);
    }

    @Override
    public UElementHandler createUastHandler(JavaContext context) {
        return new UElementHandlerImpl(context);
    }

    private static class UElementHandlerImpl extends UElementHandler{

        private static final String NAME_KEEP        = "com.heaven7.java.data.mediator.Keep";
        private static final String NAME_IMPL_METHOD = "com.heaven7.java.data.mediator.ImplMethod";

        final JavaContext context;

        UElementHandlerImpl(JavaContext context) {
            this.context = context;
        }

        static boolean hasPropInfo(Collection<PropInfo> coll, String method){
            for(PropInfo info : coll){
               if(info.getGetMethodName().equals(method) ||
                       info.getSetMethodName().equals(method) ||
                       info.getEditorMethodName().equals(method)){
                   return true;
               }
            }
            return false;
        }

        @Override
        public void visitClass(UClass uClass) {
            Set<PropInfo> infos = getPropInfoWithSupers(uClass);
            if(infos.isEmpty()){
                return;
            }

            for(UMethod method: uClass.getMethods()){
                PsiModifierList list = method.getModifierList();
                PsiAnnotation pa_keep = list.findAnnotation(NAME_KEEP);
                PsiAnnotation pa_impl = list.findAnnotation(NAME_IMPL_METHOD);
                if (pa_keep == null && pa_impl == null) {
                    if(!hasPropInfo(infos, method.getName())){
                        report(method);
                    }
                }
            }
        }

        private void report(UMethod method){
            String prop = getProp(method.getName());
            context.report(ISSUE_PROP,
                    context.getLocation(method.getPsi()), "this method '"+ method.getName()
                            +"' is not generate for any 'propName' value of @Field,\n you should either add annotation " +
                            "@Keep/@ImplMethod or add @Field(prop= \""+ prop+"\") for it.**");
        }

    }

    static class PropInfo{
        final String prop;
        final boolean is_boolean;
        final String nameOfMethod;

        public PropInfo(String prop, boolean is_boolean) {
            this.prop = prop;
            this.is_boolean = is_boolean;
            this.nameOfMethod = prop.substring(0, 1).toUpperCase() + prop.substring(1);
        }
        public String getGetMethodName(){
            return is_boolean ? "is" + nameOfMethod : "get" + nameOfMethod;
        }
        public String getSetMethodName(){
            return "set" + nameOfMethod;
        }
        public String getEditorMethodName(){
            return "begin" + nameOfMethod + "Editor";
        }
    }
}
