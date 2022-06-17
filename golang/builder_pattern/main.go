package main

/*

"""
The potential problem with the multistage building process is that
a partially built and unstable product may be exposed to the client.
The Builder pattern keeps the product private until it’s fully built.
""" 
from [this reference](https://refactoring.guru/design-patterns/builder/go/example)

*/

import "fmt"

func getBuilder(bType string) computerBuilder {

    if bType == "Apple" {
        fmt.Println("Getting apple builder")
        return &appleBuilder{}
    }

    if bType == "Dell" {
        fmt.Println("Getting dell builder")
        return &dellBuilder{}
    }

    return nil
}

func main() {
    ab := getBuilder("Apple")
    db := getBuilder("Dell")
    gb := genericBuilder{}

    apple := gb.setBuilder(ab).build()
    dell := gb.setBuilder(db).build()

    fmt.Println("Apple OS is: " + apple.os)
    fmt.Println("Dell OS is: " + dell.os)
}
