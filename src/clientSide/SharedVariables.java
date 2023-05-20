/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientSide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author gabri
 */
public class SharedVariables {
    
    
    static List<String> nameList = Collections.synchronizedList(new ArrayList<>());
    

    public static synchronized void addName(String name) {
        nameList.add(name);
    }

    public static synchronized List<String> getNameList() {
        return new ArrayList<>(nameList); // Create a copy to prevent direct modification of the original list
    }
}

    
