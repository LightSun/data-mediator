package com.heaven7.java.data.mediator.test.complexbind;

import com.heaven7.java.data.mediator.DataMediator;
import com.heaven7.java.data.mediator.DataMediatorCallback;
import com.heaven7.java.data.mediator.Property;

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
        DataMediator dm = treeInfo.resolve(root);
        //TODO
        dm.addDataMediatorCallback(new DataMediatorCallback() {
            @Override
            public void onPropertyValueChanged(Object data, Property prop, Object oldValue, Object newValue) {
                if(prop.getName().equals(treeInfo.lastSecondNode.prop)){
                    //virtual property
                  //  dm.getBaseMediator().dispatchValueChanged(prop, oldValue, newValue);
                }
            }
        });
        return null;
    }

    //@BindXXX("stus[0].name?")  @BindXXX("stu?.name?")
    private static TreeInfo parse(String propertyChain){
        final boolean supportPending;
        if(propertyChain.endsWith("?")){
            supportPending = true;
            propertyChain = propertyChain.substring(0, propertyChain.length() - 1);
        }else{
            supportPending = false;
        }
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
        info.supportPending = supportPending;
        info.expression = propertyChain;
        info.lastNode = last;
        info.lastSecondNode = lastSecondNode;
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
        boolean supportPending;
        String  expression;

        DataMediator resolve(DataMediator root) {
            return rootNode.resolve(root, supportPending);
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

        public DataMediator resolve(DataMediator root, boolean supportPending) {
            //TODO
            return null;
        }
    }
}
