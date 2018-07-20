package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Flavius on 2017-01-15.
 */
public class CustomArrayList<T> extends ArrayList<T> {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T element:
                this) {
            sb.append(element.toString());
        }
        return sb.toString();
    }
}
