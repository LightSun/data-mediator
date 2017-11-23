package com.heaven7.java.data.mediator;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.data.mediator.collector.MapPropertyDispatcher;
import com.heaven7.java.data.mediator.collector.PropertyEventReceiver;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * the data mediator tree . which help we handle property chain.
 * @param <T> the root module type
 * @author heaven7
 * @since 1.4.4
 */
//Property-chain, depth, key(for list and SparseArray)
public class DataMediatorTree<T> {
    private static final Pattern REG_BIND = Pattern.compile("^[A-Za-z_$]+[A-Za-z_$\\d]+([\\[]\\d+[]])??" +
            "([\\.][A-Za-z_$]+[A-Za-z_$\\d]+([\\[]\\d+[]])??$)");
    private static final Pattern REG_INDICATOR_BIND = Pattern.compile("[\\[]\\d+[]]$");
    private static final HashMap<String, TreeInfo> sCache;

    static {
        sCache = new HashMap<>();
    }
    private final SparseArray<DataMediator<?>> mChildrenMap;
    private DataMediator<T> root;

    public DataMediatorTree() {
        this.mChildrenMap = new SparseArray<>();
    }
    public void clearChildren(){
        mChildrenMap.clear();
    }
    public boolean hasRoot(){
        return root != null;
    }
    public void setRootDataMediator(DataMediator<T> root){
        this.root = root;
    }
    @SuppressWarnings("unchecked")
    public <M> DataMediator<M> bindPropertyChain(String propertyChain){
        TreeInfo treeInfo = sCache.get(propertyChain);
        if(treeInfo == null){
            treeInfo = parse(propertyChain);
            sCache.put(propertyChain, treeInfo);
        }
        DataMediator<?> dm = mChildrenMap.get(propertyChain.hashCode());
        if(dm == null){
            dm = treeInfo.resolve(root);
            mChildrenMap.put(propertyChain.hashCode(), dm);
        }
        dm.addDataMediatorCallback(new InternalCallback(treeInfo.lastSecondNode.prop,
                root, treeInfo.depth));
        return (DataMediator<M>) dm;
    }

    //@BindXXX("stus[0].name")  @BindXXX("stu.name") // no ?
    private static TreeInfo parse(String propertyChain){
        if(!REG_BIND.matcher(propertyChain).find()){
            throw new IllegalArgumentException("property chain must match the Data-Mediator binding specification. like xxx.xxx/xxx.xxx.xxx[1]");
        }
        String[] strs = propertyChain.split("\\.");
        //parse nodes
        final Node rootNode = parseNode(strs[0]);
        Node lastSecondNode = rootNode;
        Node last = rootNode;
        for(int size = strs.length, i = 1 ;  i < size ; i ++){
            Node node = parseNode(strs[i]);
            last.next = node;
            last = last.next;
            if(i == size - 2){
                lastSecondNode = node;
            }
        }
        //tree
        TreeInfo info = new TreeInfo();
        info.rootNode = rootNode;
        info.expression = propertyChain;
        info.lastNode = last;
        info.lastSecondNode = lastSecondNode;
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
        Node lastSecondNode;
        String expression;
        int depth;

        DataMediator resolve(DataMediator parent) {
            return rootNode.resolve(parent);
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
        public DataMediator resolve(DataMediator parent) {
            //TODO
            return null;
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
        @Override
        public void onPropertyValueChanged(Object data, Property prop, Object oldValue, Object newValue) {
            final DataMediator root = weakDm_root.get();
            if(root == null){
                return;
            }
            if(allow(prop)){
                params.mOriginalSource = data;
                params.receiver = root.getBaseMediator()._getInternalDispatcher();
                depthReceiver.onPreCallback(params);
                depthReceiver.dispatchValueChanged(root.getData(), data, prop, oldValue, newValue);
                depthReceiver.onPostCallback();
            }
        }
        @Override
        public void onPropertyApplied(Object data, Property prop, Object value) {
            final DataMediator root = weakDm_root.get();
            if(root == null){
                return;
            }
            if(allow(prop)){
                params.mOriginalSource = data;
                params.receiver = root.getBaseMediator()._getInternalDispatcher();
                depthReceiver.onPreCallback(params);
                depthReceiver.dispatchValueApplied(root.getData(), data, prop, value);
                depthReceiver.onPostCallback();
            }
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
