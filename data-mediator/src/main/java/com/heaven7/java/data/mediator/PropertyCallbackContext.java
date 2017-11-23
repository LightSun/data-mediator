package com.heaven7.java.data.mediator;

import com.heaven7.java.base.anno.CalledInternal;

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
    @CalledInternal
    public void onPreCallback(Params params) {
        mParams = params;
    }
    /**
     *  called when after callback.
     * this method should call super. or else may cause bug.
     */
    @CalledInternal
    public void onPostCallback() {
        if (mParams != null) {
            mParams.cleanUp();
            mParams = null;
        }
    }
    public Params getParams() {
        return mParams;
    }

    /**
     * the params
     * @author heaven7
     * @since 1.4.4
     */
    public static class Params {
        public Object mOriginalSource;
        public int mDepth;

        public Params(Object original, int mDepth) {
            this.mOriginalSource = original;
            this.mDepth = mDepth;
        }
        public Params(Params params) {
            this(params.mOriginalSource, params.mDepth);
        }
        public void cleanUp() {
            mOriginalSource = null;
            mDepth = 0;
        }
    }
}
