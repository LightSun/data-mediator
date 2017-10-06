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
package com.heaven7.android.data.mediator;

import android.os.Parcel;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.internal.DataMediatorDelegate;
import com.heaven7.java.data.mediator.internal.ParcelDelegate;

/**
 * impl of all delegates. this class is used internal.
 * Created by heaven7 on 2017/10/5.
 */
public final class DataMediatorDelegateImpl extends DataMediatorDelegate {

    @Override
    public <T> Binder<T> createBinder(DataMediator<T> mediator) {
        return new AndroidBinder<T>(mediator);
    }
    @Override
    public ParcelDelegate getParcelDelegate(Object parcel) {
        if(!(parcel instanceof Parcel)){
            throw new IllegalArgumentException();
        }
        return new AndroidParcelDelegate((Parcel) parcel);
    }
    private static class AndroidParcelDelegate implements ParcelDelegate{
        final Parcel in;
        AndroidParcelDelegate(Parcel in) {
            this.in = in;
        }
        @Override
        public void writeSparseArray(SparseArray val) {
            writeSparseArray0(in , val);
        }
        @Override
        public SparseArray readSparseArray(ClassLoader loader) {
            return readSparseArray0(in, loader);
        }
        static SparseArray readSparseArray0(Parcel in, ClassLoader loader) {
            int N = in.readInt();
            if (N < 0) {
                return null;
            }
            SparseArray sa = new SparseArray(N);
            readSparseArrayInternal(in, sa, N, loader);
            return sa;
        }
        static void readSparseArrayInternal(Parcel in, SparseArray outVal, int N,
                                             ClassLoader loader) {
            while (N > 0) {
                int key = in.readInt();
                Object value = in.readValue(loader);
                //Log.i(TAG, "Unmarshalling key=" + key + " value=" + value);
                outVal.append(key, value);
                N--;
            }
        }
        static void writeSparseArray0(Parcel in, SparseArray val) {
            if (val == null) {
                in.writeInt(-1);
                return;
            }
            int N = val.size();
            in.writeInt(N);
            int i=0;
            while (i < N) {
                in.writeInt(val.keyAt(i));
                in.writeValue(val.valueAt(i));
                i++;
            }
        }
    }
}
