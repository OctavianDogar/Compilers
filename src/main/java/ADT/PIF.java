package ADT;

import util.CodTbl;
import util.Pair;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Octavian on 10/23/2016.
 */

@AllArgsConstructor
@Getter
@Setter
public class PIF {

    private List<Pair> list;

    public PIF(){
        list = new ArrayList<>();
    }

    public void addAll(Collection<Pair> col){
        list.addAll(col);
    }


    /**
     * Add a new identifier in PIF, with 0 and SymTbl position
     * @param symTblPos
     */
    public void addConst(Object symTblPos){
        list.add(new Pair(CodTbl.get("constant"),symTblPos));
    }
    public void addIdent(Object symTblPos){
        list.add(new Pair(CodTbl.get("identifier"),symTblPos));
    }

    /**
     * Add a new token in PIF, knowing the token type
     * @param tokenType
     */
    public void addToken(Object tokenType){
        Integer typeCode = CodTbl.get(tokenType);
        list.add(new Pair(typeCode,-1));
    }


    @Override
    public String toString() {
        return "======= PIF =======" +'\n'+
                "Token Code"+'\t'+"SymbolTablePosition"+
                list.toString().replace("[", "").replace("]", "").replace(",","");
    }

}
