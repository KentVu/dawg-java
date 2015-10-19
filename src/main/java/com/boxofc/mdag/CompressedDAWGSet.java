package com.boxofc.mdag;

import java.util.NavigableSet;
import java.util.TreeSet;

public class CompressedDAWGSet extends DAWGSet {
    //SimpleMDAGNode from which all others in the structure are reachable (will be defined if this ModifiableDAWGSet is simplified)
    SimpleMDAGNode simplifiedSourceNode;
    
    //Array that will contain a space-saving version of the ModifiableDAWGSet after a call to simplify().
    SimpleMDAGNode[] mdagDataArray;
    
    /**
     * Quantity of words in this DAWG.
     */
    transient Integer size;
    
    /**
     * Determines whether a String is present in the ModifiableDAWGSet.
     
     * @param str       the String to be searched for
     * @return          true if {@code str} is present in the ModifiableDAWGSet, and false otherwise
     */
    public boolean contains(String str) {
        SimpleMDAGNode targetNode = SimpleMDAGNode.traverseMDAG(mdagDataArray, simplifiedSourceNode, str);
        return targetNode != null && targetNode.isAcceptNode();
    }
    
    /**
     * Retrieves Strings corresponding to all valid transition paths from a given node that satisfy a given condition.
     
     * @param strNavigableSet                    a NavigableSet of Strings to contain all those in the ModifiableDAWGSet satisfying
                                      {@code searchCondition} with {@code conditionString}
     * @param searchCondition               the SearchCondition enum field describing the type of relationship that Strings contained in the ModifiableDAWGSet
                                      must have with {@code conditionString} in order to be included in the result set
     * @param searchConditionString         the String that all Strings in the ModifiableDAWGSet must be related with in the fashion denoted
                                      by {@code searchCondition} in order to be included in the result set
     * @param prefixString                  the String corresponding to the currently traversed transition path
     * @param transitionSetBegin            an int denoting the starting index of a SimpleMDAGNode's transition set in mdagDataArray
     * @param onePastTransitionSetEnd       an int denoting one past the last index of a simpleMDAGNode's transition set in mdagDataArray
     */
    private void getStrings(NavigableSet<String> strNavigableSet, SearchCondition searchCondition, String searchConditionString, String prefixString, SimpleMDAGNode node) {
        int transitionSetBegin = node.getTransitionSetBeginIndex();
        int onePastTransitionSetEnd = transitionSetBegin +  node.getOutgoingTransitionSetSize();
        
        //Traverse all the valid transition paths beginning from each transition in transitionTreeMap, inserting the
        //corresponding Strings in to strNavigableSet that have the relationship with conditionString denoted by searchCondition
        for (int i = transitionSetBegin; i < onePastTransitionSetEnd; i++) {
            SimpleMDAGNode currentNode = mdagDataArray[i];
            String newPrefixString = prefixString + currentNode.getLetter();
            
            SearchCondition childrenSearchCondition = searchCondition;
            if (searchCondition.satisfiesCondition(newPrefixString, searchConditionString)) {
                if (currentNode.isAcceptNode())
                    strNavigableSet.add(newPrefixString);
                //If the parent node satisfies the search condition then all its child nodes also satisfy this condition.
                if (searchCondition == SearchCondition.SUBSTRING_SEARCH_CONDITION)
                    childrenSearchCondition = SearchCondition.NO_SEARCH_CONDITION;
            }
            
            //Recursively call this to traverse all the valid transition paths from currentNode
            getStrings(strNavigableSet, childrenSearchCondition, searchConditionString, newPrefixString, currentNode);
        }
    }
    
    /**
     * Retrieves all the Strings in the ModifiableDAWGSet that begin with a given String.
     
     * @param prefixStr     a String that is the prefix for all the desired Strings
     * @return              a NavigableSet containing all the Strings present in the ModifiableDAWGSet that begin with {@code prefixString}
     */
    @Override
    public NavigableSet<String> getStringsStartingWith(String prefixStr) {
        NavigableSet<String> strNavigableSet = new TreeSet<>();
        SimpleMDAGNode originNode = SimpleMDAGNode.traverseMDAG(mdagDataArray, simplifiedSourceNode, prefixStr);      //attempt to transition down the path denoted by prefixStr
        //if there a transition path corresponding to prefixString (one or more stored Strings begin with prefixStr)
        if (originNode != null) {
            if (originNode.isAcceptNode())
                strNavigableSet.add(prefixStr);
            getStrings(strNavigableSet, SearchCondition.NO_SEARCH_CONDITION, prefixStr, prefixStr, originNode);        //retrieve all Strings that extend the transition path denoted by prefixString
        }
        return strNavigableSet;
    }
    
    /**
     * Retrieves all the Strings in the ModifiableDAWGSet that contain a given String.
     
     * @param str       a String that is contained in all the desired Strings
     * @return          a NavigableSet containing all the Strings present in the ModifiableDAWGSet that begin with {@code prefixString}
     */
    @Override
    public NavigableSet<String> getStringsWithSubstring(String str) {
        NavigableSet<String> strNavigableSet = new TreeSet<>();
        if (str.isEmpty() && simplifiedSourceNode.isAcceptNode())
            strNavigableSet.add(str);
        getStrings(strNavigableSet, SearchCondition.SUBSTRING_SEARCH_CONDITION, str, "", simplifiedSourceNode);
        return strNavigableSet;
    }
    
    /**
     * Retrieves all the Strings in the ModifiableDAWGSet that begin with a given String.
     
     * @param suffixStr         a String that is the suffix for all the desired Strings
     * @return                  a NavigableSet containing all the Strings present in the ModifiableDAWGSet that end with {@code suffixStr}
     */
    @Override
    public NavigableSet<String> getStringsEndingWith(String suffixStr) {
        NavigableSet<String> strNavigableSet = new TreeSet<>();
        if (suffixStr.isEmpty() && simplifiedSourceNode.isAcceptNode())
            strNavigableSet.add(suffixStr);
        getStrings(strNavigableSet, SearchCondition.SUFFIX_SEARCH_CONDITION, suffixStr, "", simplifiedSourceNode);
        return strNavigableSet;
    }

    @Override
    public int getTransitionCount() {
        return mdagDataArray.length;
    }
    
    public int size() {
        if (size == null) {
            int s = 0;
            for (String word : this)
                s++;
            size = s;
        }
        return size;
    }
}