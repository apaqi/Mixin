/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.asm.mixin.injection.struct;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.util.ASMHelper;


/**
 * Information about the current injection target, mainly just convenience
 * rather than passing a bunch of values around.
 */
public class Target {

    /**
     * Target method
     */
    public final MethodNode method;
    
    /**
     * True if the method is static 
     */
    public final boolean isStatic;
    
    /**
     * Method arguments
     */
    public final Type[] arguments;
    
    /**
     * Method argument slots 
     */
    public final int[] argIndices;
    
    /**
     * Return type computed from the method descriptor 
     */
    public final Type returnType;
    
    /**
     * Method's (original) MAXS 
     */
    public final int maxStack;
    
    /**
     * Method's original max locals 
     */
    public final int maxLocals;

    /**
     * Callback method descriptor based on this target 
     */
    public final String callbackDescriptor;
    
    /**
     * Callback info class
     */
    public final String callbackInfoClass;

    /**
     * Make a new Target for the supplied method
     * 
     * @param method target method
     */
    public Target(MethodNode method) {
        this.method = method;
        this.isStatic = ASMHelper.methodIsStatic(method);
        this.arguments = Type.getArgumentTypes(method.desc);
        this.argIndices = this.calcArgIndices(this.isStatic ? 0 : 1);

        this.returnType = Type.getReturnType(method.desc);
        this.maxStack = method.maxStack;
        this.maxLocals = method.maxLocals;
        this.callbackInfoClass = CallbackInfo.getCallInfoClassName(this.returnType);
        this.callbackDescriptor = String.format("(%sL%s;)V", method.desc.substring(1, method.desc.indexOf(')')), this.callbackInfoClass);
    }

    private int[] calcArgIndices(int local) {
        int[] argIndices = new int[this.arguments.length];
        for (int arg = 0; arg < this.arguments.length; arg++) {
            argIndices[arg] = local;
            local += this.arguments[arg].getSize();
        }
        return argIndices;
    }

    /**
     * Get the callback descriptor
     * 
     * @param captureLocals True if the callback is capturing locals
     * @param locals Local variable types
     * @param argumentTypes Argument types
     * @param startIndex local index to start at
     * @return generated descriptor
     */
    public String getCallbackDescriptor(final boolean captureLocals, final Type[] locals, Type[] argumentTypes, int startIndex) {
        if (!captureLocals) {
            return this.callbackDescriptor;
        }

        String descriptor = this.callbackDescriptor.substring(0, this.callbackDescriptor.indexOf(')'));
        for (int l = startIndex; l < locals.length; l++) {
            if (locals[l] != null) {
                descriptor += locals[l].getDescriptor();
            }
        }

        return descriptor + ")V";
    }
    
    @Override
    public String toString() {
        return this.method.name + this.method.desc;
    }
}