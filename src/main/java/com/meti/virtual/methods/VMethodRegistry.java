package com.meti.virtual.methods;

import com.meti.virtual.util.VGenericRegistry;
import com.meti.virtual.Virtual;

/**
 * @author SirMathhman
 * @version 0.0.0
 * @since 1/15/2019
 */
public class VMethodRegistry extends VGenericRegistry<VMethod> implements Virtual {
    @Override
    public String print() {
        return String.join("\n", contentStream().map(VMethod::print).sorted().toArray(String[]::new));
    }
}
