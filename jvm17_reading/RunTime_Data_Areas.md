# Run-Time Data Areas
## Thread execution
- Per-thread data areas are created when a thread is created and destroyed when the thread exits
    - **pc (program counter) register**
    - **JVM stack**
        - is never manipulated directly except to push and pop frames
        - memory for a JVM stack does not need to be contiguous
    - **Native method stack**
        - An JVM implementation may use conventional stacks, colloquially called "C stacks," to support native methods
        - Does not exits when the JVM implementation does not supply it
- All-thread-shared data areas are created on virtual machine start-up
    - **JVM heap**
        - the run-time data area from which memory for all class instances and arrays is allocated
        - is created on virtual machine start-up
        - objects are never explicitly deallocated (garbage collector does it)
        - the memory for the heap does not need to be contiguous
    - **method area**
        - is analogous to the storage area for compiled code of a conventional language or analogous to the "text" segment in an operating system process
        - stores per-class structures such as the run-time constant pool, field and method data, and the code for methods and constructors, including the special methods used in class and interface initialization and in instance initialization
        - the method area is logically part of the heap, simple implementations may choose not to either garbage collect or compact it. This specification does not mandate the location of the method area or the policies used to manage compiled code
    - **run-time constant pool**

<br/>

## Frame
A frame is used to store data and partial results, as well as to perform dynamic linking, return values for methods, and dispatch exceptions.
- Each frame has its own 
    - **array of local variables**
        - Local variables (32bit each) are addressed by indexing
            - 64 bit variables take two consecutive local variables and only referenced by the index of the first variable
            - On instance method invocation, local variable 0 is always used to pass a reference to the object on which the instance method is being invoked (this in the Java programming language)
        - JVM uses local variables to pass parameters on method invocation
    - **operand stack**
        - The operand stack is empty when the frame that contains it is created
        - JVM supplies instructions to load constants or values from local variables or fields onto the operand stack
        - Other JVM instructions take operands from the operand stack, operate on them, and push the result back onto the operand stack
        - The operand stack is also used to prepare parameters to be passed to methods and to receive method results
        - Each entry on the operand stack can hold a value of any Java Virtual Machine type, including a value of type long or type double
    - a reference to the run-time constant pool of the class of the current method
- A frame created by a thread is local to that thread and cannot be referenced by any other thread.


### Life-cycle
A new frame is created each time a method is invoked. A frame is destroyed when its method invocation completes, whether that completion is normal or abrupt (it throws an uncaught exception).
