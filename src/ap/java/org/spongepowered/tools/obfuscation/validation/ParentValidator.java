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
package org.spongepowered.tools.obfuscation.validation;

import java.util.Collection;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import org.spongepowered.tools.obfuscation.MixinValidator;
import org.spongepowered.tools.obfuscation.TypeHandle;


/**
 * Validator which checks that the mixin parent is sane
 */
public class ParentValidator extends MixinValidator {

    /**
     * ctor
     * 
     * @param processingEnv Processing environment
     */
    public ParentValidator(ProcessingEnvironment processingEnv) {
        super(processingEnv, ValidationPass.EARLY);
    }

    /* (non-Javadoc)
     * @see org.spongepowered.tools.obfuscation.MixinValidator
     *      #validate(javax.lang.model.element.TypeElement,
     *      javax.lang.model.element.AnnotationMirror, java.util.Collection)
     */
    @Override
    public boolean validate(TypeElement mixin, AnnotationMirror annotation, Collection<TypeHandle> targets) {
        if (mixin.getEnclosingElement().getKind() != ElementKind.PACKAGE && !mixin.getModifiers().contains(Modifier.STATIC)) {
            this.error("Inner class mixin must be declared static", mixin);
        }
        
        return true;
    }
}