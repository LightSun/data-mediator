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
package com.heaven7.java.data.mediator.util;

/**
 * the default equals comparator
 * Created by heaven7 on 2017/9/13 0013.
 * @since 1.0.2
 */
public class DefaultEqualsComparator implements EqualsComparator {

    private static DefaultEqualsComparator sComparator;

    private DefaultEqualsComparator(){}

    /**
     * get an instance of DefaultEqualsComparator.
     * @return  an instance of DefaultEqualsComparator.
     */
    public static DefaultEqualsComparator getInstance(){
        return sComparator != null ? sComparator : (sComparator = new DefaultEqualsComparator());
    }

    @Override
    public boolean isEquals(Object v1, Object v2) {
        if(v1 == null){
            return v2 == null;
        }else{
            return v1.equals(v2);
        }
    }
}
