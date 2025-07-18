import confetti from "canvas-confetti";

const defaults = {
  startVelocity: 30,
  spread: 360,
  ticks: 60,
  zIndex: 0
};

const randomInRange = (min: number, max: number) =>
  Math.random() * (max - min) + min;

export const startConfettiFireworks = (duration = 1500) => {
  const animationEnd = Date.now() + duration;

  const interval = setInterval(() => {
    const timeLeft = animationEnd - Date.now();
    if (timeLeft <= 0) {
      return clearInterval(interval);
    }

    const particleCount = 100 * (timeLeft / duration);

    confetti({
      ...defaults,
      particleCount,
      origin: {
        x: randomInRange(0.1, 0.3),
        y: Math.random() - 0.2
      }
    });

    confetti({
      ...defaults,
      particleCount,
      origin: {
        x: randomInRange(0.7, 0.9),
        y: Math.random() - 0.2
      }
    });
  }, 250);
};
