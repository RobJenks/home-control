import React from 'react';


type PrimaryDisplayProps = { }

class PrimaryDisplay extends React.Component<PrimaryDisplayProps, {}> {
    private canvasRef: React.RefObject<HTMLCanvasElement>;
    private size: Point = { x: 100, y: 100};

    constructor(props: PrimaryDisplayProps) {
        super(props);
        this.canvasRef = React.createRef();
    }

    render() {
        if (this.canvasRef.current) {
            this.size = { 
                x: this.canvasRef.current.parentElement?.clientWidth || 100, 
                y: this.canvasRef.current.parentElement?.clientHeight || 100 
            };

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
        let x = 20, y = 45, w = 120, h = 80;

        context.strokeStyle = "green";
        context.strokeRect(x, y, w, h);
        context.fillStyle = 'rgba(0, 100, 255, 0.1)';
        context.fillRect(x, y, w, h);
    }

}

export default PrimaryDisplay;
