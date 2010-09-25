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
package org.berlin.lang.octane.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.berlin.lang.octane.type.ONumber;
import org.berlin.lang.octane.type.OType;

/**
 * @author bbrown
 *
 */
public class OMathFunctions implements IOpHandler {
    
    private final OType lastStackElement;    
    private final Stack<OType> dataStack;
    
    private List<IOp> ops = new ArrayList<IOp>();
    private boolean registerOps = false;
    
    /**
     * Default constructor
     */
    public OMathFunctions(final Stack<OType> dataStack, final OType lastStackElement) {
        this.dataStack = dataStack;
        this.lastStackElement = lastStackElement;
    }
     

    /**
     * Register Ops.
     */
    public void registerOps() {
     
        this.registerOp(this.createAddOp());
        this.registerOp(this.createSubOp());
        this.registerOp(this.createDivOp());
        this.registerOp(this.createMulOp());
        registerOps = true;
    }   
    
    
    /**
     * Create math op.
     */
    public IOp createAddOp() {        
        return new BaseOp(this.dataStack) {
            
            /**             
             * @return
             */            
            public OType op(final ONumber arg1, final ONumber arg2) {            
                return new ONumber((Double) arg1.getValue() + (Double) arg2.getValue());
            }
            
            /**             
             * @return
             */
            @Override
            public OType op(final OType... args) {
                return op(args[0], args[1]);
            }
            
            @Override
            public void execute() {                 
                final OType arg1 = dataStack.pop();
                final OType arg2 = dataStack.pop();    
                                
                final ONumber result = (ONumber) this.op((ONumber)arg1, (ONumber)arg2);
                dataStack.push(result);                
            }            
            @Override
            public String doc() {
                return "[ _add ( <ARG1> <ARG2> -- <RESULT>) ]";
            }
            @Override
            public boolean hasOperation(final VisitElement element) {                
                return "_add".equals(element.getLastStackElement().getValue());
            }
    
        }; // Return    
    } // End of the method //
    
    /**
     * Create math op.
     */
    public IOp createSubOp() {        
        return new BaseOp(this.dataStack) {
            
            /**             
             * @return
             */            
            public OType op(final ONumber arg1, final ONumber arg2) {            
                return new ONumber((Double) arg1.getValue() - (Double) arg2.getValue());
            }
            
            /**             
             * @return
             */
            @Override
            public OType op(final OType... args) {
                return op(args[0], args[1]);
            }
            
            @Override
            public void execute() {                 
                final OType arg1 = dataStack.pop();
                final OType arg2 = dataStack.pop();    
                                
                final ONumber result = (ONumber) this.op((ONumber)arg1, (ONumber)arg2);
                dataStack.push(result);                
            }            
            @Override
            public String doc() {
                return "[ _sub ( <ARG1> <ARG2> -- <RESULT>) ]";
            }
            @Override
            public boolean hasOperation(final VisitElement element) {                
                return "_sub".equals(element.getLastStackElement().getValue());
            }
    
        }; // Return    
    } // End of the method //
    
    /**
     * Create math op.
     */
    public IOp createDivOp() {        
        return new BaseOp(this.dataStack) {
            
            /**             
             * @return
             */            
            public OType op(final ONumber arg1, final ONumber arg2) {            
                return new ONumber((Double) arg1.getValue() / (Double) arg2.getValue());
            }
            
            /**             
             * @return
             */
            @Override
            public OType op(final OType... args) {
                return op(args[0], args[1]);
            }
            
            @Override
            public void execute() {                 
                final OType arg1 = dataStack.pop();
                final OType arg2 = dataStack.pop();    
                                
                final ONumber result = (ONumber) this.op((ONumber)arg1, (ONumber)arg2);
                dataStack.push(result);                
            }            
            @Override
            public String doc() {
                return "[ _div ( <ARG1> <ARG2> -- <RESULT>) ]";
            }
            @Override
            public boolean hasOperation(final VisitElement element) {                
                return "_div".equals(element.getLastStackElement().getValue());
            }
    
        }; // Return    
    } // End of the method //
    
    /**
     * Create math op.
     */
    public IOp createMulOp() {        
        return new BaseOp(this.dataStack) {
            
            /**             
             * @return
             */            
            public OType op(final ONumber arg1, final ONumber arg2) {            
                return new ONumber((Double) arg1.getValue() * (Double) arg2.getValue());
            }
            
            /**             
             * @return
             */
            @Override
            public OType op(final OType... args) {
                return op(args[0], args[1]);
            }
            
            @Override
            public void execute() {                 
                final OType arg1 = dataStack.pop();
                final OType arg2 = dataStack.pop();    
                                
                final ONumber result = (ONumber) this.op((ONumber)arg1, (ONumber)arg2);
                dataStack.push(result);                
            }            
            @Override
            public String doc() {
                return "[ _mul ( <ARG1> <ARG2> -- <RESULT>) ]";
            }
            @Override
            public boolean hasOperation(final VisitElement element) {                
                return "_mul".equals(element.getLastStackElement().getValue());
            }
    
        }; // Return    
    } // End of the method //
    
    
    /**
     * 
     * @param handler
     */
    public void registerOp(final IOp handler) {
        ops.add(handler);
    }
          
    /**
     * Execute.
     */
    public void execute(final OType token) {
        
        if (token == null) {
            throw new NullPointerException("Invalid token at execute math functions");
        }
        
        if (!this.registerOps) {
            throw new IllegalStateException("Invalid State, you must call register ops before executing operations");
        }
        
        // Visit all visitor nodes
        // Execute the operations
        for (final Visitor ophandler : this.ops) {
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
