/**
 * Copyright 2017
 group of data-mediator
 member: heaven7(donshine723@gmail.com)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.heaven7.java.data.mediator.internal;

import com.heaven7.java.data.mediator.GlobalSetting;
import com.heaven7.java.data.mediator.Property;

import java.util.HashMap;

/**
 * this class help we share properties.
 * @author heaven7
 * @since 1.2.1
 */
public final class SharedProperties {
  private static final HashMap<String, Property> sCache;

  static {
    GlobalSetting.getDefault().setGsonVersion(1.0);
    sCache = new HashMap<>();
    putToCache("boolean", "selected", 0);
    try {
       for(int i = 1; i < 100 ; i ++) {
        Class.forName("com.heaven7.java.data.mediator.internal.SharedProperties" + "_" +i);
      }
    } catch (Exception e) {
      //ignore 
    }
  }
  public static Property get(String typeName, String propName, int complexFlag) {
    return sCache.get(generateKey(typeName, propName, complexFlag));
  }

  public static void putToCache(String typeName, String propName, int complexFlag) {
    sCache.put(generateKey(typeName, propName, complexFlag), new Property(typeName, propName, complexFlag));
  }

  private static String generateKey(String typeName, String propName, int complexFlag) {
    return typeName.hashCode()+ "_" + propName + "_" + complexFlag;
  }
}
