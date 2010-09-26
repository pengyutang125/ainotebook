/**
 * Copyright (c) 2006-2010 Berlin Brown and botnode.com  All Rights Reserved
 *
 * http://www.opensource.org/licenses/bsd-license.php

 * All rights reserved.

 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:

 * * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * * Neither the name of the Botnode.com (Berlin Brown) nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * MethodInvokeCall.java
 * Sep 25, 2010
 * bbrown
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 */
package org.berlin.lang.octane.sys.jsys;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.berlin.lang.octane.sys.VisitElement;
import org.berlin.lang.octane.type.ONumber;
import org.berlin.lang.octane.type.OObject;
import org.berlin.lang.octane.type.OString;
import org.berlin.lang.octane.type.OType;
import org.berlin.lang.octane.type.TypeConstants;

/**
 * Create a method invoke call. - The method name - The number of arguments are
 * needed (n) - The list of classes for those arguments are needed - The
 * arguments are then required.
 * 
 * Minimum of 4 args needed.
 * 
 * [ "2" "3" args "java.lang.Object" "java.lang.Object" 2..n "<method_name>" <obj> _invoke ]
 * 
 */
public class MethodInvokeCall extends BaseCall {

    /**
     * @param dataStack
     */
    public MethodInvokeCall(Stack<OType> dataStack) {
        super(dataStack);
    }

    @Override
    public void execute() {

        // [ "2" "3" args "java.lang.Object" "java.lang.Object" 2..n "<method_name>" <obj> _invoke ]
        final OType arg1obj = this.getDataStack().pop();
        final OType arg2StrMethodName = this.getDataStack().pop();
        final OType arg3NumArgs = this.getDataStack().pop();
        
        if (arg1obj.getType() != TypeConstants.OBJECT) {
            throw new IllegalArgumentException("Invalid object argument at method invoke [INV75], arg=" + arg1obj);
        }
        if (arg2StrMethodName.getType() != TypeConstants.STRING) {
            throw new IllegalArgumentException("Invalid method name argument at method invoke [INV76], arg=" + arg2StrMethodName);
        }
        if (arg3NumArgs.getType() != TypeConstants.NUMBER) {
            throw new IllegalArgumentException("Invalid number of arguments at method invoke [INV77], arg=" + arg3NumArgs);
        }
        
        // Attempt to get the rest of the args
        final ONumber nargs = (ONumber) arg3NumArgs;
        final int n = ((Double) nargs.getValue()).intValue();
                
        // Pop these arg value types off the stack
        final List<Class<?>> classTypeList = new ArrayList<Class<?>>();
        final OObject arg5TypeList = new OObject(classTypeList);
        {            
            Class<?> clz = null;
            OString ostr = null;              
            try {
                for (int i = 0; i < n; i++) {
                    ostr = (OString) this.getDataStack().pop();            
                    clz = Class.forName(String.valueOf(ostr.getValue()));
                    classTypeList.add(clz);
                }
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Invalid argument at method invoke [INV96], {invalid class type}, last argument=" + ostr);
            } // End of the try - catch //                        
            // Pop the args word off
            final OType arg4b = this.getDataStack().pop();
            if (arg4b.getType() != TypeConstants.BREAK) {
                throw new IllegalArgumentException("Invalid argument at method invoke [INV96], {expecting ',' break}, last argument=" + arg4b);
            }
        } // End of scope
        
        // Iterate over the arg values
        final List<Object> argValList = new ArrayList<Object>(); 
        final OObject arg6ValList = new OObject(classTypeList);
        OType genarg = null;
        Object val = null;
        for (int i = 0; i < n; i++) {
            
            genarg = this.getDataStack().pop();
            if (TypeConstants.NUMBER == genarg.getType()) {
                val = genarg.getValue();
            } else if (TypeConstants.OBJECT == genarg.getType()) {
                val = genarg.getValue();
            } else if (TypeConstants.STRING == genarg.getType()) {
                val = genarg.getValue();
            }            
            if (val != null) {
                argValList.add(val);                    
           }
        } // End of the for //
                
        // Operate on the args
        final Class<?> clzForObj = (Class<?>) arg1obj.getValue().getClass();        
        final Class<?> [] arrArgs = classTypeList.toArray(new Class<?>[0]);
        final Object [] arrVals = argValList.toArray(new Object[0]);
        Object resultObj = null;
        try {
            final Method method = clzForObj.getMethod(String.valueOf(arg2StrMethodName.getValue()), classTypeList.toArray(arrArgs));
            if (n == 0) {            
                resultObj = method.invoke(arg1obj.getValue(), new Object[0]);
            } else {
                resultObj = method.invoke(arg1obj.getValue(), arrVals);
            } // End of the if
            
        } catch (Exception e) {
            throw new IllegalStateException("Error at method invoke [INV96], msg=" + e.getMessage());
        } // End of try catchs
        
        if (resultObj != null) {
            this.getDataStack().push(new OObject(resultObj));
        }
    }

    @Override
    public String doc() {
        return "[ _invoke ]";
    }

    @Override
    public boolean hasOperation(final VisitElement element) {
        return "_invoke".equals(element.getLastStackElement().getValue());
    }

    @Override
    public OType op(OType... args) {               
       return null;
    }

} // End of the Class //
