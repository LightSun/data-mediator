package com.heaven7.java.data.mediator.lint;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.checks.infrastructure.TestFiles;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.util.List;

/**
 * Created by heaven7 on 2017/11/29 0029.
 */

public class PropertyDetectorTest extends LintDetectorTest {

    public void testBasic0() throws Exception {
        String string = lintProject(
                java(getTestSource()),
               // TestFiles.copy("FlowItem.java", "src/test/dm/lint/test/FlowItem.java.java", this),
                TestFiles.copy("Java-base-1.1.0.jar", "libs/Java-base.jar", this),
                TestFiles.copy("data-mediator-1.4.4.jar", "libs/data-mediator.jar", this),
                TestFiles.copy("data-mediator-annotations-1.2.2.jar", "libs/data-mediator-annotations.jar", this)
        );
        System.out.println(string);
    }

    private String getTestSource(){
        return "" +
                "package dm.lint.test;\n" +
                "import com.heaven7.java.data.mediator.DataPools;\n" +
                "import com.heaven7.java.data.mediator.Field;\n" +
                "import com.heaven7.java.data.mediator.FieldFlags;\n" +
                "import com.heaven7.java.data.mediator.Fields;\n" +
                "import com.heaven7.java.data.mediator.ListPropertyEditor;\n" +
                "import com.heaven7.java.data.mediator.Property;\n" +
                "import com.heaven7.java.data.mediator.internal.SharedProperties;\n" +
                "\n" +
                "import java.util.List;\n" +
                "@Fields(value = {\n" +
                "        @Field(propName = \"desc\" , complexType = FieldFlags.COMPLEX_LIST),\n" +
                "})\n" +
                "public interface FlowItem extends DataPools.Poolable {\n" +
                "    Property PROP_desc = SharedProperties.get(String.class.getName(), \"desc\", 2);\n" +
                "    FlowItem setDesc(List<String> desc1);\n" +
                "    List<String> getDesc();\n" +
                "    ListPropertyEditor<? extends FlowItem, String> beginDescEditor();\n" +
                " void getxxx();\n "+
                "}";
    }
    private String getTestSource2(){
        return "" +
                "package dm.lint.test;\n" +
                "import com.heaven7.java.data.mediator.DataPools;\n" +
                "import com.heaven7.java.data.mediator.Field;\n" +
                "import com.heaven7.java.data.mediator.FieldFlags;\n" +
                "import com.heaven7.java.data.mediator.Fields;\n" +
                "import com.heaven7.java.data.mediator.ListPropertyEditor;\n" +
                "import com.heaven7.java.data.mediator.Property;\n" +
                "import com.heaven7.java.data.mediator.internal.SharedProperties;\n" +
                "\n" +
                "import java.util.List;\n" +
                "@Fields(value = {\n" +
                "        @Field(propName = \"desc\" , complexType = FieldFlags.COMPLEX_LIST),\n" +
                "})\n" +
                "public interface FlowItem extends DataPools.Poolable {\n" +
                "    Property PROP_desc = SharedProperties.get(String.class.getName(), \"desc\", 2);\n" +
                "    FlowItem setDesc(List<String> desc1);\n" +
                "    List<String> getDesc();\n" +
                "    ListPropertyEditor<? extends FlowItem, String> beginDescEditor();\n" +
                " void getxxx();\n "+
                "}";
    }

    @Override
    protected boolean allowCompilationErrors() {
        return false;
    }

    @Override
    protected Detector getDetector() {
        return new PropertyDetector();
    }
    @Override
    protected List<Issue> getIssues() {
        return new PropertyIssueRegistry().getIssues();
    }


}
