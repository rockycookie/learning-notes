package main

type dellBuilder struct {
    os string
}

func (b *dellBuilder) setOS() {
    b.os = "WindowsOS"
}

func (b *dellBuilder) getOS() string {
    return b.os
}
