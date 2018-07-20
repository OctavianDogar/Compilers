package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by Flavius on 2017-01-15.
 */
public class CustomStack<T> extends Stack<T> {
    @Override
    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        List<T> list = new ArrayList<>(this);
        Collections.reverse(list);
        for (T element:
             list) {
            sb.append(element.toString());
        }
        return sb.toString();
    }
}
