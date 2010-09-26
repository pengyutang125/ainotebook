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
 * OMathFunctions.java
 * Sep 24, 2010
 * bbrown
 * Contact: Berlin Brown <berlin dot brown at gmail.com>
 */
package org.berlin.lang.octane.sys.jsys;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.berlin.lang.octane.sys.VisitElement;
import org.berlin.lang.octane.sys.Visitor;
import org.berlin.lang.octane.type.OObject;
import org.berlin.lang.octane.type.OString;
import org.berlin.lang.octane.type.OType;

/**
 * Invoke java calls.
 * 
 * @author bbrown
 *
 */
public class OJavaSystem implements ICallHandler {
    
    private final OType lastStackElement;    
    private final Stack<OType> dataStack;
    
    private List<ICall> calls = new ArrayList<ICall>();
    private boolean registerOps = false;
    
    /**
     * Default constructor
     */
    public OJavaSystem(final Stack<OType> dataStack, final OType lastStackElement) {
        this.dataStack = dataStack;
        this.lastStackElement = lastStackElement;
    }
     
    /**
     * 
     * @param handler
     */
    @Override
    public void registerCall(final ICall handler) {
        calls.add(handler);
    }
         
    
    /**
     * Register Ops.
     */
    public void registerCalls() {
        this.registerCall(this.createClassCall());
        this.registerCall(this.createFieldCall());
        this.registerCall(this.createPrintFieldsCall());
        this.registerCall(this.createJavaDateCall());
        this.registerCall(new MethodInvokeCall(this.dataStack));
        registerOps = true;
    }   
        
    /**
     * Create math op.
     */
    public ICall createClassCall() {        
        return new BaseCall(this.dataStack) {
                                   
            @Override
            public void execute() {
                
                final OType arg1 = this.getDataStack().pop();
                                                 
                final OObject result = (OObject) this.op(arg1);
                if (result != null) {
                    this.getDataStack().push(result);
                }
            }            
            @Override
            public String doc() {
                return "[ _class ]";
            }
            @Override
            public boolean hasOperation(final VisitElement element) {                
                return "_class".equals(element.getLastStackElement().getValue());
            }
            @Override
            public OType op(OType... args) {
                
                final OString tokForClassName = (OString) args[0];
                final String  strForClass = (String) tokForClassName.getValue();
                try {
                    final Class<?> clz = Class.forName(strForClass);                    
                    return new OObject(clz);
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Invalid class at java class op [err119] - classname=" + strForClass);
                }                
            }
    
        }; // Return   
    } // End of the method //
    
    
    /**
     * Create math op.
     */
    public ICall createPrintFieldsCall() {        
        return new BaseCall(this.dataStack) {
                                   
            @Override
            public void execute() {
                
                final OType arg1 = this.getDataStack().pop();                                                
                this.op(arg1);                
            }            
            @Override
            public String doc() {
                return "[ _prn_fields ]";
            }
            @Override
            public boolean hasOperation(final VisitElement element) {                
                return "_prn_fields".equals(element.getLastStackElement().getValue());
            }
            @Override
            public OType op(OType... args) {
                
                final OObject objtype = (OObject) args[0];
                final Class<?> classobj = (Class<?>) objtype.getValue();
                int i = 1;
                for (final Field field : classobj.getFields()) {
                    System.out.format("<Field for %d [%s]> [%s] [name=%s]%n", i, classobj, field, field.getName());
                    i++;
                }
                return null;
            }
    
        }; // Return   
    } // End of the method // 
    
    /**
     * Create math op.
     */
    public ICall createFieldCall() {        
        return new BaseCall(this.dataStack) {
                                   
            @Override
            public void execute() {
                
                final OType arg1FieldClass = this.getDataStack().pop();
                final OType arg2FieldAsString = this.getDataStack().pop();
                                               
                final OObject result = (OObject) this.op(arg1FieldClass, arg2FieldAsString);                
                if (result != null) {
                    this.getDataStack().push(result);
                }                
            }            
            @Override
            public String doc() {
                return "[ _field ]";
            }
            @Override
            public boolean hasOperation(final VisitElement element) {                
                return "_field".equals(element.getLastStackElement().getValue());
            }
            @Override
            public OType op(OType... args) {
                                
                final OType arg1FieldAsClass = (OObject) args[0];
                final OString arg2FieldAsString = (OString) args[1];                
                                
                final Class<?> jclass = (Class<?>) arg1FieldAsClass.getValue();
                final String fieldname = (String) arg2FieldAsString.getValue();                
                try {                                    
                    final Field field = jclass.getField(fieldname);
                    // field.get: Returns the value of the field represented by this <code>Field</code>, on
                    // the specified object. 
                    try {
                        return new OObject(field.get(jclass));
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Illegal Argument for field [ERR213]");                        
                    } catch (IllegalAccessException e) {
                        throw new IllegalArgumentException("Illegal Argument for field [ERR213]");
                    }
                } catch (NoSuchFieldException e) {
                    throw new IllegalStateException("No Such Field");
                } // End of the try catch //
            }
    
        }; // Return   
    } // End of the method //  
    
    /**
     * Create math op.
     */
    public ICall createJavaDateCall() {        
        return new BaseCall(this.dataStack) {
                                   
            @Override
            public void execute() {                                                                               
                this.getDataStack().push(this.op((OType [])null));                
            }            
            @Override
            public String doc() {
                return "[ _date ]";
            }
            @Override
            public boolean hasOperation(final VisitElement element) {                
                return "_date".equals(element.getLastStackElement().getValue());
            }
            @Override
            public OType op(OType... args) {
                return new OObject(new Date());
            }
    
        }; // Return   
    } // End of the method // 
    
    /**
     * Execute.
     */
    public void execute(final OType token) {
        
        if (token == null) {
            throw new NullPointerException("Invalid token at execute system calls");
        }
        
        if (!this.registerOps) {
            throw new IllegalStateException("Invalid State, you must call register ops before executing operations");
        }
        
        // Visit all visitor nodes
        // Execute the operations
        for (final Visitor ophandler : this.calls) {
            this.accept(ophandler);
        }               
    }

    /**
     * @see org.berlin.lang.octane.sys.VisitElement#accept(org.berlin.lang.octane.sys.Visitor)
     */
    @Override
    public void accept(final Visitor visitor) {
        visitor.visit(this);        
    }

    /**
     * @return the lastStackElement
     */
    public OType getLastStackElement() {
        return lastStackElement;
    }

    /**
     * @return the dataStack
     */
    public Stack<OType> getDataStack() {
        return dataStack;
    }

    
} // End of the class
