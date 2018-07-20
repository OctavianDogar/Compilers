package util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Octavian on 10/23/2016.
 */

@Getter
@Setter
@NoArgsConstructor
public class Pair<K,V>{
    private K key;
    private V value;

    public Pair(K key, V value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return   ""+'\n'+'\t' + key +
                '\t'+'\t'+'\t'+'\t' + value;
    }
}