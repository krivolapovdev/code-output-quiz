import { useEffect, useState } from "react";
import { quizMetaService } from "@/shared/api/quiz";

export const useSupportedDifficultyLevels = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [difficultyLevels, setDifficultyLevels] = useState<string[]>([]);

  useEffect(() => {
    const fetchLevels = async () => {
      try {
        setLoading(true);
        const data = await quizMetaService.fetchSupportedDifficultyLevels();
        setDifficultyLevels(data);
      } catch (error) {
        setDifficultyLevels([]);
        console.error("Failed to fetch supported difficulty levels", error);
      } finally {
        setLoading(false);
      }
    };

    fetchLevels();
  }, []);

  return { loading, difficultyLevels };
};
