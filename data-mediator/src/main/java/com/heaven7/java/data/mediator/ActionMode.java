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