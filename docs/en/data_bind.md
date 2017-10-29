# Data bind (currently only android)
* here is a demo of TextView bind.
```java

public class TestTextViewBindActivity extends BaseActivity {

    @BindView(R.id.tv)
    TextView mTv;

    private final Random mRan = new Random();
    private Binder<TextViewBind> mBinder;
    private TextViewBind mProxy;

    private int[] mTextRess;
    private int[] mColorRess;
    private int[] mTextSizeRess;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_text_view_bind;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
        initResource(context);
        mBinder = DataMediatorFactory.createBinder(TextViewBind.class);
        //bind group properties to TextView
        mBinder.beginBatchTextViewBinder(mTv)
                .bindText(TextViewBind.PROP_text)
                .bindTextRes(TextViewBind.PROP_textRes)
                .bindTextColor(TextViewBind.PROP_textColor)
                .bindTextColorRes("textColorRes")
                .bindTextSize("textSize")
                .bindTextSizeRes("textSizeRes")
                .end();
        mProxy = mBinder.getDataProxy();
    }

    @Override
    protected void onDestroy() {
        mBinder.unbindAll();
        super.onDestroy();
    }

    @OnClick(R.id.bt_text)
    public void onClickChangeText(View v){
        mProxy.setText(getString(mTextRess[mRan.nextInt(5)]));
    }
    @OnClick(R.id.bt_text_res)
    public void onClickChangeTextRes(View v){
        mProxy.setTextRes(mTextRess[mRan.nextInt(5)]);
    }


    @OnClick(R.id.bt_text_color)
    public void onClickChangeTextColor(View v){
        mProxy.setTextColor(getResources().getColor(mColorRess[mRan.nextInt(5)]));
    }
    @OnClick(R.id.bt_text_color_res)
    public void onClickChangeTextColorRes(View v){
        mProxy.setTextColorRes(mColorRess[mRan.nextInt(5)]);
    }


    @OnClick(R.id.bt_text_size)
    public void onClickChangeTextSize(View v){
        mProxy.setTextSize(getResources().getDimensionPixelSize(mTextSizeRess[mRan.nextInt(5)]));
    }

    @OnClick(R.id.bt_text_size_res)
        mProxy.setTextSizeRes(mTextSizeRess[mRan.nextInt(5)]);
    }

    private void initResource(Context context) {
        mTextRess = new int[]{
                R.string.text_1,
                R.string.text_2,
                R.string.text_3,
                R.string.text_4,
                R.string.text_5,
        };
        mColorRess = new int []{
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorPrimaryDark,
                android.R.color.black,
                android.R.color.holo_red_light,
        };
        mTextSizeRess = new int []{
                R.dimen.size_15,
                R.dimen.size_20,
                R.dimen.size_25,
                R.dimen.size_30,
                R.dimen.size_35,
        };
    }


}
```
