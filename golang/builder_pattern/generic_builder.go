package main

type genericBuilder struct {
    b computerBuilder
}

func (gb *genericBuilder) setBuilder(b computerBuilder) *genericBuilder {
    gb.b = b
    return gb
}

func (gb *genericBuilder) build() computer {
    gb.b.setOS()
    return computer{ os: gb.b.getOS() }
} 
