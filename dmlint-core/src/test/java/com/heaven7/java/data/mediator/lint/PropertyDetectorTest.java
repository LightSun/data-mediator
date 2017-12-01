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

    public void testBasic1() throws Exception {
        String string = lintProject(
                java(getTestSource()),
               // TestFiles.copy("FlowItem.java", "src/test/dm/lint/test/FlowItem.java.java", this),
                TestFiles.copy("Java-base-1.1.0.jar", "libs/Java-base.jar", this),
                TestFiles.copy("data-mediator-1.4.4.jar", "libs/data-mediator.jar", this),
                TestFiles.copy("data-mediator-annotations-1.2.2.jar", "libs/data-mediator-annotations.jar", this)
        );
        System.out.println(string);
    }

    public void testBasic2() throws Exception {
        String string = lintProject(
                java(getTestSource2()),
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
        return "package com.heaven7.java.data.mediator.lint;\n" +
                "\n" +
                "import com.heaven7.java.data.mediator.DataPools;\n" +
                "import com.heaven7.java.data.mediator.Field;\n" +
                "import com.heaven7.java.data.mediator.Fields;\n" +
                "import com.heaven7.java.data.mediator.Property;\n" +
                "import com.heaven7.java.data.mediator.internal.SharedProperties;\n" +
                "\n" +
                "@Fields({\n" +
                "        @Field(propName = \"text1\")\n" +
                "})\n" +
                "public interface Parent extends DataPools.Poolable {\n" +
                "\n" +
                "    Property PROP_text1 = SharedProperties.get(\"java.lang.String\", \"text1\", 0);\n" +
                "\n" +
                "    @Fields({\n" +
                "            @Field(propName = \"text2\")\n" +
                "    })\n" +
                "    interface Child extends Parent{\n" +
                "\n" +
                "        Property PROP_text2 = SharedProperties.get(\"java.lang.String\", \"text2\", 0);\n" +
                "\n" +
                "        Child setText2(String text21);\n" +
                "        String getText2();\n" +
                "\n" +
                "        Child setText1(String text11);\n" +
                "    }\n" +
                "    @Fields({\n" +
                "            @Field(propName = \"text3\")\n" +
                "    })\n" +
                "    interface Child2  extends Child{\n" +
                "        Property PROP_text3 = SharedProperties.get(\"java.lang.String\", \"text3\", 0);\n" +
                "        Child2 setText3(String text31);\n" +
                "        String getText3();\n" +
                "\n" +
                "        Child2 setText2(String text21);\n" +
                "        Child2 setText1(String text11);\n" +
                "    }\n" +
                "\n" +
                "    Parent setText1(String text11);\n" +
                "    String getText1();\n" +
                "}\n";
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
