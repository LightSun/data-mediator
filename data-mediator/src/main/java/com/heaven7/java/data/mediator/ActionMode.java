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
package com.heaven7.java.data.mediator;

/**
 * Represents a contextual mode of the user interface. this is used for 'data-module'.
 * often return by {@linkplain DataMediator#startActionMode(Callback)}
 * @param <T> the module type
 * @since 1.1.3
 */
public class ActionMode<T> {

    private final DataMediator<T> mDm;

    /*public*/ ActionMode(DataMediator<T> mDm) {
        this.mDm = mDm;
    }

    /**
     * get the real module data.
     * @return the real module data.
     */
    public final T getData() {
        return mDm.getData();
    }
    /**
     * get the module proxy.
     * @return the module proxy.
     */
    public final T getDataProxy() {
        return mDm.getDataProxy();
    }

    /**
     * apply action to the default data consumer.
     * @see DataConsumer
     * @see DataMediator#setDataConsumer(DataConsumer)
     */
    public final void applyTo() {
        mDm.getBaseMediator().applyTo();
    }
    /**
     * apply action to the target data consumer.
     * @param consumer the data consumer .
     * @see DataConsumer
     * @see DataMediator#setDataConsumer(DataConsumer)
     */
    public final void applyTo(DataConsumer<T> consumer) {
        mDm.getBaseMediator().applyTo(consumer);
    }

    /**
     * the callback of action mode.
     * @param <T> the module data type
     * @since 1.1.3
     */
    public static abstract class Callback<T> {
        /**
         * called on prepare the action mode.
         * @param mode the action mode.
         */
        public abstract void onPrepareActionMode(ActionMode<T> mode);
    }
}