package com.heaven7.java.data.mediator.binders;

import com.heaven7.java.data.mediator.Binder;

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
     * bind text resource of Text view.
     * @param property the property
     * @return this.
     */
    public BatchTextViewBinder<T> bindTextRes(String property){
        mBinder.bindTextRes(property, mView);
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
     * bind text color resource of Text view.
     * @param property the property
     * @return this.
     */
    public BatchTextViewBinder<T> bindTextColorRes(String property){
        mBinder.bindTextColorRes(property, mView);
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
     * bind text size of Text view.
     * @param property the property
     * @return this.
     */
    public BatchTextViewBinder<T> bindTextSize(String property){
        mBinder.bindTextSize(property, mView);
        return this;
    }
    /**
     * bind text dimension size(dp value) of Text view.
     * @param property the property
     * @return this.
     */
    public BatchTextViewBinder<T> bindTextSizeDp(String property){
        mBinder.bindTextSizeDp(property, mView);
        return this;
    }
}
