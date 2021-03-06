package com.meti.virtual.classes;

import com.meti.virtual.packages.VPackage;
import com.meti.virtual.Virtual;

/**
 * @author SirMathhman
 * @version 0.0.0
 * @since 1/14/2019
 */
public abstract class VClass implements Virtual {
    final VClassState classState = new VClassState();
    final VPackage vPackage;
    final String name;

    VClass(String name) {
        this(VPackage.DEFAULT, name);
    }

    VClass(VPackage vPackage, String name) {
        this.name = name;
        this.vPackage = vPackage;
    }

    @Override
    public String print() {
        /*
        to get the class as a compilable, writable, version,
        we simply have to write the class as normal
         */

        return String.join("\n",
                vPackage.print(),
                printContent()
        );
    }

    public abstract String printContent();

    public String getName() {
        return vPackage.joinedArguments() + "." + name;
    }
}
