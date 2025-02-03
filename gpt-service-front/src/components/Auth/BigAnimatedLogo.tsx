import React, { useMemo } from 'react';
import styled, { keyframes } from 'styled-components';

interface OrbitProps {
  diameter: number;
  rotationSpeed: number; // Скорость вращения орбиты
  initialRotationX: number;
  initialRotationY: number;
  initialRotationZ: number;
  electronInitialAngle: number;
  electronRotationDirection: number;
}

// Анимация движения электрона
const electronSpin = keyframes`
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
`;

// Анимация вращения орбиты
const orbitSpin = (speed: number, initialRotationX: number, initialRotationY: number, initialRotationZ: number) => keyframes`
  from { 
    transform: translate3d(-50%, -50%, 0) 
               rotateX(${initialRotationX}deg) 
               rotateY(${initialRotationY}deg) 
               rotateZ(${initialRotationZ}deg) 
               rotate(0deg); 
  }
  to { 
    transform: translate3d(-50%, -50%, 0) 
               rotateX(${initialRotationX + 360}deg) 
               rotateY(${initialRotationY + (speed > 0 ? 360 : -360)}deg) 
               rotateZ(${initialRotationZ + 360}deg) 
               rotate(360deg); 
  }
`;

interface BigAnimatedLogoProps {
  sizeMultiplier?: number;
  orbitSpeedMultiplier?: number;
  electronSpeedMultiplier?: number;
  numberOfOrbits?: number;
  orbitDiameters?: number[];
  electronSize?: number;
  centralBallSize?: number;
  orbitThickness?: number;
}

const BigAnimatedLogo: React.FC<BigAnimatedLogoProps> = ({
  sizeMultiplier = 1,
  orbitSpeedMultiplier = 0.7,
  electronSpeedMultiplier = 0.5,
  numberOfOrbits = 3,
  orbitDiameters = [40, 56, 72],
  electronSize = 3,
  centralBallSize = 10,
  orbitThickness = 1
}) => {
  const LogoContainer = styled.div`
    perspective: 300px;
    transform-style: preserve-3d;
    width: ${80 * sizeMultiplier}px;
    height: ${80 * sizeMultiplier}px;
    position: relative;
    border-radius: 50%;
    overflow: hidden;
  `;

  const CentralBall = styled.div`
    width: ${centralBallSize * sizeMultiplier}px;
    height: ${centralBallSize * sizeMultiplier}px;
    background-color: #10a37f;
    border-radius: 50%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 1;
  `;

  const Orbit = styled.div<OrbitProps>`
    width: ${p => p.diameter * sizeMultiplier}px;
    height: ${p => p.diameter * sizeMultiplier}px;
    border: ${orbitThickness}px solid #10a37f;
    border-radius: 50%;
    position: absolute;
    top: 50%;
    left: 50%;
    animation: ${p => orbitSpin(
    p.rotationSpeed * orbitSpeedMultiplier,
    p.initialRotationX,
    p.initialRotationY,
    p.initialRotationZ
  )} ${p => Math.abs(Number(p.rotationSpeed)) / orbitSpeedMultiplier}s linear infinite;
    transform-style: preserve-3d;
  `;

  const Electron = styled.div<{ orbitDiameter: number; initialPositionAngle: number }>`
    width: ${electronSize * sizeMultiplier}px;
    height: ${electronSize * sizeMultiplier}px;
    background-color: #10a37f;
    border-radius: 50%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(
      calc(${({ orbitDiameter }) => orbitDiameter * sizeMultiplier / 2}px * cos(${({ initialPositionAngle }) => initialPositionAngle}deg)),
      calc(${({ orbitDiameter }) => orbitDiameter * sizeMultiplier / 2}px * sin(${({ initialPositionAngle }) => initialPositionAngle}deg))
    ) translate(-50%, -50%);
  `;

  const ElectronWrapper = styled.div<{ initialAngle: number; rotationDirection: number }>`
    position: absolute;
    top: 50%;
    left: 50%;
    transform-origin: center;
    transform: rotate(${({ initialAngle }) => initialAngle}deg) scale(${({ rotationDirection }) => rotationDirection});
    animation: ${electronSpin} ${2 / electronSpeedMultiplier}s linear infinite;
  `;

  const orbits = useMemo(() => {
    const diameters = orbitDiameters.slice(0, numberOfOrbits);
    const generatedOrbits = diameters.map(d => ({
      diameter: d,
      rotationSpeed: Math.random() * 5 + 3,
      initialRotationX: Math.random() * 360,
      initialRotationY: Math.random() * 360,
      initialRotationZ: Math.random() * 360,
      electronInitialAngle: Math.random() * 360,
      electronRotationDirection: Math.random() > 0.5 ? 1 : -1,
      electronInitialPositionAngle: Math.random() * 360,
    }));
    return generatedOrbits;
  }, [numberOfOrbits, orbitDiameters]);

  return (
    <LogoContainer>
      {orbits.map((orbit, index) => (
        <Orbit
          key={index}
          diameter={orbit.diameter}
          rotationSpeed={orbit.rotationSpeed * (Math.random() > 0.5 ? 1 : -1)}
          initialRotationX={orbit.initialRotationX}
          initialRotationY={orbit.initialRotationY}
          initialRotationZ={orbit.initialRotationZ}
          electronInitialAngle={orbit.electronInitialAngle}
          electronRotationDirection={orbit.electronRotationDirection}
        >
          <ElectronWrapper
            initialAngle={orbit.electronInitialAngle}
            rotationDirection={orbit.electronRotationDirection}
          >
            <Electron
              orbitDiameter={orbit.diameter}
              initialPositionAngle={orbit.electronInitialPositionAngle}
            />
          </ElectronWrapper>
        </Orbit>
      ))}
      <CentralBall />
    </LogoContainer>
  );
};



export default BigAnimatedLogo;