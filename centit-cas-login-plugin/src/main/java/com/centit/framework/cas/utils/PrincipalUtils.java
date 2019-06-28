package com.centit.framework.cas.utils;

import com.centit.support.algorithm.CollectionsOpt;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PrincipalUtils {
    public static Map<String, List<Object>> makePrinciupalAttributes(Map<String, Object> user){
        Map<String, List<Object>> attrs = new HashMap<>(user.size()+2);
        for(Map.Entry<String, Object> ent : user.entrySet()){
            if(ent.getValue() instanceof Collection){
                attrs.put(ent.getKey(), CollectionsOpt.cloneList((Collection<Object>) ent.getValue()));
            } else if(ent.getValue() instanceof Object[]){
                attrs.put(ent.getKey(), CollectionsOpt.arrayToList((Object[]) ent.getValue()));
            } else {
                attrs.put(ent.getKey(), CollectionsOpt.createList(ent.getValue()));
            }
        }
        return attrs;
    }
}
