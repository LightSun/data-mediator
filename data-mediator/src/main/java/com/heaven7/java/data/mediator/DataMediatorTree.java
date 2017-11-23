package com.heaven7.java.data.mediator;

import com.heaven7.java.data.mediator.collector.PropertyEventReceiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Property-chain, depth, key(for list and SparseArray)
public class DataMediatorTree<T> {
    private static final Pattern REG_BIND = Pattern.compile("^[A-Za-z_$]+[A-Za-z_$\\d]+([\\[]\\d+[]])??" +
            "([\\.][A-Za-z_$]+[A-Za-z_$\\d]+([\\[]\\d+[]])??$)");
    private static final Pattern REG_INDICATOR_BIND = Pattern.compile("[\\[]\\d+[]]$");
    private final DataMediator<T> root;

    public DataMediatorTree(DataMediator<T> root) {
        this.root = root;
    }

    public Object getData(String propertyChain){
        final TreeInfo treeInfo = parse(propertyChain);
        final DataMediator dm = treeInfo.resolve(root);

        final PropertyEventReceiver target = PropertyEventReceiver.of(root.getBaseMediator()._getInternalDispatcher(),
                treeInfo.depth);
        //TODO
        dm.addDataMediatorCallback(new DataMediatorCallback.NameableCallback(treeInfo.lastSecondNode.prop) {
            @Override
            public void onPropertyValueChanged(Object data, Property prop, Object oldValue, Object newValue) {
                if(allow(prop)){
                    //virtual property
                    target.dispatchValueChanged(root.getData(), data, prop, oldValue, newValue);
                }
            }
            @Override
            public void onPropertyApplied(Object data, Property prop, Object value) {
                if(allow(prop)){
                    //virtual property
                    target.dispatchValueApplied(root.getData(), data, prop, value);
                }
            }
        });
        return null;
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
        String  expression;
        int depth;

        DataMediator resolve(DataMediator root) {
            return rootNode.resolve(root);
        }
    }

    private static class Node{
        final String prop;
        final Object key;

        Node next;

        Node(String prop, Object key) {
            this.prop = prop;
            this.key = key;
        }

        public DataMediator resolve(DataMediator root) {
            //TODO
            return null;
        }
    }

}
