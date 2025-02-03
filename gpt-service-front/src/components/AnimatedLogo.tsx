import React, { useMemo } from 'react';
import styled from 'styled-components';

interface OrbitProps {
  diameter: number;
  initialRotation2d: number;
  initialRotationX: number;
  initialRotationY: number;
}

const Orbit = styled.div<OrbitProps>`
  width: ${p => p.diameter}px;
  height: ${p => p.diameter}px;
  border: 1px solid #10a37f;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  /* Изначально устанавливаем 3D трансформацию с случайными поворотами по осям X, Y и Z.
     Используем translate3d для правильного позиционирования. */
  transform: translate3d(-50%, -50%, 0) 
             rotateX(${p => p.initialRotationX}deg)
             rotateY(${p => p.initialRotationY}deg)
             rotate(${p => p.initialRotation2d}deg);
  transition: transform 1s ease;
`;

const LogoContainer = styled.div`
  perspective: 300px;
  transform-style: preserve-3d;
  width: 40px;
  height: 40px;
  position: relative;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;

  &:hover ${Orbit} {
    /* При наведении обнуляем все повороты – орбиты примут стандартное состояние */
    transform: translate3d(-50%, -50%, 0) rotateX(0deg) rotateY(0deg) rotate(0deg);
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
  const orbits = useMemo(() => {
    const diameters = [20, 28, 36];
    // Генерируем для каждой орбиты случайный поворот по трем осям
    return diameters.map(d => ({
      diameter: d,
      initialRotation2d: Math.random() * 360, // поворот вокруг Z (2D)
      initialRotationX: Math.random() * 360,    // дополнительный поворот по X
      initialRotationY: Math.random() * 360,    // дополнительный поворот по Y
    }));
  }, []);

  return (
    <LogoContainer>
      {orbits.map((orbit, index) => (
        <Orbit
          key={index}
          diameter={orbit.diameter}
          initialRotation2d={orbit.initialRotation2d}
          initialRotationX={orbit.initialRotationX}
          initialRotationY={orbit.initialRotationY}
        />
      ))}
      <CentralBall />
    </LogoContainer>
  );
};

export default AnimatedLogo;
