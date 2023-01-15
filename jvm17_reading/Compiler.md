# Compiler for JVM
Compiler from source code written in the Java programming language to the instruction set of JVM

## Format 
```
<index> <opcode> [ <operand1> [ <operand2>... ]] [<comment>]
```
- index: index of the opcode of the instruction in the array that contains the bytes of Java Virtual Machine code for this method
    - a byte offset from the beginning of the method
    - The index prefacing each instruction may be used as the target of a control transfer instruction
- opcode: mnemonic for the instruction's opcode
- operandN: operands of the instruction
    - We preface an operand representing a run-time constant pool index with a hash sign
- comment: optional
    - Some of the material in the comments is emitted by javap; the rest is supplied by the authors
