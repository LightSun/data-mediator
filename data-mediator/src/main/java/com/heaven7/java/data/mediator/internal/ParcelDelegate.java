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

import com.heaven7.java.base.anno.Hide;
import com.heaven7.java.base.util.SparseArray;

/**
 * the parcel delegate on android.
 * Created by heaven7 on 2017/10/4.
 * @since 1.1.3
 */
@Hide
public interface ParcelDelegate {

    /**
     * write sparse array to the parcel
     * @param val the sparse array
     */
    void writeSparseArray(SparseArray val);

    /**
     * read the sparse array.
     * @param loader the class loader
     * @return the sparse array
     */
    SparseArray readSparseArray(ClassLoader loader);
}
