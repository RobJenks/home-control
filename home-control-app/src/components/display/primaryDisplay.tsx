import React from 'react';


type PrimaryDisplayProps = { }

class PrimaryDisplay extends React.Component<PrimaryDisplayProps, {}> {
    private static CANVAS_DIM: number = 100.0;
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
        let x = 30, y = 30, w = 40, h = 40;

        context.strokeStyle = "green";
        context.strokeRect(this.x(x), this.y(y), this.w(w), this.h(h));
        context.fillStyle = 'rgba(0, 100, 255, 0.1)';
        context.fillRect(this.x(x), this.y(y), this.w(w), this.h(h));
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

        this.drawMultiplier = this.minSize / PrimaryDisplay.CANVAS_DIM;
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
}

export default PrimaryDisplay;
