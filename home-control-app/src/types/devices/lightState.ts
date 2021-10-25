export interface LightState {
    lastUpdate: string,
    name: string, 
    hueId: string,
    model: string, 
    lightType: string,
    lightArchetype: string, 
    reachable: boolean,
    on: boolean
    mode: string,
    xy: LightXY,
    hsb: LightHsb,
    colorTemp: number
}

export interface LightXY {
    x: number,
    y: number
}

export interface LightHsb {
    hue: number, 
    saturation: number, 
    brightness: number
}
