export type HomeState = {
    rooms: Room[],
    devices: Device[]
}

export type Room = {
    id: string,
    name: string,
    schematic: Schematic
}

export type Device = {
    id: string,
    name: string,
    deviceClass: string,
    room: string,
    schematic: Schematic,
    state: string
}

export const CoordSpace = {
    ABSOLUTE: "Absolute",
    RELATIVE: "Relative"
}

export type Schematic = {
    coordSpace: string,
    location: Vector2,
    size: Vector2,
}
