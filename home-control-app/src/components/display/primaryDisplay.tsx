import React from 'react';
import * as State from 'types/state';


type PrimaryDisplayProps = { 
    state: State.HomeState | undefined
}

class PrimaryDisplay extends React.Component<PrimaryDisplayProps, {}> {
    private static STD_DIM: number = 100.0;  // Draw functions operate in a normalised [0-100]x[0-100] canvas or container space
    private static DEFAULT_DEVICE_ICON_SIZE: Vector2 = { x: 5.0, y: 5.0 };

    private canvasRef: React.RefObject<HTMLCanvasElement>;
    private size: Point = { x: 100, y: 100};
    private minSize: number = 100;
    private drawMultiplier: number = 1.0;
    private centeringTranslation: Point = { x: 0, y: 0 };

    constructor(props: PrimaryDisplayProps) {
        super(props);
        this.canvasRef = React.createRef();
    }

    render() {
        if (this.canvasRef.current) {
            this.setSize(this.canvasRef.current);

            const context = this.canvasRef.current.getContext('2d');            
            if (context) {
                this.clearCanvas(this.canvasRef.current, context);
                this.drawRooms(context);
                this.drawDevices(context);
            }
        }

        return (
             <div className="primaryDisplay" style={{ width: "100%", height: "100%" }}>
                 <canvas ref={this.canvasRef} width={this.size.x} height={this.size.y}></canvas>
             </div>
          );
    }

    clearCanvas(canvas: HTMLCanvasElement, context: CanvasRenderingContext2D) {
        context.clearRect(0, 0, canvas.width, canvas.height);
        context.fillStyle = 'rgba(0, 100, 255, 0.1)';
        context.fillRect(0, 0, canvas.width, canvas.height);
    }

    drawRooms(context: CanvasRenderingContext2D) {
        if (!this.props.state?.rooms) return;

        for (var room of this.props.state.rooms) {
            var schematic = room.schematic;
            if (!schematic) continue;       // Not renderable

            var loc = this.loc(schematic.location);
            var sz = this.sz(schematic.size);

            context.strokeStyle = "green";
            context.strokeRect(loc.x, loc.y, sz.x, sz.y);
            context.fillStyle = 'rgba(0, 100, 255, 0.1)';
            context.fillRect(loc.x, loc.y, sz.x, sz.y);
        }
    }

    drawDevices(context: CanvasRenderingContext2D) {
        if (!this.props.state?.devices) return;

        for (var device of this.props.state.devices) {
            this.drawDevice(context, device);
        }
    }

    drawDevice(context: CanvasRenderingContext2D, device: State.Device) {
        var schematic = device.schematic;
        if (!schematic || !schematic.location) return;

        var loc = schematic.location;
        var sz = schematic.size ?? PrimaryDisplay.DEFAULT_DEVICE_ICON_SIZE;

        if (schematic.coordSpace === State.CoordSpace.RELATIVE) {
            var room = this.getRoom(device.room);
            if (room) {
                loc = {
                    x: room.schematic.location.x + (room.schematic.size.x * (loc.x / PrimaryDisplay.STD_DIM)) - (sz.x * 0.5),
                    y: room.schematic.location.y + (room.schematic.size.y * (loc.y / PrimaryDisplay.STD_DIM)) - (sz.y * 0.5),
                };

                // TODO: For now, no sizing relative to parent
                // sz = ...
            }
        }

        loc = this.loc(loc); 
        sz = this.sz(sz);

        context.strokeStyle = "blue";
        context.strokeRect(loc.x, loc.y, sz.x, sz.y);
        context.fillStyle = 'rgba(0, 0, 255, 0.5)';
        context.fillRect(loc.x, loc.y, sz.x, sz.y);
    }

    getRoom(id: string) : State.Room | undefined {
        return this.props.state?.rooms.find(x => (x.id === id));
    }

    setSize(canvas: HTMLCanvasElement) {
        this.size = { 
            x: canvas.parentElement?.clientWidth || 100, 
            y: canvas.parentElement?.clientHeight || 100 
        };

        let xExceedsY = this.size.x - this.size.y;
        if (xExceedsY > 0) {
            this.minSize = this.size.y;
            this.centeringTranslation = { x: xExceedsY * 0.5, y: 0 };
        }
        else {
            this.minSize = this.size.x;
            this.centeringTranslation = { x: 0, y: xExceedsY * -0.5 };
        }

        this.drawMultiplier = this.minSize / PrimaryDisplay.STD_DIM;
    }

    x(x: number): number {
        return (this.drawMultiplier * x) + this.centeringTranslation.x;
    }

    y(y: number): number {
        return (this.drawMultiplier * y) + this.centeringTranslation.y;
    }

    w(w: number): number {
        return (this.drawMultiplier * w);
    }

    h(h: number): number {
        return (this.drawMultiplier * h);
    }

    loc(loc: Vector2) : Vector2 {
        return { x: this.x(loc.x), y: this.y(loc.y) };
    }

    sz(sz: Vector2) : Vector2 {
        return { x: this.w(sz.x), y: this.h(sz.y) };
    }
}

export default PrimaryDisplay;
