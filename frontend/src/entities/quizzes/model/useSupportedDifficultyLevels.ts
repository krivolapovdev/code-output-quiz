import { useEffect, useState } from "react";
import { quizService } from "../api/quizService";

export const useSupportedDifficultyLevels = () => {
  const [levels, setLevels] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchLevels = async () => {
      try {
        const data = await quizService.fetchSupportedDifficultyLevels();
        setLevels(data);
      } catch (error) {
        setLevels([]);
        console.error("Failed to fetch supported difficulty levels", error);
      } finally {
        setLoading(false);
      }
    };

    fetchLevels();
  }, []);

  return { levels, loading };
};
