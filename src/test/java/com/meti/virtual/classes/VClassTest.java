package com.meti.virtual.classes;

import com.meti.virtual.packages.VPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author SirMathhman
 * @version 0.0.0
 * @since 1/14/2019
 */
class VClassTest {
    @Test
    void getSimpleName(){
        VClass vClass = new VStringClass(new VPackage("com", "meti"), "Main", "content");
        assertEquals("com.meti.Main", vClass.getName());
    }
}