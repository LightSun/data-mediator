package com.heaven7.java.data.mediator;

/**
 * the property callback context
 * @author heaven7
 * @since 1.4.4
 */
public abstract class PropertyCallbackContext {

    private Params mParams;

    /**
     * called when before callback.
     * this method should call super. or else may cause bug.
     * @param params the params
     */
    public void onPreCallback(Params params) {
        mParams = params;
    }
    /**
     *  called when after callback.
     * this method should call super. or else may cause bug.
     */
    public void onPostCallback() {
        if (mParams != null) {
            mParams.cleanUp();
            mParams = null;
        }
    }

    /**
     * get the additional params of callback
     * @return the params , may be null if the callbacks is from current data-module.
     */
    public Params getParams() {
        return mParams;
    }

    /**
     * the params
     * @author heaven7
     * @since 1.4.4
     */
    public static class Params {
        /** the original source object. indicate callback occurs from this module */
        public Object mOriginalSource;
        /** the depth from original */
        public int mDepth;

        /**
         * create callback params from target original object and depth.
         * @param original  the original source object. indicate callback occurs from this module
         * @param mDepth the depth from original object
         */
        public Params(Object original, int mDepth) {
            this.mOriginalSource = original;
            this.mDepth = mDepth;
        }

        /**
         * create params from target exist params
         * @param params the target params.
         */
        public Params(Params params) {
            this(params.mOriginalSource, params.mDepth);
        }

        /**
         * called on post callback.
         * @see PropertyCallbackContext#onPostCallback()
         */
        protected void cleanUp() {
        }
    }
}
