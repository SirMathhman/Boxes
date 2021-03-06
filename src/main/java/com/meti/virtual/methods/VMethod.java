package com.meti.virtual.methods;

import com.meti.util.StringUtil;
import com.meti.virtual.Virtual;
import com.meti.virtual.imports.VImport;

import java.util.Arrays;

/**
 * @author SirMathhman
 * @version 0.0.0
 * @since 1/15/2019
 */
public class VMethod implements Virtual {
    private final String name;
    private final VImport returnType;
    private final VParameter[] parameters;

    public VMethodContent content = new VStringContent("return null");

    public VMethod(String name, VImport returnType, VParameter... parameters) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    @Override
    public String print() {
        String returnString;

        if (returnType.content.simpleName.endsWith("Void")) {
            returnString = "void";
        } else {
            returnString = returnType.content.simpleName;
        }

        String[] parameterStrings = Arrays.stream(parameters).map(VParameter::print).toArray(String[]::new);
        return returnString +
                " " +
                name +
                StringUtil.parentheses(String.join(",", parameterStrings)) +
                StringUtil.curly(content.getContent(returnType, parameters));
    }
}
