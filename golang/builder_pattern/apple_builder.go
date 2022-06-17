package main

type appleBuilder struct {
    os string
}

func (b *appleBuilder) setOS() {
    b.os = "macOS"
}

func (b *appleBuilder) getOS() string {
    return b.os
}
