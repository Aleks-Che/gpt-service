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

// Анимация вращения орбиты (теперь с учётом начального поворота)
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
               rotateX(${initialRotationX}deg) 
               rotateY(${initialRotationY}deg) 
               rotateZ(${initialRotationZ}deg) 
               rotate(${speed > 0 ? 360 : -360}deg); 
  }
`;

// Дополнительная анимация для вращения вокруг центра логотипа (тоже с учётом начального поворота)
const spinAroundCenter = (initialRotationX: number, initialRotationY: number, initialRotationZ: number) => keyframes`
  from {
    transform: translate3d(-50%, -50%, 0) 
               rotateX(${initialRotationX}deg) 
               rotateY(${initialRotationY}deg) 
               rotateZ(${initialRotationZ}deg) 
               rotate(0deg);
  }
  to {
    transform: translate3d(-50%, -50%, 0) 
               rotateX(${initialRotationX}deg) 
               rotateY(${initialRotationY}deg) 
               rotateZ(${initialRotationZ}deg) 
               rotate(360deg);
  }
`;

// Орбита с начальными случайными 3D-поворотами и индивидуальной анимацией
const Orbit = styled.div<OrbitProps>`
  width: ${p => p.diameter}px;
  height: ${p => p.diameter}px;
  border: 1px solid #10a37f;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  
  // Добавляем анимацию вращения вокруг своей оси и вокруг центра
  animation: 
    ${p => orbitSpin(p.rotationSpeed, p.initialRotationX, p.initialRotationY, p.initialRotationZ)} ${p => Math.abs(Number(p.rotationSpeed))}s linear infinite,
    ${p => spinAroundCenter(p.initialRotationX, p.initialRotationY, p.initialRotationZ)} ${p => Math.abs(Number(p.rotationSpeed)) * 2}s linear infinite;
  
  transform-style: preserve-3d;
`;

// Контейнер логотипа
const LogoContainer = styled.div`
  perspective: 300px;
  transform-style: preserve-3d;
  width: 80px;
  height: 80px;
  position: relative;
  border-radius: 50%;
  overflow: hidden;
`;

// Центральный шар
const CentralBall = styled.div`
  width: 10px;
  height: 10px;
  background-color: #10a37f;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1;
`;

// Оболочка для электрона
const ElectronWrapper = styled.div<{ initialAngle: number; rotationDirection: number }>`
  position: absolute;
  top: 50%;
  left: 50%;
  transform-origin: center;
  transform: rotate(${({ initialAngle }) => initialAngle}deg) scale(${({ rotationDirection }) => rotationDirection});
  animation: ${electronSpin} 2s linear infinite;
`;

// Сам электрон
const Electron = styled.div<{ orbitDiameter: number; initialPositionAngle: number }>`
  width: 3px;
  height: 3px;
  background-color: #10a37f;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(
    calc(${({ orbitDiameter }) => orbitDiameter / 2}px * cos(${({ initialPositionAngle }) => initialPositionAngle}deg)),
    calc(${({ orbitDiameter }) => orbitDiameter / 2}px * sin(${({ initialPositionAngle }) => initialPositionAngle}deg))
  ) translate(-50%, -50%);
`;

const BigAnimatedLogo: React.FC = () => {
  const orbits = useMemo(() => {
    const diameters = [40, 56, 72]; // Размеры орбит
    const generatedOrbits = diameters.map(d => ({
      diameter: d,
      rotationSpeed: Math.random() * 5 + 3, // Случайная скорость вращения (3–8 секунд)
      initialRotationX: Math.random() * 360, // Случайный поворот вокруг X
      initialRotationY: Math.random() * 360, // Случайный поворот вокруг Y
      initialRotationZ: Math.random() * 360, // Случайный поворот вокруг Z
      electronInitialAngle: Math.random() * 360, // Случайный начальный угол электрона
      electronRotationDirection: Math.random() > 0.5 ? 1 : -1, // Случайное направление вращения электрона
      electronInitialPositionAngle: Math.random() * 360, // Случайное начальное положение электрона на орбите
    }));
    console.log('Generated Orbits:', generatedOrbits);
    return generatedOrbits;
  }, []);

  return (
    <LogoContainer>
      {orbits.map((orbit, index) => (
        <Orbit
          key={index}
          diameter={orbit.diameter}
          rotationSpeed={orbit.rotationSpeed * (Math.random() > 0.5 ? 1 : -1)} // Случайное направление вращения орбиты
          initialRotationX={orbit.initialRotationX}
          initialRotationY={orbit.initialRotationY}
          initialRotationZ={orbit.initialRotationZ}
          electronInitialAngle={orbit.electronInitialAngle}
          electronRotationDirection={orbit.electronRotationDirection}
        >
          {/* Добавляем движущийся электрон */}
          <ElectronWrapper initialAngle={orbit.electronInitialAngle} rotationDirection={orbit.electronRotationDirection}>
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