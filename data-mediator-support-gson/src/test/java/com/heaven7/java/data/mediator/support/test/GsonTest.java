package com.heaven7.java.data.mediator.support.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class GsonTest {

    public static void main(String[] args){
        //ok
       // long i = Character.MIN_VALUE;
       // char j = (char) i;

        Gson gson = getGson(); //builder.
       // Gson gson = new Gson();

        IStudent module = new StudentModuleImpl();
        module.setName("heaven7");
        module.setId("xxx");

        try {
            Method setTags = module.getClass().getMethod("setTags", List.class);
            module.getClass().getMethod("setAge", int.class).invoke(module, Integer.valueOf(26));
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * if use static register. here StudentModuleImpl.class can't use interface type
         */
        String json = gson.toJson(module, StudentModuleImpl.class);
        log(json);
        IStudent stu = gson.fromJson(json, StudentModuleImpl.class);
        log(stu);
    }

    private static Gson getGson() {
        return new GsonBuilder() //动态注册优先于 @JsonAdapter
                .excludeFieldsWithoutExposeAnnotation()
                    .registerTypeAdapter(IStudent.class, new TypeAdapter<IStudent>() {
                        @Override
                        public void write(JsonWriter out, IStudent value) throws IOException {
                            log("write_getGson");
                            out.beginObject();
                            out.name("name").value("heaven7");
                            out.name("id").value("26");
                            out.endObject();
                        }
                        @Override
                        public IStudent read(JsonReader in) throws IOException {
                            log("read_getGson");
                            StudentModuleImpl module = new StudentModuleImpl();
                            in.beginObject();
                            while (in.hasNext()){
                                switch (in.nextName()){
                                    case "name":
                                        module.setName(in.nextString());
                                        break;

                                    case "id":
                                        module.setAge(in.nextInt());
                                        break;
                                }
                            }
                            in.endObject();
                            return module;
                        }
                    })
                    .create();
    }

    public static void log(Object obj){
        System.out.println(obj);
    }
}
