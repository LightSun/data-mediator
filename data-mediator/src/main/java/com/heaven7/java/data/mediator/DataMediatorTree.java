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

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.collector.MapPropertyDispatcher;
import com.heaven7.java.data.mediator.collector.PropertyEventReceiver;
import com.heaven7.java.data.mediator.util.PropertyChainException;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * the data mediator tree . which help we handle property chain.
 * @param <T> the root module type
 * @author heaven7
 * @since 1.4.4
 */
//Property-chain, depth, key(for list and SparseArray)
/*public*/ class DataMediatorTree<T> {
    private static final String PREFIX_PROP = "PROP_";
    private static final Pattern REG_BIND = Pattern.compile("^[A-Za-z_$]+[A-Za-z_$\\d]+([\\[]\\d+[]])??" +
            "([\\.][A-Za-z_$]+[A-Za-z_$\\d]+([\\[]\\d+[]])??$)");
    private static final Pattern REG_INDICATOR_BIND = Pattern.compile("[\\[]\\d+[]]$");
    private static final HashMap<String, TreeInfo> sCache;

    static {
        sCache = new HashMap<>();
    }
    private final WeakHashMap<Object, List<DataMediatorCallback>> mCallbacks;
    private final DataMediator<T> mRoot;

    public DataMediatorTree(DataMediator<T> root) {
        this.mRoot = root;
        this.mCallbacks = new WeakHashMap<>();
    }
    public List<DataMediatorCallback> getInflateCallbacks(Object data) {
        List<DataMediatorCallback> callbacks = mCallbacks.get(data);
        return callbacks == null ? Collections.<DataMediatorCallback>emptyList() : callbacks;
    }
    @SuppressWarnings("unchecked")
    public void inflatePropertyChain(String propertyChain) throws PropertyChainException{
        TreeInfo treeInfo = sCache.get(propertyChain);
        if(treeInfo == null){
            treeInfo = parse(propertyChain);
            sCache.put(propertyChain, treeInfo);
        }
        Object data = treeInfo.resolveData(mRoot.getData());
        if(data == null){
            throw new PropertyChainException("can't inflate data. full expre = " + treeInfo.expression);
        }
        List<DataMediatorCallback> list = mCallbacks.get(data);
        if(list == null){
            list = new ArrayList<>();
            mCallbacks.put(data, list);
        }
        list.add(new InternalCallback(treeInfo.lastNode.prop,
                mRoot, treeInfo.depth));
    }

    //@BindXXX("stus[0].name")  @BindXXX("stu.name") // no ?
    private static TreeInfo parse(String propertyChain){
        if(!REG_BIND.matcher(propertyChain).find()){
            throw new IllegalArgumentException("property chain must match the Data-Mediator binding specification. like xxx.xxx/xxx.xxx.xxx[1]");
        }
        String[] strs = propertyChain.split("\\.");
        //parse nodes
        final Node rootNode = parseNode(strs[0]);
        Node last = rootNode;
        for(int size = strs.length, i = 1 ;  i < size ; i ++){
            last.next = parseNode(strs[i]);
            last = last.next;
        }
        //tree
        TreeInfo info = new TreeInfo();
        info.rootNode = rootNode;
        info.expression = propertyChain;
        info.lastNode = last;
        info.depth = strs.length - 1;
        return info;
    }
    //stus[0]  stus
    private static Node parseNode(String str) {
        String firstStr = str.trim();
        if(firstStr.length() == 0){
            throw new IllegalArgumentException("all property can't be empty");
        }
        Matcher matcher = REG_INDICATOR_BIND.matcher(str);

        final String prop;
        final Object key;
        if(matcher.find()){
            String temp = matcher.group();
            //currently only can be int
            key = Integer.valueOf(temp.substring(temp.indexOf("[")+ 1, temp.lastIndexOf("]")));
            prop = str.substring(0, str.lastIndexOf("["));
        }else {
            key = null;
            prop = str;
        }
        return new Node(prop, key);
    }

    private static class TreeInfo{
        Node rootNode;
        Node lastNode;
        String expression;
        int depth;

        Object resolveData(Object parent) {
            return rootNode.resolveData(expression, parent);
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TreeInfo treeInfo = (TreeInfo) o;
            return depth == treeInfo.depth &&
                    Objects.equals(expression, treeInfo.expression);
        }
        @Override
        public int hashCode() {
            return Objects.hash(expression, depth);
        }
    }

    private static class Node{
        final String prop; //-> Property.
        final Object key;

        Node next;

        Node(String prop, Object key) {
            this.prop = prop;
            this.key = key;
        }
        Object resolveData(String expression, Object parent) {
            //no next
            if(next == null){
                return parent;
            }
            /*
             * get PROP_XXX
             * get data (by field/method).
             */
            try {
                Field field_prop = parent.getClass().getField(PREFIX_PROP + prop);
                Property realProp = (Property) field_prop.get(null);
                String getPrefix = "get";
                switch (realProp.getComplexType()){
                    case FieldFlags.COMPLEX_ARRAY: {
                        Object fieldValue = getFieldValue(expression, parent, getPrefix, true);
                        Object target = Array.get(fieldValue, getKeyAsInt(expression));
                        return next.resolveData(expression, target);
                    }

                    case FieldFlags.COMPLEX_LIST: {
                        List list = (List) getFieldValue(expression, parent, getPrefix, true);
                        return next.resolveData(expression, list.get(getKeyAsInt(expression)));
                    }

                    case FieldFlags.COMPLEX_SPARSE_ARRAY:
                        SparseArray sa = (SparseArray) getFieldValue(expression, parent, getPrefix, true);
                        return next.resolveData(expression, sa.get(getKeyAsInt(expression)));

                    default:
                        getPrefix = realProp.getType()== boolean.class ? "is" : "get";
                        Object fieldValue = getFieldValue(expression, parent, getPrefix, false);
                        return next.resolveData(expression, fieldValue);
                }
            } catch (Exception e) {
                throw new PropertyChainException("can't resolve property chain. "
                        + "root = " + parent + "full expression = " + expression + " ,prop = " + prop, e);
            }
        }

        Object getFieldValue(String expression, Object parent, String getPrefix, boolean checkNullKey)
                throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
            if(checkNullKey){
                throwIfNullKey(expression);
            }
            //default method is public.
            Method getMethod = parent.getClass().getMethod(getPrefix
                    + prop.substring(0, 1).toUpperCase() + prop.substring(1));
            return getMethod.invoke(parent);
        }

        void throwIfNullKey(String expression){
            if(key == null){
                throw new PropertyChainException("type is array but not assign index. like 'name[0}'. " +
                        "full expression = " + expression + " ,prop = " + prop);
            }
        }
        int getKeyAsInt(String expression){
            try {
                return (Integer) key;
            }catch (Exception e){
                throw new PropertyChainException("key must be int(value is " + key+ ")."
                        + "full expression = " + expression + " ,prop = " + prop , e);
            }
        }
    }

    private static class InternalCallback extends DataMediatorCallback.NameableCallback<Object>{
        final DepthPropertyEventReceiver depthReceiver;
        final WeakReference<DataMediator> weakDm_root;
        final DepthParams params;

        InternalCallback(String name, DataMediator root, int depth) {
            super(name);
            this.depthReceiver = new DepthPropertyEventReceiver();
            this.weakDm_root = new WeakReference<DataMediator>(root);
            this.params = new DepthParams(depth);
        }
        //if not permit return null.
        DataMediator provideRootDataMediator(Property prop){
            if(allow(prop)){
                return weakDm_root.get();
            }
            return null;
        }
        @Override
        public void onPropertyValueChanged(Object data, Property prop, Object oldValue, Object newValue) {
            final DataMediator root = provideRootDataMediator(prop);
            if(root == null){
                return;
            }
            params.mOriginalSource = data;
            params.receiver = root.getBaseMediator()._getInternalDispatcher();
            depthReceiver.onPreCallback(params);
            depthReceiver.dispatchValueChanged(root.getData(), data, prop, oldValue, newValue);
            depthReceiver.onPostCallback();
        }
        @Override
        public void onPropertyApplied(Object data, Property prop, Object value) {
            final DataMediator root = provideRootDataMediator(prop);
            if(root == null){
                return;
            }
            params.mOriginalSource = data;
            params.receiver = root.getBaseMediator()._getInternalDispatcher();
            depthReceiver.onPreCallback(params);
            depthReceiver.dispatchValueApplied(root.getData(), data, prop, value);
            depthReceiver.onPostCallback();
        }
        @Override
        public void onAddPropertyValues(Object data, Property prop, Object newValue, Object addedValue) {
            final DataMediator root = provideRootDataMediator(prop);
            if(root == null){
                return;
            }
            params.mOriginalSource = data;
            params.receiver = root.getBaseMediator()._getInternalDispatcher();
            depthReceiver.onPreCallback(params);
            depthReceiver.dispatchOnAddPropertyValues(root.getData(), data, prop, newValue, addedValue);
            depthReceiver.onPostCallback();
        }
        @Override
        public void onAddPropertyValuesWithIndex(Object data, Property prop, Object newValue, Object addedValue, int index) {
            final DataMediator root = provideRootDataMediator(prop);
            if(root == null){
                return;
            }
            params.mOriginalSource = data;
            params.receiver = root.getBaseMediator()._getInternalDispatcher();
            depthReceiver.onPreCallback(params);
            depthReceiver.dispatchOnAddPropertyValuesWithIndex(root.getData(), data, prop, newValue, addedValue, index);
            depthReceiver.onPostCallback();
        }
        @Override
        public void onRemovePropertyValues(Object data, Property prop, Object newValue, Object removeValue) {
            final DataMediator root = provideRootDataMediator(prop);
            if(root == null){
                return;
            }
            params.mOriginalSource = data;
            params.receiver = root.getBaseMediator()._getInternalDispatcher();
            depthReceiver.onPreCallback(params);
            depthReceiver.dispatchOnRemovePropertyValues(root.getData(), data, prop, newValue, removeValue);
            depthReceiver.onPostCallback();
        }
        @Override
        public void onPropertyItemChanged(Object data, Property prop, Object oldItem, Object newItem, int index) {
            final DataMediator root = provideRootDataMediator(prop);
            if(root == null){
                return;
            }
            params.mOriginalSource = data;
            params.receiver = root.getBaseMediator()._getInternalDispatcher();
            depthReceiver.onPreCallback(params);
            depthReceiver.dispatchOnPropertyItemChanged(root.getData(), data, prop, oldItem, newItem, index);
            depthReceiver.onPostCallback();
        }
    }
    private static class DepthPropertyEventReceiver extends PropertyEventReceiver{
        PropertyEventReceiver base;
        @Override
        public void onPreCallback(Params params) {
            if(!(params instanceof DepthParams)){
                throw new IllegalArgumentException();
            }
            super.onPreCallback(params);
            base = ((DepthParams) params).receiver;
            base.onPreCallback(params);
        }
        @Override
        public void onPostCallback() {
            super.onPostCallback();
            base.onPostCallback();
            this.base = null;
        }
        public void dispatchValueChanged(Object data, Object original, Property prop,
                                         Object oldValue, Object newValue) {
            base.dispatchValueChanged(data, original, prop, oldValue, newValue);
        }
        public void dispatchValueApplied(Object data, Object original, Property prop, Object value) {
            base.dispatchValueApplied(data, original, prop, value);
        }
        public void dispatchOnAddPropertyValues(Object data, Object original, Property prop,
                                                Object newValue, Object addedValue){
            base.dispatchOnAddPropertyValues(data, original, prop, newValue, addedValue);
        }
        public void dispatchOnAddPropertyValuesWithIndex(Object data, Object original, Property prop,
                                                         Object newValue, Object addedValue, int index){
            base.dispatchOnAddPropertyValuesWithIndex(data, original, prop, newValue, addedValue, index);
        }
        public void dispatchOnRemovePropertyValues(Object data, Object original, Property prop,
                                                   Object newValue, Object removeValue){
            base.dispatchOnRemovePropertyValues(data, original, prop, newValue, removeValue);
        }

        public void dispatchOnPropertyItemChanged(Object data, Object original, Property prop,
                                                  Object oldItem, Object newItem, int index){
            base.dispatchOnPropertyItemChanged(data, original, prop, oldItem, newItem, index);
        }
        public MapPropertyDispatcher<Integer> getSparseArrayDispatcher(){
            return base.getSparseArrayDispatcher();
        }
    }

    private static class DepthParams extends PropertyCallbackContext.Params{

        PropertyEventReceiver receiver;

        public DepthParams(int mDepth) {
            super(null, mDepth);
        }
        @Override
        public void cleanUp() {
            //super.cleanUp();
            mOriginalSource = null;
            receiver = null;
        }
    }
}
