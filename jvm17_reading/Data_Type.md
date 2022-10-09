# Data type
## Principle
- JVM expects that nearly all type checking is done prior to run time
- The instruction set of the JVM distinguishes its operand types using instructions intended to operate on values of specific types
    - For instance, iadd, ladd, fadd, and dadd

<br/>

## Two types
### Primitive type
- Contains:
    - numeric type
        - integral type
            - byte (8-bit signed two's-complement integers, default 0)
            - short (16-bit signed two's-complement integers, default 0)
            - int (32-bit signed two's-complement integers, default 0)
            - long (64-bit signed two's-complement integers, default 0)
            - char (16-bit unsigned integers representing Unicode code, UTF-16, default null code point ('\u0000'))
        - floating-point type
            - float (32-bit IEEE 754 binary32 format, default +0)
            - double (64-bit IEEE 754 binary64 format, default +0)
    - boolean type
        - encode the truth values true and false, default false
        - compiled as JVM int data type
        - boolean array compiled as JVM byte array data type
    - returnAddress type
        - pointers to the opcodes of JVM instructions
        - JVM's jsr, ret, and jsr_w instructions

<br/>

### Reference type
- A reference to an object is considered to have JVM type reference; an object is 
    - either a dynamically allocated class instance
    - or an array
- More than one reference to an object may exist
- Objects are always operated on, passed, and tested via values of type reference
- Contains:
    - class type
    - array type
    - interface type
