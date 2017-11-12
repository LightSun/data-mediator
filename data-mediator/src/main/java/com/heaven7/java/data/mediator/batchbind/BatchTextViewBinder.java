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
package com.heaven7.java.data.mediator.batchbind;

import com.heaven7.java.data.mediator.Binder;
import com.heaven7.java.data.mediator.Property;

/**
 *  the batch binder of TextView.
 * @param <T> the module data type
 * @author heaven7
 * @since 1.0.8
 */
public class BatchTextViewBinder<T> extends BatchViewBinder<T> {
    /**
     * create batch base view binder for target view
     *
     * @param mBinder the binder impl
     * @param textView    the text view . on android is  any child of 'android.widget.TextView'.
     */
    public BatchTextViewBinder(Binder<T> mBinder, Object textView) {
        super(mBinder, textView);
    }

    /**
     * bind text of Text view.
     * @param property the property
     * @return this.
     */
    public BatchTextViewBinder<T> bindText(String property){
        mBinder.bindText(property, mView);
        return this;
    }
    /**
     * bind text of Text view.
     * @param property the property
     * @return this.
     * @since 1.1.2
     */
    public BatchTextViewBinder<T> bindText(Property property){
        mBinder.bindText(property.getName(), mView);
        return this;
    }
    /**
     * bind text resource of Text view.
     * @param property the property
     * @return this.
     */
    public BatchTextViewBinder<T> bindTextRes(String property){
        mBinder.bindTextRes(property, mView);
        return this;
    }
    /**
     * bind text resource of Text view.
     * @param property the property
     * @return this.
     * @since 1.1.2
     */
    public BatchTextViewBinder<T> bindTextRes(Property property){
        mBinder.bindTextRes(property.getName(), mView);
        return this;
    }
    /**
     * bind text color of Text view.
     * @param property the property
     * @return this.
     */
    public BatchTextViewBinder<T> bindTextColor(String property){
        mBinder.bindTextColor(property, mView);
        return this;
    }
    /**
     * bind text color of Text view.
     * @param property the property
     * @return this.
     * @since 1.1.2
     */
    public BatchTextViewBinder<T> bindTextColor(Property property){
        mBinder.bindTextColor(property.getName(), mView);
        return this;
    }
    /**
     * bind text color resource of Text view.
     * @param property the property
     * @return this.
     */
    public BatchTextViewBinder<T> bindTextColorRes(String property){
        mBinder.bindTextColorRes(property, mView);
        return this;
    }
    /**
     * bind text color resource of Text view.
     * @param property the property
     * @return this.
     * @since 1.1.2
     */
    public BatchTextViewBinder<T> bindTextColorRes(Property property){
        mBinder.bindTextColorRes(property.getName(), mView);
        return this;
    }
    /**
     * bind text size resource of Text view.
     * @param property the property
     * @return this.
     */
    public BatchTextViewBinder<T> bindTextSizeRes(String property){
        mBinder.bindTextSizeRes(property, mView);
        return this;
    }
    /**
     * bind text size resource of Text view.
     * @param property the property
     * @return this.
     * @since 1.1.2
     */
    public BatchTextViewBinder<T> bindTextSizeRes(Property property){
        mBinder.bindTextSizeRes(property.getName(), mView);
        return this;
    }
    /**
     * bind text size of Text view.
     * @param property the property
     * @return this.
     */
    public BatchTextViewBinder<T> bindTextSize(String property){
        mBinder.bindTextSize(property, mView);
        return this;
    }
    /**
     * bind text size of Text view.
     * @param property the property
     * @return this.
     * @since 1.1.2
     */
    public BatchTextViewBinder<T> bindTextSize(Property property){
        mBinder.bindTextSize(property.getName(), mView);
        return this;
    }
    /**
     * <p>Use {@linkplain #bindTextSize(String)} instead.</p>
     * bind text dimension size(dp value) of Text view.
     * @param property the property
     * @return this.
     */
    @Deprecated
    public BatchTextViewBinder<T> bindTextSizeDp(String property){
        mBinder.bindTextSize(property, mView);
        return this;
    }
    /**
     * <p>Use {@linkplain #bindTextSize(Property)} instead.</p>
     * bind text dimension size(dp value) of Text view.
     * @param property the property
     * @return this.
     * @since 1.1.2
     */
    @Deprecated
    public BatchTextViewBinder<T> bindTextSizeDp(Property property){
        mBinder.bindTextSize(property.getName(), mView);
        return this;
    }
    /**
     * bind text dimension size(pix value) of Text view.
     * @param property the property
     * @return this.
     * @since 1.4.1
     */
    public BatchTextViewBinder<T> bindTextSizePx(Property property){
        mBinder.bindTextSizePx(property.getName(), mView);
        return this;
    }
    /**
     * bind text dimension size(pix value) of Text view.
     * @param property the property
     * @return this.
     * @since 1.4.1
     */
    public BatchTextViewBinder<T> bindTextSizePx(String property){
        mBinder.bindTextSizePx(property, mView);
        return this;
    }
}
