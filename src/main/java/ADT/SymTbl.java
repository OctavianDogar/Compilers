package ADT;

import util.Pair;

import java.util.*;

/**
 * Created by Octavian on 10/23/2016.
 */
public class SymTbl {

    private Map<Object,Integer> constsMap;
    private Map<Object,Integer> identsMap;
    private Integer constCount=0;
    private Integer identsCount=0;

    public SymTbl() {
        this.constsMap = Collections.synchronizedMap(new LinkedHashMap<>());
        this.identsMap = Collections.synchronizedMap(new LinkedHashMap<>());
    }

    /**
     * Adds a new constant to the constants symTbl, returning the position
     * on which the new record is made
     * @param cnt
     * @return
     */
    public Integer addConst(Object cnt){
        if(!exists(cnt)){
            constsMap.put(cnt,constCount++);
            return constCount-1;
        }
        return getConstPos(cnt);
    }

    public Integer addIdent(Object ident){
        if(!exists(ident)){
            identsMap.put(ident,identsCount++);
            return identsCount-1;
        }
        return getIdentPos(ident);
    }

    public boolean exists(Object subject){
        return constsMap.containsKey(subject) || identsMap.containsKey(subject);
    }

    /**
     * Reversed the Key-Value setting in the maps, for an ease of access when grabbing the
     * item's position. Otherwise a bidirectional would have been needed.
     * @param value
     * @return
     */
    public Integer getConstPos(Object value){
        return constsMap.get(value);
    }

    public Integer getIdentPos(Object value){
        return identsMap.get(value);
    }

    private String iterateMaps(Map<Object,Integer> map){
        String result = "";
        for(Map.Entry e : map.entrySet()){
            result +=""+'\t' + e.getValue() +
                    '\t'+'\t'+'\t'+ e.getKey()+'\n';
        }
        return result == "" ? "No entries" : "  Index:"+'\t'+'\t'+"Symbol:"+'\n'+result;
    }

    @Override
    public String toString() {
        return "Constants SymTbl:" +'\n'+
                iterateMaps(constsMap)+'\n'+
                "Identifiers SymTbl:"+'\n'+
                iterateMaps(identsMap);
    }
}
