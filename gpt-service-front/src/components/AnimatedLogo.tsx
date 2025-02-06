import React, { useMemo } from 'react';
import styled from 'styled-components';

interface OrbitProps {
  diameter: number;
  initialRotation2d: number;
  initialRotationX: number;
  initialRotationY: number;
  hoverRotation2d: number;
  hoverRotationX: number;
  hoverRotationY: number;
}

const LogoContainer = styled.div`
  perspective: 300px;
  transform-style: preserve-3d;
  width: 40px;
  height: 40px;
  position: relative;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
`;

const Orbit = styled.div<OrbitProps>`
  width: ${p => p.diameter}px;
  height: ${p => p.diameter}px;
  border: 1px solid #10a37f;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate3d(-50%, -50%, 0) 
             rotateX(${p => p.initialRotationX}deg)
             rotateY(${p => p.initialRotationY}deg)
             rotate(${p => p.initialRotation2d}deg);
  transition: transform 1s ease;

  ${LogoContainer}:hover & {
    transform: translate3d(-50%, -50%, 0) 
               rotateX(${p => p.hoverRotationX}deg)
               rotateY(${p => p.hoverRotationY}deg)
               rotate(${p => p.hoverRotation2d}deg);
  }
`;

const CentralBall = styled.div`
  width: 8px;
  height: 8px;
  background-color: #10a37f;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
`;

const AnimatedLogo: React.FC = () => {
  const getControlledRandom = (min: number, max: number) => {
    return min + Math.random() * (max - min);
  };

  const getRandomDirection = () => Math.random() > 0.5 ? 1 : -1;

  const orbits = useMemo(() => {
    const diameters = [20, 28, 36];
    const generatedOrbits = diameters.map((d, index) => {
      const direction = getRandomDirection();
      const orbitData = {
        diameter: d,
        initialRotation2d: getControlledRandom(120, 250),
        initialRotationX: getControlledRandom(150, 280),
        initialRotationY: getControlledRandom(140, 220),
        hoverRotation2d: getControlledRandom(0, 150) * (index === 1 ? 2 : 1) * direction,
        hoverRotationX: getControlledRandom(0, 150) * (index === 1 ? 2 : 1) * direction,
        hoverRotationY: getControlledRandom(0, 150) * (index === 1 ? 2 : 1) * direction,
      };
      console.log(`Orbit ${index + 1} rotations:`, orbitData);
      return orbitData;
    });

    console.log('All orbits data:', generatedOrbits);
    return generatedOrbits;
  }, []);


  return (
    <LogoContainer >
      {orbits.map((orbit, index) => (
        <Orbit
          key={index}
          diameter={orbit.diameter}
          initialRotation2d={orbit.initialRotation2d}
          initialRotationX={orbit.initialRotationX}
          initialRotationY={orbit.initialRotationY}
          hoverRotation2d={orbit.hoverRotation2d}
          hoverRotationX={orbit.hoverRotationX}
          hoverRotationY={orbit.hoverRotationY}
        />
      ))}
      <CentralBall />
    </LogoContainer>
  );
};

export default AnimatedLogo;
