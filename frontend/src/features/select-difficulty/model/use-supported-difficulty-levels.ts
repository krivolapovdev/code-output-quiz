import { useEffect, useState } from "react";
import { quizMetaService } from "@/shared/api/quiz";

export const useSupportedDifficultyLevels = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [levels, setLevels] = useState<string[]>([]);

  useEffect(() => {
    const fetchLevels = async () => {
      try {
        const data = await quizMetaService.fetchSupportedDifficultyLevels();
        setLevels(data);
      } catch (error) {
        console.error("Failed to fetch difficulty levels:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchLevels();
  }, []);

  return { loading, levels };
};
