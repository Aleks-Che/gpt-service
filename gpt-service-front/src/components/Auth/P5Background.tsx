import p5 from 'p5';
import React, { useEffect, useRef } from 'react';
import styled from 'styled-components';

import BigAnimatedLogo from './BigAnimatedLogo';

import "@fontsource/montserrat/100.css";

interface P5BackgroundProps {
    width: number;
    height: number;
}

interface FloatingShape {
    x: number;
    y: number;
    size: number;
    dx: number;
    dy: number;
    noiseOffset: number;
}

const OverlayContainer = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
`;

const Header = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  position: absolute;
  top: 6.5%;
  left: 50%;
  transform: translateX(-50%);
  pointer-events: auto;
`;

const SiteTitle = styled.h1`
  font-size: 50px;
  margin: 0;
  color: #ffffff;
  text-shadow: 0 0 5px #000;
  font-family: montserrat;
  font-weight: 100;
  user-select: none;
`;


const P5Background: React.FC<P5BackgroundProps> = ({ width, height }) => {
    const containerRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const shapes: FloatingShape[] = [];

        const sketch = (p: p5) => {
            let numShapes = p.floor(p.random(7, 26)); // случайное количество от 7 до 25

            p.setup = () => {
                p.createCanvas(width, height);
                p.textAlign(p.CENTER, p.CENTER);
                p.textFont('Montserrat');
                p.smooth();
                for (let i = 0; i < numShapes; i++) {
                    shapes.push({
                        x: p.random(p.width),
                        y: p.random(p.height),
                        size: p.random(200, 600),
                        dx: p.random(-0.3, 0.3),
                        dy: p.random(-0.3, 0.3),
                        noiseOffset: p.random(1000),
                    });
                }
            };

            p.windowResized = () => {
                p.resizeCanvas(width, height);
            };

            p.draw = () => {
                p.background(10);
                for (let i = 0; i < 50; i++) {
                    p.fill(p.random(30, 70));
                    p.noStroke();
                    p.ellipse(p.random(p.width), p.random(p.height), p.random(1, 3));
                }

                p.noFill();
                p.stroke(
                    p.random(30, 50),
                    p.random(40, 80),
                    p.random(30, 50),
                    80
                );
                p.strokeWeight(2);
                shapes.forEach((shape) => {
                    shape.x += shape.dx;
                    shape.y += shape.dy;
                    if (shape.x < -shape.size) shape.x = p.width + shape.size;
                    if (shape.x > p.width + shape.size) shape.x = -shape.size;
                    if (shape.y < -shape.size) shape.y = p.height + shape.size;
                    if (shape.y > p.height + shape.size) shape.y = -shape.size;

                    p.push();
                    p.translate(shape.x, shape.y);
                    p.curveTightness(0.5);
                    p.beginShape();
                    let vertexCount = 40;
                    let firstAngle = 0;
                    let baseRadius = shape.size / 2;
                    let noiseValFirst = p.noise(
                        Math.cos(firstAngle) + shape.noiseOffset,
                        Math.sin(firstAngle) + shape.noiseOffset,
                        p.frameCount * 0.005
                    );
                    let offsetFirst = p.map(noiseValFirst, 0, 1, -baseRadius * 0.25, baseRadius * 0.25);
                    let firstX = (baseRadius + offsetFirst) * p.cos(firstAngle);
                    let firstY = (baseRadius + offsetFirst) * p.sin(firstAngle);
                    p.curveVertex(firstX, firstY);
                    p.curveVertex(firstX, firstY);
                    for (let i = 0; i <= vertexCount; i++) {
                        let angle = p.map(i, 0, vertexCount, 0, p.TWO_PI);
                        let noiseVal = p.noise(
                            Math.cos(angle) + shape.noiseOffset,
                            Math.sin(angle) + shape.noiseOffset,
                            p.frameCount * 0.005
                        );
                        let offset = p.map(noiseVal, 0, 1, -baseRadius * 0.25, baseRadius * 0.25);
                        let r = baseRadius + offset;
                        let x = r * p.cos(angle);
                        let y = r * p.sin(angle);
                        p.curveVertex(x, y);
                    }
                    p.curveVertex(firstX, firstY);
                    p.endShape();
                    p.pop();
                });
            };
        };

        const myP5 = new p5(sketch, containerRef.current!);
        return () => {
            myP5.remove();
        };
    }, [width, height]);

    return (
        <>
            <div ref={containerRef} />
            <OverlayContainer>
                <Header>
                    <BigAnimatedLogo
                    />
                    <SiteTitle>GPT SERVICE</SiteTitle>
                </Header>
            </OverlayContainer>
        </>
    );
};

export default P5Background;
