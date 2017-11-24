package com.heaven7.java.data.mediator;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

/**
 * the super class of data binding.
 * <p>Here is a simple demo:</p>
 * <pre>
 * public class TestViewBindActivity extends BaseActivity {
 *
     * {@literal }@BindView(R.id.v_bg) @BindBackground("background")
     * View mV_bg;
     *
     * private ResHelper mHelper = new ResHelper();
     * private Binder<ViewBind> binder;
     *
     * {@literal } @Override
     * protected int getLayoutId() {
     * return R.layout.ac_test_view_bind;
     * }
     * {@literal } @Override
     * protected void onInit(Context context, Bundle savedInstanceState) {
     * mHelper.init(context);
     *
     * final ViewBind data = DataMediatorFactory.createData(ViewBind.class);
     * //bind data.
     * binder = DataMediatorFactory.bind(this, data);
     * }
     *
     * {@literal } @OnClick(R.id.bt_change_bg)
     * public void onClickChangeBg(View v){
     * //（drawable）
     * binder.getDataProxy().setBackground(mHelper.toggleDrawable());
     * }
 *
 * }
 * </pre>
 *
 * @param <T> the object which whose view will be bind.
 * @author heaven7
 * @since 1.4.0
 */
public abstract class DataBinding<T> {

    private final HashSet<BindInfo> mBinds = new HashSet<>();
    private final T mTarget;
    private BinderFactory mBinderFactory;
    private Class<? extends Binder> mBinderClass;
    private BindMethodSupplier mBindMethodSupplier;

    /**
     * <p><h2>Note: Use {@linkplain SimpleParameterSupplier} instead as this class can't handle complex property.</h2></p>
     * interface for supply parameters which is used for data-binding when we need additional parameters.
     *
     * @author heaven7
     * @since 1.4.0
     */
    @Deprecated
    public interface ParameterSupplier {
        /**
         * get the parameters for target property from data.
         *
         * @param data     the module data which is used for this bind.
         * @param property the full property name
         * @return the parameters
         */
        Object[] getParameters(Object data, String property);
    }

    /**
     * create data-binding for target object.
     *
     * @param target the target object.
     */
    public DataBinding(T target) {
        Throwables.checkNull(target);
        this.mTarget = target;
    }

    /**
     * create bind info, this method often called internal. you should not call this.
     *
     * @param view       the view
     * @param propName   the property name
     * @param index      the index
     * @param methodName the method name
     * @return an instance of Binder info
     * @see BindInfo
     */
    public static BindInfo createBindInfo(Object view, String propName, int index, String methodName) {
        return new BindInfo(view, propName, index, methodName);
    }

    /**
     * get bind method supplier.
     * @return the bind method supplier.
     * @since 1.4.3
     */
    protected final BindMethodSupplier getBindMethodSupplier() {
        return mBindMethodSupplier;
    }
    /**
     * set the  bind method supplier.
     * @since 1.4.3
     */
    protected final void setBindMethodSupplier(BindMethodSupplier bindMethodSupplier) {
        this.mBindMethodSupplier = bindMethodSupplier;
    }

    /**
     * add a bind info
     *
     * @param bindInfo the bind info to add.
     */
    public void addBindInfo(BindInfo bindInfo) {
        mBinds.add(bindInfo);
    }

    /**
     * set the binder class.
     *
     * @param binderClass the binder class.
     */
    public void setBinderClass(Class<? extends Binder> binderClass) {
        this.mBinderClass = binderClass;
    }

    /**
     * set binder factory
     *
     * @param factory the binder factory.
     */
    public void setBinderFactory(BinderFactory factory) {
        this.mBinderFactory = factory;
    }

    /**
     * get the target
     *
     * @return the target
     */
    public final T getTarget() {
        return mTarget;
    }

    /**
     * bind data to the all defined  bind views
     *
     * @param data the module data
     * @param <D>  the module data type
     * @return the binder
     */
    public <D> Binder<D> bind(D data) {
        return bind(data, 0);
    }

    /**
     * bind data to the all defined bind views
     *
     * @param data  the module data
     * @param index the index
     * @param <D>   the module data type
     * @return the binder
     */
    public <D> Binder<D> bind(D data, int index) {
        return bind(data, index, null);
    }

    /**
     * bind data to the all defined bind views
     *
     * @param data        the module data
     * @param index       the index
     * @param interceptor the property interceptor
     * @param <D>         the module data type
     * @return the binder
     */
    public <D> Binder<D> bind(D data, int index, @Nullable PropertyInterceptor interceptor) {
        return bind(data, index, null, interceptor);
    }

    /**
     * bind data to the all defined bind views
     *
     * @param data        the module data
     * @param supplier    the additional parameter supplier, can be null . if not need.
     * @param interceptor the property interceptor
     * @param <D>         the module data type
     * @return the binder
     */
    public <D> Binder<D> bind(D data, @Nullable ParameterSupplier supplier,
                              @Nullable PropertyInterceptor interceptor) {
        return bind(data, 0, supplier, interceptor);
    }

    /**
     * bind data to the all defined bind views
     *
     * @param data        the module data
     * @param supplier    the additional parameter supplier, can be null . if not need.
     * @param index       the index
     * @param interceptor the property interceptor
     * @param <D>         the module data type
     * @return the binder
     */
    @SuppressWarnings("unchecked")
    public <D> Binder<D> bind(D data, int index, @Nullable ParameterSupplier supplier,
                              @Nullable PropertyInterceptor interceptor) {
        final DataMediator<D> dm = DataMediatorFactory.createDataMediator(data);
        //create binder . order : binder class-> binderFactor -> default.
        Binder<D> binder = null;
        if (mBinderClass != null) {
            try {
                binder = mBinderClass.getConstructor(dm.getClass()).newInstance(dm);
            } catch (Exception e) {
                System.err.println(String.format("can't create binder for target binder class( %s ), " +
                        "start use BinderFactory or default Binder.", mBinderClass.getName()));
            }
        }
        if (binder == null) {
            if (mBinderFactory != null) {
                binder = mBinderFactory.createBinder(getTarget(), dm);
            }
            if (binder == null) {
                binder = DataMediatorFactory.createBinder(dm);
            }
        }
        if (interceptor != null) {
            binder.setPropertyInterceptor(interceptor);
        }
        for (BindInfo info : mBinds) {
            if (info.index == index) {
                bindInternal(binder, info, supplier);
            }
        }
        return binder;
    }

    /**
     * bind data to the all defined bind views. and apply it immediately.
     *
     * @param data        the module data
     * @param index       the index
     * @param interceptor the property interceptor
     * @param <D>         the module data type
     * @return the binder
     */
    public <D> Binder<D> bindAndApply(D data, int index, PropertyInterceptor interceptor) {
        Binder<D> binder = bind(data, index, null, interceptor);
        binder.applyProperties();
        return binder;
    }

    /**
     * bind data to the all defined bind views. and apply it immediately.
     *
     * @param data        the module data
     * @param index       the index
     * @param supplier    the additional parameter supplier, can be null . if not need.
     * @param interceptor the property interceptor
     * @param <D>         the module data type
     * @return the binder
     */
    public <D> Binder<D> bindAndApply(D data, int index, @Nullable ParameterSupplier supplier,
                                      @Nullable PropertyInterceptor interceptor) {
        final Binder<D> binder = bind(data, index, supplier, interceptor);
        binder.applyProperties();
        return binder;
    }

    private static void bindInternal(Binder<?> binder, BindInfo info, @Nullable ParameterSupplier supplier) {
        //verifyPropertyName(binder, info);

        //extra parameters
        //property name may be complex.like stu.name. and etc.
        final Object[] extraParams ;
        final String simpleProp;
         // handle simple property name
        final String fullName = info.propName;
        if(fullName.contains(".")){
            binder.getDataMediator().inflatePropertyChain(fullName);
            simpleProp = fullName.substring(fullName.lastIndexOf(".") + 1);
        }else{
            simpleProp = fullName;
            //verify property name will be moved to lint.
            verifyPropertyName(binder, simpleProp);
        }
        //handle extra parameters
        if(supplier == null){
            extraParams = null;
        }else{
            if(supplier instanceof SimpleParameterSupplier){
                SimpleParameterSupplier localSupplier = (SimpleParameterSupplier) supplier;
                localSupplier.fullProperty = fullName;
                extraParams = localSupplier.getParameters(binder.getData(), simpleProp, info.methodName, info.methodTypes);
            }else{
                extraParams = supplier.getParameters(binder.getData(), fullName);
            }
        }
      /*  final Object[] extraParams = supplier == null ? null : (
                (supplier instanceof SimpleParameterSupplier) ?
                        ((SimpleParameterSupplier) supplier).getParameters(binder.getData(), info.propName, info.methodName, info.methodTypes)
                        : supplier.getParameters(binder.getData(), info.propName));*/

        //------- start handle parameters of bind method ------------
        Object[] params = { simpleProp, info.view};
        int preLength = params.length;
        //if need add internal parameter which is imported by annotation
        if (!Predicates.isEmpty(info.extraValues)) {
            Object[] params2 = new Object[preLength + info.extraValues.length];
            System.arraycopy(params, 0, params2, 0, preLength);
            for (int i = 0, size = info.extraValues.length; i < size; i++) {
                params2[preLength + i] = info.extraValues[i];
            }
            params = params2;
            preLength = params2.length;
        }
        //add extra parameter if need
        if (!Predicates.isEmpty(extraParams)) {
            Object[] params2 = new Object[preLength + extraParams.length];
            System.arraycopy(params, 0, params2, 0, preLength);
            for (int i = 0, size = extraParams.length; i < size; i++) {
                params2[preLength + i] = extraParams[i];
            }
            params = params2;
        }
        try {
            Method method = binder.getClass().getMethod(info.methodName, info.methodTypes);
            method.invoke(binder, params);
        } catch (Exception e) {
            throw new RuntimeException("can't find method, name = " + info.methodName + " ,info.methodTypes = "
                    + (info.methodTypes != null ? Arrays.toString(info.methodTypes) : null), e);
        }
    }

   private static void verifyPropertyName(Binder<?> binder, String simpleProp) {
        final Class<?> clazz = binder.getData().getClass();

        Field field = null;
        Class<?> curClazz = clazz;
        while (curClazz != Object.class && !curClazz.getName().startsWith("java.")
                && !curClazz.getName().startsWith("android.")) {
            try {
                field = curClazz.getDeclaredField(simpleProp);
                break;
            } catch (NoSuchFieldException e) {
                curClazz = curClazz.getSuperclass();
            }
        }
        if (field == null) {
            throw new RuntimeException(String.format("can't find property '%s' from class(%s)",
                    simpleProp, clazz.getName()));
        }
    }

    /**
     * the simple parameter supplier.
     *
     * @author heaven7
     * @since 1.4.1
     */
    public static abstract class SimpleParameterSupplier implements ParameterSupplier {

        String fullProperty;

        /**
         * get the full property name .
         * @return the full property name.
         * @since 1.4.4
         */
        public final String getFullProperty() {
            return fullProperty;
        }

        /**
         * get the property depth
         * @return the depth of full property. start from 0.
         * @since 1.4.4
         */
        public final int getDepth() {
            return fullProperty.split("\\.").length - 1;
        }

        @Override
        public Object[] getParameters(Object data, String property) {
            return getParameters(data, property, null, null);
        }

        /**
         * get additional parameters by target data module , property,binder method name and method parameterTypes.
         *
         * @param data        the data module
         * @param property    the simple property
         * @param method      the method name of Binder.
         * @param methodTypes the method parameter types of Binder.
         * @return the additional parameters. null means default parameters.
         * @see Binder
         */
        public Object[] getParameters(Object data, String property, String method, Class<?>[] methodTypes) {
            if (!"bindImageUrl".equals(method)) {
                return null;
            }
            Object loader = getImageLoader();
            if (loader == null) {
                throw new IllegalArgumentException("you must assign the image loader. can't be null.");
            }
            return new Object[]{loader};
        }

        /**
         * get the image loader. which is used to {@linkplain Binder#bindImageUrl(String, Object, Object)}.
         *
         * @return the image loader to load image url. can't be null.
         */
        protected abstract Object getImageLoader();
    }

    /**
     * bind info: which indicate once bind of view .
     *
     * @author heaven7
     * @since 1.4.0
     */
    public static class BindInfo {
        final Object view;
        final String propName;
        final int index;

        final String methodName;
        Class<?>[] methodTypes;
        Object[] extraValues;

        private int mTypeIndex;
        private int mExtraIndex;

        BindInfo(Object view, String propName, int index, String methodName) {
            this.view = view;
            this.methodName = methodName;
            this.propName = propName;
            this.index = index;
        }

        /**
         * inflate the bind method parameter types by target supplier.
         * @param supplier the bind method supplier. can't be null.
         * @since 1.4.3
         */
        public void inflateMethodParamTypes(BindMethodSupplier supplier){
            if(supplier == null){
                throw new IllegalStateException("for use @BindAny or @BindsAny you must " +
                        "use annotation @BindMethodSupplierClass first!");
            }
            this.methodTypes = supplier.getMethodParameterTypes(view, propName, methodName);
        }

        /**
         * assign the internal additional parameter count for Binder method.
         *
         * @param count the internal additional parameter count
         */
        public void extraValueCount(int count) {
            extraValues = new Object[count];
            mExtraIndex = 0;
        }

        /**
         * add extra/additional parameter value for Binder method
         *
         * @param value extra/additional parameter value
         */
        public void addExtraValue(Object value) {
            extraValues[mExtraIndex++] = value;
        }

        /**
         * assign the count of method parameter type.
         *
         * @param count the count of method parameter type.
         */
        public void typeCount(int count) {
            methodTypes = new Class<?>[count];
            mTypeIndex = 0;
        }

        /**
         * add  method parameter type.
         *
         * @param type the type
         */
        public void addType(Class<?> type) {
            methodTypes[mTypeIndex++] = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BindInfo bindInfo = (BindInfo) o;
            return Objects.equals(view, bindInfo.view) &&
                    Objects.equals(propName, bindInfo.propName);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new Object[]{view, propName});
        }
    }
}
