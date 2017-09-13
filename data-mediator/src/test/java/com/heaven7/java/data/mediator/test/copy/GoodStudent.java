package com.heaven7.java.data.mediator.test.copy;

/**
 * Created by heaven7 on 2017/9/12 0012.
 */
public class GoodStudent extends Student implements IGoodStudent{

    private int thinking;

    public int getThinking() {
        return thinking;
    }
    public void setThinking(int thinking) {
        this.thinking = thinking;
    }

    @Override
    public GoodStudent copy() {
        GoodStudent gs = new GoodStudent();
        copyTo(gs);
        return gs;
    }

    @Override
    public void copyTo(IStudent out) {
        super.copyTo(out);
        if(out instanceof IGoodStudent){
            IGoodStudent gs = (IGoodStudent) out;
            gs.setThinking(getThinking());
        }
    }
}
