package com.heaven7.java.data.mediator;

import com.heaven7.java.base.anno.CalledInternal;
import com.heaven7.java.base.anno.Hide;

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
        if (mParams == null) {
            mParams = new Params(params);
        }else{
            mParams.set(params);
        }
    }
    /**
     *  called when after callback.
     * this method should call super. or else may cause bug.
     */
    @CalledInternal
    public void onPostCallback() {
        if (mParams != null) {
            mParams.cleanUp();
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
        private Object mOriginalSource;
        private int mDepth;

        public Params(Object original, int mDepth) {
            this.mOriginalSource = original;
            this.mDepth = mDepth;
        }
        public Params(Params params) {
            this.mOriginalSource = params.mOriginalSource;
            this.mDepth = params.mDepth;
        }
        public void set(Params params) {
            this.mOriginalSource = params.mOriginalSource;
            this.mDepth = params.mDepth;
        }
        public void cleanUp() {
            mOriginalSource = null;
            mDepth = 0;
        }
        public Object getOriginalSource() {
            return mOriginalSource;
        }
        public int getDepth() {
            return mDepth;
        }
        public void setDepth(int depth) {
            this.mDepth = depth;
        }
    }
}